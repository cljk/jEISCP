package de.csmp.jeiscp.app.fx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Renders the app icon PNGs that {@link FxGuiController} puts on the stage, and the .ico of the Windows launcher.
 * Run: {@code java src/test/java/de/csmp/jeiscp/app/fx/IconGenerator.java src/main/resources/de/csmp/jeiscp/app/fx/icons [src/main/jpackage/jeiscp.ico] [preview.png]}
 */
public class IconGenerator {

	static final int[] SIZES = { 16, 24, 32, 48, 64, 128, 256 };
	static final Color TEAL = new Color(0x2dd4bf);
	static final Color TEAL_LIGHT = new Color(0x7af5e4);

	public static void main(String[] args) throws Exception {
		File dir = new File(args[0]);
		dir.mkdirs();
		for (int size : SIZES) {
			ImageIO.write(render(size), "png", new File(dir, "icon-" + size + ".png"));
		}
		for (int i = 1; i < args.length; i++) {
			if (args[i].endsWith(".ico")) {
				writeIco(new File(args[i]));
			} else {
				ImageIO.write(previewSheet(), "png", new File(args[i]));
			}
		}
	}

	/** An .ico with one PNG per size, as Windows reads it since Vista. */
	static void writeIco(File file) throws IOException {
		List<byte[]> images = new ArrayList<>();
		for (int size : SIZES) {
			ByteArrayOutputStream png = new ByteArrayOutputStream();
			ImageIO.write(render(size), "png", png);
			images.add(png.toByteArray());
		}
		ByteBuffer directory = ByteBuffer.allocate(6 + 16 * SIZES.length).order(ByteOrder.LITTLE_ENDIAN);
		directory.putShort((short) 0).putShort((short) 1).putShort((short) SIZES.length);
		int offset = directory.capacity();
		for (int i = 0; i < SIZES.length; i++) {
			int size = SIZES[i];
			directory.put((byte) (size >= 256 ? 0 : size)).put((byte) (size >= 256 ? 0 : size))	// 0 means 256
					.put((byte) 0).put((byte) 0).putShort((short) 1).putShort((short) 32)
					.putInt(images.get(i).length).putInt(offset);
			offset += images.get(i).length;
		}
		file.getParentFile().mkdirs();
		try (OutputStream out = new FileOutputStream(file)) {
			out.write(directory.array());
			for (byte[] image : images) {
				out.write(image);
			}
		}
	}

	static BufferedImage render(int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.scale(size / 256.0, size / 256.0);
		boolean tiny = size <= 24;	// tile, ring and OK key only; arrows from 48 px
		boolean small = size <= 48;	// bolder details

		// tile
		double inset = tiny ? 4 : 10;
		RoundRectangle2D tile = new RoundRectangle2D.Double(inset, inset, 256 - 2 * inset, 256 - 2 * inset, 112, 112);
		g.setPaint(new GradientPaint(0, 0, new Color(0x434851), 0, 256, new Color(0x1a1c20)));
		g.fill(tile);
		if (!tiny) {
			g.setStroke(new BasicStroke(small ? 8f : 4f));
			g.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 70), 0, 140, new Color(255, 255, 255, 0)));
			g.draw(tile);
		}

		// LCD bar
		if (!tiny) {
			double h = small ? 36 : 30;
			g.setColor(new Color(0x061516));
			g.fill(new RoundRectangle2D.Double(58, 34, 140, h, 18, 18));
			double barH = small ? 14 : 10;
			double barY = 34 + (h - barH) / 2;
			g.setColor(TEAL_LIGHT);
			g.fill(new RoundRectangle2D.Double(72, barY, small ? 80 : 70, barH, barH, barH));
			if (!small) {
				g.setColor(new Color(122, 245, 228, 110));
				g.fill(new RoundRectangle2D.Double(150, barY, 32, barH, barH, barH));
			}
		}

		// direction pad
		double cx = 128;
		double cy = tiny ? 128 : 152;
		double ring = tiny ? 100 : 80;
		g.setColor(new Color(0, 0, 0, 120));
		g.fill(circle(cx, cy + (tiny ? 4 : 5), ring));
		g.setPaint(new GradientPaint(0, (float) (cy - ring), new Color(0x656c76), 0, (float) (cy + ring), new Color(0x2a2d32)));
		g.fill(circle(cx, cy, ring));
		g.setPaint(new GradientPaint(0, (float) (cy - ring), new Color(0x4a5059), 0, (float) (cy + ring), new Color(0x2c3035)));
		g.fill(circle(cx, cy, ring - (tiny ? 10 : 5)));

		if (size >= 48) {
			g.setColor(new Color(0xdfe3e8));
			g.setStroke(new BasicStroke(small ? 13f : 9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			double d = ring - 22;
			double halfWidth = small ? 14 : 12;
			double depth = small ? 9 : 8;
			g.draw(chevron(cx, cy, 0, -1, d, halfWidth, depth));
			g.draw(chevron(cx, cy, 0, 1, d, halfWidth, depth));
			g.draw(chevron(cx, cy, -1, 0, d, halfWidth, depth));
			g.draw(chevron(cx, cy, 1, 0, d, halfWidth, depth));
		}

		// OK key: glow, teal ring, graphite cap
		double ok = tiny ? 48 : 32;
		g.setPaint(new RadialGradientPaint((float) cx, (float) cy, (float) (ok * 1.9),
				new float[] { 0.45f, 1f }, new Color[] { new Color(45, 212, 191, 120), new Color(45, 212, 191, 0) }));
		g.fill(circle(cx, cy, ok * 1.9));
		g.setColor(TEAL);
		g.fill(circle(cx, cy, ok));
		g.setPaint(new GradientPaint(0, (float) (cy - ok), new Color(0x5a616b), 0, (float) (cy + ok), new Color(0x33373d)));
		g.fill(circle(cx, cy, ok - (tiny ? 12 : 6)));

		g.dispose();
		return img;
	}

	static Ellipse2D circle(double cx, double cy, double r) {
		return new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r);
	}

	/** Chevron at distance {@code d} from the centre, pointing outward along (ux, uy). */
	static Path2D chevron(double cx, double cy, double ux, double uy, double d, double halfWidth, double depth) {
		double tipX = cx + ux * (d + depth / 2);
		double tipY = cy + uy * (d + depth / 2);
		double baseX = cx + ux * (d - depth / 2);
		double baseY = cy + uy * (d - depth / 2);
		double px = -uy;
		double py = ux;
		Path2D p = new Path2D.Double();
		p.moveTo(baseX + px * halfWidth, baseY + py * halfWidth);
		p.lineTo(tipX, tipY);
		p.lineTo(baseX - px * halfWidth, baseY - py * halfWidth);
		return p;
	}

	/** Light row and dark row at native size, then 16/24/32/48 enlarged 4x without smoothing. */
	static BufferedImage previewSheet() {
		int pad = 20;
		int width = pad;
		for (int size : SIZES) {
			width += size + pad;
		}
		int rowH = 256 + 2 * pad;
		int zoomH = 48 * 4 + 2 * pad;
		BufferedImage sheet = new BufferedImage(width, 2 * rowH + zoomH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = sheet.createGraphics();
		g.setColor(new Color(0xf3f3f3));
		g.fillRect(0, 0, width, rowH);
		g.setColor(new Color(0x202124));
		g.fillRect(0, rowH, width, rowH + zoomH);
		int x = pad;
		for (int size : SIZES) {
			BufferedImage icon = render(size);
			g.drawImage(icon, x, pad + 256 - size, null);
			g.drawImage(icon, x, rowH + pad + 256 - size, null);
			x += size + pad;
		}
		x = pad;
		for (int size : new int[] { 16, 24, 32, 48 }) {
			g.drawImage(render(size), x, 2 * rowH + pad, size * 4, size * 4, null);
			x += size * 4 + pad;
		}
		g.dispose();
		return sheet;
	}
}
