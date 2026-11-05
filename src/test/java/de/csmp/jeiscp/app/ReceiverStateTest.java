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
		assertNull(state.netList().getService());

		state.receivedIscpMessage("NLT0A21000000010100180A00Daily Mix");	// service type Spotify
		assertEquals("Spotify", state.netList().getService());
		assertEquals("Daily Mix", state.netList().getTitle());

		state.receivedIscpMessage("NLTF33100000001010000FF00TuneIn");	// old firmware: NET, service only in the title
		assertEquals("TuneIn", state.netList().getService());

		state.receivedIscpMessage("NLTF311000000010100000E00Favorites");	// NET with the TuneIn icon
		assertEquals("TuneIn Radio", state.netList().getService());

		state.receivedIscpMessage("SLI10");
		assertNull(state.netList().getService());
	}

	/** As captured: right after connecting, the receiver sends NLT before the answer to SLIQSTN. */
	@Test
	public void serviceSurvivesTheFirstInputAnswer() {
		state.receivedIscpMessage("NLT0A22000000000001000A00");
		state.receivedIscpMessage("NLT0A31000000010200FF0A00Spotify");
		state.receivedIscpMessage("SLI2B");

		assertEquals("Spotify", state.netList().getService());
	}

	@Test
	public void playTimeFromNtm() {
		assertNull(state.playback().getPlayTime());

		state.receivedIscpMessage("NTM00:36/02:46");
		assertEquals(new NetPlayback.PlayTime(36, 166), state.playback().getPlayTime());
		assertEquals(36 / 166.0, state.playback().getPlayTime().progress(), 1e-9);

		state.receivedIscpMessage("NTM01:02:03/02:00:00");
		assertEquals(new NetPlayback.PlayTime(3723, 7200), state.playback().getPlayTime());

		state.receivedIscpMessage("NTM00:10/--:--");	// e.g. a radio stream without length
		assertEquals(new NetPlayback.PlayTime(10, 0), state.playback().getPlayTime());
		assertEquals(-1, state.playback().getPlayTime().progress(), 0);

		state.receivedIscpMessage("NTM--:--/--:--");
		assertNull(state.playback().getPlayTime());
	}

	/** As captured from a TX-NR626: on a title change the previous cover comes again first, then the new one. */
	@Test
	public void staleCoverIsHiddenUntilTheNewOneArrives() {
		MutableClock clock = new MutableClock();
		ReceiverState timed = new ReceiverState(clock);
		timed.receivedIscpMessage("NTIOld Song");
		cover(timed, "AAAA");
		assertTrue(timed.cover().isCurrent());

		timed.receivedIscpMessage("NTINew Song");
		assertFalse("old cover hidden", timed.cover().isCurrent());
		cover(timed, "AAAA");	// the repetition
		assertFalse("repetition ignored", timed.cover().isCurrent());
		cover(timed, "BBBB");
		assertTrue(timed.cover().isCurrent());
		assertArrayEquals(new byte[] { (byte) 0xBB, (byte) 0xBB }, timed.cover().getImage());
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

		clock.advance(CoverArt.STALE_TIMEOUT.minusSeconds(1));
		assertFalse(timed.cover().isCurrent());
		clock.advance(java.time.Duration.ofSeconds(1));
		assertTrue(timed.cover().isCurrent());
	}

	@Test
	public void firstTitleDoesNotHideTheCover() {
		cover(state, "AAAA");
		state.receivedIscpMessage("NTIFirst Song");

		assertTrue(state.cover().isCurrent());
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
		assertNull(state.playback().getPlayStatus());
		state.receivedIscpMessage("NSTP-S");
		assertEquals(NetPlayback.PlayStatus.PLAY, state.playback().getPlayStatus());
		state.receivedIscpMessage("NSTp-S");	// end of a Spotify playlist
		assertEquals(NetPlayback.PlayStatus.PAUSE, state.playback().getPlayStatus());
		state.receivedIscpMessage("NSTS--");
		assertEquals(NetPlayback.PlayStatus.STOP, state.playback().getPlayStatus());
		state.receivedIscpMessage("NSTF--");
		assertEquals(NetPlayback.PlayStatus.PLAY, state.playback().getPlayStatus());

		state.receivedIscpMessage("SLI10");
		assertNull("another input has its own play status", state.playback().getPlayStatus());
	}

	/** As the TX-NR626 at start: NSTQSTN stays unanswered, the time keeps running. */
	@Test
	public void playingIsInferredFromTheRunningTime() {
		state.receivedIscpMessage("SLI2B");
		state.receivedIscpMessage("NTM01:08/03:28");
		assertNull("one time alone does not show it runs", state.playback().getPlayStatus());
		state.receivedIscpMessage("NTM01:09/03:28");
		assertEquals(NetPlayback.PlayStatus.PLAY, state.playback().getPlayStatus());
		assertNull("repeat is not inferred", state.playback().getRepeat());

		state.receivedIscpMessage("NSTp-S");
		state.receivedIscpMessage("NTM01:10/03:28");
		assertEquals("a reported status wins", NetPlayback.PlayStatus.PAUSE, state.playback().getPlayStatus());
	}

	@Test
	public void repeatFromNst() {
		state.receivedIscpMessage("SLI2B");
		assertNull(state.playback().getRepeat());
		state.receivedIscpMessage("NSTP-S");
		assertEquals(NetPlayback.Repeat.OFF, state.playback().getRepeat());
		state.receivedIscpMessage("NSTPRS");
		assertEquals(NetPlayback.Repeat.ALL, state.playback().getRepeat());
		state.receivedIscpMessage("NSTP1S");
		assertEquals(NetPlayback.Repeat.ONE, state.playback().getRepeat());
		state.receivedIscpMessage("NSTPxx");
		assertEquals(NetPlayback.Repeat.UNAVAILABLE, state.playback().getRepeat());
		state.receivedIscpMessage("NSTP");
		assertNull(state.playback().getRepeat());
	}

	@Test
	public void shuffleFromNst() {
		state.receivedIscpMessage("SLI2B");
		assertNull(state.playback().getShuffle());
		state.receivedIscpMessage("NSTP--");
		assertEquals(NetPlayback.Shuffle.OFF, state.playback().getShuffle());
		state.receivedIscpMessage("NSTp-S");
		assertEquals(NetPlayback.Shuffle.ALL, state.playback().getShuffle());
		assertTrue(state.playback().getShuffle().isOn());
		state.receivedIscpMessage("NSTP-A");
		assertEquals(NetPlayback.Shuffle.ALBUM, state.playback().getShuffle());
		state.receivedIscpMessage("NSTPxx");
		assertEquals(NetPlayback.Shuffle.UNAVAILABLE, state.playback().getShuffle());
		assertFalse(state.playback().getShuffle().isOn());

		state.receivedIscpMessage("SLI10");
		assertNull(state.playback().getShuffle());
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

		NetList.Popup popup = state.netList().getPopup();
		assertEquals("TuneIn", popup.topTitle());
		assertEquals("Verbindungsfehler.", popup.title());
		assertEquals("Die Verbindung zum Server konnte nicht hergestellt werden.", popup.message());
		assertEquals(List.of("OK"), popup.buttons());
		assertEquals(List.of(Aspect.POPUP), fired);
	}

	@Test
	public void netPopupWithoutButtonsAndListType() {
		state.receivedIscpMessage("NPUBService\0Title\0Message\0" + "0");
		assertEquals(List.of(), state.netList().getPopup().buttons());

		state.receivedIscpMessage("NPULSetup\0Server\0on\0Port\0" + "8080\0");
		assertEquals("Setup", state.netList().getPopup().topTitle());
		assertEquals("Server: on\nPort: 8080", state.netList().getPopup().message());
	}

	@Test
	public void popupClosesWithNewListPageOrInput() {
		state.receivedIscpMessage("NPUTTuneIn\0Error\0Text\0" + "0");
		state.receivedIscpMessage("NLSC0P");
		assertNull(state.netList().getPopup());

		state.receivedIscpMessage("NPUTTuneIn\0Error\0Text\0" + "0");
		state.receivedIscpMessage("SLI10");
		assertNull(state.netList().getPopup());

		state.receivedIscpMessage("NPUTTuneIn\0Error\0Text\0" + "0");
		state.netList().dismissPopup();
		assertNull(state.netList().getPopup());
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

		assertEquals(List.of("My Music", "Internet Radio"), state.netList().getLines());
		assertEquals(1, state.netList().getCursor());

		state.receivedIscpMessage("NLSC0P");
		assertTrue(state.netList().getLines().isEmpty());
	}

	@Test
	public void coverArtIsAssembledFromBlocks() {
		state.receivedIscpMessage("NJA10FFD8");
		state.receivedIscpMessage("NJA11FFE0");
		assertNull(state.cover().getImage());

		state.receivedIscpMessage("NJA12FFD9");
		assertArrayEquals(new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, (byte) 0xFF, (byte) 0xD9 },
				state.cover().getImage());
		assertTrue(state.cover().isCurrent());

		state.receivedIscpMessage("NLSC0P");
		assertFalse(state.cover().isCurrent());
	}

	@Test
	public void coverArtWithoutFirstBlockIsDropped() {
		state.receivedIscpMessage("NJA11FFE0");
		state.receivedIscpMessage("NJA12FFD9");

		assertNull(state.cover().getImage());
	}
}
