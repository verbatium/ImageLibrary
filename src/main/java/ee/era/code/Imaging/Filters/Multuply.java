package ee.era.code.Imaging.Filters;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

public class Multuply implements BufferedImageOp {

	static byte getOrientation(int gx, int gy) {
		return getOrientation(getAngle(gx, gy));
	}

	static byte getOrientation(double angle) {
		return (byte) (Math.round(angle / Math.PI * 4.0) % 4);
	}

	private static double getAngle(int gx, int gy) {
		double orientation;
		if (gx == 0) {
			// can not divide by zero
			orientation = (gy == 0) ? 0 : Math.PI / 2;
		} else {
			orientation = Math.PI + Math.atan((double) gy / gx);
		}
		return orientation;
	}

	@Override
	public BufferedImage filter(BufferedImage horizontal, BufferedImage vertical) {
		int leftPixel = 0, rightPixel = 0;
		int width = horizontal.getWidth();
		int height = horizontal.getHeight();
		byte[][] orients = new byte[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int G = horizontal.getRGB(x, y);
				int V = vertical.getRGB(x, y);
				Double c = (Math.sqrt(G * G + V * V));
				vertical.setRGB(x, y, (int) c.longValue());
				orients[x][y] = getOrientation(G, V);
			}
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
// get two adjacent pixels
				switch (orients[x][y]) {
					case 0:
						leftPixel = vertical.getRGB(x - 1, y);
						rightPixel = vertical.getRGB(x + 1, y);
						break;
					case 1:
						leftPixel = vertical.getRGB(x - 1, y + 1);
						rightPixel = vertical.getRGB(x + 1, y - 1);
						break;
					case 2:
						leftPixel = vertical.getRGB(x, y + 1);
						rightPixel = vertical.getRGB(x, y - 1);
						break;
					case 3:
						leftPixel = vertical.getRGB(x + 1, y + 1);
						rightPixel = vertical.getRGB(x - 1, y - 1);
						break;
				}
				// compare current pixels value with adjacent pixels
				if ((vertical.getRGB(x, y) < leftPixel) || (vertical.getRGB(x, y) < rightPixel)) {
					horizontal.setRGB(x, y, 0);
				} else {
					horizontal.setRGB(x, y, vertical.getRGB(x, y));
				}
			}
		}

		return horizontal;

	}

	@Override
	public Rectangle2D getBounds2D(BufferedImage src) {
		return null;
	}

	@Override
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
		return null;
	}

	@Override
	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		return null;
	}

	@Override
	public RenderingHints getRenderingHints() {
		return null;
	}
}
