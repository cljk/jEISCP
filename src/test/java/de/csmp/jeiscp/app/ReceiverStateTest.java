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

import de.csmp.jeiscp.EiscpConnector.ConnectionState;
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
	public void netServiceFromListTitle() {
		state.receivedIscpMessage("SLI2B");
		state.receivedIscpMessage("NLTF300000600090000FFFF00NE");	// NET top level
		assertNull(state.getNetService());

		state.receivedIscpMessage("NLT0A21000000010100180A00Daily Mix");	// service type Spotify
		assertEquals("Spotify", state.getNetService());
		assertEquals("Daily Mix", state.getListTitle());

		state.receivedIscpMessage("NLTF33100000001010000FF00TuneIn");	// old firmware: NET, service only in the title
		assertEquals("TuneIn", state.getNetService());

		state.receivedIscpMessage("NLTF311000000010100000E00Favorites");	// NET with the TuneIn icon
		assertEquals("TuneIn Radio", state.getNetService());

		state.receivedIscpMessage("SLI10");
		assertNull(state.getNetService());
	}

	/** As captured: right after connecting, the receiver sends NLT before the answer to SLIQSTN. */
	@Test
	public void serviceSurvivesTheFirstInputAnswer() {
		state.receivedIscpMessage("NLT0A22000000000001000A00");
		state.receivedIscpMessage("NLT0A31000000010200FF0A00Spotify");
		state.receivedIscpMessage("SLI2B");

		assertEquals("Spotify", state.getNetService());
	}

	@Test
	public void playTimeFromNtm() {
		assertNull(state.getPlayTime());

		state.receivedIscpMessage("NTM00:36/02:46");
		assertEquals(new ReceiverState.PlayTime(36, 166), state.getPlayTime());
		assertEquals(36 / 166.0, state.getPlayTime().progress(), 1e-9);

		state.receivedIscpMessage("NTM01:02:03/02:00:00");
		assertEquals(new ReceiverState.PlayTime(3723, 7200), state.getPlayTime());

		state.receivedIscpMessage("NTM00:10/--:--");	// e.g. a radio stream without length
		assertEquals(new ReceiverState.PlayTime(10, 0), state.getPlayTime());
		assertEquals(-1, state.getPlayTime().progress(), 0);

		state.receivedIscpMessage("NTM--:--/--:--");
		assertNull(state.getPlayTime());
	}

	/** As captured from a TX-NR626: on a title change the previous cover comes again first, then the new one. */
	@Test
	public void staleCoverIsHiddenUntilTheNewOneArrives() {
		MutableClock clock = new MutableClock();
		ReceiverState timed = new ReceiverState(clock);
		timed.receivedIscpMessage("NTIOld Song");
		cover(timed, "AAAA");
		assertTrue(timed.isCoverArtCurrent());

		timed.receivedIscpMessage("NTINew Song");
		assertFalse("old cover hidden", timed.isCoverArtCurrent());
		cover(timed, "AAAA");	// the repetition
		assertFalse("repetition ignored", timed.isCoverArtCurrent());
		cover(timed, "BBBB");
		assertTrue(timed.isCoverArtCurrent());
		assertArrayEquals(new byte[] { (byte) 0xBB, (byte) 0xBB }, timed.getCoverArt());
	}

	@Test
	public void sameAlbumCoverReturnsAfterTimeout() {
		MutableClock clock = new MutableClock();
		ReceiverState timed = new ReceiverState(clock);
		timed.receivedIscpMessage("NTITrack 1");
		cover(timed, "AAAA");
		timed.receivedIscpMessage("NTITrack 2");
		cover(timed, "AAAA");
		cover(timed, "AAAA");

		clock.advance(ReceiverState.STALE_COVER_TIMEOUT.minusSeconds(1));
		assertFalse(timed.isCoverArtCurrent());
		clock.advance(java.time.Duration.ofSeconds(1));
		assertTrue(timed.isCoverArtCurrent());
	}

	@Test
	public void firstTitleDoesNotHideTheCover() {
		cover(state, "AAAA");
		state.receivedIscpMessage("NTIFirst Song");

		assertTrue(state.isCoverArtCurrent());
	}

	private static void cover(ReceiverState target, String hex) {
		target.receivedIscpMessage("NJA00" + hex);
		target.receivedIscpMessage("NJA02");
	}

	static final class MutableClock extends java.time.Clock {
		java.time.Instant now = java.time.Instant.parse("2026-11-05T12:00:00Z");

		void advance(java.time.Duration duration) {
			now = now.plus(duration);
		}

		@Override
		public java.time.ZoneId getZone() {
			return java.time.ZoneOffset.UTC;
		}

		@Override
		public java.time.Clock withZone(java.time.ZoneId zone) {
			return this;
		}

		@Override
		public java.time.Instant instant() {
			return now;
		}
	}

	@Test
	public void playStatusFromNst() {
		state.receivedIscpMessage("SLI2B");
		assertNull(state.getPlayStatus());
		state.receivedIscpMessage("NSTP-S");
		assertEquals(ReceiverState.PlayStatus.PLAY, state.getPlayStatus());
		state.receivedIscpMessage("NSTp-S");	// end of a Spotify playlist
		assertEquals(ReceiverState.PlayStatus.PAUSE, state.getPlayStatus());
		state.receivedIscpMessage("NSTS--");
		assertEquals(ReceiverState.PlayStatus.STOP, state.getPlayStatus());
		state.receivedIscpMessage("NSTF--");
		assertEquals(ReceiverState.PlayStatus.PLAY, state.getPlayStatus());

		state.receivedIscpMessage("SLI10");
		assertNull("another input has its own play status", state.getPlayStatus());
	}

	/** As the TX-NR626 at start: NSTQSTN stays unanswered, the time keeps running. */
	@Test
	public void playingIsInferredFromTheRunningTime() {
		state.receivedIscpMessage("SLI2B");
		state.receivedIscpMessage("NTM01:08/03:28");
		assertNull("one time alone does not show it runs", state.getPlayStatus());
		state.receivedIscpMessage("NTM01:09/03:28");
		assertEquals(ReceiverState.PlayStatus.PLAY, state.getPlayStatus());
		assertNull("repeat is not inferred", state.getRepeat());

		state.receivedIscpMessage("NSTp-S");
		state.receivedIscpMessage("NTM01:10/03:28");
		assertEquals("a reported status wins", ReceiverState.PlayStatus.PAUSE, state.getPlayStatus());
	}

	@Test
	public void repeatFromNst() {
		state.receivedIscpMessage("SLI2B");
		assertNull(state.getRepeat());
		state.receivedIscpMessage("NSTP-S");
		assertEquals(ReceiverState.Repeat.OFF, state.getRepeat());
		state.receivedIscpMessage("NSTPRS");
		assertEquals(ReceiverState.Repeat.ALL, state.getRepeat());
		state.receivedIscpMessage("NSTP1S");
		assertEquals(ReceiverState.Repeat.ONE, state.getRepeat());
		state.receivedIscpMessage("NSTPxx");
		assertEquals(ReceiverState.Repeat.UNAVAILABLE, state.getRepeat());
		state.receivedIscpMessage("NSTP");
		assertNull(state.getRepeat());
	}

	@Test
	public void shuffleFromNst() {
		state.receivedIscpMessage("SLI2B");
		assertNull(state.getShuffle());
		state.receivedIscpMessage("NSTP--");
		assertEquals(ReceiverState.Shuffle.OFF, state.getShuffle());
		state.receivedIscpMessage("NSTp-S");
		assertEquals(ReceiverState.Shuffle.ALL, state.getShuffle());
		assertTrue(state.getShuffle().isOn());
		state.receivedIscpMessage("NSTP-A");
		assertEquals(ReceiverState.Shuffle.ALBUM, state.getShuffle());
		state.receivedIscpMessage("NSTPxx");
		assertEquals(ReceiverState.Shuffle.UNAVAILABLE, state.getShuffle());
		assertFalse(state.getShuffle().isOn());

		state.receivedIscpMessage("SLI10");
		assertNull(state.getShuffle());
	}

	@Test
	public void mirrorsConnectionState() {
		assertTrue(state.isConnected());

		state.setConnectionState(ConnectionState.LOST);

		assertFalse(state.isConnected());
		assertEquals(List.of(Aspect.CONNECTION), fired);
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
