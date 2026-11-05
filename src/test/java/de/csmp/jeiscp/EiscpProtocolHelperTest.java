package de.csmp.jeiscp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class EiscpProtocolHelperTest {

	@Test
	public void buildsEiscpFrame() throws Exception {
		byte[] frame = EiscpProtocolHelper.iscpToEiscpMessage("!1PWRQSTN");

		assertEquals("49 53 43 50 00 00 00 10 00 00 00 0B 01 00 00 00 21 31 50 57 52 51 53 54 4E 0D 0A",
				EiscpProtocolHelper.convertToHexString(frame));
	}

	@Test
	public void roundTrip() throws Exception {
		byte[] frame = EiscpProtocolHelper.iscpToEiscpMessage("!1MVL20");

		assertEquals("MVL20", EiscpProtocolHelper.interpreteEiscpResponse(frame));
	}

	@Test
	public void stripsUpToThreeTerminators() throws Exception {
		assertEquals("PWR01", EiscpProtocolHelper.parseIscpMessage(ascii("!1PWR01\u001a\r\n")));
		assertEquals("PWR01", EiscpProtocolHelper.parseIscpMessage(ascii("!1PWR01\r\n")));
		assertEquals("PWR01", EiscpProtocolHelper.parseIscpMessage(ascii("!1PWR01")));
	}

	@Test
	public void decodesUtf8Payload() throws Exception {
		byte[] message = "!1NTIBjörk\u001a".getBytes(StandardCharsets.UTF_8);

		assertEquals("NTIBjörk", EiscpProtocolHelper.parseIscpMessage(message));
	}

	@Test
	public void rejectsMessageWithoutStartCharacters() {
		assertThrows(EiscpMessageFormatException.class, () -> EiscpProtocolHelper.parseIscpMessage(ascii("PWR01")));
	}

	@Test
	public void rejectsTooShortOrEmptyMessage() {
		assertThrows(EiscpMessageFormatException.class, () -> EiscpProtocolHelper.parseIscpMessage(new byte[0]));
		assertThrows(EiscpMessageFormatException.class, () -> EiscpProtocolHelper.parseIscpMessage(ascii("!")));
		assertThrows(EiscpMessageFormatException.class, () -> EiscpProtocolHelper.parseIscpMessage(ascii("!1\u001a\r\n")));
	}

	@Test
	public void rejectsTooLongMessage() {
		assertThrows(IOException.class, () -> EiscpProtocolHelper.iscpToEiscpMessage("!1" + "X".repeat(254)));
	}

	@Test
	public void validatesHeaderFields() throws Exception {
		byte[] frame = EiscpProtocolHelper.iscpToEiscpMessage("!1PWR01");
		EiscpProtocolHelper.validateIscpSignature(frame, 0);
		EiscpProtocolHelper.validateHeaderLengthSignature(frame, 4);
		assertEquals(9, EiscpProtocolHelper.readMessageSize(frame, 8));
		EiscpProtocolHelper.validateEiscpVersion(frame, 12);

		assertThrows(EiscpMessageFormatException.class, () -> EiscpProtocolHelper.validateIscpSignature(ascii("ISCX"), 0));
		assertThrows(EiscpMessageFormatException.class, () -> EiscpProtocolHelper.validateHeaderLengthSignature(new byte[] { 0, 0, 0, 0x14 }, 0));
		assertThrows(EiscpMessageFormatException.class, () -> EiscpProtocolHelper.validateEiscpVersion(new byte[] { 2, 0, 0, 0 }, 0));
	}

	@Test
	public void intConversionIsBigEndian() {
		byte[] bytes = EiscpProtocolHelper.intToByteArray(0x01020304);

		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bytes);
		assertEquals(0x01020304, EiscpProtocolHelper.byteArrayToInt(bytes));
		assertEquals(200, EiscpProtocolHelper.byteArrayToInt(EiscpProtocolHelper.intToByteArray(200)));
	}

	@Test
	public void hexStringCoversAllByteValues() {
		assertEquals("00 0A 7F 80 FF", EiscpProtocolHelper.convertToHexString(new byte[] { 0, 10, 127, (byte) 0x80, (byte) 0xFF }));
		assertEquals("1A", EiscpProtocolHelper.convertToHexString((byte) 26));
		assertEquals("", EiscpProtocolHelper.convertToHexString(new byte[0]));
	}

	@Test
	public void recognizesTerminators() {
		assertEquals(true, EiscpProtocolHelper.isEofMarker((byte) 0x1A));
		assertEquals(true, EiscpProtocolHelper.isEofMarker((byte) '\r'));
		assertEquals(true, EiscpProtocolHelper.isEofMarker((byte) '\n'));
		assertEquals(false, EiscpProtocolHelper.isEofMarker((byte) 'A'));
	}

	private static byte[] ascii(String s) {
		return s.getBytes(StandardCharsets.US_ASCII);
	}
}
