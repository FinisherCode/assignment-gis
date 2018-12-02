package sk.finishersapps.finisher.lovemydogo.model.data_objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Filip on 24.11.2017.
 * Serves to hold any object that is refered by postion on map, name and distance from
 * my location
 */

public class GenericMapObject implements Serializable {

    private static final long serialVersionUID = 6163376825214725240L;
    private double longitude;
    private double latitude;
    private String name;
    private double distance;
    private boolean isTargetPoint;
    private double elevation;

    /**
     * Parses json into object
     *
     * @throws JSONException
     */
    public GenericMapObject(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
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

    // method to calculate distance calculates distance from point to acutal position,
    // elevation is redundant
    public double calculateDistance(double latitude,
                                    double longitued, double elevation) {
        if (latitude < -90 || latitude > 90 || longitued < -180 || longitued > 180 || elevation < 0) {
            throw new IllegalArgumentException();
        }
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(latitude - this.latitude);
        double lonDistance = Math.toRadians(longitued - this.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = this.elevation - elevation;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
