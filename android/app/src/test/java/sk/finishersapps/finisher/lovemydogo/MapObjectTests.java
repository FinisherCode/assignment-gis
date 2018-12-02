package sk.finishersapps.finisher.lovemydogo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import sk.finishersapps.finisher.lovemydogo.model.data_objects.GenericMapObject;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 */
public class MapObjectTests {
    @Test

    public void nullResultTest() throws Exception {
        GenericMapObject mapObject = new GenericMapObject(null);
    }

    @Test
    public void distanceTestShouldBeZero() throws Exception {
        JSONObject json = Mockito.mock(JSONObject.class);
        GenericMapObject mapObject = new GenericMapObject(json);
        double calculationRestul = mapObject.calculateDistance(0, 0, 0);
        assertEquals(0.0d, calculationRestul, 0.00001d);
    }

    @Test
    public void distanceTestShouldBeValue() throws Exception {
        JSONObject json = Mockito.mock(JSONObject.class);
        GenericMapObject mapObject = new GenericMapObject(json);
        double calculationRestul = mapObject.calculateDistance(10, 10, 0);
        assertEquals(1568520.556798576d, calculationRestul, 0.00001d);
    }

    @Test
    public void distanceTestIllegalValues() throws Exception {
        JSONObject json = Mockito.mock(JSONObject.class);
        GenericMapObject mapObject = new GenericMapObject(json);
        try {
            double calculationRestul = mapObject.calculateDistance(-100, 12221, -512);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }
    }
}