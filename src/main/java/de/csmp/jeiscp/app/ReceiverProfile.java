package de.csmp.jeiscp.app;

import java.util.Map;

/** Which queries a model answers, as far as known; unknown models are asked everything. */
public record ReceiverProfile(boolean answersPlayStatusQuery, boolean answersCoverRequest) {

	public static final ReceiverProfile DEFAULT = new ReceiverProfile(true, true);

	/** The TX-NR626 (2013 firmware) sends NST only on changes and the cover only on a track change. */
	private static final Map<String, ReceiverProfile> KNOWN = Map.of("TX-NR626", new ReceiverProfile(false, false));

	/** @param model e.g. {@code TX-NR626}; {@code null} if unknown */
	public static ReceiverProfile of(String model) {
		return model == null ? DEFAULT : KNOWN.getOrDefault(model, DEFAULT);
	}
}
