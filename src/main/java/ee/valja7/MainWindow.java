package ee.valja7;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ee.era.code.Imaging.Filters.CannyEdgeDetector;

/**
 * Created by valeri on 27.02.16.
 */
public class MainWindow extends JFrame {
    private JPanel panel1;
    private JSlider sliderSigma;
    private JTextField textSigma;
    private JTextField textLowGain;
    private JSlider sliderLowGain;
    private JButton applyButton;
    private JSlider sliderHighGain;
    private JTextField textHighGain;
    private JPanelWithImage panel2;
    private JButton saveBtn;
    private Double sigma;
    private Integer lowGain;
    private Integer highGain;
    private NumberMapper sigmaMapper = new NumberMapper(0.0, 25.5, 0.0, 255.);
    private NumberMapper lowGainMapper = new NumberMapper(0.0, 255.0, 0.0, 255.0);
    private NumberMapper HighGainMapper = new NumberMapper(0.0, 255.0, 0.0, 255.0);
    private BufferedImage img;

    public MainWindow() throws HeadlessException {
        super("Canny Edge Detector");

        setSize(1024, 768);
        setContentPane(panel1);
        sliderSigma.setMaximum(255);
        sliderLowGain.setMaximum(255);
        sliderHighGain.setMaximum(255);
        sigma = 1.4;
        sliderSigma.setValue(sigmaMapper.Convert(sigma).intValue());
        lowGain = 20;
        highGain = 100;
        sliderLowGain.setValue(lowGain);
        sliderHighGain.setValue(highGain);

        textSigma.setText(sigmaMapper.ConvertBack(((Integer) sliderSigma.getValue()).doubleValue()).toString());
        Integer value = lowGainMapper.ConvertBack(((Integer) sliderLowGain.getValue()).doubleValue()).intValue();
        textLowGain.setText(value.toString());
        value = HighGainMapper.ConvertBack(((Integer) sliderHighGain.getValue()).doubleValue()).intValue();
        textHighGain.setText(value.toString());

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sliderSigma.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sigma = sigmaMapper.ConvertBack(((Integer) sliderSigma.getValue()).doubleValue());
                textSigma.setText(sigma.toString());
            }
        });

        sliderLowGain.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                lowGain = lowGainMapper.ConvertBack(((Integer) sliderLowGain.getValue()).doubleValue()).intValue();
                textLowGain.setText(lowGain.toString());
            }
        });
        sliderHighGain.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                highGain = HighGainMapper.ConvertBack(((Integer) sliderHighGain.getValue()).doubleValue()).intValue();
                textHighGain.setText(highGain.toString());
            }
        });
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CannyEdgeDetector cannyEdgeDetector = new CannyEdgeDetector(highGain, lowGain, sigma.floatValue());
                panel2.setImage(cannyEdgeDetector.filter(img, null));
                panel2.repaint();
            }
        });
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = panel2.getImage();
                File outputfile = new File("target/filtered.png");
                try {
                    ImageIO.write(img, "png", outputfile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        setVisible(true);

        try {
			img = ImageIO.read(Main.class.getResourceAsStream("/star.bmp"));
			// img = ImageIO.read(new File("target/Hough.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
