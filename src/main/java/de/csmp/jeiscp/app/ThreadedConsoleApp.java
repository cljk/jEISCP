package de.csmp.jeiscp.app;

import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpListener;
import de.csmp.jeiscp.eiscp.Command;
import de.csmp.jeiscp.eiscp.EiscpCommandsParser;
import de.csmp.jeiscp.eiscp.EiscpCommmandsConstants;


public class ThreadedConsoleApp implements EiscpListener {
	private static final Logger log = LoggerFactory.getLogger(ThreadedConsoleApp.class);
	
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
            	conn.attachListener(gui);
            	
            	// get some infos on start
            	conn.sendIscpCommand(SYSTEM_POWER_QUERY_ISCP);
            	conn.sendIscpCommand(MASTER_VOLUME_QUERY_ISCP);
            	conn.sendIscpCommand(VIDEO_INFOMATION_QUERY_ISCP);
            	conn.sendIscpCommand(MONITOR_OUT_RESOLUTION_QUERY_ISCP);
            	conn.sendIscpCommand(INPUT_SELECTOR_QUERY_ISCP);

            	Thread.sleep(200);	// wait for results displayed by background thread

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
            		conn.sendIscpCommand(input);
            	}
            }
            
            conn.close();
            System.exit(0);
    	} catch (Exception ex) {
    		log.error(ex.getMessage(), ex);
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
