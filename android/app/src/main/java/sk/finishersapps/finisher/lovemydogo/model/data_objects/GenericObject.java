package sk.finishersapps.finisher.lovemydogo.model.data_objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Filip on 24.11.2017.
 * Serves to hold any object that is refered by postion on map, name and distance from
 * my location
 */

public class GenericObject implements Serializable {

    private static final long serialVersionUID = 6163376825214725240L;
    private double longitude;
    private double latitude;
    private String name;
    private double distance;
    private boolean isTargetPoint;

    /**
     * Parses json into object
     * @param json
     * @throws JSONException
     */
    public GenericObject(JSONObject json) throws JSONException {
        this.longitude = json.getDouble("xCoordinate");
        this.latitude = json.getDouble("yCoordinate");
        this.distance = json.getDouble("distance");
        this.name = json.getString("name");
        this.isTargetPoint = false;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isTargetPoint() {
        return isTargetPoint;
    }

    public void setTargetPoint(boolean targetPoint) {
        isTargetPoint = targetPoint;
    }
}
