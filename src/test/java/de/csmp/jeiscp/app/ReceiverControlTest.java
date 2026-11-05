package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.csmp.jeiscp.app.ReceiverControl.Direction;
import de.csmp.jeiscp.app.ReceiverControl.Playback;
import de.csmp.jeiscp.app.ReceiverControl.Target;

public class ReceiverControlTest {

	ReceiverState state;
	List<String> sent;
	ReceiverControl control;

	@Before
	public void setup() {
		state = new ReceiverState();
		sent = new ArrayList<>();
		control = new ReceiverControl(sent::add, state);
	}

	@Test
	public void togglesFollowTheReportedState() {
		control.togglePower();
		control.toggleMute();
		state.receivedIscpMessage("PWR01");
		state.receivedIscpMessage("AMT01");
		control.togglePower();
		control.toggleMute();

		assertEquals(List.of("PWR01", "AMT01", "PWR00", "AMT00"), sent);
	}

	@Test
	public void volumeIsHexClampedAndNotEchoed() {
		control.setVolume(26);
		control.setVolume(150);
		control.setVolume(-3);
		state.receivedIscpMessage("MVL1A");
		control.setVolume(26);
		control.volumeUp();
		control.volumeDown();

		assertEquals(List.of("MVL1A", "MVL64", "MVL00", "MVLUP", "MVLDOWN"), sent);
	}

	@Test
	public void directionPadDependsOnTarget() {
		control.navigate(Target.NET, Direction.UP);
		control.navigate(Target.TV, Direction.LEFT);
		control.ok(Target.NET);
		control.ok(Target.TV);
		control.back(Target.NET);
		control.back(Target.TV);
		control.home(Target.NET);
		control.home(Target.TV);

		assertEquals(List.of("NTCUP", "OSDLEFT", "NTCSELECT", "OSDENTER", "NTCRETURN", "OSDEXIT", "NTCTOP", "OSDHOME"), sent);
	}

	@Test
	public void menusPlaybackInputsAndNumbers() {
		control.openSetupMenu();
		control.openQuickMenu();
		control.playback(Playback.PREVIOUS);
		control.playback(Playback.PLAY);
		control.playback(Playback.PAUSE);
		control.playback(Playback.STOP);
		control.playback(Playback.NEXT);
		control.toggleRepeat();
		control.toggleShuffle();
		control.selectInput("2B");
		control.numberKey(0);
		control.numberKey(9);
		control.query("LMD");

		assertEquals(List.of("OSDMENU", "OSDQUICK", "NTCTRDN", "NTCPLAY", "NTCPAUSE", "NTCSTOP", "NTCTRUP", "NTCREPEAT", "NTCRANDOM",
				"SLI2B", "NTC0", "NTC9", "LMDQSTN"), sent);
		assertThrows(IllegalArgumentException.class, () -> control.numberKey(10));
	}

	@Test
	public void txNr626IsNotAskedWhatItDoesNotAnswer() {
		ReceiverControl nr626 = new ReceiverControl(sent::add, state, ReceiverProfile.of("TX-NR626"));
		state.receivedIscpMessage("SLI2B");
		state.receivedIscpMessage("NTIInstant Crush");
		state.receivedIscpMessage("NTM01:42/05:37");
		nr626.queryNowPlayingIfMissing();

		assertFalse(sent.contains("NSTQSTN"));
		assertFalse(sent.contains("NJAREQ"));
		assertEquals(ReceiverProfile.DEFAULT, ReceiverProfile.of("TX-NR709"));
		assertEquals(ReceiverProfile.DEFAULT, ReceiverProfile.of(null));
	}

	@Test
	public void typedCommands() {
		assertEquals("MVLQSTN", control.sendTyped("  MVLQSTN "));
		assertEquals("PWR01", control.sendTyped("!1PWR01"));
		assertNull(control.sendTyped("   "));
		assertNull(control.sendTyped("!1"));
		assertEquals(List.of("MVLQSTN", "PWR01"), sent);
	}

	@Test
	public void statusAndNowPlayingQueries() {
		control.queryStatus();
		assertEquals(List.of("PWRQSTN", "MVLQSTN", "AMTQSTN", "IFVQSTN", "RESQSTN", "SLIQSTN"), sent);

		sent.clear();
		control.queryNowPlayingIfMissing();	// input unknown
		state.receivedIscpMessage("SLI2B");
		control.queryNowPlayingIfMissing();	// nothing plays yet: no cover request
		assertEquals(List.of("NATQSTN", "NTIQSTN", "NALQSTN", "NTMQSTN", "NSTQSTN"), sent);

		sent.clear();
		state.receivedIscpMessage("NATDaft Punk");
		state.receivedIscpMessage("NALRandom Access Memories");
		state.receivedIscpMessage("NSTP-S");
		control.queryNowPlayingIfMissing();
		assertEquals(List.of("NTIQSTN", "NTMQSTN"), sent);
	}

	@Test
	public void coverIsRequestedOncePerPlayingTitle() {
		state.receivedIscpMessage("SLI2B");
		control.requestCoverIfMissing();	// no time yet: a menu list, not a track
		state.receivedIscpMessage("NTM01:42/05:37");
		control.requestCoverIfMissing();	// title not there yet, it would be asked twice otherwise
		state.receivedIscpMessage("NTIInstant Crush");
		control.requestCoverIfMissing();
		state.receivedIscpMessage("NTM01:43/05:37");
		control.requestCoverIfMissing();	// same title: already asked
		state.receivedIscpMessage("NTIGet Lucky");
		control.requestCoverIfMissing();
		state.receivedIscpMessage("NJA10FFD8");
		state.receivedIscpMessage("NJA12FFD9");
		state.receivedIscpMessage("NTILose Yourself to Dance");
		control.requestCoverIfMissing();	// a cover is known; the receiver pushes the next one itself

		assertEquals(List.of("NJAREQ", "NJAREQ"), sent);
	}

	@Test
	public void seekNeedsAKnownTotalAndUsesItsFormat() {
		control.seek(30);	// nothing plays
		state.receivedIscpMessage("NTM00:36/--:--");
		control.seek(30);	// a stream without length
		state.receivedIscpMessage("NTM00:36/02:46");
		control.seek(65);
		control.seek(500);	// clamped to the end
		state.receivedIscpMessage("NTM00:10:00/01:30:00");
		control.seek(3725);

		assertEquals(List.of("NTS01:05", "NTS02:46", "NTS01:02:05"), sent);
	}

	@Test
	public void sentListenersSeeEveryCommand() {
		List<String> seen = new ArrayList<>();
		control.addSentListener(seen::add);

		control.send("DIM01");
		control.volumeUp();

		assertEquals(List.of("DIM01", "MVLUP"), seen);
	}
}
