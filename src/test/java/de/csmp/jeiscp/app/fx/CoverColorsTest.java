package de.csmp.jeiscp.app.fx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Arrays;

import org.junit.Test;

public class CoverColorsTest {

	@Test
	public void greyCoverKeepsTheDisplayColour() {
		int[] grey = new int[100];
		Arrays.fill(grey, 0xFF808080);
		grey[0] = 0xFFFF0000;	// a single red pixel is no accent

		assertEquals(-1, CoverColors.accent(grey));
	}

	@Test
	public void mostCommonClearHueWins() {
		// like the Helsloot cover: grey landscape, an orange band
		int[] pixels = new int[100];
		Arrays.fill(pixels, 0xFF6E6E6E);
		Arrays.fill(pixels, 0, 20, 0xFFF0A060);	// orange
		Arrays.fill(pixels, 20, 25, 0xFF3060F0);	// a little blue

		float[] hsb = Color.RGBtoHSB(0, 0, 0, null);
		int accent = CoverColors.accent(pixels);
		Color.RGBtoHSB((accent >> 16) & 0xFF, (accent >> 8) & 0xFF, accent & 0xFF, hsb);
		assertEquals("orange hue", 26 / 360f, hsb[0], 0.03f);
		assertTrue("bright on the dark display", hsb[2] > 0.9f);
	}

	@Test
	public void emptyImage() {
		assertEquals(-1, CoverColors.accent(new int[0]));
	}
}
