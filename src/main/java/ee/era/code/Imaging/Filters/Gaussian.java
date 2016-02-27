package ee.era.code.Imaging.Filters;

import java.awt.image.Kernel;

/**
 * Created by valeri on 26.02.16.
 */
public class Gaussian {
    public static final float SIGMA_MIN = 0.00000001f;
    // sigma value
    private float sigma = 1.0f;
    // squared sigma
    private float sqrSigma = 1.0f;

    public Gaussian(float sigma) {
        this.setSigma(sigma);
    }

    private float Function2D(float x, float y) {
        return (float) (Math.exp((x * x + y * y) / (-2.0f * sqrSigma)) / (2.0f * Math.PI * sqrSigma));
    }

    public float getSigma() {
        return sigma;
    }

    public void setSigma(float value) {
        sigma = Math.max(SIGMA_MIN, value);
        sqrSigma = sigma * sigma;
    }

    public Kernel getKernel(int size) {
        int height = size * 2 + 1;
        Kernel kernel = new Kernel(height, height, Kernel2DFlat(size));
        return kernel;
    }


    public float[] Kernel2DFlat(int radius) {
        if (radius > 50) {
            throw new IllegalArgumentException("Wrong kernal size.");
        }

        // raduis
        int size = radius * 2 + 1;
        // kernel
        float[] kernel = new float[size * size];
        // compute kernel
        for (int y = -radius, i = 0; i < size; y++, i++) {
            for (int x = -radius, j = 0; j < size; x++, j++) {
                kernel[i * size + j] = sqrSigma * Function2D(x, y);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.format("%.9f ", kernel[i * size + j]);

            }
            System.out.println("");
        }
        System.out.println("");
        return kernel;
    }
}
