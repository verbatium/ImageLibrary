package ee.era.code.Imaging.Filters;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class SoebelFilter implements BufferedImageOp {

	public static int HORIZONTAL = 0;
	public static int VERTICAL = 1;

	private int direction = HORIZONTAL;

	private float[][] kernelMatrix = { { 1, 0, -1, 2, 0, -2, 1, 0, -1 }, { 1, 2, -1, 0, 0, 0, -1, -2, -1 } };

	public SoebelFilter(int direction) {
		this.direction = direction;
	}

	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		Kernel kernel = new Kernel(3, 3, kernelMatrix[direction]);
		final ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
		return convolve.filter(src, dest);
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
