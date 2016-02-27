package ee.era.code.Imaging.Filters;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

/**
 * Created by valeri on 26.02.16.
 */
public class GaussianBlur implements BufferedImageOp {
    private float sigma = 1.412f;
    private int size = 2;
    private Kernel kernel;
    private Gaussian gaus;


    public GaussianBlur(float sigma, int size) {
        this.setSigma(sigma);
        this.setSize(size);
    }


    public GaussianBlur() {
        gaus = new Gaussian(sigma);
        kernel = gaus.getKernel(size);
    }


    public GaussianBlur(float sigma) {
        this.setSigma(sigma);
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        final ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        return convolve.filter(src, dest);
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

    public void setSigma(float sigma) {
        this.sigma = Math.max(0.5f, Math.min(5.0f, sigma));
        gaus = new Gaussian(sigma);
        kernel = gaus.getKernel(size);
    }

    public void setSize(int size) {
        this.size = Math.max(1, Math.min(10, size));
        kernel = gaus.getKernel(size);
    }
}
