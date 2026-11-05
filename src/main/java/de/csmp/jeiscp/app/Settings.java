package de.csmp.jeiscp.app;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** What jEISCP remembers between runs, kept with {@code java.util.prefs}: last receiver, window position, number keys. */
public final class Settings {
	private static final Logger log = LoggerFactory.getLogger(Settings.class);

	private static final String RECEIVER = "receiver";
	private static final String WINDOW_X = "window.x";
	private static final String WINDOW_Y = "window.y";
	private static final String NUMBER_KEYS = "numberKeys";

	private final Preferences prefs;

	public Settings(Preferences prefs) {
		this.prefs = prefs;
	}

	public static Settings user() {
		return new Settings(Preferences.userRoot().node("de/csmp/jeiscp"));
	}

	/** @return {@code host} or {@code host:port}, {@code null} if none */
	public String lastReceiver() {
		return prefs.get(RECEIVER, null);
	}

	public void rememberReceiver(String address) {
		prefs.put(RECEIVER, address);
		flush();
	}

	public record Position(double x, double y) {
	}

	/** @return {@code null} if none */
	public Position windowPosition() {
		double x = prefs.getDouble(WINDOW_X, Double.NaN);
		double y = prefs.getDouble(WINDOW_Y, Double.NaN);
		return Double.isNaN(x) || Double.isNaN(y) ? null : new Position(x, y);
	}

	public void rememberWindowPosition(double x, double y) {
		prefs.putDouble(WINDOW_X, x);
		prefs.putDouble(WINDOW_Y, y);
		flush();
	}

	/** Whether the remote shows its number keys; hidden by default. */
	public boolean numberKeysShown() {
		return prefs.getBoolean(NUMBER_KEYS, false);
	}

	public void rememberNumberKeysShown(boolean shown) {
		prefs.putBoolean(NUMBER_KEYS, shown);
		flush();
	}

	private void flush() {
		try {
			prefs.flush();
		} catch (BackingStoreException ex) {
			log.debug("settings not saved: {}", ex.getMessage());
		}
	}
}
