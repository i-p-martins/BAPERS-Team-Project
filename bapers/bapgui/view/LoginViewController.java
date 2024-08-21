package bapers.bapgui.view;

import bapers.bapadmn.StaffManager;
import bapers.bapdb.DBConnectivity;
import bapers.bapgui.viewmodel.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class LoginViewController {


    public LoginViewModel loginViewModel;
    public Button loginButton;
    public Button frgPass;
    public Label usernameLabel;
    public Label passwordLabel;
    public PasswordField passwordField;
    public TextField usernameField;
    public Label softwareLabel;
    public Label copyrightLabel;
    public Label copyrightDescLabel;

    public String ID;
    //the controller class registers employee information
    public static String username;
    public static String password;
    public static String loginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    //variables for DATABASE connection
    private final StaffManager staffManager = new StaffManager();

    public static String userIDLabel;
    public static String userRoleLabel;
    public static String userNameLabel;

    public LoginViewController() { }

    /**
     * @param actionEvent
     */
    public void login(ActionEvent actionEvent) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        try {
            Stage stage = new Stage();
            BorderPane root;

            Scene scene;
            Alert alert;

            String username = usernameField.getText();
            String password = passwordField.getText();

            String empID = staffManager.retrieveConnectedEmpId(username, password);

            //updating static variables
            LoginViewController.username = username;
            LoginViewController.password = password;

            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String connectQuery = "SELECT * FROM bapuser WHERE username = \""+username+"\"";

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            queryOutput.next();
            setUserIDLabel(queryOutput.getString("ID"));
            setUserRoleLabel(queryOutput.getString("role"));
            setUserNameLabel(queryOutput.getString("name"));

            //check if login is office manager
            if (staffManager.isOfficeManagerLogin(username, password)) {

                try {
                    //moves to office manager screen
                    staffManager.updateLastLogin();
                    root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/OfficeManagerView.fxml"));

                    scene = new Scene(root,width*.666,height*.61);

                    //@_programmer: Igor Pereira Martins
                    // swaps to selected screen
                    Stage stageToSwap = (Stage) loginButton.getScene().getWindow();
                    stageToSwap.setScene(scene);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //check if login is shift manager
            } else if (staffManager.isShiftManagerLogin(username, password)) {

                try {
                    staffManager.updateLastLogin();
                    //moves to shift manager screen
                    root = FXMLLoader.load(getClass().getResource("fxml/ShiftManagerFXML/ShiftManagerView.fxml"));

                    scene = new Scene(root,width*.666,height*.61);

                    //@_programmer: Igor Pereira Martins
                    // swaps to selected screen
                    Stage stageToSwap = (Stage) loginButton.getScene().getWindow();
                    stageToSwap.setScene(scene);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //check if login is receptionist
            } else if (staffManager.isReceptionistLogin(username, password)) {

                try {
                    staffManager.updateLastLogin();
                    //moves to receptionist screen
                    root = FXMLLoader.load(getClass().getResource("fxml/ReceptionistFXML/ReceptionistView.fxml"));

                    scene = new Scene(root,width*.666,height*.61);

                    //@_programmer: Igor Pereira Martins
                    // swaps to selected screen
                    Stage stageToSwap = (Stage) loginButton.getScene().getWindow();
                    stageToSwap.setScene(scene);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //check if login is technician
            } else if (staffManager.isTechnicianLogin(username, password)) {

                try {
                    staffManager.updateLastLogin();
                    //moves to technician screen
                    root = FXMLLoader.load(getClass().getResource("fxml/TechnicianFXML/TasksDashboard.fxml"));

                    scene = new Scene(root,width*.666,height*.61);

                    //@_programmer: Igor Pereira Martins
                    // swaps to selected screen
                    Stage stageToSwap = (Stage) loginButton.getScene().getWindow();
                    stageToSwap.setScene(scene);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                alert = new Alert(Alert.AlertType.WARNING);

                Stage tempStage = (Stage) alert.getDialogPane().getScene().getWindow();
                tempStage.getIcons().add(new Image(String.valueOf(getClass().getResource("fxml/assets/app.png"))));

                alert.setTitle("Warning!");
                alert.setHeaderText("Incorrect credentials!");
                alert.setContentText("Please enter valid credentials \nor contact the Shift Manager for support!");
                alert.showAndWait();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void forgotPassword(ActionEvent actionEvent) {
        try {
            TextInputDialog dialog = new TextInputDialog();

            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(String.valueOf(getClass().getResource("fxml/assets/app.png"))));

            dialog.setTitle("Oops!");
            dialog.setHeaderText("Forgot password?\n\nWe will email instructions on\nhow to reset your password.");
            dialog.setContentText("Please enter your username:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();

            if (staffManager.findUsername(dialog.getResult())) {
                if (result.isPresent()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(new Image(String.valueOf(getClass().getResource("fxml/assets/app.png"))));

                    alert.setTitle("Information!");
                    alert.setHeaderText("An email has been sent!.");
                    alert.setContentText("Please follow the provided instructions\nto reset your password!");
                    alert.showAndWait();
                }
            } else {

                if (result.isPresent()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(new Image(String.valueOf(getClass().getResource("fxml/assets/app.png"))));

                    alert.setTitle("Error!");
                    alert.setHeaderText("Invalid username!");
                    alert.setContentText("Please enter a valid username!");
                    alert.showAndWait();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUserIDLabel() {
        return userIDLabel;
    }

    public void setUserIDLabel(String userIDLabel) {
        this.userIDLabel = userIDLabel;
    }

    public static String getUserRoleLabel() {
        return userRoleLabel;
    }

    public void setUserRoleLabel(String userRoleLabel) {
        this.userRoleLabel = userRoleLabel;
    }

    public static String getUserNameLabel() {
        return userNameLabel;
    }

    public void setUserNameLabel(String userNameLabel) {
        this.userNameLabel = userNameLabel;
    }
}

