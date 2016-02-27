package ee.valja7;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        /*
        BufferedImage image = ImageIO.read(Main.class.getResourceAsStream("/image.jpg"));

        //BufferedImage imageOutput = new BufferedImage(image.getWidth(), image.getHeight(),image.getType());
        BufferedImage imageOutput;

        GaussianBlur gaussianBlur = new GaussianBlur();
        imageOutput = gaussianBlur.filter(image,null);
        File outputfile = new File("target/blur.jpg");
        ImageIO.write(imageOutput, "jpg", outputfile);

        GreyscaleFilter grayscaleFilter = new GreyscaleFilter();
        imageOutput = grayscaleFilter.filter(image,null);
        outputfile = new File("target/grayscale.jpg");
        ImageIO.write(imageOutput, "jpg", outputfile);


        CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector();
        imageOutput = cannyEdgeDetector.filter(image,null);
        outputfile = new File("target/edge.jpg");
        ImageIO.write(imageOutput, "jpg", outputfile);
        */
        MainWindow window = new MainWindow();

    }


}
