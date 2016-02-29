package ee.valja7;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import sun.awt.image.ByteInterleavedRaster;
import ee.era.code.Imaging.Filters.CannyEdgeDetector;
import ee.era.code.Imaging.Filters.HoughFilter;

/**
 * Created by valeri on 28.02.16.
 */
public class HoughTest {
    public static void main(String[] args) throws IOException {
		HoughFilter houghFilter = new HoughFilter(0.1f, 10, 20);

		BufferedImage source = ImageIO.read(Main.class.getResourceAsStream("/star.bmp"));

		// CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1,6,0); // FFor root
		CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1, 1, 0f); // FFor root
		BufferedImage edges = cannyEdgeDetector.filter(source, null);

		File outputfile = new File("target/edges.png");
		ImageIO.write(edges, "png", outputfile);

		BufferedImage houghMap = houghFilter.filter(edges, null);

		ByteInterleavedRaster phaseData = (ByteInterleavedRaster)houghMap.getData();
		byte[] phaseMap = phaseData.getDataStorage();

		Map<Integer, Integer> toSort = new HashMap<>();

		for (int i = 0; i < phaseMap.length; i++) {
			if ((phaseMap[i] & 0xFF) > 20)
				toSort.put(i, phaseMap[i] & 0xFF);
		}
		// toSort = sortByValue(toSort);
		toSort = HoughFilter.sortByValue(toSort);
		int scanlineStride = phaseData.getScanlineStride();

		System.out.println("-------------------------------------");
		int rMax = phaseMap.length / scanlineStride;
		for (Integer i : toSort.keySet()) {
			Double freq = (i / rMax) / houghFilter.getStepPerGrad();
			Double dist = (i - freq * rMax) / houghFilter.getStepPerPixel();

			System.out.printf("angle = %2f°, distance = %2f, line size = %2d%n", freq, dist, Math.round((double)toSort.get(i)
					* rMax / 255));
		}

		outputfile = new File("target/Hough.png");
		ImageIO.write(houghMap, "png", outputfile);
    }
}
