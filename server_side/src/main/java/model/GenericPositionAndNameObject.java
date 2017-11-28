package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * All object on map are represented by name, x, y and distance.
 */
public class GenericPositionAndNameObject {
    private double xCoordinate;
    private double yCoordinate;
    private String name;
    private double distance;

    public GenericPositionAndNameObject() {

    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }

    /**
     * Builder class that Builds GenericPositionAndNameObject from result set loaded from database
     */
    public static class Builder {
        public static GenericPositionAndNameObject buildFromResultSet(ResultSet rs) throws SQLException {
            GenericPositionAndNameObject result = new GenericPositionAndNameObject();
            result.name = rs.getString("name");
            result.xCoordinate = rs.getDouble("x");
            result.yCoordinate = rs.getDouble("y");
            result.distance = rs.getDouble("distance");
            return result;
        }
    }
}
