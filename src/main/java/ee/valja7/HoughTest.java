package ee.valja7;

import ee.era.code.Imaging.Filters.*;
import sun.awt.image.ByteInterleavedRaster;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by valeri on 28.02.16.
 */
public class HoughTest {

	public static void main(String[] args) throws IOException {
		HoughFilter houghFilter = new HoughFilter(0.7f, 1, 1);
		// HoughFilter houghFilter = new HoughFilter(0.0f, 1, 1);
		// CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1,6,0); // FFor root
		CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1, 1, 0f); // FFor root
		GaussianBlur gaussianBlur = new GaussianBlur();
		gaussianBlur.setSigma(0.3f);
		Multuply multuply = new Multuply();

		//BufferedImage src = ImageIO.read(Main.class.getResourceAsStream("/BIGSTAR.png"));
		BufferedImage src = ImageIO.read(Main.class.getResourceAsStream("/vanna.jpg"));
		//BufferedImage src = ImageIO.read(Main.class.getResourceAsStream("/star.bmp"));
		BufferedImage dst = convertAndSave(src, cannyEdgeDetector, "target/edges.png", null);
		dst = convertAndSave(dst, houghFilter, "target/hough.png", null);
		// printLines(dst, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());

		// dst = convertAndSave(dst, houghFilter, "target/hough1.png", null);
		// printLines(dst, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());

		dst = convertAndSave(dst, gaussianBlur, "target/blured.png", null);
		dst = convertAndSave(dst, new ExtremumFilter(), "target/extremums.png", null);

		// printLines(dst, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());

		// BufferedImage hor = convertAndSave(dst, new SoebelFilter(SoebelFilter.HORIZONTAL), "target/SoebelH.png",
		// null);
		// BufferedImage ver = convertAndSave(dst, new SoebelFilter(SoebelFilter.VERTICAL), "target/SoebelV.png", null);
		// dst = convertAndSave(hor, new SoebelFilter(SoebelFilter.VERTICAL), "target/multiplyed.png", ver);
		dst = drawLines(dst, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel(), src);
		ImageIO.write(dst, "png", new File("target/lines.png"));
	}

	private static BufferedImage convertAndSave(BufferedImage houghMap, BufferedImageOp filter, String fileName, BufferedImage dst) throws IOException {
		BufferedImage retVal = filter.filter(houghMap, dst);
		ImageIO.write(retVal, "png", new File(fileName));
		return retVal;
	}

	static BufferedImage drawLines(BufferedImage houghMap, double stepPerGrad, double stepPerPixel, BufferedImage dst) {

		ByteInterleavedRaster phaseData = (ByteInterleavedRaster)houghMap.getData();
		byte[] phaseMap = phaseData.getDataStorage();

		Map<Integer, Integer> toSort = new HashMap<>();

		for (int i = 0; i < phaseMap.length; i++) {
			if ((phaseMap[i] & 0xFF) > 0)
				toSort.put(i, phaseMap[i] & 0xFF);
		}
		// toSort = sortByValue(toSort);
		toSort = HoughFilter.sortByValue(toSort);
		int scanlineStride = phaseData.getScanlineStride();

		//Graphics2D graphics2D = dst.createGraphics();
		//graphics2D.setColor(Color.RED);

		System.out.println("-------------------------------------");
		int rMax = phaseMap.length / scanlineStride;
		for (Integer i : toSort.keySet()) {

			Integer grad = (int)((i / scanlineStride));
			Integer r = (int)((i - (grad * scanlineStride)));

			Double angle = ((double)grad / 180 * Math.PI);

			System.out.printf("r = %d, grad = %d, angle = %f%n", r, grad, angle);

			double cosTh = Math.cos(angle);
			double sinTh = Math.sin(angle);
			double p1 = -cosTh / sinTh;
			double p2 = (double) r / sinTh;
			for (int x = 0; x < dst.getWidth() * 2; x++) {
				int y = (int) (p1 * x / 2.0 + p2);
				if (y >= 0 && y < dst.getHeight())
					dst.setRGB(x / 2, y, Color.RED.getRGB());
			}

			//System.out.printf("p1_x = %f,p1_y = %f, p2_x = %f, p2_y = %f %n", p1_x, p1_y, p2_x, p2_y);

			//graphics2D.drawLine((int)p1_x, (int)p1_y, (int)p2_x, (int)p2_y);
		}

		return dst;
	}

	static void printLines(BufferedImage houghMap, double stepPerGrad, double stepPerPixel) {
		ByteInterleavedRaster phaseData = (ByteInterleavedRaster)houghMap.getData();
		byte[] phaseMap = phaseData.getDataStorage();

		Map<Integer, Integer> toSort = new HashMap<>();

		for (int i = 0; i < phaseMap.length; i++) {
			if ((phaseMap[i] & 0xFF) > 0)
				toSort.put(i, phaseMap[i] & 0xFF);
		}
		// toSort = sortByValue(toSort);
		toSort = HoughFilter.sortByValue(toSort);
		int scanlineStride = phaseData.getScanlineStride();

		System.out.println("-------------------------------------");
		int rMax = phaseMap.length / scanlineStride;
		for (Integer i : toSort.keySet()) {
			Double dist = (i / rMax) / stepPerGrad;
			Double freq = (i - Math.round(i / rMax) * rMax) / stepPerPixel;

			double rad = freq / 180 * Math.PI;
			Double x = Math.cos(rad);
			Double y = Math.sin(rad);
			Double k = -dist;
			System.out.printf("x = [%2.3f,0], y = [0,%2.3f]%n", k / x, k / y);
			System.out.printf("angle = %2f°, distance = %2f, line weight = %2d%n", freq, dist, Math.round((double)toSort.get(i)
					* rMax / 255));
		}
	}
}
