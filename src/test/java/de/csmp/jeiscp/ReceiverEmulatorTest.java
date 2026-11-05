package de.csmp.jeiscp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.function.BooleanSupplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.csmp.jeiscp.app.ReceiverState;
import de.csmp.jeiscp.app.NetPlayback.PlayStatus;
import de.csmp.jeiscp.app.NetPlayback.Repeat;
import de.csmp.jeiscp.app.NetPlayback.Shuffle;

/** The emulator through the real connector and model, as the app sees it. */
public class ReceiverEmulatorTest {

	private ReceiverEmulator emulator;
	private EiscpConnector conn;
	private final ReceiverState state = new ReceiverState();

	@Before
	public void connect() throws Exception {
		// no ticks, so nothing plays on by itself; slow enough covers to see the old one hidden while they come
		emulator = new ReceiverEmulator(0, Duration.ofHours(1), Duration.ofMillis(3), false);
		conn = new EiscpConnector("127.0.0.1", emulator.port());
		conn.addListener(state);
		conn.connect();
	}

	@After
	public void close() {
		conn.close();
		emulator.close();
	}

	@Test
	public void answersTheAutodiscovery() {
		EiscpDevice device = EiscpAutodiscovery.identify("127.0.0.1", emulator.port());

		assertNotNull(device);
		assertEquals(ReceiverEmulator.MODEL, device.getDeviceType());
		assertEquals(emulator.port(), (int) device.getPort());
	}

	@Test
	public void pushesTheServiceRightAfterConnecting() {
		await(() -> "Spotify".equals(state.netList().getService()));
	}

	@Test
	public void answersQueriesAndEchoesChanges() throws Exception {
		conn.sendIscpCommand("PWRQSTN");
		conn.sendIscpCommand("SLIQSTN");
		conn.sendIscpCommand("NTIQSTN");
		conn.sendIscpCommand("MVL20");
		conn.sendIscpCommand("AMTTG");

		await(() -> Boolean.TRUE.equals(state.getPower()) && state.isNetworkInput() && state.playback().getTitle() != null
				&& Integer.valueOf(0x20).equals(state.getVolume()) && Boolean.TRUE.equals(state.getMuted()));
		assertEquals(ReceiverEmulator.PLAYLIST.get(0).title(), state.playback().getTitle());
	}

	@Test
	public void playbackKeysReportTheirStatus() throws Exception {
		conn.sendIscpCommand("NTCPAUSE");
		await(() -> state.playback().getPlayStatus() == PlayStatus.PAUSE);
		conn.sendIscpCommand("NTCREPEAT");
		conn.sendIscpCommand("NTCRANDOM");
		await(() -> state.playback().getRepeat() == Repeat.ALL && state.playback().getShuffle() == Shuffle.ALL);
	}

	@Test
	public void trackChangeRepeatsThePreviousCoverFirst() throws Exception {
		conn.sendIscpCommand("NTCTRUP");	// same album: the first cover
		await(() -> state.cover().getImage() != null && state.cover().isCurrent());
		byte[] album = state.cover().getImage();
		assertArrayEquals(ReceiverEmulator.cover(ReceiverEmulator.PLAYLIST.get(1)), album);

		conn.sendIscpCommand("NTCTRUP");	// another album: the old cover again, then the new one
		await(() -> ReceiverEmulator.PLAYLIST.get(2).title().equals(state.playback().getTitle()));
		assertFalse("hidden after the title change", state.cover().isCurrent());
		await(() -> state.cover().isCurrent());
		assertArrayEquals(ReceiverEmulator.cover(ReceiverEmulator.PLAYLIST.get(2)), state.cover().getImage());
	}

	@Test
	public void ignoresWhatTheTxNr626Ignores() throws Exception {
		conn.sendIscpCommand("NSTQSTN");
		conn.sendIscpCommand("NJAREQ");
		conn.sendIscpCommand("PWRQSTN");	// answered in order, so the two above had their chance

		await(() -> state.getPower() != null);
		assertEquals(null, state.playback().getPlayStatus());
		assertEquals(null, state.cover().getImage());
	}

	private static void await(BooleanSupplier condition) {
		long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
		while (!condition.getAsBoolean()) {
			assertTrue("timed out", System.nanoTime() < deadline);
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex) {
				throw new AssertionError(ex);
			}
		}
	}
}
