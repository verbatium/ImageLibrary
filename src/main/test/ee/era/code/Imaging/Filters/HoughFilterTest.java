package ee.era.code.Imaging.Filters;

import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by valeri on 28.02.16.
 */
public class HoughFilterTest {
    @Test
    public void filterMustReturnNewImageIfNotProvided() throws Exception {
        HoughFilter houghFilter = new HoughFilter();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage newImage = houghFilter.filter(img, null);
        Assert.assertNotNull(newImage);
    }
}