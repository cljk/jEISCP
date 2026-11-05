package de.csmp.jeiscp.app;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Opens URLs in the system's default browser through the platform's own launcher, so the JavaFX app needs no AWT.
 */
public final class Browser {
	private static final Logger log = LoggerFactory.getLogger(Browser.class);

	private Browser() {
	}

	/** The receiver's web interface, e.g. {@code http://192.168.0.97/}. */
	public static String webInterfaceUrl(String address) {
		String host = address.contains(":") ? "[" + address + "]" : address;	// IPv6 literal
		return "http://" + host + "/";
	}

	/** Starts the browser in the background; failures are only logged. */
	public static void open(String url) {
		List<String> command = command(System.getProperty("os.name"), url);
		try {
			new ProcessBuilder(command).redirectErrorStream(true).start();
			log.info("opened {}", url);
		} catch (IOException ex) {
			log.warn("cannot open {} with {}: {}", url, command, ex.getMessage());
		}
	}

	static List<String> command(String osName, String url) {
		String os = osName == null ? "" : osName.toLowerCase(Locale.ROOT);
		if (os.contains("win")) {
			return List.of("rundll32", "url.dll,FileProtocolHandler", url);
		}
		if (os.contains("mac")) {
			return List.of("open", url);
		}
		return List.of("xdg-open", url);
	}
}
