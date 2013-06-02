package de.csmp.jeiscp;


import static de.csmp.jeiscp.eiscp.EiscpCommmandsConstants.MASTER_VOLUME_QUERY;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import de.csmp.jeiscp.eiscp.Command;
import de.csmp.jeiscp.eiscp.EiscpCommandsParser;

public class EiscpDeviceTest {
	// was on 192.168.2.111:60128
	public static final String AUTODISCOVER_STRING = "ECNTX-NR609/60128/XX/002719B23FAA";

	@Test
	public void testDevicenameExtraction() {
		EiscpDevice d = new EiscpDevice(AUTODISCOVER_STRING, "192.168.2.111");
		
		System.out.println(d);
		Assert.assertTrue(d.getDeviceType().equals("TX-NR609"));
		Assert.assertTrue(d.getPort() == 60128);
		Assert.assertTrue(! StringUtils.isEmpty(d.getIpAddress()));
	}
	

	@Test
	public void testDeviceCapabilities() {
		EiscpDevice d = new EiscpDevice(AUTODISCOVER_STRING, "192.168.2.111");
		
		Assert.assertTrue(d.getCapableEiscpParserModelsets().size() > 1);
		Assert.assertTrue(d.getCapableEiscpParserModelsets().size() < EiscpCommandsParser.getModelsets().keySet().size());
		System.out.println("capableModelsets: " + d.getCapableEiscpParserModelsets());
		
		Command volQuery = EiscpCommandsParser.getCommand(MASTER_VOLUME_QUERY);
		Assert.assertTrue(d.isCapable(volQuery));
	}
}
