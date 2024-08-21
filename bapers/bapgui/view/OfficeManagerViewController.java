package bapers.bapgui.view;

import bapers.bapacct.CustomerAccount;
import bapers.bapadmn.StaffManager;
import bapers.bapdb.DBConnectivity;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class OfficeManagerViewController implements Initializable {

    private final StaffManager staffManager = new StaffManager();
    public Button logoutButton;

    public Label userNameLabel;
    public Label userRoleLabel;
    public Label userIDLabel;

    public Button manageJobsButton;
    public Button manageCustomersButton;
    public Button manageTasksButton;
    public Button manageUsersButton;
    public Button manageReportsButton;
    public Button manageBackupsButton;
    public Button managePaymentsButton;
    public Scene scene;

    private String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:dd").format(new Date());
    private String backupDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private String path = "C:/Users/Igor/Documents/DatabaseBackups/BAPERS/" + "asw_" + backupDate + ".sql";

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(LoginViewController.getUserNameLabel());
        userIDLabel.setText(LoginViewController.getUserIDLabel());
        userRoleLabel.setText(LoginViewController.getUserRoleLabel());

        Timeline checkJobs = new Timeline(new KeyFrame(Duration.seconds(60), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBConnectivity connectivity = new DBConnectivity();
                Connection connectDB = connectivity.DBConnectivity();

                String connectQuery = "SELECT * FROM bapjobs";

                try {
                    Statement statement = connectDB.createStatement();
                    ResultSet queryOutput = statement.executeQuery(connectQuery);

                    while (queryOutput.next()) {
                        String timeQuery = "SELECT TIMEDIFF(" +
                                queryOutput.getString("dateAccepted") + " " + queryOutput.getString("timeAccepted)") + "," +
                                queryOutput.getString("date") + " " + queryOutput.getString("timeStarted)") + ") AS TimeDiff;";

                        String timeQuery2 = "SELECT TIMEDIFF(" +
                                queryOutput.getString("dateAccepted") + " " + queryOutput.getString("timeAccepted)") + "," +
                                date + ") AS TimeDiff;";


                        ResultSet queryOutput2 = statement.executeQuery(timeQuery);
                        ResultSet queryOutput3 = statement.executeQuery(timeQuery2);

                        queryOutput2.next();
                        queryOutput3.next();

                        String diff = queryOutput2.getString("TimeDiff");
                        diff = diff.substring(diff.indexOf(":"));

                        String diff2 = queryOutput2.getString("TimeDiff");
                        diff2 = diff2.substring(diff2.indexOf(":"));

                        if (queryOutput.getString("urgency").equals("N")) {
                            if (Integer.parseInt(diff) > 24 || Integer.parseInt(diff2) > 24) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("JOB UNABLE TO MEET DEADLINE");
                                alert.setHeaderText("A job is no longer able to meet it's deadline");
                                alert.setContentText("The job with id: " + queryOutput.getString("ID") + "will fail to meet it's deadline");
                            }
                        } else {
                            if (Integer.parseInt(diff) > 6) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("JOB UNABLE TO MEET DEADLINE");
                                alert.setHeaderText("A job is no longer able to meet it's deadline");
                                alert.setContentText("The job with id: " + queryOutput.getString("ID") + "will fail to meet it's deadline");
                            }
                        }
                    }

                } catch (Exception e) { }
            }
        }));

        checkJobs.setCycleCount(Timeline.INDEFINITE);
        checkJobs.play();
    }

    public void logout(ActionEvent actionEvent) {

        try {

            Parent root1 = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));

            scene = new Scene(root1 ,width*.666,height*.61);

            // swaps to selected screen
            Stage stageToSwap = (Stage) logoutButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void viewUsersDashboard(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/UsersDashboard.fxml"));

            scene = new Scene(root ,width*.666,height*.61);

            // swaps to selected screen
            Stage stageToSwap = (Stage) manageUsersButton.getScene().getWindow();
            stageToSwap.setScene(scene);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewCustomersDashboard(ActionEvent actionEvent) {
        try {

            Parent root = (Pane)FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/CustomersDashboard.fxml"));

            scene = new Scene(root ,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins



            // swaps to selected screen
            Stage stageToSwap = (Stage) manageCustomersButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewPaymentsDashboard(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/PaymentsDashboard.fxml"));

            scene = new Scene(root ,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) managePaymentsButton.getScene().getWindow();
            stageToSwap.setScene(scene);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewJobsDashboard(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/JobsDashboard.fxml"));

            Scene scene = new Scene(root ,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) manageJobsButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewTasksDashboard(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/TasksDashboard.fxml"));

            Scene scene = new Scene(root ,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) manageTasksButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewBackupsDashboard(ActionEvent actionEvent) {
        Process p = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec("C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe -uroot -pCityUniversityTeam162021 --add-drop-database -B bapers -r" + path);

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

    public void viewReportsDashboard(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/ReportsDashboard.fxml"));

            Scene scene = new Scene(root ,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) manageReportsButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewDiscountDashboard(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("fxml/OfficeManagerFXML/DiscountsDashboard.fxml"));

            Scene scene = new Scene(root ,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) manageReportsButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
