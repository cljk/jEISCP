# AGENTS.md

This file provides guidance to AI coding agents working with code in this repository.

## Project

jEISCP is a Java client for Onkyo/Integra AV receivers using eISCP (ISCP over Ethernet, port 60128). It is a single-module Maven project on Java 21, using JavaFX 21 (OpenJFX from Maven Central), JUnit 4 and slf4j 2 with reload4j, the maintained drop-in for log4j 1.2 (same `org.apache.log4j` API).

## Build & run

The command catalogue comes from the git submodule `src/main/resources/miracle2k-onkyo-eiscp`; its `eiscp-commands.yaml` is loaded from the classpath. Without the submodule, the tests and the app fail with an NPE:

```bash
git submodule update --init
```

```bash
mvn test
```

```bash
mvn test -Dtest=EiscpDeviceTest#testDevicenameExtraction
```

`mvn test` also writes a JaCoCo coverage report to `target/site/jacoco/index.html`. Tests use JUnit 4.13. Network tests (`EiscpAutodiscoveryTest`, `EiscpConnectorTest`) simulate the receiver on loopback sockets with free ports, so they need no real device.

`mvn clean` fails on Windows while a jEISCP instance runs from `target/classes`.

```bash
./run.sh -a 192.168.2.29
```

`run.sh` and `run.ps1` work in two steps:

1. Maven only builds. It runs `mvn -q compile dependency:build-classpath@javafx-module-path dependency:build-classpath@app-class-path`. The two executions have `<phase>none</phase>` and write `target/javafx-module-path.txt` and `target/class-path.txt`.
2. The scripts start `java` themselves with JavaFX on the module path and `de.csmp.jeiscp.app.ThreadedConsoleApp`.

Before building, they check for Java 21+ and Maven and fetch a missing submodule. They use the JDK from `JAVA_HOME` for both build and run.

Do not go back to `mvn javafx:run`. It forks the JVM with Java's process API, so the JVM gets no console. Ctrl+C or closing the terminal then ends only Maven, and the app lives on as an orphan. The plugin is still in the pom for manual use.

`run.ps1` must stay ASCII-only for Windows PowerShell 5.1. It has no `param()` block because `-a` would otherwise be bound as a parameter abbreviation. `.gitattributes` keeps `*.sh` on LF.

The app takes these flags:

- `-a <ip>`: skip UDP autodiscovery.
- `-nogui`
- `-noconsole`
- `-?`: show parameter help.

Without `-a`, the app runs autodiscovery and exits with a hint if no receiver answers within 30 s. With `-a`, it asks that address for its model by UDP (`EiscpAutodiscovery.identify`); if there is no answer, the model is unknown. Logs are appended to `eiscp.log` in the working directory.

The receiver port is fixed at 60128, and `-a` takes no port. On Windows this port can fall into an excluded port range (check with `netsh interface ipv4 show excludedportrange protocol=tcp`), so a fake receiver for local tests may not be able to bind it. In that case, connect with `new EiscpConnector("127.0.0.1", port)` from a test launcher instead.

## Architecture

- **Transport** (`de.csmp.jeiscp`): `EiscpProtocolHelper` wraps an ISCP string in an eISCP frame and parses it back. The frame consists of:
  - the `ISCP` magic,
  - header size 0x10,
  - the data size as a big-endian int,
  - version `01 00 00 00`,
  - the `!1…` payload, terminated by CR LF.

  `EiscpConnector` owns the TCP socket. `sendIscpCommand("MVL20")` prepends `!1` itself; `sendIscpMessage` expects the `!1` to be there already.
- **Autodiscovery** (`EiscpAutodiscovery`, used by `EiscpConnector.autodiscover()`):
  - It sends `!xECNQSTN` by UDP to every interface's directed broadcast address plus 255.255.255.255. Windows sends the limited broadcast out of one interface only, and that can be a Hyper-V, WSL or VPN adapter.
  - It resends every second and gives up with a `SocketTimeoutException` after 30 s. Answers that are not `ECN…` are ignored.
  - The constructor with targets, port, interval and timeout exists for tests.
- **Receiving is listener-based.** The first `addListener` call starts `EiscpConnectorSocketReaderThread`, which blocks on the socket. On a malformed frame it resyncs by skipping to the next CR/LF/EOF byte; garbage ending in CR LF can therefore still swallow the following frame. Listeners get the message without the `!1` prefix and without trailing terminators (e.g. `MVL16`). Callbacks run on this reader thread, not on the JavaFX thread. `sendIscpMessage` is synchronized because the console, the GUI and listener callbacks all send.
- **Command catalogue** (`de.csmp.jeiscp.eiscp`): `EiscpCommandsParser` lazily parses the YAML zones `main` and `dock` into static maps. Upstream keeps the NET/USB commands (`NTC`, `NLS`, `NJA`, …) in `dock`; `zone2`–`zone4` are ignored. The maps are:
  - `CommandBlock`: the 3-letter prefix, e.g. `MVL`.
  - `Command`: prefix + value, with the id `block-name::value-name`.
  - `modelsets`: maps set ids to receiver model names.

  Block names are not unique (e.g. `NLS`, `NLA` and `NLU` are all `net-usb-list-info`); the id maps keep the first entry.

  `EiscpDevice` parses the autodiscovery answer (`ECN<model>/<port>/…`) and works out which modelsets the model supports. If the model is unknown, it falls back to TX-NR709.
- **`EiscpCommmandsConstants`** (note the triple "m") is generated; do not edit it by hand. `EiscpCommandsParserTest#generatedConstantsMatchCatalogue` fails when the file no longer matches the YAML, e.g. after a submodule update. To regenerate it:
  1. Run `mvn test -Dtest=EiscpCommandsParserConstGenerator`. Plain `mvn test` skips this class because of its name.
  2. Copy the output, `target/EiscpCommmandsConstants.java`, over the file in `src/main/java/de/csmp/jeiscp/eiscp/`.

  If upstream renamed a constant the code uses, the generator test cannot compile. Generate first with the old name in the code, then switch to the new name. This happened with `VIDEO_INFOMATION_*`, which became `VIDEO_INFORMATION_*`.
- **Apps** (`de.csmp.jeiscp.app`): the entry point is `ThreadedConsoleApp`. It runs the console and the GUI side by side on one connector:
  - `ConsoleController`: reads raw ISCP commands from stdin on its own thread and prints ANSI output via jansi.
    - When stdin closes (end of input), it stops. It exits the app only without a GUI.
    - Before this fix it sent `!1null` to the receiver in an endless loop.
  - `ReceiverState`: a GUI-independent model. It is an `EiscpListener` that turns incoming messages into power, volume, mute, input, now playing, the NET list and cover art.
    - It keeps the last value of every 3-letter command (`getLastValue`).
    - It notifies its listeners with an `Aspect`, on the reader thread. Messages without a dedicated aspect fire `OTHER`.
  - `DeviceFunctions`: GUI-independent. It lists the command blocks a model supports (`EiscpDevice.isCapable`), reduced to literal values without `QSTN` or placeholders. The YAML's model sets are coarse: `set1` covers almost everything, so a TX-NR626 is offered Sirius/XM/HD Radio as well.
  - `fx/FxGuiController`: starts JavaFX with `Platform.startup`, so `ThreadedConsoleApp` does not extend `Application`. It sends the initial queries and marshals state changes to the FX thread with `Platform.runLater`. The stage is `StageStyle.TRANSPARENT`, i.e. borderless. Its close request exits the JVM.
  - `fx/RemoteControlView`: the remote-control UI, built in code with no FXML and styled by `src/main/resources/de/csmp/jeiscp/app/fx/remote.css`. Icons are SVG paths on a 24×24 grid.
    - It has its own window keys: minimize, and a close cross that fires the stage's close request. Dragging on any non-interactive area moves the window.
    - The header shows the model and `EiscpConnector.getAddress()` as a link. The link opens `http://<ip>/` via `Browser`, which uses the platform launcher (`rundll32`, `open` or `xdg-open`) instead of AWT.
    - The power, mute and volume keys send a command and then wait for the receiver's echo to update the UI. The `updatingVolume` flag stops the slider from sending that echo back.
    - `Mode` NET/TV decides whether the direction pad, OK, return and home keys (and the keyboard shortcuts) send `NTC…` or `OSD…`.
      - The switch is two small keys in the pad's top corners; the OK key shows the target.
      - A new input switches the mode. SETUP (`OSDMENU`) and QUICK switch to TV.
      - The `OSD` commands are plain key presses: the receiver neither answers them nor reports its TV menus.
    - NET popups (`NPU`, parsed in `ReceiverState.parsePopup`, NUL-separated) cover the list in the display. They close on click, on OK/Return, on a new list page or input, or after 30 s.
  - `LogBuffer`: a log4j appender on the root logger that keeps the last 5000 entries for the GUI. `ThreadedConsoleApp` installs it before autodiscovery when the GUI is on.
    - It recognizes ISCP traffic by the message prefixes `sendIscpCommand: ` (from `EiscpConnector`) and `received: ` (from the reader thread). Changing those log texts breaks the →/← marking.
    - `subscribe` returns the history and passes on later entries, with no gap between the two.
  - `fx/LogWindow`: borderless, owned by the remote. Clicking the header LED or pressing L opens it. It batches entries from the logging thread into one `runLater` and offers FOLLOW, ISCP ONLY, copy (Ctrl+C for the selection), clear and a resize grip.
  - `fx/FunctionsPage`: opened by the "⋯" key. A searchable list of `DeviceFunctions`; selecting a function sends its `QSTN` and shows the current value.
  - The app icon PNGs in `src/main/resources/de/csmp/jeiscp/app/fx/icons/` are rendered by `src/test/java/de/csmp/jeiscp/app/fx/IconGenerator.java`, which is a standalone `java` program, not a test.

  `BlockingDumbConsoleApp` is an old polling prototype with a hard-coded IP.
