package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class BrowserTest {

	@Test
	public void webInterfaceUrl() {
		assertEquals("http://192.168.0.97/", Browser.webInterfaceUrl("192.168.0.97"));
		assertEquals("http://[fe80::1]/", Browser.webInterfaceUrl("fe80::1"));
	}

	@Test
	public void usesThePlatformLauncher() {
		String url = "http://192.168.0.97/";
		assertEquals(List.of("rundll32", "url.dll,FileProtocolHandler", url), Browser.command("Windows 11", url));
		assertEquals(List.of("open", url), Browser.command("Mac OS X", url));
		assertEquals(List.of("xdg-open", url), Browser.command("Linux", url));
	}
}
