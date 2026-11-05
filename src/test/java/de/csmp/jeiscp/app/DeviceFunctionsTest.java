package de.csmp.jeiscp.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import de.csmp.jeiscp.EiscpDevice;
import de.csmp.jeiscp.app.DeviceFunctions.Function;
import de.csmp.jeiscp.eiscp.Command;

public class DeviceFunctionsTest {

	static final EiscpDevice NR626 = new EiscpDevice("ECNTX-NR626/60128/XX/0009B095369F", "192.168.0.97");

	@Test
	public void offersSetupMenuAndListeningModeForNr626() {
		List<Function> functions = DeviceFunctions.forDevice(NR626);

		Function setup = find(functions, "OSD").orElseThrow();
		assertTrue(values(setup).containsAll(List.of("MENU", "HOME", "QUICK", "ENTER", "EXIT")));
		assertTrue(setup.queryable() == false);

		Function listeningMode = find(functions, "LMD").orElseThrow();
		assertTrue(listeningMode.queryable());
		assertTrue(values(listeningMode).contains("STEREO"));
	}

	@Test
	public void leavesOutQueriesPlaceholdersAndInfoOnlyBlocks() {
		List<Function> functions = DeviceFunctions.forDevice(NR626);

		for (Function function : functions) {
			for (String value : values(function)) {
				assertTrue(function.prefix() + value, value.matches("[A-Z0-9/]+") && !value.equals("QSTN"));
			}
		}
		// artist name only has QSTN and a placeholder
		assertFalse(find(functions, "NAT").isPresent());
	}

	@Test
	public void unknownModelGetsEveryFunction() {
		assertTrue(DeviceFunctions.forDevice(null).size() >= DeviceFunctions.forDevice(NR626).size());
	}

	@Test
	public void isSortedByLabel() {
		List<Function> functions = DeviceFunctions.forDevice(NR626);
		for (int i = 1; i < functions.size(); i++) {
			assertTrue(functions.get(i - 1).label().compareTo(functions.get(i).label()) <= 0);
		}
	}

	@Test
	public void labels() {
		assertEquals("Listening mode", DeviceFunctions.prettify("listening-mode"));
		assertEquals("TV logic", DeviceFunctions.prettify("tv-logic"));
		assertEquals("Dolby EX", DeviceFunctions.prettify("dolby-ex"));
		assertEquals("HDMI audio out", DeviceFunctions.prettify("hdmi-audio-out"));
		assertEquals("", DeviceFunctions.prettify(null));
		assertEquals("Stereo", DeviceFunctions.describeValue("LMD", "00"));
		assertEquals("ZZ", DeviceFunctions.describeValue("LMD", "ZZ"));
		assertEquals(null, DeviceFunctions.describeValue("LMD", null));
	}

	private static Optional<Function> find(List<Function> functions, String prefix) {
		return functions.stream().filter(f -> f.prefix().equals(prefix)).findFirst();
	}

	private static List<String> values(Function function) {
		return function.actions().stream().map(Command::getCommand).toList();
	}
}
