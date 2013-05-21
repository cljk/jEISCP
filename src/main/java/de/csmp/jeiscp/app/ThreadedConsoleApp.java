package de.csmp.jeiscp.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpListener;


/**
 * Hello world!
 *
 */
public class ThreadedConsoleApp implements EiscpListener {
	private static final Log log = LogFactory.getLog(ThreadedConsoleApp.class);
	
	private static final boolean ENABLE_GUI = true;
	
	AppGuiController gui = null;
	
	public static void main(String[] args) {
		ThreadedConsoleApp app = new ThreadedConsoleApp();
		app.start();
	}    
    
    public void start() {
    	try {
            System.out.println( ThreadedConsoleApp.class.getName() );
            System.out.println( StringUtils.repeat('=', 60));
            
            EiscpConnector conn = EiscpConnector.autodiscover();
            //EiscpConnector conn = new EiscpConnector("192.168.2.15");
            
            conn.attachListener(this);	// starts background thread for blocking reads
            if (ENABLE_GUI) {
            	gui = new AppGuiController(conn);
            }
            
            // get some infos on start
            conn.sendCommand("PWRQSTN");
            conn.sendCommand("MVLQSTN");
            conn.sendCommand("IFVQSTN");
            conn.sendCommand("RESQSTN");

            Thread.sleep(500);	// wait for results displayed by background thread
            
            if (ENABLE_GUI) {
            	gui.show();
            }
            
            System.out.println( StringUtils.repeat('-', 60));
            System.out.println("Enter quit or ISCP commands like...");
            System.out.println("\tPWRQSTN\n\tPWR01\n\tPWR00\n\tMVLQSTN\n\tMVL{0-100}\n\tIFAQSTN\n\tIFVQSTN\n\tRESQSTN");
            System.out.println("For command reference compare to manual");
            System.out.println( StringUtils.repeat('-', 60));
            
            
            // start console loop - asking for input (commands)
            BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
            boolean quit = false;
            while (! quit) {
            	String input = bin.readLine();
            	if ("quit".equalsIgnoreCase(input)) {
            		System.out.println("bye");
            		quit = true;
            	} else {
            		conn.sendCommand(input);
            	}
            }
            
            conn.close();
            System.exit(0);
    	} catch (Exception ex) {
    		log.error(ex);
    	}
    }
    
    
    /**
     * just display responses to console
     */
    @Override
	public void receivedIscpMessage(String message) {
		if (message.startsWith("NJA")) {
			if (message.startsWith("NJA00")) {
				System.out.print(">> NJAxx receive image.");
			} else if (message.startsWith("NJA01")) {
				System.out.print(".");
			} else if (message.startsWith("NJA02")) {
				System.out.print("done");
			}
		} else {
	    	System.out.println(">> " + message);
		}
		
		if (gui != null) {
			gui.receivedIscpMessage(message);
		}
	}
}
