package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Map;
import java.util.prefs.Preferences;

import org.junit.After;
import org.junit.Test;

public class SettingsTest {

	private final Preferences node = Preferences.userRoot().node("de/csmp/jeiscp-test-" + System.nanoTime());
	private final Settings settings = new Settings(node);

	@After
	public void removeNode() throws Exception {
		node.removeNode();
	}

	@Test
	public void remembersReceiverAndPosition() {
		assertNull(settings.lastReceiver());
		assertNull(settings.windowPosition());

		settings.rememberReceiver("192.168.0.97");
		settings.rememberWindowPosition(120, 80.5);

		Settings again = new Settings(node);
		assertEquals("192.168.0.97", again.lastReceiver());
		assertEquals(new Settings.Position(120, 80.5), again.windowPosition());
	}

	@Test
	public void logDirectoryPerPlatform() {
		assertEquals(new File("."), LogFile.directory(".", Map.of(), "Windows 11", "C:/home"));
		assertEquals(new File("C:/Users/me/AppData/Local", "jEISCP"),
				LogFile.directory(null, Map.of("LOCALAPPDATA", "C:/Users/me/AppData/Local"), "Windows 11", "C:/Users/me"));
		assertEquals(new File("/home/me/.local/state", "jeiscp"), LogFile.directory(null, Map.of(), "Linux", "/home/me"));
		assertEquals(new File("/xdg/state", "jeiscp"), LogFile.directory("", Map.of("XDG_STATE_HOME", "/xdg/state"), "Linux", "/home/me"));
		assertEquals(new File("/Users/me", "Library/Logs/jEISCP"), LogFile.directory(null, Map.of(), "Mac OS X", "/Users/me"));
	}
}
