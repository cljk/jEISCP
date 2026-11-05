package de.csmp.jeiscp.app;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * Keeps the latest log lines in memory for the GUI's log window, as a log4j appender on the root logger.
 * Listeners run on the logging thread, inside the buffer's lock: they must only hand the entry on.
 */
public final class LogBuffer extends AppenderSkeleton {

	public enum Kind { SENT, RECEIVED, OTHER }

	/** @param source simple name of the logger, e.g. {@code EiscpConnector} */
	public record Entry(long time, String level, String source, String message, Kind kind) {
	}

	/** Prefixes of the messages that {@code EiscpConnector} and its reader thread log per command. */
	static final String SENT_PREFIX = "sendIscpCommand: ";
	static final String RECEIVED_PREFIX = "received: ";

	private static LogBuffer installed;

	private final int capacity;
	private final ArrayDeque<Entry> entries = new ArrayDeque<>();
	private final List<Consumer<Entry>> listeners = new CopyOnWriteArrayList<>();

	LogBuffer(int capacity) {
		this.capacity = capacity;
	}

	/** Attaches a buffer to the root logger on the first call; later calls return the same buffer. */
	public static synchronized LogBuffer install() {
		if (installed == null) {
			installed = new LogBuffer(5000);
			installed.setName("jeiscp-log-buffer");
			Logger.getRootLogger().addAppender(installed);
		}
		return installed;
	}

	/** Returns the entries so far and passes every later one to the listener, without gaps or duplicates. */
	public List<Entry> subscribe(Consumer<Entry> listener) {
		synchronized (entries) {
			listeners.add(listener);
			return List.copyOf(entries);
		}
	}

	public void unsubscribe(Consumer<Entry> listener) {
		listeners.remove(listener);
	}

	@Override
	protected void append(LoggingEvent event) {
		String message = String.valueOf(event.getRenderedMessage());
		ThrowableInformation thrown = event.getThrowableInformation();
		if (thrown != null && thrown.getThrowable() != null) {
			message += " - " + thrown.getThrowable();
		}
		Kind kind = Kind.OTHER;
		if (message.startsWith(SENT_PREFIX)) {
			kind = Kind.SENT;
			message = message.substring(SENT_PREFIX.length());
		} else if (message.startsWith(RECEIVED_PREFIX)) {
			kind = Kind.RECEIVED;
			message = message.substring(RECEIVED_PREFIX.length());
		}
		String logger = event.getLoggerName();
		Entry entry = new Entry(event.getTimeStamp(), event.getLevel().toString(),
				logger.substring(logger.lastIndexOf('.') + 1), message, kind);

		synchronized (entries) {
			entries.addLast(entry);
			while (entries.size() > capacity) {
				entries.removeFirst();
			}
			for (Consumer<Entry> listener : listeners) {
				listener.accept(entry);
			}
		}
	}

	@Override
	public void close() {
		listeners.clear();
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
}
