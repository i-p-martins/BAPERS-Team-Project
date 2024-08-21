package bapers.bapgui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceptionistViewController implements Initializable {
    public Button logoutButton;
    public Label userIDLabel;
    public Label userRoleLabel;
    public Label userNameLabel;
    public Button handleCustomersButton;
    public Button handleTasksButton;
    public Button handleJobsButton;
    public Button handleBackupsButton;
    public Scene scene;

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(LoginViewController.getUserNameLabel());
        userIDLabel.setText(LoginViewController.getUserIDLabel());
        userRoleLabel.setText(LoginViewController.getUserRoleLabel());
    }

    public void logout(ActionEvent actionEvent) {

        try {

            Parent root1 = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));

            Scene scene = new Scene(root1,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) logoutButton.getScene().getWindow();
            stageToSwap.setScene(scene);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void viewCustomersDashboard(ActionEvent actionEvent) {
        try {

            Parent root = (Pane)FXMLLoader.load(getClass().getResource("fxml/ReceptionistFXML/CustomersDashboard.fxml"));

            scene = new Scene(root,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) handleCustomersButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewJobsDashboard(ActionEvent actionEvent){
        try {

            Parent root = (Pane)FXMLLoader.load(getClass().getResource("fxml/ReceptionistFXML/JobsDashboard.fxml"));

            scene = new Scene(root,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) handleCustomersButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewPaymentsDashboard(ActionEvent actionEvent){
        try {

            Parent root = (Pane)FXMLLoader.load(getClass().getResource("fxml/ReceptionistFXML/PaymentsDashboard.fxml"));

            scene = new Scene(root,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) handleCustomersButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
