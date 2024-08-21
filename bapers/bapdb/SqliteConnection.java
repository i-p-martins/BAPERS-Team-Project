package bapers.bapdb;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnection {

    public static Connection Connector() {

        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:C:/Users/Igor/Documents/University Materials/Year 2 (2020-2021)/IN2018 - Team Project/codetest/codetest/src/bapers/bapdb/bapers.sqlite");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}