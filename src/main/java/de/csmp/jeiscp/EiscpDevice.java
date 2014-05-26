package de.csmp.jeiscp;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csmp.jeiscp.eiscp.Command;
import de.csmp.jeiscp.eiscp.EiscpCommandsParser;

public class EiscpDevice {

	private static final String FALLBACK_DEVICE_TYPE = "TX-NR709";

	String autodiscoverString;
	
	String deviceType;
	
	String ipAddress;
	
	Integer port;
	
	
	List<String> capableEiscpParserModelsets;
	
	
	private static final Logger log = LoggerFactory.getLogger(EiscpDevice.class);
	
	
	public EiscpDevice() {
		initModelsets();
	}
	
	public EiscpDevice(String autodiscoverString) {
		initByAutodiscoverString(autodiscoverString);
		initModelsets();
	}
	
	public EiscpDevice(String autodiscoverString, String ipAddress) {
		initByAutodiscoverString(autodiscoverString);
		initModelsets();

		this.ipAddress = ipAddress;
	}

	protected void initByAutodiscoverString(String autodiscoverString) {
		this.autodiscoverString = autodiscoverString;
		
		if (autodiscoverString.startsWith("ECN")) {
			int endOfDeviceType = autodiscoverString.indexOf("/", 3);
			deviceType = autodiscoverString.substring(3, endOfDeviceType);
			
			int endOfPort = autodiscoverString.indexOf("/", endOfDeviceType + 1);
			String port = autodiscoverString.substring(endOfDeviceType + 1, endOfPort);
			this.port = Integer.parseInt(port);
		} else {
			log.error("wrong autodiscover message: " + autodiscoverString);
		}
	}

	protected void initModelsets() {
		Map<String, List<String>> modelSets = EiscpCommandsParser.getModelsets();
		
		List<String> regognizedModelSets = regognizeModelSets(modelSets, deviceType);
		
		if (regognizedModelSets.size() == 0) {
			log.warn("no recognized device capabilities for " + deviceType + " - fallback to " + FALLBACK_DEVICE_TYPE);
			regognizedModelSets = regognizeModelSets(modelSets, FALLBACK_DEVICE_TYPE);
		}
		
		this.capableEiscpParserModelsets = regognizedModelSets;
		log.debug("capable modelsets: " + regognizedModelSets.size());
	}

	protected List<String> regognizeModelSets(Map<String, List<String>> modelSets, String forDeviceType) {
		List<String> recognizedModelSets = new LinkedList<String>();
		for (String setId : modelSets.keySet()) {
			List<String> modelIds = modelSets.get(setId);
			if (modelIds.contains(forDeviceType)) {
				recognizedModelSets.add(setId);
			}
		}
		return recognizedModelSets;
	}
	
	@Override
	public String toString() {
		return "EiscpDevice " + deviceType + " @" + (ipAddress != null ? ipAddress : "") + ":" + port;
	}
	
	public boolean isCapable(Command cmd) {
		return this.capableEiscpParserModelsets.contains(cmd.getModels());
	}
	

	public String getAutodiscoverString() {
		return autodiscoverString;
	}

	public void setAutodiscoverString(String autodiscoverString) {
		this.autodiscoverString = autodiscoverString;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	public List<String> getCapableEiscpParserModelsets() {
		return capableEiscpParserModelsets;
	}

	public void setCapableEiscpParserModelsets(List<String> eiscpParserModelsets) {
		this.capableEiscpParserModelsets = eiscpParserModelsets;
	}
}
