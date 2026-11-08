package de.csmp.jeiscp.eiscp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EiscpCommandsParserTest {
	
	private static final Logger log = LoggerFactory.getLogger(EiscpCommandsParserTest.class);

	EiscpCommandsParser parser = null;
	
	@Before
	public void setup() {
		parser = new EiscpCommandsParser();
	}

	/** Fails after a submodule update until the constants are regenerated. */
	@Test
	public void generatedConstantsMatchCatalogue() throws Exception {
		String checkedIn = Files.readString(EiscpCommandsParserConstGenerator.SOURCE, StandardCharsets.UTF_8).replace("\r\n", "\n");

		Assert.assertTrue("EiscpCommmandsConstants is out of date: run mvn test -Dtest=EiscpCommandsParserConstGenerator"
				+ " and copy target/EiscpCommmandsConstants.java to " + EiscpCommandsParserConstGenerator.SOURCE,
				EiscpCommandsParserConstGenerator.generate().equals(checkedIn));
	}

	@Test
	public void readsNetUsbCommandsFromDockZone() {
		Command play = EiscpCommandsParser.getCommandByIscp("NTCPLAY");

		Assert.assertNotNull(play);
		Assert.assertEquals("network-usb::play", play.getIdentifier());
		Assert.assertNotNull(EiscpCommandsParser.getCommandByIscp("PWR01"));
	}

	@Test
	public void zonesAreKeptApartFromTheMainCommands() {
		List<String> zone2 = EiscpCommandsParser.getCommandBlocks("zone2").stream().map(CommandBlock::getCommand).toList();

		Assert.assertTrue(zone2.containsAll(List.of("ZPW", "ZVL", "ZMT", "SLZ")));
		Assert.assertFalse("getMainCommandBlocks stays main and dock",
				EiscpCommandsParser.getMainCommandBlocks().stream().anyMatch(b -> b.getCommand().equals("ZPW")));
		Assert.assertTrue(EiscpCommandsParser.getCommandBlocks("no-such-zone").isEmpty());
		Assert.assertThrows(UnsupportedOperationException.class, () -> EiscpCommandsParser.getCommandBlocks("zone2").clear());
	}

	@Test
	public void zoneCommandsResolveWithoutReplacingMainOnes() {
		Assert.assertEquals("power", EiscpCommandsParser.getCommandByIscp("ZPW01").getCommandBlock().getName());
		Assert.assertEquals("PW3", EiscpCommandsParser.getCommandByIscp("PW3QSTN").getCommandBlock().getCommand());

		// zone2 lists NTC as well, with the same value
		Command fromMain = EiscpCommandsParser.getCommandByIscp("NTCPLAY");
		Assert.assertTrue(EiscpCommandsParser.getMainCommandBlocks().contains(fromMain.getCommandBlock()));
	}

	/** The catalogue is shared by the reader, FX and main threads, so nobody may change it. */
	@Test
	public void catalogueIsUnmodifiable() {
		Command play = EiscpCommandsParser.getCommandByIscp("NTCPLAY");

		Assert.assertThrows(UnsupportedOperationException.class, () -> EiscpCommandsParser.getIscpToCommandMap().put("X", play));
		Assert.assertThrows(UnsupportedOperationException.class, () -> EiscpCommandsParser.getMainCommandBlocks().clear());
		Assert.assertThrows(UnsupportedOperationException.class, () -> play.getCommandBlock().getValues().clear());
		Assert.assertThrows(UnsupportedOperationException.class, () -> EiscpCommandsParser.getModelsets().get("set1").add("X"));
	}

	@Test
	public void derivedModelNamesMatch() {
		// the catalogue lists "TX-NR609(Ether)"; autodiscovery reports "TX-NR609"
		Assert.assertFalse(EiscpCommandsParser.getKeysetForModel("TX-NR609").isEmpty());
	}

	@Test
	public void sharedBlockNameResolvesToFirstBlock() {
		// NLS, NLA and NLU are all called net-usb-list-info
		Assert.assertEquals("NLS", EiscpCommandsParser.getIdToCommandBlockMap().get("net-usb-list-info").getCommand());
	}
	

	@Test
	public void testParse() {
		Map<String, List<String>> modelset = parser.getModelsets();
		
		List<String> keysetList;
		
		keysetList = parser.getKeysetForModel("TX-NR709");
		Assert.assertTrue(keysetList.size() < modelset.size());
		Assert.assertTrue(keysetList.size() > 1);
		log.info("keyset: " + keysetList.size());
		
		keysetList = parser.getKeysetForModel("DTC-7");
		Assert.assertTrue(keysetList.size() < modelset.size());
		Assert.assertTrue(keysetList.size() > 1);
		log.info("keyset: " + keysetList.size());
		
		
		
		List<CommandBlock> cmds = parser.getMainCommandBlocks();
		
		
		log.info("DDD");
	}

	
	@Ignore
	@Test
	public void testIt() {
		Map<String, Object> o = parser.getEiscpCommands();
		
		log.info("loaded");
		
		for (String key : o.keySet()) {
			log.info("key: " + key);
			Map<String, Object> o2 = (Map<String, Object>) o.get(key);
			
			log.info("o2: " + o2.getClass().getName());
			
			for (String key2 : o2.keySet()) {
				log.info(" key2: " + key2);
				Object o3 = o2.get(key2);
				
				log.info(" o3: " + o3.getClass().getName());
			}
		}
		
		
	}
}
