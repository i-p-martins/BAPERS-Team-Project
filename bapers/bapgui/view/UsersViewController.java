package bapers.bapgui.view;

import bapers.bapadmn.Staff_ImplClass;
import bapers.bapdb.DBConnectivity;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsersViewController implements Initializable {
    public TableView<Staff_ImplClass> tbData = new TableView<>();
    public TableColumn Name;
    public TableColumn lastLogin;
    public TableColumn role;
    public TableColumn username;
    public TableColumn userID;
    public TableColumn password;

    public Label userIDLabel;
    public Label userRoleLabel;
    public Label userNameLabel;

    public Button goBackButton;
    public Button logoutButton;
    public Button deleteUserButton;
    public Button updateUserButton;
    public Button createUserButton;

    public TextField searchBox;

    private ObservableList<Staff_ImplClass> data = tbData.getItems();

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    public static String doHashing(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(LoginViewController.getUserNameLabel());
        userIDLabel.setText(LoginViewController.getUserIDLabel());
        userRoleLabel.setText(LoginViewController.getUserRoleLabel());

        displayDatabase();
        FilteredList<Staff_ImplClass> filteredData = new FilteredList<Staff_ImplClass>(data, p -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return user.getId().toLowerCase().contains(lowerCaseFilter) ||
                        user.getName().toLowerCase().contains(lowerCaseFilter) ||
                        user.getRole().toLowerCase().contains(lowerCaseFilter) ||
                        user.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                        user.getPassword().toLowerCase().contains(lowerCaseFilter) ||
                        user.getLastLogin().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Staff_ImplClass> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbData.comparatorProperty());
        tbData.setItems(sortedData);
    }

    private void displayDatabase() {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        String connectQuery = "SELECT * FROM bapuser";

        try {

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            data.clear();
            while (queryOutput.next()) {
                data.add(new Staff_ImplClass(queryOutput.getString("ID"),
                        queryOutput.getString("name"),
                        queryOutput.getString("role"),
                        queryOutput.getString("username"),
                        doHashing(queryOutput.getString("password")),
                        queryOutput.getString("lastLogin")
                ));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) {
        try {

            Parent root1 = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));

            Scene scene = new Scene(root1,width*.666,height*.61);

            // swaps to selected screen
            Stage stageToSwap = (Stage) logoutButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBack(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/OfficeManagerView.fxml"));

            Scene scene = new Scene(root,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) goBackButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUserAccount(ActionEvent actionEvent) {
        Dialog<ObservableList<Staff_ImplClass>> dialog = new Dialog<>();
        dialog.setTitle("Create New Customer");
        dialog.setHeaderText("Input the customer's information below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField userIdField = new TextField();
        TextField NameField = new TextField();
        ComboBox roleField = new ComboBox();
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        grid.add(new Label("User ID:"), 0, 0);
        grid.add(userIdField, 1, 0);
        grid.add(new Label("Full Name:"), 0, 1);
        grid.add(NameField, 1, 1);

        roleField.getItems().addAll("Office Manager","Shift Manager","Receptionist","Technician");
        grid.add(new Label("Role:"),0,2);
        grid.add(roleField,1,2);

        grid.add(new Label("Username:"), 0, 3);
        grid.add(usernameField, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordField, 1, 4);

        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(userIdField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                DBConnectivity connectivity = new DBConnectivity();
                Connection connectDB = connectivity.DBConnectivity();

                String insertQuery = "INSERT INTO bapuser VALUES(\"" +
                        userIdField.getText() + "\",\"" +
                        NameField.getText() + "\",\"" +
                        roleField.getValue().toString() + "\",\"" +
                        usernameField.getText() + "\",\"" +
                        passwordField.getText() + "\"," +
                        "\"0000-00-00 00:00:00\")";

                try {
                    Statement statement = connectDB.createStatement();
                    if (!(userIdField.getText().equals("") &&
                            NameField.getText().equals("") &&
                            roleField.getValue().toString().equals("") &&
                            usernameField.getText().equals("") &&
                            passwordField.getText().equals(""))) {
                        statement.executeUpdate(insertQuery);
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("One of your inputs is incorrect");
                    e.printStackTrace();
                    return null;
                }

                displayDatabase();
                return data;
            }
            return null;
        });

        Optional<ObservableList<Staff_ImplClass>> result = dialog.showAndWait();
    }

    public void updateUserAccount(ActionEvent actionEvent){
        ObservableList<Staff_ImplClass> data = tbData.getItems();

        List<String> choices = new ArrayList<>();
        for (Staff_ImplClass datum : data) {
            choices.add(datum.getId());
        }

        Dialog<ObservableList<Staff_ImplClass>> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Input the user's new information below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox updateField = new ComboBox();

        TextField NameField = new TextField();
        ComboBox roleField = new ComboBox();
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();

        updateField.getItems().addAll(choices);
        grid.add(new Label("User ID:"),0,0);
        grid.add(updateField,1,0);

        grid.add(new Label("Full Name:"), 0, 1);
        grid.add(NameField, 1, 1);
        roleField.getItems().addAll("Office Manager","Shift Manager","Receptionist","Technician");
        grid.add(new Label("Role:"),0,2);
        grid.add(roleField,1,2);
        grid.add(new Label("Username:"), 0, 3);
        grid.add(usernameField, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordField, 1, 4);

        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){
                DBConnectivity connectivity = new DBConnectivity();
                Connection connectDB = connectivity.DBConnectivity();

                String insertQuery = "";

                try{
                    Statement statement = connectDB.createStatement();
                    if (!(NameField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapuser " +
                                "SET name = \"" + NameField.getText() + "\"" +
                                "WHERE ID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(roleField.getValue().toString().trim().isEmpty())) {
                        insertQuery = "UPDATE bapuser " +
                                "SET role= \"" + roleField.getValue().toString() + "\"" +
                                "WHERE ID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(usernameField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapuser " +
                                "SET username = \"" + usernameField.getText() + "\"" +
                                "WHERE ID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(passwordField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapuser " +
                                "SET password = \"" + passwordField.getText() + "\"" +
                                "WHERE ID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                }

                catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("One of your inputs is incorrect");
                    e.printStackTrace();
                    return null;
                }

                displayDatabase();
                return data;
            }
            return null;
        });
        Optional<ObservableList<Staff_ImplClass>> result = dialog.showAndWait();
    }

    public void deleteUserAccount(ActionEvent actionEvent){
        ObservableList<Staff_ImplClass> data = tbData.getItems();
        List<String> choices = new ArrayList<>();
        for (int i = 0; i<data.size(); i++){
            choices.add(data.get(i).getId());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Choose which user to delete");
        dialog.setContentText("User ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String removeQuery = "DELETE FROM bapuser WHERE ID = \"" + result.get() + "\"";

            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(removeQuery);
            }

            catch (Exception e){
                e.printStackTrace();
            }

            displayDatabase();
        }
    }
}
