package de.csmp.jeiscp.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;


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
				System.out.println("available parameters:");
				System.out.println("\t-a {ip-address}\t\tspecify ip-adress, no autodiscovery");
				System.out.println("\t-nogui         \t\tdisable gui");
				System.out.println("\t-noconsole     \t\tdisable console (manual ISCP in-/output)");
				
				System.exit(0);
			} else {
				System.err.println("unrecognized parameter " + args[i]);
				System.exit(1);
			}
		}
		
		
		if (address == null) {
			conn = EiscpConnector.autodiscover();
		} else {
			conn = new EiscpConnector(address);
		}
		
		
		if (ENABLE_CONSOLE) {
			ConsoleController consoleController = new ConsoleController(conn);
			Thread consoleThread = new Thread(consoleController);
			consoleThread.start();
		}
		
		if (ENABLE_GUI) {
			AppGuiController guiController = new AppGuiController(conn);
        	Thread guiThread = new Thread(guiController);
        	guiThread.start();
        }
		
		log.debug("controller threads started");
	}    
}
