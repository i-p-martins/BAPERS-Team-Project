package bapers.bapgui.view;

import bapers.bapacct.CustomerAccount;
import bapers.bapacct.Job;
import bapers.bapdb.DBConnectivity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class JobsViewController implements Initializable {
    public TableView<Job> tbData = new TableView<>();
    public TableColumn ID;
    public TableColumn customerID;
    public TableColumn tasksID;
    public TableColumn userID;
    public TableColumn urgency;
    public TableColumn stipDeadline;
    public TableColumn price;
    public TableColumn date;
    public TableColumn timeStarted;
    public TableColumn timeAccepted;
    public TableColumn timeTaken;
    public TableColumn orderID;

    public Label userNameLabel;
    public Label userRoleLabel;
    public Label userIDLabel;

    public Button logoutButton;
    public Button createJobButton;
    public Button findJobButton;
    public Button deleteJobButton;
    public Button goBackButton;
    public Button assignTaskButton;

    public TextField searchBox;

    private ObservableList<Job> data = tbData.getItems();

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    public static String startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    public static String startTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(LoginViewController.getUserNameLabel());
        userIDLabel.setText(LoginViewController.getUserIDLabel());
        userRoleLabel.setText(LoginViewController.getUserRoleLabel());

        displayDatabase();
        FilteredList<Job> filteredData = new FilteredList<>(data, p -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(job -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return job.getCustomerID().toLowerCase().contains(lowerCaseFilter) ||
                        job.getTaskID().toLowerCase().contains(lowerCaseFilter) ||
                        job.getUserID().toLowerCase().contains(lowerCaseFilter) ||
                        job.getID().toLowerCase().contains(lowerCaseFilter) ||
                        job.getUrgency().toLowerCase().contains(lowerCaseFilter) ||
                        job.getStipDeadline().toLowerCase().contains(lowerCaseFilter) ||
                        job.getPrice().toLowerCase().contains(lowerCaseFilter) ||
                        job.getDate().toLowerCase().contains(lowerCaseFilter) ||
                        job.getTimeStarted().toLowerCase().contains(lowerCaseFilter)||
                        job.getTimeTaken().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Job> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbData.comparatorProperty());
        tbData.setItems(sortedData);
    }

    public void displayDatabase(){
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        String connectQuery = "SELECT * FROM bapjobs";

        try{

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            data.clear();
            while (queryOutput.next()){
                data.add(new Job(queryOutput.getString("customerID"),
                        queryOutput.getString("taskID"),
                        queryOutput.getString("userID"),
                        queryOutput.getString("ID"),
                        queryOutput.getString("orderID"),
                        queryOutput.getString("urgency"),
                        queryOutput.getString("stipDeadline"),
                        queryOutput.getString("price"),
                        queryOutput.getString("date"),
                        queryOutput.getString("timeStarted"),
                        queryOutput.getString("timeTaken"),
                        queryOutput.getString("timeAccepted")
                ));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
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

    public void OfficeManagerGoBack(ActionEvent actionEvent) {
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

    public void ShiftManagerGoBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/ShiftManagerFXML/ShiftManagerView.fxml"));

            Scene scene = new Scene(root,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) goBackButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReceptionistGoBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/ReceptionistFXML/ReceptionistView.fxml"));

            Scene scene = new Scene(root,width*.666,height*.61);

            //@_programmer: Igor Pereira Martins
            // swaps to selected screen
            Stage stageToSwap = (Stage) goBackButton.getScene().getWindow();
            stageToSwap.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createJob(ActionEvent actionEvent) {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> customerChoices = new ArrayList<>();
        List<String> taskIdChoices = new ArrayList<>();
        List<String> userIdChoices = new ArrayList<>();

        String customerQuery = "SELECT * FROM bapcust";
        String taskQuery = "SELECT * FROM baptasks";
        String userQuery = "SELECT * FROM bapuser WHERE ID LIKE \"TC%\"";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(customerQuery);
            while (queryOutput.next()){
                customerChoices.add(queryOutput.getString("fullName"));
            }

            queryOutput = statement.executeQuery(taskQuery);
            while (queryOutput.next()){
                taskIdChoices.add(queryOutput.getString("ID"));
            }

            queryOutput = statement.executeQuery(userQuery);
            while (queryOutput.next()){
                userIdChoices.add(queryOutput.getString("ID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        Dialog<ObservableList<Job>> dialog = new Dialog<>();
        dialog.setTitle("Create New Job");
        dialog.setHeaderText("Input the job's information below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox customerIdField = new ComboBox();
        ComboBox taskIdField = new ComboBox();
        ComboBox urgencyField = new ComboBox();

        TextField IDField = new TextField();
        TextField orderIdField = new TextField();
        TextField stipDeadlineField = new TextField();
        TextField amountField = new TextField();

        customerIdField.getItems().addAll(customerChoices);
        grid.add(new Label("Customer:"),0,0);
        grid.add(customerIdField,1,0);
        taskIdField.getItems().addAll(taskIdChoices);
        grid.add(new Label("Task ID:"),0,1);
        grid.add(taskIdField,1,1);
        grid.add(new Label("Job ID:"),0,2);
        grid.add(IDField,1,2);
        grid.add(new Label("Order ID:"),0,3);
        grid.add(orderIdField, 1, 3);
        urgencyField.getItems().addAll("Y","N");
        grid.add(new Label("Urgent:"),0,4);
        grid.add(urgencyField,1,4);

        grid.add(new Label("Stipulated Deadline:"),0,5);
        grid.add(stipDeadlineField,1,5);
        grid.add(new Label("Amount:"),0,6);
        grid.add(amountField,1,6);
        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(customerIdField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                 for (int i=0; i<Integer.parseInt(amountField.getText()); i++) {
                     try {
                         Statement statement = connectDB.createStatement();

                         String insertQuery = "SELECT customerID, valued FROM bapcust WHERE fullName = \""
                                 + customerIdField.getValue().toString() + "\";";

                         String insertQuery2 = "SELECT price FROM baptasks WHERE ID = \""
                                 + taskIdField.getValue().toString() + "\";";

                         ResultSet queryOutput = statement.executeQuery(insertQuery);
                         queryOutput.next();
                         String ID = queryOutput.getString("customerID");
                         String value = queryOutput.getString("valued");

                         queryOutput = statement.executeQuery(insertQuery2);
                         queryOutput.next();
                         String price = queryOutput.getString("price");

                         String insertQueryfinal = "INSERT INTO bapjobs VALUES(\"" + ID + "\",\"" +
                                 taskIdField.getValue().toString() + "\"," +
                                 "NULL" + ",\"" +
                                 (Integer.parseInt(IDField.getText())+i) + "\",\"" +
                                 urgencyField.getValue().toString() + "\",\"" +
                                 stipDeadlineField.getText() + "\",\"" +
                                 price + "\",\"" +
                                 startDate + "\",\"" +
                                 startTime + "\"," +
                                 "NULL,\"" +
                                 orderIdField.getText() + "\",NULL,NULL);";

                         statement.executeUpdate(insertQueryfinal);

                         String discountQuery = "SELECT * FROM bapdisc WHERE CustomerID = \"" + ID + "\"";
                         queryOutput = statement.executeQuery(discountQuery);
                         String discount = "";

                         while (queryOutput.next()) {
                             if (queryOutput.getString("fixed").equals("N")) {
                                 try {
                                     if (queryOutput.getString("taskID").equals(taskIdField.getValue().toString())) {
                                         discount = queryOutput.getString("discountRate");
                                     }
                                 } catch (NullPointerException nullpointer) {
                                 }
                             } else {
                                 discount = queryOutput.getString("discountRate");
                             }
                         }
                         if (discount.length()>0) {
                             discount = discount.substring(0, discount.length() - 1);
                             price = String.valueOf(Double.parseDouble(price) - (Double.parseDouble(price) * (Double.parseDouble(discount) / 100)));
                             price = String.valueOf(Double.parseDouble(price) + (Double.parseDouble(price) * (2 / 10)));
                         }

                         String payDate = "";
                         if (value.equals("Y")) {
                             payDate = startDate.substring(0, 4) + "-" + (Integer.parseInt(startDate.substring(5, 7)) + 1) + "-10";
                             if (payDate.substring(5, 6) == "13") {
                                 payDate = (Integer.parseInt(startDate.substring(0, 3)) + 1) + "-" + (Integer.parseInt(startDate.substring(5, 6)) + 1) + "-10";
                             }
                         } else {
                             payDate = startDate;
                         }


                         String insertPaymentQuery = "INSERT INTO bappay VALUES(" + (Integer.parseInt(IDField.getText())+i) + ",\""+
                                 ID + "\",\"" +
                                 orderIdField.getText() + "\",\"" +
                                 price + "\",\"" +
                                 payDate + "\"," +
                                 "\"N\",NUll,NULL,NULL,NULL);";
                         System.out.println(insertPaymentQuery);
                         statement.executeUpdate(insertPaymentQuery);
                     }
                     catch (Exception e) {
                         Alert alert = new Alert(Alert.AlertType.ERROR);
                         alert.setTitle("ERROR");
                         alert.setHeaderText("Input Error");
                         alert.setContentText("One of your inputs is incorrect");
                         e.printStackTrace();
                         return null;
                     }
                 }

                displayDatabase();
                return data;
            }
            return null;
        });

        Optional<ObservableList<Job>> result = dialog.showAndWait();
    }

    public void deleteJob(ActionEvent actionEvent) {
        ObservableList<Job> data = tbData.getItems();
        List<String> choices = new ArrayList<>();
        for (int i = 0; i<data.size(); i++){
            choices.add(data.get(i).getID());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Delete Job");
        dialog.setHeaderText("Choose which job to delete");
        dialog.setContentText("Job ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String removeQuery = "DELETE FROM bapjobs WHERE ID = \"" + result.get() + "\"";

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

    public void assignTask(ActionEvent actionEvent) {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> jobChoices = new ArrayList<>();
        String jobQuery = "SELECT * FROM bapjobs";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(jobQuery);
            while (queryOutput.next()){
                jobChoices.add(queryOutput.getString("ID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        List<String> userIdChoices = new ArrayList<>();
        String userQuery = "SELECT * FROM bapuser WHERE role = \"Technician\";";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(userQuery);
            while (queryOutput.next()){
                userIdChoices.add(queryOutput.getString("ID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        Dialog<ObservableList<Job>> dialog = new Dialog<>();
        dialog.setTitle("Assign Task");
        dialog.setHeaderText("Select which technician to assign the job to");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox jobIdField = new ComboBox();
        ComboBox userIdField = new ComboBox();

        jobIdField.getItems().addAll(jobChoices);
        grid.add(new Label("Job ID:"),0,0);
        grid.add(jobIdField,1,0);
        userIdField.getItems().addAll(userIdChoices);
        grid.add(new Label("User ID:"),0,1);
        grid.add(userIdField,1,1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){

                String updateQuery = "Update bapjobs SET userID = \""+ userIdField.getValue().toString() +
                        "\", timeStarted = \"" + startTime + "\", dateAccepted = \"" + startDate +
                        "\" WHERE ID = \"" + jobIdField.getValue().toString() +"\"";

                try{

                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(updateQuery);
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
        Optional<ObservableList<Job>> result = dialog.showAndWait();
    }

    public void acceptTask(ActionEvent actionEvent) {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> jobChoices = new ArrayList<>();
        String jobQuery = "SELECT * FROM bapjobs";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(jobQuery);
            while (queryOutput.next()){
                jobChoices.add(queryOutput.getString("ID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        Dialog<ObservableList<Job>> dialog = new Dialog<>();
        dialog.setTitle("Accept Task");
        dialog.setHeaderText("Select which job to accept:");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox jobIdField = new ComboBox();

        jobIdField.getItems().addAll(jobChoices);
        grid.add(new Label("Job ID:"),0,0);
        grid.add(jobIdField,1,0);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){

                String updateQuery = "Update bapjobs SET userID = \""+ LoginViewController.getUserIDLabel() +
                        "\", timeStarted = \"" + startTime + "\", dateAccepted = \"" + startDate +
                        "\" WHERE ID = \"" + jobIdField.getValue().toString() +"\"";

                try{

                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(updateQuery);
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
        Optional<ObservableList<Job>> result = dialog.showAndWait();
    }

    public void completeTask(ActionEvent actionEvent) {
        ObservableList<Job> data = tbData.getItems();
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> choices = new ArrayList<>();
        String jobQuery = "SELECT * FROM bapjobs";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(jobQuery);
            while (queryOutput.next()){
                choices.add(queryOutput.getString("ID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }


        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Complete a Job");
        dialog.setHeaderText("Choose which job to mark as complete");
        dialog.setContentText("Job ID:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){

            String timeQuery = "SELECT timeStarted from bapjobs WHERE ID = \"" + result.get() + "\"";

            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryOutput = statement.executeQuery(timeQuery);

                String time = "";
                while (queryOutput.next()){
                    time = queryOutput.getString("timeStarted");
                }
                String finishTime = difference(time, startTime);

                System.out.println(finishTime);

                String updateQuery = "UPDATE bapjobs SET timeTaken = \"" + finishTime +
                        "\", timeAccepted = \"" + startTime + "\" WHERE ID = \"" + result.get() +"\"";

                statement.executeUpdate(updateQuery);
            }

            catch (Exception e){
                e.printStackTrace();
            }

            displayDatabase();
        }
    }

    private static String difference(String start, String stop){
        int startHours = Integer.parseInt(start.substring(0,2));
        int startMinutes = Integer.parseInt(start.substring(3,5));
        int stopHours = Integer.parseInt(stop.substring(0,2));
        int stopMinutes = Integer.parseInt(stop.substring(3,5));

        if (stopHours<startHours){
            stopHours += 24;
        }

        if (stopMinutes<startMinutes){
            stopHours-= 1;
            stopMinutes += 60;
        }

        stopMinutes -= startMinutes;
        stopHours -= startHours;
        stopHours *= 60;

        String timeDifference = String.valueOf(stopHours+stopMinutes);
        return timeDifference;
    }
}
