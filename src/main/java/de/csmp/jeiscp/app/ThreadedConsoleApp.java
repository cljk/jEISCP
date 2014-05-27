package de.csmp.jeiscp.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;


public class ThreadedConsoleApp {
	private static final Logger log = LoggerFactory.getLogger(ThreadedConsoleApp.class);
	
	private static final boolean ENABLE_GUI = true;
	
	
	public static void main(String[] args) throws Exception {
		// init connection
		EiscpConnector conn = EiscpConnector.autodiscover();
        //EiscpConnector conn = new EiscpConnector("192.168.2.15");
        
		
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
