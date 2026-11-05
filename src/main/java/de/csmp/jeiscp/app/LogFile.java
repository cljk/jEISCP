package de.csmp.jeiscp.app;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/** The rolling {@code eiscp.log}: in the user's data directory, or in {@code -Djeiscp.logDir} (the run scripts use the working directory). */
public final class LogFile {

	static final String DIR_PROPERTY = "jeiscp.logDir";

	private LogFile() {
	}

	/** Attaches the file to the root logger. @return the log file */
	public static File install() {
		File file = new File(directory(System.getProperty(DIR_PROPERTY), System.getenv(), System.getProperty("os.name"),
				System.getProperty("user.home")), "eiscp.log");
		file.getAbsoluteFile().getParentFile().mkdirs();
		RollingFileAppender appender = new RollingFileAppender();
		appender.setName("jeiscp-log-file");
		appender.setFile(file.getPath());
		appender.setAppend(true);
		appender.setMaxFileSize("5MB");
		appender.setMaxBackupIndex(2);
		appender.setLayout(new PatternLayout("[%d{HH:mm:ss} %5p] (%F:%L)\t: %m%n"));
		appender.activateOptions();
		Logger.getRootLogger().addAppender(appender);
		return file;
	}

	static File directory(String override, Map<String, String> env, String os, String home) {
		if (override != null && !override.isBlank()) {
			return new File(override);
		}
		if (os.startsWith("Windows")) {
			String local = env.get("LOCALAPPDATA");
			return new File(local != null && !local.isBlank() ? local : home, "jEISCP");
		}
		if (os.startsWith("Mac")) {
			return new File(home, "Library/Logs/jEISCP");
		}
		String state = env.get("XDG_STATE_HOME");
		return new File(state != null && !state.isBlank() ? state : home + "/.local/state", "jeiscp");
	}
}
