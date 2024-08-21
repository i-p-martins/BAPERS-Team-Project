package bapers.bapgui.view;

import bapers.bapcust.DiscountPlan;
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
import org.w3c.dom.Text;

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

public class DiscountsViewController implements Initializable{
    public TableView<DiscountPlan> tbData = new TableView<>();
    public TableColumn discountID;
    public TableColumn customerID;
    public TableColumn fixed;
    public TableColumn taskID;
    public TableColumn volume;
    public TableColumn discountRate;

    public Label userIDLabel;
    public Label userRoleLabel;
    public Label userNameLabel;

    public Button logoutButton;
    public Button goBackButton;
    public Button editDiscountButton;
    public Button deleteDiscountButton;
    public Button createDiscountButton;

    public TextField searchBox;

    private ObservableList<DiscountPlan> data = tbData.getItems();

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayDatabase();
        FilteredList<DiscountPlan> filteredData = new FilteredList<>(data, p -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(discountPlan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                Boolean taskIDCheck = false;
                Boolean volumeCheck = false;
                try {
                    taskIDCheck = discountPlan.getTaskID().toLowerCase().contains(lowerCaseFilter);
                }
                catch (NullPointerException nullPointer){
                }
                try {
                    volumeCheck = discountPlan.getTaskID().toLowerCase().contains(lowerCaseFilter);
                }
                catch (NullPointerException nullPointer){
                }
                return discountPlan.getDiscountID().toLowerCase().contains(lowerCaseFilter) ||
                        discountPlan.getCustomerID().toLowerCase().contains(lowerCaseFilter) ||
                        discountPlan.getFixed().toLowerCase().contains(lowerCaseFilter) ||
                        taskIDCheck ||
                        volumeCheck ||
                        discountPlan.getDiscountRate().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<DiscountPlan> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbData.comparatorProperty());
        tbData.setItems(sortedData);
    }

    private void displayDatabase(){
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        String connectQuery = "SELECT * FROM bapdisc";

        try{

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            data.clear();
            while (queryOutput.next()){
                data.add(new DiscountPlan(queryOutput.getString("DiscountID"),
                        queryOutput.getString("CustomerID"),
                        queryOutput.getString("fixed"),
                        queryOutput.getString("taskID"),
                        queryOutput.getString("volume"),
                        queryOutput.getString("discountRate")
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

    public void createDiscount(){
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> customerChoices = new ArrayList<>();
        List<String> taskIdChoices = new ArrayList<>();

        String customerQuery = "SELECT * FROM bapcust";
        String taskQuery = "SELECT * FROM baptasks";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(customerQuery);
            while (queryOutput.next()){
                customerChoices.add(queryOutput.getString("customerID"));
            }

            queryOutput = statement.executeQuery(taskQuery);
            while (queryOutput.next()){
                taskIdChoices.add(queryOutput.getString("ID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        Dialog<ObservableList<DiscountPlan>> dialog = new Dialog<>();
        dialog.setTitle("Create New Discount Plan");
        dialog.setHeaderText("Input the plan below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField discountIdField = new TextField();

        ComboBox customerIdField = new ComboBox();
        ComboBox<String> fixedField = new ComboBox<>();
        ComboBox taskIDField = new ComboBox();

        TextField volumeField = new TextField();
        TextField discountRateField = new TextField();

        grid.add(new Label("Discount ID:"),0,0);
        grid.add(discountIdField,1,0);

        customerIdField.getItems().addAll(customerChoices);
        grid.add(new Label("Customer ID:"),0,1);
        grid.add(customerIdField,1,1);
        fixedField.getItems().addAll("Y","N");
        grid.add(new Label("Fixed Rate:"),0,2);
        grid.add(fixedField,1,2);
        taskIDField.getItems().add("");
        taskIDField.getItems().addAll(taskIdChoices);
        grid.add(new Label("Task ID:"),0,3);
        grid.add(taskIDField,1,3);
        grid.add(new Label("Volume:"),0,4);
        grid.add(volumeField,1,4);
        grid.add(new Label("Discount Rate:"),0,5);
        grid.add(discountRateField,1,5);

        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(customerIdField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){
                try{
                    Statement statement = connectDB.createStatement();
                    if (fixedField.getSelectionModel().getSelectedItem() == "Y") {
                        String insertQuery = "INSERT INTO bapdisc VALUES(" +
                                "\"" + discountIdField.getText() + "\"," +
                                "\"" +customerIdField.getValue().toString() + "\"," +
                                "\"" + fixedField.getValue() + "\"," +
                                "NULL,"+
                                "NULL,"+
                                "\"" + discountRateField.getText() + "\");";

                        statement.executeUpdate(insertQuery);
                    }
                    else{
                        if (!(taskIDField.getSelectionModel().isEmpty())) {
                            String insertQuery = "INSERT INTO bapdisc VALUES(" +
                                    "\"" + discountIdField.getText() + "\"," +
                                    "\"" + customerIdField.getValue().toString() + "\"," +
                                    "\"" + fixedField.getValue() + "\"," +
                                    "\"" + taskIDField.getValue().toString() + "\","+
                                    "NULL," +
                                    "\"" + discountRateField.getText() + "\")";
                            statement.executeUpdate(insertQuery);
                        }
                        else{
                            String insertQuery = "INSERT INTO bapdisc VALUES(\"" +
                                    discountIdField.getText() + "\",\"" +
                                    customerIdField.getValue().toString() + "\",\"" +
                                    fixedField.getValue() + "\",NULL,\"" +
                                    volumeField.getText() + "\",\"" +
                                    discountRateField.getText() + "\")";
                            statement.executeUpdate(insertQuery);
                        }
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

        Optional<ObservableList<DiscountPlan>> result = dialog.showAndWait();
    }

    public void editDiscount(){ObservableList<DiscountPlan> data = tbData.getItems();
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> customerChoices = new ArrayList<>();
        List<String> taskIdChoices = new ArrayList<>();

        String customerQuery = "SELECT * FROM bapcust";
        String taskQuery = "SELECT * FROM baptasks";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(customerQuery);
            while (queryOutput.next()){
                customerChoices.add(queryOutput.getString("customerID"));
            }

            queryOutput = statement.executeQuery(taskQuery);
            while (queryOutput.next()){
                taskIdChoices.add(queryOutput.getString("ID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
        List<String> choices = new ArrayList<>();
        for (DiscountPlan datum : data) {
            choices.add(datum.getDiscountID());
        }

        Dialog<ObservableList<DiscountPlan>> dialog = new Dialog<>();
        dialog.setTitle("Edit Discount Plan");
        dialog.setHeaderText("Input the new discount plan below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox<String> updateField = new ComboBox<>();
        ComboBox<String> CustomerIDField = new ComboBox<>();
        ComboBox<String> fixedField = new ComboBox<>();
        ComboBox<String> taskIDField = new ComboBox<>();
        TextField volumeField = new TextField();
        TextField discountRateField = new TextField();

        updateField.getItems().addAll(choices);
        grid.add(new Label("Discount Plan ID:"),0,0);
        grid.add(updateField,1,0);
        CustomerIDField.getItems().addAll(customerChoices);
        grid.add(new Label("Customer ID:"),0,1);
        grid.add(CustomerIDField,1,1);
        fixedField.getItems().addAll("Y","N");
        grid.add(new Label("Fixed:"),0,2);
        grid.add(fixedField,1,2);
        taskIDField.getItems().add("");
        taskIDField.getItems().addAll(taskIdChoices);
        grid.add(new Label("Task ID:"),0,3);
        grid.add(taskIDField,1,3);
        grid.add(new Label("Volume:"),0,4);
        grid.add(volumeField,1,4);
        grid.add(new Label("Discount Rate:"),0,5);
        grid.add(discountRateField,1,5);

        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){
                String insertQuery = "";

                try{
                    Statement statement = connectDB.createStatement();
                    if (!(CustomerIDField.getSelectionModel().isEmpty())) {
                        insertQuery = "UPDATE bapdisc " +
                                "SET CustomerID = \"" + CustomerIDField.getValue() + "\"" +
                                "WHERE DiscountID = \""+ updateField.getValue() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(fixedField.getSelectionModel().isEmpty())) {
                        insertQuery = "UPDATE bapdisc " +
                                "SET fixed = \"" + fixedField.getValue() + "\"" +
                                "WHERE DiscountID = \""+ updateField.getValue() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(taskIDField.getSelectionModel().isEmpty())) {
                        insertQuery = "UPDATE bapdisc " +
                                "SET taskID = \"" + taskIDField.getValue() + "\"" +
                                "WHERE DiscountID = \""+ updateField.getValue() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(volumeField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapdisc " +
                                "SET volume = \"" + volumeField.getText() + "\"" +
                                "WHERE DiscountID = \""+ updateField.getValue() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(discountRateField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapdisc " +
                                "SET discountRate = \"" + discountRateField.getText() + "\"" +
                                "WHERE DiscountID = \""+ updateField.getValue() +"\"";
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

        Optional<ObservableList<DiscountPlan>> result = dialog.showAndWait();}

    public void deleteDiscount(){ObservableList<DiscountPlan> data = tbData.getItems();
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> customerChoices = new ArrayList<>();

        String customerQuery = "SELECT * FROM bapcust";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(customerQuery);
            while (queryOutput.next()){
                customerChoices.add(queryOutput.getString("customerID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",customerChoices);
        dialog.setTitle("Delete Discount Plan");
        dialog.setHeaderText("Choose which customer's discount plan to delete");
        dialog.setContentText("Customer ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){


            String removeQuery = "DELETE FROM bapdisc WHERE DiscountID = \"" + result.get() + "\"";

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
