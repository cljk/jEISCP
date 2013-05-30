package de.csmp.jeiscp.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import de.csmp.jeiscp.EiscpConnector;
import de.csmp.jeiscp.EiscpProtocolHelper;


public class BlockingDumbConsoleApp 
{
    public static void main( String[] args )
    {
    	try {
            System.out.println( BlockingDumbConsoleApp.class.getName() );
            System.out.println( StringUtils.repeat('=', 30));
            
            EiscpConnector conn = new EiscpConnector("192.168.2.15");
        
            Thread.sleep(100);
            if (conn.available() > 0) {
            	byte[] msg = conn.readMessage();
            	String iscpResult = EiscpProtocolHelper.interpreteEiscpResponse(msg);
                System.out.println("greeting: " + iscpResult);
            }
            	
            
            BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
            
            sendCommandDisplayResult(conn, "PWRQSTN");
            sendCommandDisplayResult(conn, "MVLQSTN");
            sendCommandDisplayResult(conn, "IFVQSTN");
            
            
            System.out.println("ready");
            System.out.println("Enter quit or command like...");
            System.out.println("\tPWRQSTN PWR01 PWR00 MVLQSTN MVL{0-100} IFAQSTN IFVQSTN RESQSTN");
            
            String input;
            boolean quit = false;
            while (! quit) {
            	input = bin.readLine();
            	if ("quit".equals(input)) {
            		System.out.println("bye");
            		quit = true;
            	} else {
            		conn.readMessage();
                    
            		sendCommandDisplayResult(conn, input);
            	}
            }
            
    	} catch (Exception ex) {
    		System.err.println("Fehler");
    		ex.printStackTrace();
    	}
        
    }

	public static void sendCommandDisplayResult(EiscpConnector conn,
			String input) throws IOException, InterruptedException, Exception {
		conn.sendIscpCommand(input);
		Thread.sleep(200);
		
		byte[] message = conn.readMessage();
		if (message != null) {
			String iscpResult = EiscpProtocolHelper.interpreteEiscpResponse(message);
			System.out.println("iscpResult: " + iscpResult);
		} else {
			System.out.println("no result");
		}
	}
}
