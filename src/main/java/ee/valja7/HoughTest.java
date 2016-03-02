package ee.valja7;

import ee.era.code.Imaging.Filters.*;
import sun.awt.image.ByteInterleavedRaster;

import javax.imageio.ImageIO;
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
	    HoughFilter houghFilter = new HoughFilter(0.88f, 1, 1);
	    //HoughFilter houghFilter = new HoughFilter(0.0f, 1, 1);
	    // CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1,6,0); // FFor root
	    CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1, 1, 0f); // FFor root
	    GaussianBlur gaussianBlur = new GaussianBlur();
	    gaussianBlur.setSigma(4.0f);
	    Multuply multuply = new Multuply();

	    //BufferedImage dst = ImageIO.read(Main.class.getResourceAsStream("/BIGSTAR.png"));
	    BufferedImage dst = ImageIO.read(Main.class.getResourceAsStream("/star.bmp"));
	    dst = convertAndSave(dst, cannyEdgeDetector, "target/edges.png", null);
	    dst = convertAndSave(dst, houghFilter, "target/hough.png", null);
	    //printLines(dst, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());

	    //dst = convertAndSave(dst, houghFilter, "target/hough1.png", null);
	    //printLines(dst, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());

	    //dst =convertAndSave(dst,gaussianBlur,"target/blured.png", null);
	    dst = convertAndSave(dst, new ExtremumFilter(), "target/extremums.png", null);

	    printLines(dst, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());

	    //BufferedImage hor = convertAndSave(dst, new SoebelFilter(SoebelFilter.HORIZONTAL), "target/SoebelH.png", null);
	    //BufferedImage ver = convertAndSave(dst, new SoebelFilter(SoebelFilter.VERTICAL), "target/SoebelV.png", null);
	    //dst = convertAndSave(hor, new SoebelFilter(SoebelFilter.VERTICAL), "target/multiplyed.png", ver);
    }

	private static BufferedImage convertAndSave(BufferedImage houghMap, BufferedImageOp filter, String fileName, BufferedImage dst) throws IOException {
		BufferedImage retVal = filter.filter(houghMap, dst);
		ImageIO.write(retVal, "png", new File(fileName));
		return retVal;
	}

	static void printLines(BufferedImage houghMap, double stepPerGrad, double stepPerPixel) {
		ByteInterleavedRaster phaseData = (ByteInterleavedRaster) houghMap.getData();
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
            Double dist = (i / rMax) / stepPerPixel;
            Double freq = (i - (i / rMax) * rMax) / stepPerGrad;

	        Double x = Math.cos(freq);
	        Double y = Math.sin(freq);
	        Double k = -dist;
	        System.out.printf("x = [%2.3f,0], y = [0,%2.3f]%n", k * x, k * y);
	        System.out.printf("angle = %2f°, distance = %2f, line weight = %2d%n", freq, dist, Math.round((double) toSort.get(i)
                    * rMax / 255));
		}
    }
}
