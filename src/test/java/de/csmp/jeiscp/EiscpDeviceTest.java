package de.csmp.jeiscp;


import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.eiscp.Command;
import de.csmp.jeiscp.eiscp.EiscpCommandsParser;

public class EiscpDeviceTest {
	private static final Logger log = LoggerFactory.getLogger(EiscpDeviceTest.class);

	public static final String AUTODISCOVER_STRING_NR609 = "ECNTX-NR609/60128/XX/002719B23FAA";
	public static final String AUTODISCOVER_STRING_NR626 = "ECNTX-NR626/60128/XX/0009B095369F";

	@Test
	public void testDevicenameExtraction() {
		log.info("testDevicenameExtraction");
		EiscpDevice d = new EiscpDevice(AUTODISCOVER_STRING_NR609, "192.168.2.111");
		
		System.out.println(d);
		Assert.assertTrue(d.getDeviceType().equals("TX-NR609"));
		Assert.assertTrue(d.getPort() == 60128);
		Assert.assertTrue(! StringUtils.isEmpty(d.getIpAddress()));
	}
	

	@Test
	public void testDeviceCapabilities609() {
		log.info("testDeviceCapabilities");
		EiscpDevice d = new EiscpDevice(AUTODISCOVER_STRING_NR609, "192.168.2.111");
		
		Assert.assertTrue(d.getCapableEiscpParserModelsets().size() > 1);
		Assert.assertTrue(d.getCapableEiscpParserModelsets().size() < EiscpCommandsParser.getModelsets().keySet().size());
		log.info("capableModelsets for device: {}", d.getCapableEiscpParserModelsets());
		
		Command volQuery = EiscpCommandsParser.getCommand(MASTER_VOLUME_QUERY_ID);
		Assert.assertNotNull(volQuery);
		
		log.info("check command: {}", volQuery);
		log.info("command modelSets: {}", volQuery.getModels());
		Assert.assertTrue("mistake in YAML?", d.isCapable(volQuery));
	}
	
	@Test
	public void testDeviceCapabilities626() {
		log.info("testDeviceCapabilities");
		EiscpDevice d = new EiscpDevice(AUTODISCOVER_STRING_NR626, "192.168.2.111");
		
		Assert.assertTrue(d.getCapableEiscpParserModelsets().size() > 1);
		Assert.assertTrue(d.getCapableEiscpParserModelsets().size() < EiscpCommandsParser.getModelsets().keySet().size());
		log.info("capableModelsets for device: {}", d.getCapableEiscpParserModelsets());
		
		Command volQuery = EiscpCommandsParser.getCommand(MASTER_VOLUME_QUERY_ID);
		Assert.assertNotNull(volQuery);
		
		log.info("check command: {}", volQuery);
		log.info("command modelSets: {}", volQuery.getModels());
		Assert.assertTrue("mistake in YAML?", d.isCapable(volQuery));
	}
}
