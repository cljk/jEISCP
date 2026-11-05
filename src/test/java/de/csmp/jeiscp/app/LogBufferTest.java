package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.csmp.jeiscp.app.LogBuffer.Entry;
import de.csmp.jeiscp.app.LogBuffer.Kind;

public class LogBufferTest {

	final Logger logger = Logger.getLogger("jeiscp.test.Probe");
	LogBuffer buffer;

	@Before
	public void attach() {
		buffer = new LogBuffer(3);
		logger.setAdditivity(false);	// keep the test lines out of eiscp.log
		logger.setLevel(Level.DEBUG);
		logger.addAppender(buffer);
	}

	@After
	public void detach() {
		logger.removeAppender(buffer);
	}

	@Test
	public void classifiesSentAndReceivedCommands() {
		logger.debug("sendIscpCommand: PWRQSTN");
		logger.debug("received: PWR01");
		logger.info("connected");

		List<Entry> entries = buffer.subscribe(e -> { });
		assertEquals(List.of(Kind.SENT, Kind.RECEIVED, Kind.OTHER), entries.stream().map(Entry::kind).toList());
		assertEquals(List.of("PWRQSTN", "PWR01", "connected"), entries.stream().map(Entry::message).toList());
		assertEquals("Probe", entries.get(2).source());
		assertEquals("INFO", entries.get(2).level());
	}

	@Test
	public void keepsOnlyTheLatestEntries() {
		for (int i = 1; i <= 5; i++) {
			logger.info("line " + i);
		}

		assertEquals(List.of("line 3", "line 4", "line 5"), buffer.subscribe(e -> { }).stream().map(Entry::message).toList());
	}

	@Test
	public void subscriberGetsHistoryThenNewEntries() {
		logger.info("before");
		List<Entry> live = new ArrayList<>();
		Consumer<Entry> listener = live::add;

		List<Entry> history = buffer.subscribe(listener);
		logger.info("after");
		buffer.unsubscribe(listener);
		logger.info("unsubscribed");

		assertEquals(List.of("before"), history.stream().map(Entry::message).toList());
		assertEquals(List.of("after"), live.stream().map(Entry::message).toList());
	}

	@Test
	public void appendsException() {
		logger.warn("connection lost", new IllegalStateException("boom"));

		assertTrue(buffer.subscribe(e -> { }).get(0).message().endsWith("java.lang.IllegalStateException: boom"));
	}
}
