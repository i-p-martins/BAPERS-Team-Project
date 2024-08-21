package bapers.bapgui.view;

import bapers.bapdb.DBConnectivity;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class BackupsViewController {
    public Label userNameLabel;
    public Label userRoleLabel;
    public Label userIDLabel;
    public Button logoutButton;
    public Button goBackButton;
    public Button fullBackupButton;
    public Button localBackupButton;
    public Button remoteBackupButton;
    private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private String path = "C:/Users/Igor/Documents/DatabaseBackups/BAPERS/" + "asw_" + date + ".sql";
//    public TableView tbData;
//    public TableColumn backupID;
//    public TableColumn backupType;
//    public TableColumn backupDate;
//    public TableColumn userID;
//    public TableColumn role;
//    public TableColumn location;
//    public TableColumn status;

    public void logout(ActionEvent actionEvent) {
        try {

            Parent root1 = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));

            Scene scene = new Scene(root1);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) logoutButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OfficeManagerGoBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/OfficeManagerView.fxml"));

            Scene scene = new Scene(root);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) goBackButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShiftManagerGoBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/ShiftManagerFXML/ShiftManagerView.fxml"));

            Scene scene = new Scene(root);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) goBackButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fullBackup(ActionEvent actionEvent) {
        Process p = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec("C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe -uroot -pCityUniversityTeam162021 --add-drop-database -B bapers -r"+path);

            int processComplete = p.waitFor();

            if (processComplete == 0) {
                System.out.println("Backup created successfully!");
            } else {
                System.out.println("Could not create the backup");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void localBackup(ActionEvent actionEvent) {
    }

    public void remoteBackup(ActionEvent actionEvent) {
    }
}
