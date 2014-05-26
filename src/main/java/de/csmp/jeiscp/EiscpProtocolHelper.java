package de.csmp.jeiscp;

import static de.csmp.jeiscp.EiscpConstants.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EiscpProtocolHelper {
	private static final Logger log = LoggerFactory.getLogger(EiscpProtocolHelper.class);

	
	/*
	 * ISCP encapsulated in Ethernet (eISCP)
	 */
	public static byte[] iscpToEiscpMessage(String message) throws IOException {
		byte[] messageBytes = message.getBytes();
		
		int dataSize = messageBytes.length + 2;
		if (dataSize > 255) throw new IOException("dataSize > 255 not implemented");
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write("ISCP".getBytes());
		
		// header size - fixed
		bos.write(0);
		bos.write(0);
		bos.write(0);
		bos.write(0x10);
		
		// dataSize - always < 255
		bos.write(0);
		bos.write(0);
		bos.write(0);
		bos.write(dataSize);
		
		// begin eISCP block
		bos.write(1);	// eISCP version 1
		bos.write(0);	// reserved block
		bos.write(0);	// reserved block
		bos.write(0);	// reserved block
		
		bos.write(messageBytes);
		
		bos.write(CR);
		bos.write(LF);
		
		
		byte[] eiscpMessage = bos.toByteArray();
		return eiscpMessage;
	}
	
	/**
	 * eISCP -> ISCP
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	public static byte[] parseEiscpToIscpMessage(byte[] response) throws Exception {
		validateIscpSignature(response, 0);
		validateHeaderLengthSignature(response, 4);
		int messageSize = readMessageSize(response, 8);
		
		validateEiscpVersion(response, 12);
		
		int messageOffset = 12 + 4;
		//messageSize = messageSize - 4; // skip version
		byte[] messageBytes = new byte[messageSize];

		System.arraycopy(response, messageOffset, messageBytes, 0, messageSize);
		return messageBytes;
	}

	public static void validateEiscpVersion(byte[] response, int offset)
			throws EiscpMessageFormatException {
		if (
				(response[offset++] != 0x01) ||	// version
				(response[offset++] != 0x00) ||	
				(response[offset++] != 0x00) ||	
				(response[offset++] != 0x00)		
				) {
			throw new EiscpMessageFormatException("illegal version != 01 00 00 00");
		}
	}

	public static int readMessageSize(byte[] response, int offset)
			throws EiscpMessageFormatException {
		if (
				(response[offset++] != 0x00) ||
				(response[offset++] != 0x00) 
				) {
			throw new EiscpMessageFormatException("illegal message size > 0x0000ffff");
		}
		int messageSize = response[offset++] * 256 + response[offset++];
		return messageSize;
	}

	public static void validateHeaderLengthSignature(byte[] response, int offset)
			throws EiscpMessageFormatException {
		if (
				(response[offset++] != 0x00) ||
				(response[offset++] != 0x00) ||	
				(response[offset++] != 0x00) ||	
				(response[offset++] != 0x10)		
				) {
			throw new EiscpMessageFormatException("illegal header size != 0x10");
		}
	}

	public static void validateIscpSignature(byte[] response, int offset)
			throws EiscpMessageFormatException {
		if (
				(response[offset++] != 0x49) ||	// I
				(response[offset++] != 0x53) ||	// S
				(response[offset++] != 0x43) ||	// C
				(response[offset++] != 0x50)	// P
				) {
			throw new EiscpMessageFormatException("illegal signature != ISCP");
		}
	}

	public static String parseIscpMessage(byte[] iscpMessage) throws EiscpMessageFormatException {
		if ((iscpMessage[0] == 0x21)
			&& (iscpMessage[1] == 0x31)) {
			int length = iscpMessage.length;
			if (EiscpProtocolHelper.isEofMarker(iscpMessage[length-1])) {
				length -= 1;
				if (EiscpProtocolHelper.isEofMarker(iscpMessage[length-1])) {
					length -= 1;
					if (EiscpProtocolHelper.isEofMarker(iscpMessage[length-1])) {
						length -= 1;
					}
				}
			}
			
			String res = new String(iscpMessage, 2, length - 3);
			return res;
		} else {
			throw new EiscpMessageFormatException("wrong ISC signature");
		}
	}
	
	
	public static boolean isEofMarker(byte b) {
		return (
				(b == CR) ||
				(b == LF) 
				//|| (b == EOF)
			);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public static String interpreteEiscpResponse(byte[] response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("response: " + response.length + " bytes - " + EiscpProtocolHelper.convertToHexString(response));
		}
		byte[] iscpMessage = EiscpProtocolHelper.parseEiscpToIscpMessage(response);
		if (log.isDebugEnabled()) {
			log.debug("iscpMessage: " + EiscpProtocolHelper.convertToHexString(iscpMessage));
		}
		String iscpResult = EiscpProtocolHelper.parseIscpMessage(iscpMessage);
		return iscpResult;
        
	}
	
	/**
	 * converts a byte array to human readable hex representation
	 * like
	 * 49 53 43 50 00 00 00 10 00 00 00 1a 01 00 00 00 21 31 4d 56 4c 51 53 54 4e 0d
	 * @param byteArray
	 * @return
	 */
	public static String convertToHexString(byte[] byteArray) {
		String ret = "";
		for (int i=0; i<byteArray.length; i++) {
			if (i > 0) ret += " ";
			if (byteArray[i] < 16) ret += "0";
			ret += Integer.toString(byteArray[i], 16);
		}
		return ret.toUpperCase();
	}

	public static String convertToHexString(byte sByte) {
		return convertToHexString(new byte[]{ sByte });
	}
}
