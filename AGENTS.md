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

Without `-a`, the app runs autodiscovery. With the GUI, `fx/StartupWindow` shows the search with a 30 s countdown and then the connect; if nothing answers or the connect fails, it asks for an address (`host[:port]`) or another search, and its close key quits. Without the GUI, the app exits with a hint instead. With `-a`, it asks that address for its model by UDP (`EiscpAutodiscovery.identify`); if there is no answer, the model is unknown. Logs are appended to `eiscp.log` in the working directory.

`-a` also takes `host:port`, for the receiver emulator in the test sources. `ReceiverEmulator` plays a TX-NR626 on 127.0.0.1 with TCP and autodiscovery on one port, including its quirks (no answer to `NSTQSTN` and `NJAREQ`, the old cover again before a new one). It needs only the JDK and runs without a build:

```bash
java src/test/java/de/csmp/jeiscp/ReceiverEmulator.java 0
```

`0` picks a port that is free for both TCP and UDP; the emulator prints it. On Windows, other port ranges are excluded for UDP than for TCP (`netsh interface ipv4 show excludedportrange protocol=udp`), and 60128 may be one of them. `ReceiverEmulatorTest` drives it through the real connector and `ReceiverState`.

## Architecture

- **Transport** (`de.csmp.jeiscp`): `EiscpProtocolHelper` wraps an ISCP string in an eISCP frame and parses it back. The frame consists of:
  - the `ISCP` magic,
  - header size 0x10,
  - the data size as a big-endian int,
  - version `01 00 00 00`,
  - the `!1…` payload, terminated by CR LF.

  `EiscpConnector` owns the TCP socket.
  - The constructor only stores host and port. `connect()` makes the first connection and throws if it fails (5 s timeout).
  - When the connection is lost later, the connector reconnects in the background, pausing 1 s, doubling up to 30 s, until `close()`.
  - `ConnectionState` (`DISCONNECTED`, `CONNECTING`, `CONNECTED`, `LOST`, `CLOSED`) goes to `addStateListener` listeners. Message listeners stay registered across reconnects.
  - `sendIscpCommand("MVL20")` prepends `!1` itself; `sendIscpMessage` expects the `!1` to be there already. Both throw `IOException` while the connection is lost.
- **Autodiscovery** (`EiscpAutodiscovery`, used by `EiscpConnector.autodiscover()`):
  - It sends `!xECNQSTN` by UDP to every interface's directed broadcast address plus 255.255.255.255. Windows sends the limited broadcast out of one interface only, and that can be a Hyper-V, WSL or VPN adapter.
  - It resends every second and gives up with a `SocketTimeoutException` after 30 s. Answers that are not `ECN…` are ignored.
  - The constructor with targets, port, interval and timeout exists for tests.
- **Receiving is listener-based.** Every connection gets its own daemon `EiscpConnectorSocketReaderThread`, which blocks on the socket and reports an end of stream or I/O error back to the connector; the connector passes messages on to its listeners. After a malformed frame (wrong header size or version, data size outside 2 bytes..64 KiB, no `!1`, empty message) it resyncs on the next `ISCP` marker, and the connection stays up. Listeners get the message without the `!1` prefix and without trailing terminators (e.g. `MVL16`). Callbacks run on this reader thread, not on the JavaFX thread. Sending is synchronized because the console, the GUI and listener callbacks all send.
- **Command catalogue** (`de.csmp.jeiscp.eiscp`): `EiscpCommandsParser` parses the YAML zones `main` and `dock` once, on first use, through a holder class; class initialization makes that thread-safe for the reader, FX and main threads. All maps, lists and each block's value set are unmodifiable. Upstream keeps the NET/USB commands (`NTC`, `NLS`, `NJA`, …) in `dock`; `zone2`–`zone4` are ignored. The maps are:
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
  - `ReceiverState`: a GUI-independent model. It is an `EiscpListener` that turns incoming messages into power, volume, mute, input, now playing, play status, repeat and shuffle (`NST`), the NET list and cover art.
    - It keeps the last value of every 3-letter command (`getLastValue`).
    - It notifies its listeners with an `Aspect`, on the reader thread. Messages without a dedicated aspect fire `OTHER`.
  - `ReceiverControl`: GUI-independent. It holds the remote's operations as ISCP commands, so views never build command strings or import `EiscpCommmandsConstants`.
    - Operations: power/mute toggles from `ReceiverState`, volume as hex with an echo check, direction keys/OK/return/home per `Target` NET (`NTC…`) or TV (`OSD…`), playback, repeat and shuffle (`NTCREPEAT`, `NTCRANDOM`; the receiver steps through the service's modes), number keys and queries.
    - Sent-listeners see every command; the header LED uses them.
    - `queryNowPlayingIfMissing` (on input changes) asks for artist, title, album, time and play status.
    - `requestCoverIfMissing` (on now-playing updates) sends `NJAREQ` only while time and title are known, i.e. something plays, and once per title. The TX-NR626 (2013 firmware) does not answer `NJAREQ`; it only pushes the cover itself, as BMP, when the track changes. It usually sends the previous cover once more first. That is why `ReceiverState` hides the cover after a title change, ignores a transfer identical to the old cover, and shows the old cover again after `STALE_COVER_TIMEOUT` (15 s), since tracks of one album share their cover.
  - `DeviceFunctions`: GUI-independent. It lists the command blocks a model supports (`EiscpDevice.isCapable`), reduced to literal values without `QSTN` or placeholders. The YAML's model sets are coarse: `set1` covers almost everything, so a TX-NR626 is offered Sirius/XM/HD Radio as well.
  - `fx/FxGuiController`: starts JavaFX through `Ui.onFxThread` (`Platform.startup`, or `runLater` once the `StartupWindow` started it), so `ThreadedConsoleApp` does not extend `Application`. `ThreadedConsoleApp` hides the `StartupWindow` only after `start()`, because JavaFX ends with its last window.
    - Its constructor registers `ReceiverState` on the connector, so `ThreadedConsoleApp` creates it before `connect()`: right after connecting, the receiver pushes `NLT` with the NET service unasked.
    - It sends the initial queries, and sends them again after every reconnect.
    - It mirrors the connection state into `ReceiverState`, so the display shows OFFLINE or CONNECTING.
    - It marshals state changes to the FX thread with `Platform.runLater`. The stage is `StageStyle.TRANSPARENT`, i.e. borderless. Its close request exits the JVM.
  - `fx/RemoteControlView`: the remote-control UI, built in code with no FXML and styled by `src/main/resources/de/csmp/jeiscp/app/fx/remote.css`.
    - It only assembles the parts and owns the borderless scene.
    - It maps each `ReceiverState.Aspect` to the parts' `refresh…` methods and holds the keyboard shortcuts.
    - Its parts, all package-private in `fx`:
    - `HeaderBar`: power key, the traffic LED (flashes on every `ReceiverControl` command, opens the log), model, the address link, and the minimize and close keys. The close key fires the stage's close request. The link opens `http://<ip>/` via `Browser`, which uses the platform launcher (`rundll32`, `open` or `xdg-open`) instead of AWT.
    - `DisplayPanel`: the LCD with input, volume bar, now playing, NET list or cover art. The TX-NR626 sends 128×128 covers; up to `COVER_SIZE` they show unscaled, larger ones are fitted, and the list's height leaves room for them. The NET service comes from `ReceiverState.getNetService()`: the `NLT` service type, its icon field, or, on old firmware that keeps `F3` inside a service, the list title below the top level. At the bottom sit the elapsed/total time and a playback position bar from `ReceiverState.getPlayTime()`, parsed from `NTM`. A transparent slider over the bar (thumb shown on hover) seeks with `ReceiverControl.seek` (`NTS` with `mm:ss` or `hh:mm:ss`) only on mouse release; pressing and dragging just preview the target time, because dragging along a slider track changes its value on every mouse move. The `updatingSeek` and `seeking` flags keep the receiver's time updates from sending a seek back or moving the thumb under the mouse. Its space is always reserved, so the layout does not jump. The panel also builds the window title.
      - The `updatingVolume` flag stops the slider from sending the receiver's echo back.
      - NET popups (`NPU`, parsed in `ReceiverState.parsePopup`, NUL-separated) cover the list. They close on click, on OK/Return, on a new list page or input, or after 30 s.
    - `KeyPad`: all keys below the display, the input list (`SOURCES`) and the current `ReceiverControl.Target` NET/TV.
      - The target decides whether the direction pad, OK, return and home keys (and the keyboard shortcuts) act on `NTC…` or `OSD…`. Its switch is two small keys in the pad's top corners; the OK key shows the target.
      - The app decides the target; the receiver reports no mode. NET is the default and returns with every new input; SETUP (`OSDMENU`), QUICK and the TV key switch to TV.
      - The `OSD` commands are plain key presses: the receiver neither answers them nor reports its TV menus.
      - Play, pause and stop light up with `ReceiverState.getPlayStatus()`, repeat and shuffle while they are on. The TX-NR626 does not answer `NSTQSTN` and sends `NST` only on a change, so at start `ReceiverState` infers PLAY from a running `NTM` (it stops in pause); repeat and shuffle stay unknown until the first `NST`. All of them only on a NET input; repeat and shuffle are disabled elsewhere and when the service reports none (`x`).
    - `WindowDrag`: moves a borderless window while it is dragged on a non-control area; also used by `LogWindow`.
    - `Ui`: key, icon and spacer helpers. Icons are SVG paths on a 24×24 grid.
    - Power, mute and volume send a command and then wait for the receiver's echo to update the UI.
  - `LogBuffer`: a log4j appender on the root logger that keeps the last 5000 entries for the GUI. `ThreadedConsoleApp` installs it before autodiscovery when the GUI is on.
    - It recognizes ISCP traffic by the message prefixes `sendIscpCommand: ` (from `EiscpConnector`) and `received: ` (from the reader thread). Changing those log texts breaks the →/← marking.
    - `subscribe` returns the history and passes on later entries, with no gap between the two.
  - `fx/LogWindow`: borderless, owned by the remote. Clicking the header LED or pressing L opens it. It batches entries from the logging thread into one `runLater` and offers FOLLOW, ISCP ONLY, copy (Ctrl+C for the selection), clear and a resize grip. Below the log is a command line: Enter sends through `ReceiverControl.sendTyped` (drops a leading `!1`), up/down browse the earlier commands of this run, the first Escape empties the line.
  - `fx/FunctionsPage`: opened by the "⋯" key. A searchable list of `DeviceFunctions`; selecting a function sends its `QSTN` and shows the current value.
  - The app icon PNGs in `src/main/resources/de/csmp/jeiscp/app/fx/icons/` are rendered by `src/test/java/de/csmp/jeiscp/app/fx/IconGenerator.java`, which is a standalone `java` program, not a test.

