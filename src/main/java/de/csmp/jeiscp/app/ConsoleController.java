package de.csmp.jeiscp.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpListener;
import de.csmp.jeiscp.eiscp.Command;
import de.csmp.jeiscp.eiscp.EiscpCommandsParser;

public class ConsoleController implements Runnable, EiscpListener {
	private static final Logger log = LoggerFactory.getLogger(ConsoleController.class);
	
	EiscpConnector conn = null; 
	
	public ConsoleController(EiscpConnector conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void run() {
		try {
    		System.out.println( this.getClass().getName() );
            System.out.println( StringUtils.repeat('=', 60));
            
            System.out.println( StringUtils.repeat('-', 60));
            System.out.println("Enter quit or ISCP commands like...");
            System.out.println("\tPWRQSTN\n\tPWR01\n\tPWR00\n\tMVLQSTN\n\tMVL{0-100}\n\tIFAQSTN\n\tIFVQSTN\n\tRESQSTN");
            System.out.println("For command reference compare to manual");
            System.out.println( StringUtils.repeat('-', 60));

            conn.addListener(this);	// starts background thread for blocking reads
            
            // start console loop - asking for input (commands)
            BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
            boolean quit = false;
            while (! quit) {
            	String input = bin.readLine();
            	if ("quit".equalsIgnoreCase(input)) {
            		System.out.println("bye");
            		quit = true;
            	} else {
            		conn.sendIscpCommand(input);
            	}
            }
            
            conn.close();
            System.exit(0);
    	} catch (Exception ex) {
    		log.error(ex.getMessage(), ex);
    		System.exit(1);
    	}
	}
	
	
	/**
     * just display responses to console
     */
    @Override
	public void receivedIscpMessage(String message) {
    	Command cmd = EiscpCommandsParser.getCommandByIscp(message);
    	if (message.length() > 50) {
			System.out.println(">> " + message.substring(0, 47) + " [...]");
		} else if (cmd != null) {
			System.out.println(">> " + cmd);
    	} else {
	    	System.out.println(">> " + message);
		}		
	}
}
