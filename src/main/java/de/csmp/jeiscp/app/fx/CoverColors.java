package de.csmp.jeiscp.app.fx;

import java.awt.Color;

/** The accent of a cover: its most common clear hue, brightened for the dark display. */
final class CoverColors {

	private static final int BINS = 36;
	private static final float MIN_SATURATION = 0.25f;
	private static final float MIN_BRIGHTNESS = 0.2f;
	/** Below this share of colourful pixels a cover counts as grey and keeps the display's own colour. */
	private static final double MIN_COLOURFUL_SHARE = 0.03;

	private CoverColors() {
	}

	/** @param argb pixels as from a PixelReader; @return RGB, or -1 for a grey cover */
	static int accent(int[] argb) {
		double[] weight = new double[BINS];
		double[] hueSum = new double[BINS];
		double[] saturationSum = new double[BINS];
		int colourful = 0;
		float[] hsb = new float[3];
		for (int pixel : argb) {
			if ((pixel >>> 24) < 128) {
				continue;	// transparent
			}
			Color.RGBtoHSB((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF, hsb);
			if (hsb[1] < MIN_SATURATION || hsb[2] < MIN_BRIGHTNESS) {
				continue;
			}
			colourful++;
			int bin = Math.min(BINS - 1, (int) (hsb[0] * BINS));
			double w = hsb[1] * hsb[2];
			weight[bin] += w;
			hueSum[bin] += hsb[0] * w;
			saturationSum[bin] += hsb[1] * w;
		}
		if (argb.length == 0 || colourful < argb.length * MIN_COLOURFUL_SHARE) {
			return -1;
		}
		int best = 0;
		for (int bin = 1; bin < BINS; bin++) {
			if (weight[bin] > weight[best]) {
				best = bin;
			}
		}
		float hue = (float) (hueSum[best] / weight[best]);
		float saturation = (float) Math.max(0.45, Math.min(0.75, saturationSum[best] / weight[best]));
		return Color.HSBtoRGB(hue, saturation, 0.95f) & 0xFFFFFF;
	}
}
