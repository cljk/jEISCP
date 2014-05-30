package de.csmp.jeiscp.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;


public class ThreadedConsoleApp {
	private static final Logger log = LoggerFactory.getLogger(ThreadedConsoleApp.class);
	
	private static final boolean ENABLE_GUI = true;
	
	
	public static void main(String[] args) throws Exception {
		// init connection
		
		
		String address = null;
		EiscpConnector conn = null;
		
		for (int i=0; i<args.length; i++) {
			if ("-a".equals(args[i])) {
				i++;
				address = args[i];
			}
		}
		
		if (address == null) {
			conn = EiscpConnector.autodiscover();
		} else {
			conn = new EiscpConnector(address);
		}
		
		
		ConsoleController consoleController = new ConsoleController(conn);
		Thread consoleThread = new Thread(consoleController);
		consoleThread.start();
		
		if (ENABLE_GUI) {
			AppGuiController guiController = new AppGuiController(conn);
        	Thread guiThread = new Thread(guiController);
        	guiThread.start();
        }
		
		log.debug("controller threads started");
	}    
}
