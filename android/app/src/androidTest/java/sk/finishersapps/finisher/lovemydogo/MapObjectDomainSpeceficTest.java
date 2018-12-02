package sk.finishersapps.finisher.lovemydogo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import sk.finishersapps.finisher.lovemydogo.acitivities.MainMenuActivity;
import sk.finishersapps.finisher.lovemydogo.model.data_objects.GenericMapObject;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MapObjectDomainSpeceficTest {

    @Test
    public void incorrectJsonTest() throws Exception {
        try {
            JSONObject json = new JSONObject();
            json.put("love", "me");
            GenericMapObject obj = new GenericMapObject(json);
            Assert.fail();
        } catch (JSONException ignored) {

        }
    }

    @Test
    public void correctJsonTest() throws Exception {
        try {
            JSONObject json = new JSONObject("{\"xCoordinate\":17.137208313871692,\"yCoordinate\":48.168644365300594," +
                    "\"name\":\"Veterinárna ambulancia MVDr. Ladislav Šranko\",\"distance\":5433.67427287}");
            GenericMapObject obj = new GenericMapObject(json);
        } catch (JSONException ignored) {
            Assert.fail();
        }
    }
}
