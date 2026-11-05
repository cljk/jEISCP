package de.csmp.jeiscp.app;

import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpAutodiscovery;
import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.fx.FxGuiController;


public class ThreadedConsoleApp {
	private static final Logger log = LoggerFactory.getLogger(ThreadedConsoleApp.class);
	
	
	
	
	public static void main(String[] args) throws Exception {
		// init connection
		
		
		String address = null;
		EiscpConnector conn = null;

		boolean ENABLE_GUI = true;
		boolean ENABLE_CONSOLE = true;

		
		for (int i=0; i<args.length; i++) {
			if ("-a".equals(args[i])) {
				i++;
				address = args[i];
			} else if ("-nogui".equals(args[i])){
				ENABLE_GUI = false;
			} else if ("-noconsole".equals(args[i])){
				ENABLE_CONSOLE = false;
			} else if ("-?".equals(args[i])){
				displayParameterHelp();
				
				System.exit(0);
			} else {
				System.err.println("unrecognized parameter " + args[i] + "\n");
				
				displayParameterHelp();
				
				System.exit(1);
			}
		}
		
		
		if (ENABLE_GUI) {
			LogBuffer.install();	// from the start, so the GUI's log window also shows the autodiscovery
		}

		EiscpDevice device = null;	// model, for the functions the GUI offers
		if (address == null) {
			try {
				device = new EiscpAutodiscovery().discover();
			} catch (SocketTimeoutException ex) {
				System.err.println(ex.getMessage() + ". Is the receiver on (or network standby enabled)"
						+ " and in this network? Otherwise connect directly with -a {ip-address}.");
				System.exit(1);
			}
			conn = new EiscpConnector(device.getIpAddress(), device.getPort());
		} else {
			conn = new EiscpConnector(address);
			if (ENABLE_GUI) {
				device = EiscpAutodiscovery.identify(address);
			}
		}
		
		
		if (ENABLE_CONSOLE) {
			ConsoleController consoleController = new ConsoleController(conn, !ENABLE_GUI);
			Thread consoleThread = new Thread(consoleController);
			consoleThread.start();
		}
		
		if (ENABLE_GUI) {
			new FxGuiController(conn, device).start();
        }
		
		log.debug("controller threads started");
	}




	private static void displayParameterHelp() {
		System.out.println("available parameters:");
		System.out.println("\t-?             \t\tdisplay parameter help (this page)");
		System.out.println("\t-a {ip-address}\t\tspecify ip-adress, no autodiscovery");
		System.out.println("\t-nogui         \t\tdisable gui");
		System.out.println("\t-noconsole     \t\tdisable console (manual ISCP in-/output)");
	}    
}
