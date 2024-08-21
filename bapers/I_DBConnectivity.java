package bapers;

import java.sql.Connection;
import java.sql.ResultSet;

public interface I_DBConnectivity {

    /**
     * @param sql
     */
    abstract Connection connect(String sql);

    /**
     * @param sql
     * @param connection
     */
    abstract int write(String sql, Connection connection);

    /**
     * @param sql
     * @param connection
     */
    abstract ResultSet read(String sql, Connection connection);

    /**
     * @param sql
     * @param connection
     */
    abstract void closeConnection(String sql, Connection connection);

}