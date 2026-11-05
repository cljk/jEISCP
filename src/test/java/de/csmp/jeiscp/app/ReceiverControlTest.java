package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;
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
		control.selectInput("2B");
		control.numberKey(0);
		control.numberKey(9);
		control.query("LMD");

		assertEquals(List.of("OSDMENU", "OSDQUICK", "NTCTRDN", "NTCPLAY", "NTCPAUSE", "NTCSTOP", "NTCTRUP",
				"SLI2B", "NTC0", "NTC9", "LMDQSTN"), sent);
		assertThrows(IllegalArgumentException.class, () -> control.numberKey(10));
	}

	@Test
	public void statusAndNowPlayingQueries() {
		control.queryStatus();
		assertEquals(List.of("PWRQSTN", "MVLQSTN", "AMTQSTN", "IFVQSTN", "RESQSTN", "SLIQSTN"), sent);

		sent.clear();
		control.queryNowPlayingIfMissing();	// input unknown
		state.receivedIscpMessage("SLI2B");
		state.receivedIscpMessage("NATDaft Punk");
		control.queryNowPlayingIfMissing();
		assertEquals(List.of("NTIQSTN"), sent);
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
