package ee.era.code.Imaging.Filters;

import org.junit.Assert;
import org.junit.Test;

public class CannyEdgeDetectorTest {
    @Test
    public void testOrientation() throws Exception {
        /*
                  90
                  gy-
             3 3 2|2  1 1
             0    |     0
        gx-  -----|----- gx +  0
             0    |     0
             1 1 2|2  3 3
                  gy+
                  270
         */

        byte orientation = CannyEdgeDetector.getOrientation(0, 0);
        Assert.assertEquals(0, orientation);

        orientation = CannyEdgeDetector.getOrientation(1, 0); //0
        Assert.assertEquals(0, orientation);

        orientation = CannyEdgeDetector.getOrientation(1, 1); //45
        Assert.assertEquals(1, orientation);

        orientation = CannyEdgeDetector.getOrientation(0, 1); //90
        Assert.assertEquals(2, orientation);

        orientation = CannyEdgeDetector.getOrientation(-1, 1); //135
        Assert.assertEquals(3, orientation);

        orientation = CannyEdgeDetector.getOrientation(-1, 0); //180
        Assert.assertEquals(0, orientation);

        orientation = CannyEdgeDetector.getOrientation(-1, -1); //225
        Assert.assertEquals(1, orientation);

        orientation = CannyEdgeDetector.getOrientation(0, -1); //270
        Assert.assertEquals(2, orientation);

        orientation = CannyEdgeDetector.getOrientation(1, -1); //315
        Assert.assertEquals(3, orientation);
    }

    @Test
    public void testCheckOrientation() {
        double angle = 1.0 / 16.0; //180/16 = 12.25
        //if ( angle < 22.5 ) orientation = 0;
        Assert.assertEquals(0, CannyEdgeDetector.getOrientation(Math.PI * angle * 0.0)); // 0.0
        Assert.assertEquals(0, CannyEdgeDetector.getOrientation(Math.PI * angle * 1.0)); //12.25
        //else if ( angle < 67.5 ) orientation = 1;
        Assert.assertEquals(1, CannyEdgeDetector.getOrientation(Math.PI * angle * 2.0)); //22.5
        Assert.assertEquals(1, CannyEdgeDetector.getOrientation(Math.PI * angle * 3.0)); //45-12.25
        Assert.assertEquals(1, CannyEdgeDetector.getOrientation(Math.PI * angle * 4.0)); //45
        Assert.assertEquals(1, CannyEdgeDetector.getOrientation(Math.PI * angle * 5.0)); //45 + 12.25
        //else if ( angle < 112.5 )  orientation = 2;
        Assert.assertEquals(2, CannyEdgeDetector.getOrientation(Math.PI * angle * 6.0)); //67.5
        Assert.assertEquals(2, CannyEdgeDetector.getOrientation(Math.PI * angle * 7.0)); //90 - 12.25
        Assert.assertEquals(2, CannyEdgeDetector.getOrientation(Math.PI * angle * 8.0)); //90
        Assert.assertEquals(2, CannyEdgeDetector.getOrientation(Math.PI * angle * 9.0)); //90 + 12.25
        // else if ( angle < 157.5 )             orientation = 3;
        Assert.assertEquals(3, CannyEdgeDetector.getOrientation(Math.PI * angle * 10.0)); //112.5
        Assert.assertEquals(3, CannyEdgeDetector.getOrientation(Math.PI * angle * 11.0)); //135 - 12.25
        Assert.assertEquals(3, CannyEdgeDetector.getOrientation(Math.PI * angle * 12.0)); //135
        Assert.assertEquals(3, CannyEdgeDetector.getOrientation(Math.PI * angle * 13.0)); //135 + 12.25
        // else orientation = 0;
        Assert.assertEquals(0, CannyEdgeDetector.getOrientation(Math.PI * angle * 14.0)); //180-12.25
        Assert.assertEquals(0, CannyEdgeDetector.getOrientation(Math.PI * angle * 15.0)); //180-12.25
        Assert.assertEquals(0, CannyEdgeDetector.getOrientation(Math.PI * angle * 16.0)); //180

    }
}