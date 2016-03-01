package ee.valja7;

import sun.awt.image.ByteInterleavedRaster;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Created by valeri on 29.02.16.
 */
public class ExtremumFilter implements BufferedImageOp {
    @Override
    public BufferedImage filter(BufferedImage srcImage, BufferedImage destImage) {

        int width = srcImage.getWidth();
        int height = srcImage.getHeight();


        //create destination
        BufferedImage dstImage = destImage;
        if (dstImage == null)
            dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());

        //retrive source array;
        ByteInterleavedRaster srcData = (ByteInterleavedRaster) srcImage.getData();
        byte[] src = srcData.getDataStorage();
        //retrive destination array;
        ByteInterleavedRaster dstData = (ByteInterleavedRaster) dstImage.getData();
        byte[] dst = dstData.getDataStorage();
        int direction;
        //process
        // пробегаемся по пикселям изображения контуров

        for (int y = 0; y < height - 1; y++) {
            int ptr = (y * srcData.getScanlineStride());
            for (int x = 0; x < width; x++) {
                Integer current = (Integer) (src[ptr + x] & 0xFF);
                int next = src[ptr + x + 1] & 0xff;
                direction = current.compareTo(next);
                dst[ptr + x] = (byte) (direction == 1 ? 127 : direction);
            }
        }


        //write back
        WritableRaster wr = dstData.createCompatibleWritableRaster(srcImage.getWidth(), srcImage.getHeight());
        wr.setDataElements(0, 0, srcImage.getWidth(), srcImage.getHeight(), dst);
        dstImage.setData(wr);

        return dstImage;
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
