package ee.era.code.Imaging.Filters;

import org.junit.Assert;

public class GaussianTest {
    @org.junit.Test
    public void testKernel() throws Exception {
        Gaussian gaussian = new Gaussian(1.41f);
        float[] floats = gaussian.Kernel2DFlat(1);
        Assert.assertEquals(floats[0], floats[2], 0.001);
        Assert.assertEquals(floats[0], floats[6], 0.001);
        Assert.assertEquals(floats[0], floats[8], 0.001);
        Assert.assertEquals(floats[1], floats[3], 0.001);
        Assert.assertEquals(floats[1], floats[5], 0.001);
        Assert.assertEquals(floats[1], floats[7], 0.001);

        // 0.077847	0.123317	0.077847
        // 0.123317	0.195346	0.123317
        // 0.077847	0.123317	0.077847
    }
}