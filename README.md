jEISCP
======

jEISCP is a Java implementation of Onkyo (E)ISCP-protocol used by various network enabled (Onkyo-) receivers to control volume, source selection and so on.

ISCP is called the message format used by Onkyo for already previously available serial connections to the devices. EISCP is the ethernet (TCP/IP) encapsulation of ISCP - used for WLAN and LAN.


Screenshots
=
**Console**

![Screenshot of console](https://raw.githubusercontent.com/cljk/jEISCP/master/src/main/img/screenshot_console.png)

**GUI**

![Screenshot of GUI](https://raw.githubusercontent.com/cljk/jEISCP/master/src/main/img/screenshot_gui.png)




Usage
=
Check out the repository as usual

    git clone https://github.com/cljk/jEISCP.git

and then checkout the used submodule

    cd jEISCP

    git submodule update --init

You need a working [Maven] installation to run this application.

    run.bat

on Windows(TM) or 

    ./run.sh
    
on nearly every other OS like Linux/OS X.

Both just call "mvn exec:java" with the main class.

Startup
-
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

Also a basic GUI with a volumeslider will show up.



Status/Version
=

**pre-pre-pre-alpha** 
1.0-SNAPSHOT

Encoding/Decoding of EISCP-messages for my receiver (TX-NR609) is currently working including a simple autodiscovery mechanism. 

For sure there is a lot of work to do - f.e. the application implementation is *very* basic and more or less only capable of switching on/off, control volume or to send/receive textual commands.

References
=

For (E)ISCP you can compare to the official Documentation (which this project is mainly based on). Also there are other projects with a comparable topic.

* https://github.com/miracle2k/onkyo-eiscp (Python implementation with further references). This project is also used as submodule mainly for using the YAML file describing ISCP-command-capabilities of the different receiver models (to be done).



License
-
need further thinking on that - but for now and until different is stated the source code is licenced under
**[GPL v3]**


  [maven]: http://maven.apache.org/
  [gpl v3]: http://www.gnu.org/licenses/gpl.html

    
