package ee.valja7;

import ee.era.code.Imaging.Filters.CannyEdgeDetector;
import ee.era.code.Imaging.Filters.GaussianBlur;
import ee.era.code.Imaging.Filters.HoughFilter;
import sun.awt.image.ByteInterleavedRaster;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by valeri on 28.02.16.
 */
public class HoughTest {
    public static void main(String[] args) throws IOException {
        HoughFilter houghFilter = new HoughFilter(1f, 3, 3);

        BufferedImage source = ImageIO.read(Main.class.getResourceAsStream("/star.bmp"));

        // CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1,6,0); // FFor root
        CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(1, 1, 0f); // FFor root
        BufferedImage edges = cannyEdgeDetector.filter(source, null);

        File outputfile = new File("target/edges.png");
        ImageIO.write(edges, "png", outputfile);

        BufferedImage houghMap = houghFilter.filter(edges, null);

        printLines(houghMap, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());


        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setSigma(4.0f);
        //cannyEdgeDetector = new CannyEdgeDetector(10, 100, 2.0f); // FFor root

        houghMap = gaussianBlur.filter(houghMap, null);


        ExtremumFilter extremumFilter = new ExtremumFilter();
        houghMap = extremumFilter.filter(houghMap, null);

        printLines(houghMap, houghFilter.getStepPerGrad(), houghFilter.getStepPerPixel());

        outputfile = new File("target/HoughBLured.png");
        ImageIO.write(houghMap, "png", outputfile);
    }


    static void printLines(BufferedImage houghMap, double stepPerGrad, double stepPerPixel) {
        ByteInterleavedRaster phaseData = (ByteInterleavedRaster) houghMap.getData();
        byte[] phaseMap = phaseData.getDataStorage();

        Map<Integer, Integer> toSort = new HashMap<>();

        for (int i = 0; i < phaseMap.length; i++) {
            if ((phaseMap[i] & 0xFF) > 60)
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

            System.out.printf("angle = %2f°, distance = %2f, line weight = %2d%n", freq, dist, toSort.get(i));
        }
    }
}
