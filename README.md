jEISCP
======

jEISCP is a Java implementation of the eISCP-protocol used by various network enabled Onkyo-/Integra- AV-receivers (and other devices - but focus here is on AV-receivers). With jEISCP it is possible to control volume, source selection and so on.

_ISCP_ (Integra Serial Control Protocol) is called the message format for serial connections to the devices. _eISCP_ (ISCP over ethernet) is the ethernet (TCP/IP) encapsulation of ISCP - used for WLAN and LAN (default port 60128). _ISCP_ and _eISCP_ are described in a technical documentation maintained by Onkyo (google for "onkyo iscp technical documentation").

### Status
[![Build Status](https://travis-ci.org/cljk/jEISCP.png)](https://travis-ci.org/cljk/jEISCP)

Screenshots
===========
**Console**

![Screenshot of console](https://raw.githubusercontent.com/cljk/jEISCP/master/src/main/img/screenshot_console.png)

**GUI**

![Screenshot of GUI](https://raw.githubusercontent.com/cljk/jEISCP/master/src/main/img/screenshot_gui.png)




Usage
=====
You can use the source code "as it is" on your own risk. I´ll not be responsible for any damages to your devices/computer.


Check out the repository as usual

    git clone https://github.com/cljk/jEISCP.git

and then checkout the used submodule

    cd jEISCP

    git submodule update --init

You need Java 21 and a working [Maven] installation to run this application.

    .\run.ps1

on Windows(TM) (if script execution is blocked: `powershell -ExecutionPolicy Bypass -File run.ps1`) or 

    ./run.sh
    
on nearly every other OS like Linux/OS X.
The autodiscovery broadcasts on every network interface and repeats the query every second. If no receiver answers within 30 seconds, it gives up with a hint. In that case, make sure the receiver is on or has network standby enabled.
To skip the autodiscovery process you can add the IP-address of your AV-receiver with the "-a" parameter like

    .\run.ps1 -a 192.168.2.29

Both scripts do the following:
- They check for Java 21 and Maven. The JDK from `JAVA_HOME` is used for both build and run; without `JAVA_HOME`, the one on the PATH is used.
- They fetch the submodule if it is missing.
- They let Maven build the project; JavaFX is pulled from Maven Central.
- They start Java directly in your terminal, so Ctrl+C ends the application.

`Get-Help .\run.ps1` lists the parameters.

Startup
-------
After some output of maven the console will dump several log outputs like the autodiscover process... (assuming that at least one Onkyo device is available)

    [DEBUG,EiscpConnector] send autodiscover datagram: !xECNQSTN
    [INFO,EiscpConnector] wait for autodiscover answere
    [DEBUG,EiscpConnector] answere from /192.168.2.111:60128
    ...
    [DEBUG,EiscpConnector] autodiscovered: ECNTX-NR609/60128/XX/002719B23FAA
    [DEBUG,EiscpConnector] connect to 192.168.2.111:60128
    [DEBUG,EiscpConnector] connected

Then some status question commands will be sent and answeres of the receiver will be printed out (idented with >>).

    [DEBUG,EiscpConnector] sendCommand: PWRQSTN
    [DEBUG,EiscpConnector] sendCommand: MVLQSTN
    [DEBUG,EiscpConnector] sendCommand: IFVQSTN
    [DEBUG,EiscpConnector] sendCommand: RESQSTN
    >> PWR00
    >> IFV
    >> MVL16
    [DEBUG,EiscpConnector] sendCommand: MVL16
    >> RES05
    >> MVL16
    
Basically means: 
Asking for power status (PWRQSTN), main volume (MVLQSTN) and so on. Answere PWR00 means "power is off" and "MVL16" means "main volume is on 0x16 (hex)"

After initialization you have the possibility to enter ISCP-commands on the console - like MVL20 to change volume - and see the response. The application will build the datagram with checksum encapsulate it in EISCP and send it to the receiver.

Also a JavaFX GUI styled like a remote control will show up. The window is borderless:
- Drag the remote to move it.
- The keys at its top right minimize or quit.

It shows the NET list and cover art and has keys for:
- power, input selection and volume,
- NET/USB navigation and playback,
- SETUP and QUICK, which open the receiver's menus on the TV.

The small NET and TV keys at the top corners of the direction pad decide what it controls:
- NET: the NET/USB lists.
- TV: the receiver's menus on the TV.

The OK key shows the current target. It follows the input until you switch it.

Messages of NET services, such as a TuneIn connection error, appear in the display. A click, OK or Return closes them.

The "⋯" key opens all functions that the connected model supports according to the command catalogue, e.g. listening mode or dimmer. They are searchable and show their current value.

The header shows the receiver's model and IP address. Clicking the address opens the receiver's web interface in your browser, e.g. http://192.168.0.97/.

The small LED in the header blinks with every command sent. Clicking it (or pressing L) opens the log window:
- It shows the complete log live, with commands sent (→) and received (←) highlighted.
- It can be filtered to ISCP traffic only and copied.

Keyboard shortcuts:
- arrow keys, Enter and Backspace: navigation
- +/-: volume
- M: mute
- L: log window
- digits: number keys



Status/Version
==============

**pre-pre-pre-alpha** 
2.0-SNAPSHOT

Encoding/Decoding of EISCP-messages for my receiver (TX-NR609 / TX-NR626) is currently working including a simple autodiscovery mechanism. 

For sure there is a lot of work to do - f.e. the GUI still misses functions like direct selection of list elements and so on.

My main focus is currently on enhancing the gui.


References
==========

For (E)ISCP you can compare to the official Documentation (which this project is mainly based on). Also there are other projects with a comparable topic.

* https://github.com/miracle2k/onkyo-eiscp (Python implementation with further references). This project is also used as submodule mainly for using the YAML file describing ISCP-command-capabilities of the different receiver models (to be done).



License
-
need further thinking on that - but for now and until different is stated the source code is licenced under
**[GPL v3]**


  [maven]: http://maven.apache.org/
  [gpl v3]: http://www.gnu.org/licenses/gpl.html

    
