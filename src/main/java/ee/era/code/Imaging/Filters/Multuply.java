package ee.era.code.Imaging.Filters;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

public class Multuply implements BufferedImageOp {

	@Override
	public BufferedImage filter(BufferedImage horizontal, BufferedImage vertical) {

		BufferedImage retval = new BufferedImage(horizontal.getWidth(), horizontal.getHeight(), horizontal.getType());

		return retval;

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
