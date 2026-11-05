package de.csmp.jeiscp.app;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.csmp.jeiscp.app.ReceiverState.Aspect;

public class ReceiverStateTest {

	ReceiverState state;
	List<Aspect> fired;

	@Before
	public void setup() {
		state = new ReceiverState();
		fired = new ArrayList<>();
		state.addListener(fired::add);
	}

	@Test
	public void basicValues() {
		state.receivedIscpMessage("PWR01");
		state.receivedIscpMessage("MVL1A");
		state.receivedIscpMessage("AMT00");
		state.receivedIscpMessage("SLI2B");

		assertTrue(state.getPower());
		assertEquals(Integer.valueOf(26), state.getVolume());
		assertFalse(state.getMuted());
		assertEquals("2B", state.getInput());
		assertEquals(List.of(Aspect.POWER, Aspect.VOLUME, Aspect.MUTE, Aspect.INPUT), fired);
	}

	@Test
	public void keepsLastValueOfEveryCommand() {
		state.receivedIscpMessage("LMD0C");
		state.receivedIscpMessage("PWR01");

		assertEquals("0C", state.getLastValue("LMD"));
		assertEquals("01", state.getLastValue("PWR"));
		assertNull(state.getLastValue("DIM"));
		assertEquals(List.of(Aspect.OTHER, Aspect.POWER), fired);
	}

	@Test
	public void netPopupWithButton() {
		state.receivedIscpMessage("NPUTTuneIn\0Verbindungsfehler.\0Die Verbindung zum Server konnte nicht hergestellt werden.\0" + "1OK\0");

		ReceiverState.Popup popup = state.getPopup();
		assertEquals("TuneIn", popup.topTitle());
		assertEquals("Verbindungsfehler.", popup.title());
		assertEquals("Die Verbindung zum Server konnte nicht hergestellt werden.", popup.message());
		assertEquals(List.of("OK"), popup.buttons());
		assertEquals(List.of(Aspect.POPUP), fired);
	}

	@Test
	public void netPopupWithoutButtonsAndListType() {
		state.receivedIscpMessage("NPUBService\0Title\0Message\0" + "0");
		assertEquals(List.of(), state.getPopup().buttons());

		state.receivedIscpMessage("NPULSetup\0Server\0on\0Port\0" + "8080\0");
		assertEquals("Setup", state.getPopup().topTitle());
		assertEquals("Server: on\nPort: 8080", state.getPopup().message());
	}

	@Test
	public void popupClosesWithNewListPageOrInput() {
		state.receivedIscpMessage("NPUTTuneIn\0Error\0Text\0" + "0");
		state.receivedIscpMessage("NLSC0P");
		assertNull(state.getPopup());

		state.receivedIscpMessage("NPUTTuneIn\0Error\0Text\0" + "0");
		state.receivedIscpMessage("SLI10");
		assertNull(state.getPopup());

		state.receivedIscpMessage("NPUTTuneIn\0Error\0Text\0" + "0");
		state.dismissPopup();
		assertNull(state.getPopup());
	}

	@Test
	public void invalidVolumeIsIgnored() {
		state.receivedIscpMessage("MVLN/A");

		assertNull(state.getVolume());
		assertTrue(fired.isEmpty());
	}

	@Test
	public void netListPageLinesAndCursor() {
		state.receivedIscpMessage("NLSC0P");
		state.receivedIscpMessage("NLSU0-My Music");
		state.receivedIscpMessage("NLSU1-Internet Radio");
		state.receivedIscpMessage("NLSC1C");

		assertEquals(List.of("My Music", "Internet Radio"), state.getNetLines());
		assertEquals(1, state.getNetCursor());

		state.receivedIscpMessage("NLSC0P");
		assertTrue(state.getNetLines().isEmpty());
	}

	@Test
	public void coverArtIsAssembledFromBlocks() {
		state.receivedIscpMessage("NJA10FFD8");
		state.receivedIscpMessage("NJA11FFE0");
		assertNull(state.getCoverArt());

		state.receivedIscpMessage("NJA12FFD9");
		assertArrayEquals(new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, (byte) 0xFF, (byte) 0xD9 },
				state.getCoverArt());
		assertTrue(state.isCoverArtCurrent());

		state.receivedIscpMessage("NLSC0P");
		assertFalse(state.isCoverArtCurrent());
	}

	@Test
	public void coverArtWithoutFirstBlockIsDropped() {
		state.receivedIscpMessage("NJA11FFE0");
		state.receivedIscpMessage("NJA12FFD9");

		assertNull(state.getCoverArt());
	}
}
