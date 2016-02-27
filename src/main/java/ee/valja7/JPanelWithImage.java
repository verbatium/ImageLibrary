package ee.valja7;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by valeri on 27.02.16.
 */
public class JPanelWithImage extends JPanel {
    private BufferedImage image;

    public JPanelWithImage() {
        try {
            BufferedImage img = ImageIO.read(Main.class.getResourceAsStream("/image.jpg"));
            setImage(img);
        } catch (IOException ex) {
            // handle exception...
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.setSize(image.getWidth(), image.getHeight());
        this.setMinimumSize(getSize());

    }

}
