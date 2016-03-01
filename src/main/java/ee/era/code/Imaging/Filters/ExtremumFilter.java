package ee.era.code.Imaging.Filters;

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

		BufferedImage retval = destImage;
		if (retval == null)
			retval = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());

		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		//double[] histogramW =  new double[width];
		//double[] histogramH = new double[height];
		ByteInterleavedRaster srcData = (ByteInterleavedRaster) srcImage.getData();
		byte[] src = srcData.getDataStorage();
		ByteInterleavedRaster dstData = (ByteInterleavedRaster) retval.getData();
		byte[] dst = dstData.getDataStorage();
		int dstStride = dstData.getScanlineStride();

		int pixel;
		int newPixel;
		int pixels[] = new int[8];
		int xx, yy;
		int ptr = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++, ptr++) {
				//first row
				pixel = src[ptr] & 0xff;

				for (int i = -1; i < 2; i++) {
					yy = y + i;
					for (int j = -1; j < 2; j++) {
						xx = x + j;

						if (xx > 0 && yy > 0 && xx < width - 1 && yy < height - 1) {
							int i1 = src[yy * dstStride + xx] & 0xff;
							if (pixel < i1) {
								pixel = 0;
							} else if (pixel == i1) {
								pixel--;
							}
						}
					}
				}
				if (pixel >= 0) {
					dst[ptr] = (byte) pixel;
				}
//				pixels[0] = x == 0 ? 0 : y == 0 ? 0 : srcImage.getRGB(x - 1, y - 1) & 0xFF;
//				pixels[1] = x == 0 ? 0 : srcImage.getRGB(x - 1, y) & 0xFF;
//				pixels[2] = x == 0 ? 0 : y == height - 1 ? 0 : srcImage.getRGB(x - 1, y + 1) & 0xFF;
//				//secondRow
//				pixels[3] = x == 0 ? 0 : y == 0 ? 0 : srcImage.getRGB(x, y - 1) & 0xFF;
//				pixel = srcImage.getRGB(x, y) & 0xFF;
//				pixels[4] = x == 0 ? 0 : y == height - 1 ? 0 : srcImage.getRGB(x, y + 1) & 0xFF;
//				//Third row
//				pixels[5] = x == width - 1 ? 0 : y == 0 ? 0 : srcImage.getRGB(x + 1, y - 1) & 0xFF;
//				pixels[6] = x == width - 1 ? 0 : srcImage.getRGB(x + 1, y) & 0xFF;
//				pixels[7] = x == width - 1 ? 0 : y == height - 1 ? 0 : srcImage.getRGB(x + 1, y + 1) & 0xFF;
//				retval.setRGB(x, y, pixel);
//				for (int i = 0; i < 8; i++) {
//					if (pixel < pixels[i])
//						retval.setRGB(x, y, 0);
//
//				}
				//histogramW[x]+=(pixel)/width;
				//histogramH[y]+=(pixel)/height;
			}
		}
		WritableRaster wr = dstData.createCompatibleWritableRaster(srcImage.getWidth(), srcImage.getHeight());
		wr.setDataElements(0, 0, srcImage.getWidth(), srcImage.getHeight(), dst);
		retval.setData(wr);
		return retval;
	}
//    @Override
//    public BufferedImage filter(BufferedImage srcImage, BufferedImage destImage) {
//
//        int width = srcImage.getWidth();
//        int height = srcImage.getHeight();
//
//
//        //create destination
//        BufferedImage dstImage = destImage;
//        if (dstImage == null)
//            dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());
//
//        //retrive source array;
//        ByteInterleavedRaster srcData = (ByteInterleavedRaster) srcImage.getData();
//        byte[] src = srcData.getDataStorage();
//        //retrive destination array;
//        ByteInterleavedRaster dstData = (ByteInterleavedRaster) dstImage.getData();
//        byte[] dst = dstData.getDataStorage();
//        int direction, prev = 0;
//        //process
//        // пробегаемся по пикселям изображения контуров
//        for (int y = 0; y < height - 1; y++) {
//            int ptr = (y * srcData.getScanlineStride());
//            for (int x = 0; x < width; x++) {
//                Integer current = (Integer) (src[ptr + x] & 0xFF);
//                int next = src[ptr + x + 1] & 0xff;
//
//                direction = current.compareTo(next);
//                if (prev !=direction)
//                    dst[ptr + x] =  src[ptr + x];
//                prev = direction;
//            }
//        }
//
//
//        //write back
//        WritableRaster wr = dstData.createCompatibleWritableRaster(srcImage.getWidth(), srcImage.getHeight());
//        wr.setDataElements(0, 0, srcImage.getWidth(), srcImage.getHeight(), dst);
//        dstImage.setData(wr);
//
//        return dstImage;
//    }

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
