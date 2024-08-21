package bapers.bapgui.view;

import bapers.bapproc.Task;
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

public class TasksViewController implements Initializable{
    public TableView<Task> tbData = new TableView<>();
    public TableColumn taskId;
    public TableColumn taskDescription;
    public TableColumn location;
    public TableColumn price;
    public TableColumn duration;

    public Label userNameLabel;
    public Label userRoleLabel;
    public Label userIDLabel;

    public Button logoutButton;
    public Button goBackButton;
    public Button createTaskButton;
    public Button findTaskButton;
    public Button deleteTaskButton;
    public Button updateTaskButton;

    public TextField taskIdField;
    public TextField taskDescriptionField;
    public TextField locationField;
    public TextField priceField;
    public TextField durationField;

    public TextField searchBox;

    private ObservableList<Task> data = tbData.getItems();

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        userNameLabel.setText(LoginViewController.getUserNameLabel());
        userIDLabel.setText(LoginViewController.getUserIDLabel());
        userRoleLabel.setText(LoginViewController.getUserRoleLabel());

        displayDatabase();
        FilteredList<Task> filteredData = new FilteredList<Task>(data, p -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(task -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return task.getId().toLowerCase().contains(lowerCaseFilter) ||
                        task.getDescription().toLowerCase().contains(lowerCaseFilter) ||
                        task.getLocation().toLowerCase().contains(lowerCaseFilter) ||
                        task.getPrice().toLowerCase().contains(lowerCaseFilter) ||
                        task.getDuration().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Task> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbData.comparatorProperty());
        tbData.setItems(sortedData);
    }

    private void displayDatabase(){
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        String connectQuery = "SELECT * FROM baptasks";

        try{

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            data.clear();
            while (queryOutput.next()){
                data.add(new Task(queryOutput.getString("ID"),
                        queryOutput.getString("taskDescription"),
                        queryOutput.getString("location"),
                        queryOutput.getString("price"),
                        queryOutput.getString("duration")
                ));
            }
        }

        catch (Exception e){
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

    public void createTask(ActionEvent actionEvent) {
        Dialog<ObservableList<Task>> dialog = new Dialog<>();
        dialog.setTitle("Create New Task");
        dialog.setHeaderText("Input the task's information below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField taskIdField = new TextField();
        TextField taskDescriptionField = new TextField();
        TextField locationField = new TextField();
        TextField priceField = new TextField();
        TextField durationField = new TextField();

        grid.add(new Label("Task ID:"),0,0);
        grid.add(taskIdField,1,0);
        grid.add(new Label("Description:"),0,1);
        grid.add(taskDescriptionField,1,1);
        grid.add(new Label("Location:"),0,2);
        grid.add(locationField,1,2);
        grid.add(new Label("Price:"),0,3);
        grid.add(priceField,1,3);
        grid.add(new Label("Duration:"),0,4);
        grid.add(durationField,1,4);

        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(taskIdField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                DBConnectivity connectivity = new DBConnectivity();
                Connection connectDB = connectivity.DBConnectivity();

                String insertQuery = "INSERT INTO baptasks VALUES(\"" +
                        taskIdField.getText() + "\",\"" +
                        taskDescriptionField.getText() + "\",\"" +
                        locationField.getText() + "\",\"" +
                        priceField.getText() + "\",\"" +
                        durationField.getText() + "\")";
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(insertQuery);
                }

                catch (Exception e) {
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

        Optional<ObservableList<Task>> result = dialog.showAndWait();
    }

    public void deleteTask(ActionEvent actionEvent) {
        ObservableList<Task> data = tbData.getItems();
        List<String> choices = new ArrayList<>();
        for (int i = 0; i<data.size(); i++){
            choices.add(data.get(i).getId());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Delete Task");
        dialog.setHeaderText("Choose which task to delete");
        dialog.setContentText("Task ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String removeQuery = "DELETE FROM baptasks WHERE ID = \"" + result.get() + "\"";

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

    public void updateTask(ActionEvent actionEvent) {
        ObservableList<Task> data = tbData.getItems();

        List<String> choices = new ArrayList<>();
        for (Task datum : data) {
            choices.add(datum.getId());
        }

        Dialog<ObservableList<Task>> dialog = new Dialog<>();
        dialog.setTitle("Edit Task");
        dialog.setHeaderText("Input the task's new information below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox updateField = new ComboBox();

        TextField taskDescriptionField = new TextField();
        TextField locationField = new TextField();
        TextField priceField = new TextField();
        TextField durationField = new TextField();

        updateField.getItems().addAll(choices);
        grid.add(new Label("Task ID:"),0,0);
        grid.add(updateField,1,0);

        grid.add(new Label("Description:"),0,1);
        grid.add(taskDescriptionField,1,1);
        grid.add(new Label("Location:"),0,2);
        grid.add(locationField,1,2);
        grid.add(new Label("Price:"),0,3);
        grid.add(priceField,1,3);
        grid.add(new Label("Duration:"),0,4);
        grid.add(durationField,1,4);


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
                    if (!(taskDescriptionField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE baptasks " +
                                "SET fullName = \"" + taskDescriptionField.getText() + "\"" +
                                "WHERE ID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(locationField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE baptasks " +
                                "SET fullName = \"" + locationField.getText() + "\"" +
                                "WHERE ID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(priceField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE baptasks " +
                                "SET fullName = \"" + priceField.getText() + "\"" +
                                "WHERE ID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(durationField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE baptasks " +
                                "SET fullName = \"" + durationField.getText() + "\"" +
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
        Optional<ObservableList<Task>> result = dialog.showAndWait();
    }
}
