package ee.valja7;

import ee.era.code.Imaging.Filters.HoughFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by valeri on 28.02.16.
 */
public class HoughTest {
    public static void main(String[] args) throws IOException {
        HoughFilter houghFilter = new HoughFilter();

        BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/filtered.png"));

        BufferedImage imageOutput = houghFilter.filter(image, null);
        File outputfile = new File("target/Hough.png");
        ImageIO.write(imageOutput, "png", outputfile);
    }
}
