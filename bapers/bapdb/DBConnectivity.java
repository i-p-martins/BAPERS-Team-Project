package bapers.bapdb;

import bapers.I_DBConnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectivity implements I_DBConnectivity {

    public Connection connection;

    public Connection DBConnectivity() {
        String dbName = "bapers";
        String username = "root";
        String password = "CityUniversityTeam162021";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+dbName, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * @param sql
     */
    public Connection connect(String sql) {
        // TODO - implement DBConnectivity.connect
        throw new UnsupportedOperationException();
    }

    /**
     * @param sql
     * @param connection
     */
    public int write(String sql, Connection connection) {
        // TODO - implement DBConnectivity.write
        throw new UnsupportedOperationException();
    }

    /**
     * @param sql
     * @param connection
     */
    public ResultSet read(String sql, Connection connection) {
        // TODO - implement DBConnectivity.read
        throw new UnsupportedOperationException();
    }

    /**
     * @param sql
     * @param connection
     */
    public void closeConnection(String sql, Connection connection) {
        // TODO - implement DBConnectivity.closeConnection
        throw new UnsupportedOperationException();
    }

}