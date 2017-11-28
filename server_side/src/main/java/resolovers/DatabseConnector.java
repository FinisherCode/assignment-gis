package resolovers;

import model.GenericPositionAndNameObject;
import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Connector to database - ut connects to database and runs query.
 */
public class DatabseConnector {

    private static final Logger LOG = Logger.getLogger(DataGatherer.class.getName());

    private Connection connection = null;

    /**
     * Contructor first checks wheater is JDBC driver loaded into project. Than tries to connect to database.
     */
    public DatabseConnector(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            return;
        }
        connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgis", "postgres", "admin");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
        }
    }

    /**
     * Runs query that is inserted in params (most generic solution that i could come with).
     * @param query
     * @return
     */
    public ResultSet runSelect(String query){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            LOG.info("executed sql = " + query);
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
}
