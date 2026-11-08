package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import de.csmp.jeiscp.EiscpDevice;

public class ZoneTest {

	private static EiscpDevice model(String name) {
		return new EiscpDevice("ECN" + name + "/60128/XX/0009B095369F", "192.168.0.97");
	}

	@Test
	public void commandsMapToTheirZone() {
		assertEquals(Zone.ZONE2, Zone.ofCommand("SLZ"));
		assertEquals(Zone.ZONE3, Zone.ofCommand("VL3"));
		assertEquals(Zone.ZONE4, Zone.ofCommand("PW4"));
		assertNull(Zone.ofCommand("MVL"));
	}

	@Test
	public void supportedZonesComeFromTheCatalogue() {
		assertEquals(List.of(Zone.ZONE2), Zone.supportedBy(model("TX-NR626")));
		assertEquals(List.of(Zone.ZONE2, Zone.ZONE3), Zone.supportedBy(model("TX-NR3010")));
		assertEquals("unknown models are offered everything", List.of(Zone.values()), Zone.supportedBy(null));
	}
}
