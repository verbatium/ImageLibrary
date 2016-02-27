package ee.valja7;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by valeri on 27.02.16.
 */
public class NumberMapperTest {
    @Test
    public void testGeneric() throws Exception {
        NumberMapper helperIntInt = new NumberMapper(10.0, 20.0, 40.0, 60.0);

        Double value = helperIntInt.Convert(10.0);
        Assert.assertTrue(40.0 == value);

        value = helperIntInt.Convert(20.0);
        Assert.assertTrue(60.0 == value);

        value = helperIntInt.Convert(15.0);
        Assert.assertTrue(50.0 == value);

        value = helperIntInt.ConvertBack(50.0);
        Assert.assertTrue(15.0 == value);

        value = helperIntInt.ConvertBack(60.0);
        Assert.assertTrue(20.0 == value);

        value = helperIntInt.ConvertBack(40.0);
        Assert.assertTrue(10.0 == value);
    }
}