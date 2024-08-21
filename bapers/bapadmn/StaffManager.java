package bapers.bapadmn;

import bapers.bapdb.DBConnectivity;
import bapers.bapdb.SqliteConnection;
import bapers.bapgui.view.LoginViewController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffManager {

    private final Connection connection;


    public StaffManager() {
        connection = SqliteConnection.Connector();

        //handling null if Exception throws
        if (connection == null)
            System.exit(1);
    }

    public boolean isDatabaseConnected() {

        try {
            return connection.isClosed();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isOfficeManagerLogin(String user, String pass) throws SQLException {

        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * from bapuser where username =? and password = ? and role = 'Office Manager' ";
        boolean isOfficeManagerLogin = false;
        try {

            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);


            resultSet = preparedStatement.executeQuery();


            isOfficeManagerLogin = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            assert preparedStatement != null;
            preparedStatement.close();
            assert resultSet != null;
            resultSet.close();
        }
        return isOfficeManagerLogin;
    }

    public boolean isShiftManagerLogin(String user, String pass) throws SQLException {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * from bapuser where username =? and password = ? and role = 'Shift Manager' ";
        boolean isShiftManagerLogin = false;
        try {

            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);


            resultSet = preparedStatement.executeQuery();


            isShiftManagerLogin = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            assert preparedStatement != null;
            preparedStatement.close();
            assert resultSet != null;
            resultSet.close();
        }
        return isShiftManagerLogin;
    }

    public boolean isReceptionistLogin(String user, String pass) throws SQLException {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * from bapuser where username =? and password = ? and role = 'Receptionist' ";
        boolean isReceptionistLogin = false;
        try {

            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);


            resultSet = preparedStatement.executeQuery();


            isReceptionistLogin = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            assert preparedStatement != null;
            preparedStatement.close();
            assert resultSet != null;
            resultSet.close();
        }
        return isReceptionistLogin;
    }

    public boolean isTechnicianLogin(String user, String pass) throws SQLException {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * from bapuser where username =? and password = ? and role = 'Technician' ";
        boolean isTechnicianLogin = false;
        try {

            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);


            resultSet = preparedStatement.executeQuery();


            isTechnicianLogin = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            assert preparedStatement != null;
            preparedStatement.close();
            assert resultSet != null;
            resultSet.close();
        }
        return isTechnicianLogin;
    }

    public String retrieveConnectedEmpId(String username, String password) throws SQLException {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select ID from bapuser where username =? and password = ?";
        String empID = "";
        try {

            preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                empID = resultSet.getString("ID");
                //TablesController.setStaffType(resultSet.getString(2));


            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        } finally {
            assert preparedStatement != null;
            preparedStatement.close();
            assert resultSet != null;
            resultSet.close();
        }
        return empID;
    }

    public void updateLastLogin() {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        String lastLogin = LoginViewController.loginTime;
        String username = LoginViewController.username;
        String password = LoginViewController.password;


        String query = "UPDATE bapuser SET lastLogin =? where username = ? and password = ?";
        try (PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {

            preparedStatement.setString(1, lastLogin);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public boolean findUsername(String userName) throws Exception {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * from Staff where username =?";
        boolean userFound = false;
        try {

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);

            resultSet = preparedStatement.executeQuery();


            userFound = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            assert preparedStatement != null;
            preparedStatement.close();
            assert resultSet != null;
            resultSet.close();
        }
        return userFound;

    }


}
