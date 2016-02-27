package ee.era.code.Imaging.Filters;

import sun.awt.image.ByteInterleavedRaster;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class CannyEdgeDetector implements BufferedImageOp {

    private GreyscaleFilter greyscaleFilter = new GreyscaleFilter();
    private GaussianBlur gaussianFilter = new GaussianBlur();
    private int lowThreshold = 20;
    private int highThreshold = 100;

    public CannyEdgeDetector(int highThreshold, int lowThreshold, float sigma) {
        this.highThreshold = highThreshold;
        this.lowThreshold = lowThreshold;
        gaussianFilter.setSigma(sigma);
    }

    public CannyEdgeDetector() {
    }

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

    public BufferedImage filter(BufferedImage srcImage, BufferedImage destImage) {
        // processing start and stop X,Y positions
        int startX = 1;
        int startY = 1;
        int width = srcImage.getWidth() - 2;
        int height = srcImage.getHeight() - 2;
        int stopX = startX + width;
        int stopY = startY + height;
        // pixel's value and gradients
        int gx, gy;

        float leftPixel = 0, rightPixel = 0;

        //int dstStride = destData;

        // STEP 1 - blur image
        BufferedImage blurredImage;
        blurredImage = greyscaleFilter.filter(srcImage, null);
        blurredImage = gaussianFilter.filter(blurredImage, null);
        // orientation array
        byte[] orients = new byte[width * height];


        // gradients array
        float[][] gradients = new float[srcImage.getWidth()][srcImage.getHeight()];
        float maxGradient = Float.NEGATIVE_INFINITY;

        ByteInterleavedRaster srcData = (ByteInterleavedRaster) blurredImage.getData();
        byte[] src = srcData.getDataStorage();
        BufferedImage dstImage = destImage;
        if (dstImage == null)
            dstImage = new BufferedImage(blurredImage.getWidth(), blurredImage.getHeight(), blurredImage.getType());

        ByteInterleavedRaster dstData = (ByteInterleavedRaster) dstImage.getData();
        byte[] dst = dstData.getDataStorage();

        int srcStride = srcData.getScanlineStride();
        int dstStride = dstData.getScanlineStride();
        int dstOffset = dstStride - dstImage.getWidth() + 2;
        int srcOffset = srcStride - srcImage.getWidth() + 2;
        // allign pointer
        int srcIdx = srcStride * startY + startX;

        // STEP 2 - calculate magnitude and edge orientation
        int p = 0;
        // for each line
        for (int y = startY; y < stopY; y++) {
            // for each pixel
            for (int x = startX; x < stopX; x++, srcIdx++, p++) {
                gx = src[srcIdx - srcStride + 1] + src[srcIdx + srcStride + 1]
                        - src[srcIdx - srcStride - 1] - src[srcIdx + srcStride - 1]
                        + 2 * (src[srcIdx + 1] - src[srcIdx + -1]);

                gy = src[srcIdx - srcStride - 1] + src[srcIdx - srcStride + 1]
                        - src[srcIdx + srcStride - 1] - src[srcIdx - srcStride + 1]
                        + 2 * (src[srcIdx - srcStride] - src[srcIdx + srcStride]);
                // get gradient value
                gradients[x][y] = (float) Math.sqrt(gx * gx + gy * gy);
                if (gradients[x][y] > maxGradient) {
                    maxGradient = gradients[x][y];
                }
                // --- get orientation and save orientation
                orients[p] = getOrientation(gx, gy);
            }
            srcIdx += srcOffset;
        }

        // STEP 3 - suppres non maximums
        int dstIndx = dstStride * startY + startX;
        p = 0;

        // for each line
        for (int y = startY; y < stopY; y++) {
            // for each pixel
            for (int x = startX; x < stopX; x++, dstIndx++, p++) {
                // get two adjacent pixels
                switch (orients[p]) {
                    case 0:
                        leftPixel = gradients[x - 1][y];
                        rightPixel = gradients[x + 1][y];
                        break;
                    case 1:
                        leftPixel = gradients[x - 1][y + 1];
                        rightPixel = gradients[x + 1][y - 1];
                        break;
                    case 2:
                        leftPixel = gradients[x][y + 1];
                        rightPixel = gradients[x][y - 1];
                        break;
                    case 3:
                        leftPixel = gradients[x + 1][y + 1];
                        rightPixel = gradients[x - 1][y - 1];
                        break;
                }
                // compare current pixels value with adjacent pixels
                if ((gradients[x][y] < leftPixel) || (gradients[x][y] < rightPixel)) {
                    dst[dstIndx] = 0;
                } else {
                    dst[dstIndx] = (byte) (gradients[x][y] / maxGradient * 255);
                }
            }
            dstIndx += dstOffset;
        }

        // STEP 4 - hysteresis
        dstIndx = dstStride * startY + startX;
        // for each line
        for (int y = startY; y < stopY; y++) {
            // for each pixel
            for (int x = startX; x < stopX; x++, dstIndx++) {
                if (dst[dstIndx] < highThreshold) {
                    if (dst[dstIndx] < lowThreshold) {
                        // non edge
                        dst[dstIndx] = 0;
                    } else {
                        // check 8 neighboring pixels
                        if ((dst[dstIndx - 1] < highThreshold) &&
                                (dst[dstIndx + 1] < highThreshold) &&
                                (dst[dstIndx - dstStride - 1] < highThreshold) &&
                                (dst[dstIndx - dstStride] < highThreshold) &&
                                (dst[dstIndx - dstStride + 1] < highThreshold) &&
                                (dst[dstIndx + dstStride - 1] < highThreshold) &&
                                (dst[dstIndx + dstStride] < highThreshold) &&
                                (dst[dstIndx + dstStride + 1] < highThreshold)) {
                            dst[dstIndx] = 0;
                        } else {
                            dst[dstIndx] = -1;
                        }
                    }
                } else {
                    dst[dstIndx] = -1;
                }
            }
            dstIndx += dstOffset;
        }

        // STEP 5 - draw black rectangle to remove those pixels, which were not processed


        WritableRaster wr = dstData.createCompatibleWritableRaster(srcImage.getWidth(), srcImage.getHeight());
        wr.setDataElements(0, 0, srcImage.getWidth(), srcImage.getHeight(), dst);
        dstImage.setData(wr);

        return dstImage;
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
