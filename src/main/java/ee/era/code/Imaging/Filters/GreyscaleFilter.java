package ee.era.code.Imaging.Filters;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;

/**
 * Created by valeri on 26.02.16.
 */
public class GreyscaleFilter implements BufferedImageOp {

    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        BufferedImageOp grayscaleConv =
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return grayscaleConv.filter(src, dest);
    }


    public Rectangle2D getBounds2D(BufferedImage src) {
        return null;
    }


    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        return null;
    }


    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        return null;
    }


    public RenderingHints getRenderingHints() {
        return null;
    }
}
