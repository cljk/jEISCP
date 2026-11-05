package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ThreadedConsoleAppTest {

	@Test
	public void addressWithOrWithoutPort() {
		assertEquals("192.168.0.97", ThreadedConsoleApp.host("192.168.0.97"));
		assertEquals(60128, ThreadedConsoleApp.port("192.168.0.97"));
		assertEquals("127.0.0.1", ThreadedConsoleApp.host("127.0.0.1:50555"));
		assertEquals(50555, ThreadedConsoleApp.port("127.0.0.1:50555"));
		assertEquals("fe80::1", ThreadedConsoleApp.host("fe80::1"));	// IPv6, no port
		assertEquals(60128, ThreadedConsoleApp.port("fe80::1"));
	}
}
