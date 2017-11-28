package resolovers;

import model.BusStopsHolder;
import model.GenericPositionAndNameObject;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Something like Processing layer. RestController Calls required method from this class to obtain needed data.
 */
public class DataGatherer {

    private static final Logger LOG = Logger.getLogger(DataGatherer.class.getName());

    public DataGatherer() {

    }
    private List<GenericPositionAndNameObject> getVetsFromPoints(double x, double y, Integer distance) {
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        try {
            String query = "SELECT name, st_x(way) AS x, st_y(way) AS y, " +
                    "st_distance(st_setsrid(st_point(" + x + "," + y + "), 4326)::GEOGRAPHY , way::GEOGRAPHY) AS distance " +
                    "FROM planet_osm_point WHERE amenity = 'veterinary' AND name NOTNULL " +
                    "AND st_distance(st_setsrid(st_point(" + x + "," + y + "), 4326)::GEOGRAPHY , way::GEOGRAPHY) < " + distance +
                    " ORDER BY distance";
            ResultSet rs = new DatabseConnector().runSelect(query);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                result.add(obj);
            }
        } catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return result;
    }

    public List<GenericPositionAndNameObject> getVets(double x, double y, Integer distance) {
        return mergeLists(getVetsFromPoints(x, y, distance), getVetsFromPolygons(x, y, distance));
    }

    /**
     * Merges all inserted lists into one and than sorts result by Distance ascending.
     * Needed in case that there are some points of same type in both polygons and points tables.
     * @param lists
     * @return
     */
    @SafeVarargs
    private final List<GenericPositionAndNameObject> mergeLists(List<GenericPositionAndNameObject>... lists) {
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        if (lists != null) {
            for (List<GenericPositionAndNameObject> list : lists) {
                if (list != null) {
                    result.addAll(list);
                }
            }
        }
        result.sort((o1, o2) -> ((int) o1.getDistance() - (int) o2.getDistance()));
        return result;
    }

    private List<GenericPositionAndNameObject> getVetsFromPolygons(double x, double y, Integer distance) {
        if (distance == 0) {
            distance = 100000;
        }
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM (SELECT st_x(point) AS x, st_y(point) AS y, name," +
                    "st_distance(st_setsrid(st_point(" + x + ", " + y + "), 4326)::geography, point::geography) AS " +
                    "distance FROM (SELECT name, st_centroid(way)" +
                    "AS point FROM planet_osm_polygon WHERE amenity = 'veterinary' AND name NOTNULL) AS inner_table) AS inn" +
                    " WHERE distance < " + distance;
            ResultSet rs = new DatabseConnector().runSelect(query);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                result.add(obj);
            }
        } catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return result;
    }


    public BusStopsHolder getBusStops(double myX, double myY, double destinationX, double destinationY) {
        BusStopsHolder busStopsHolder = new BusStopsHolder();
        //17.107748, 47.4925
        try{
            String startBusQuery = "SELECT st_x(way) AS x, st_y(way) AS y, name, " +
                    "st_distance(st_setsrid(st_point(" + myX + ", " + myY + "), 4326)::geography, way) AS distance " +
                    "FROM planet_osm_point WHERE highway LIKE  'bus_stop' " +
                    "ORDER BY  distance LIMIT 1";
            String endBusQuery = "SELECT st_x(way) AS x, st_y(way) AS y, name, " +
                    "st_distance(st_setsrid(st_point(" + destinationX + ", " + destinationY + "), 4326)::geography, way) AS distance " +
                    "FROM planet_osm_point WHERE highway LIKE  'bus_stop' " +
                    "ORDER BY  distance LIMIT 1";
            ResultSet rs = new DatabseConnector().runSelect(startBusQuery);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                busStopsHolder.setStarBusStop(obj);
            }
            rs = new DatabseConnector().runSelect(endBusQuery);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                busStopsHolder.setEndBusStop(obj);
            }
        }  catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return busStopsHolder;
    }

    public List<GenericPositionAndNameObject> getParkLots(double x, double y, double myX, double myY) {
        return mergeLists(getParkLotsFromPoints(x, y, myX, myY), getParkLotsFromPolygons(x, y, myX, myY));
    }

    private List<GenericPositionAndNameObject> getParkLotsFromPolygons(double x, double y, double myX, double myY) {
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM (SELECT st_x(point) AS x, st_y(point) AS y, name, " +
                    "st_distance(st_setsrid(st_point(" + x + ", " + y + "), 4326)::geography, point::geography) AS distance,  " +
                    "st_distance(st_setsrid(st_point(" + myX + ", " + myY + "), 4326)::geography, point::geography) AS distance_from_me  " +
                    "FROM (SELECT name, st_centroid(way)AS point FROM planet_osm_polygon WHERE amenity = 'parking' AND name NOTNULL)  " +
                    "AS inner_table) AS inn " +
                    "WHERE distance < distance_from_me";
            ResultSet rs = new DatabseConnector().runSelect(query);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                result.add(obj);
            }
        } catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return result;
    }

    private List<GenericPositionAndNameObject> getParkLotsFromPoints(double x, double y, double myX, double myY) {
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM (SELECT st_x(way) AS x, st_y(way) AS y, name, " +
                    "st_distance(st_setsrid(st_point(" + x + ", " + y + "), 4326)::geography, way) AS distance, " +
                    "st_distance(st_setsrid(st_point(" + myX + ", " + myY + "), 4326)::geography, way) AS distance_from_me " +
                    "FROM planet_osm_point WHERE amenity LIKE 'parking' AND name NOTNULL) AS inner_table WHERE distance < distance_from_me";
            ResultSet rs = new DatabseConnector().runSelect(query);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                result.add(obj);
            }
        } catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return result;
    }


    public List<GenericPositionAndNameObject> getPetStores(double x, double y, Integer distance) {
        return mergeLists(getPetStoresFromPoints(x, y, distance), getPetStoresFromPolygons(x, y, distance));
    }

    private List<GenericPositionAndNameObject> getPetStoresFromPolygons(double x, double y, Integer distance) {
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM (SELECT st_x(point) AS x, st_y(point) AS y, name, " +
                    "st_distance(st_setsrid(st_point(" + x + ", " + y + "), 4326)::geography, point::geography) AS " +
                    "distance FROM " +
                            "(SELECT name, st_centroid(way) AS point " +
                                    "FROM planet_osm_polygon WHERE shop = 'pet' AND name NOTNULL) " +
                    "AS inner_table) AS inn WHERE  distance < " + distance;
            ResultSet rs = new DatabseConnector().runSelect(query);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                result.add(obj);
            }
        } catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return result;
    }

    private List<GenericPositionAndNameObject> getPetStoresFromPoints(double x, double y, Integer distance) {
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM (SELECT name, st_x(way) AS x, st_y(way) AS y, " +
                    "st_distance(st_setsrid(st_point(" + x + ", " + y + "), 4326)::geography, way) AS distance " +
                    "FROM planet_osm_point WHERE shop = 'pet' AND name NOTNULL ORDER BY distance) AS subq " +
                    "WHERE  distance <= " + distance;
            ResultSet rs = new DatabseConnector().runSelect(query);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                result.add(obj);
            }
        } catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return result;
    }

    public List<GenericPositionAndNameObject> getDogParks(double x, double y) {
        List<GenericPositionAndNameObject> result = new ArrayList<>();
        try {
            String query = "SELECT name, st_x(point) AS x, st_y(point) AS y" +
                    ", " +
                    "st_distance(st_setsrid(st_point(" + x + ", " + y + "), 4326)::geography, point) AS distance " +
                    "FROM (SELECT name, st_centroid(way) AS point " +
                    "FROM  planet_osm_polygon WHERE leisure LIKE 'dog_park' AND name NOTNULL) AS inner_table";
            ResultSet rs = new DatabseConnector().runSelect(query);
            while (rs.next()) {
                GenericPositionAndNameObject obj = GenericPositionAndNameObject.Builder.buildFromResultSet(rs);
                result.add(obj);
            }
        } catch (SQLException e) {
            LOG.error("Something went wrong during database query " + e.getMessage());
        }
        return result;
    }
}
