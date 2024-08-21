package bapers.bapgui.view;

import bapers.bapacct.CustomerAccount;
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

public class CustomersViewController implements Initializable {
    public TableView<CustomerAccount> tbData = new TableView<>();
    public TableColumn customerId;
    public TableColumn fullName;
    public TableColumn contactName;
    public TableColumn Address;
    public TableColumn phoneNumber;
    public TableColumn discountPlan;
    public TableColumn Valued;

    public Label userIDLabel;
    public Label userRoleLabel;
    public Label userNameLabel;

    public Button logoutButton;
    public Button goBackButton;
    public Button downgradeCustomerButton;
    public Button upgradeCustomerButton;
    public Button deleteCustomerButton;
    public Button findCustomerButton;
    public Button createCustomerButton;

    public TextField searchBox;

    private ObservableList<CustomerAccount> data = tbData.getItems();

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayDatabase();
        FilteredList<CustomerAccount> filteredData = new FilteredList<>(data, p -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customerAccount -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return customerAccount.getId().toLowerCase().contains(lowerCaseFilter) ||
                        customerAccount.getName().toLowerCase().contains(lowerCaseFilter) ||
                        customerAccount.getContactName().toLowerCase().contains(lowerCaseFilter) ||
                        customerAccount.getContactEmail().toLowerCase().contains(lowerCaseFilter) ||
                        customerAccount.getAddress().toLowerCase().contains(lowerCaseFilter) ||
                        customerAccount.getPhoneNumber().toLowerCase().contains(lowerCaseFilter) ||
                        customerAccount.getDiscountPlan().toLowerCase().contains(lowerCaseFilter) ||
                        customerAccount.getValued().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<CustomerAccount> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbData.comparatorProperty());
        tbData.setItems(sortedData);
    }

    private void displayDatabase(){

        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        String connectQuery = "SELECT * FROM bapcust";

        try{

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            data.clear();
            while (queryOutput.next()){
                data.add(new CustomerAccount(queryOutput.getString("customerID"),
                        queryOutput.getString("fullName"),
                        queryOutput.getString("contactName"),
                        queryOutput.getString("contactEmail"),
                        queryOutput.getString("address"),
                        queryOutput.getString("phoneNumber"),
                        queryOutput.getString("discount"),
                        queryOutput.getString("valued")
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

    //@_programmer: Igor Pereira Martins
    public void downgradeCustomerAccount(ActionEvent actionEvent) {
        ObservableList<CustomerAccount> data = tbData.getItems();
        List<String> choices = new ArrayList<>();
        for (CustomerAccount datum : data) {
            choices.add(datum.getId());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Downgrade Customer");
        dialog.setHeaderText("Choose which customer to downgrade");
        dialog.setContentText("Customer ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String updateQuery = "Update bapcust SET valued = \"N\" WHERE customerID = \"" + result.get()+"\"";

            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(updateQuery);
            }

            catch (Exception e){
                e.printStackTrace();
            }

            displayDatabase();
        }
    }

    //@_programmer: Igor Pereira Martins
    public void createCustomerAccount(ActionEvent actionEvent) {
        Dialog<ObservableList<CustomerAccount>> dialog = new Dialog<>();
        dialog.setTitle("Create New Customer");
        dialog.setHeaderText("Input the customer's information below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField customerIdField = new TextField();
        TextField fullNameField = new TextField();
        TextField contactNameField = new TextField();
        TextField contactEmailField = new TextField();
        TextField addressField = new TextField();
        TextField phoneNumberField = new TextField();
        TextField discountField = new TextField();

        ComboBox valuedField = new ComboBox();

        grid.add(new Label("Customer ID:"),0,0);
        grid.add(customerIdField,1,0);
        grid.add(new Label("Full Name:"),0,1);
        grid.add(fullNameField,1,1);
        grid.add(new Label("Contact Name:"),0,2);
        grid.add(contactNameField,1,2);
        grid.add(new Label("Contact Email:"),0,3);
        grid.add(contactEmailField,1,3);
        grid.add(new Label("Address:"),0,4);
        grid.add(addressField,1,4);
        grid.add(new Label("Phone Number:"),0,5);
        grid.add(phoneNumberField,1,5);
        grid.add(new Label("Discount Plan:"),0,6);
        grid.add(discountField,1,6);

        valuedField.getItems().addAll("Y","N");
        grid.add(new Label("Valued:"),0,7);
        grid.add(valuedField,1,7);

        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(customerIdField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){
                DBConnectivity connectivity = new DBConnectivity();
                Connection connectDB = connectivity.DBConnectivity();

                String insertQuery = "INSERT INTO bapcust VALUES(\""+
                    customerIdField.getText()+"\",\""+
                    fullNameField.getText()+"\",\""+
                    contactNameField.getText()+"\",\""+
                    addressField.getText()+"\",\""+
                    phoneNumberField.getText()+"\",\""+
                    discountField.getText()+"\",\""+
                    valuedField.getValue().toString() +"\",\""+
                    contactEmailField.getText()+"\")";

                try{

                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(insertQuery);
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

        Optional<ObservableList<CustomerAccount>> result = dialog.showAndWait();
    }

    //@_programmer: Igor Pereira Martins
    public void upgradeCustomerAccount(ActionEvent actionEvent) {
        ObservableList<CustomerAccount> data = tbData.getItems();
        List<String> choices = new ArrayList<>();
        for (int i = 0; i<data.size(); i++){
            choices.add(data.get(i).getId());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Upgrade Customer");
        dialog.setHeaderText("Choose which customer to upgrade");
        dialog.setContentText("Customer ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String updateQuery = "Update bapcust SET valued = \"Y\" WHERE customerID = \"" + result.get()+"\"";

            try {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(updateQuery);
            }

            catch (Exception e){
                e.printStackTrace();
            }

            displayDatabase();
        }
    }

    //@_programmer: Igor Pereira Martins
    public void deleteCustomerAccount(ActionEvent actionEvent) {
        ObservableList<CustomerAccount> data = tbData.getItems();
        List<String> choices = new ArrayList<>();
        for (int i = 0; i<data.size(); i++){
            choices.add(data.get(i).getId());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Delete Customer");
        dialog.setHeaderText("Choose which customer to delete");
        dialog.setContentText("Customer ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String removeQuery = "DELETE FROM bapcust WHERE customerID = \"" + result.get() + "\"";

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

    public void editCustomerAccount(ActionEvent actionEvent) {
        ObservableList<CustomerAccount> data = tbData.getItems();

        List<String> choices = new ArrayList<>();
        for (CustomerAccount datum : data) {
            choices.add(datum.getId());
        }

        Dialog<ObservableList<CustomerAccount>> dialog = new Dialog<>();
        dialog.setTitle("Edit Customer");
        dialog.setHeaderText("Input the customer's new information below");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField fullNameField = new TextField();
        TextField contactNameField = new TextField();
        TextField contactEmailField = new TextField();
        TextField addressField = new TextField();
        TextField phoneNumberField = new TextField();
        TextField discountField = new TextField();

        ComboBox valuedField = new ComboBox();
        ComboBox updateField = new ComboBox();

        updateField.getItems().addAll(choices);
        grid.add(new Label("Customer ID:"),0,0);
        grid.add(updateField,1,0);

        grid.add(new Label("Full Name:"),0,1);
        grid.add(fullNameField,1,1);
        grid.add(new Label("Contact Name:"),0,2);
        grid.add(contactNameField,1,2);
        grid.add(new Label("Contact Email:"),0,3);
        grid.add(contactEmailField,1,3);
        grid.add(new Label("Address:"),0,4);
        grid.add(addressField,1,4);
        grid.add(new Label("Phone Number:"),0,5);
        grid.add(phoneNumberField,1,5);
        grid.add(new Label("Discount Plan:"),0,6);
        grid.add(discountField,1,6);

        valuedField.getItems().addAll("Y","N");
        grid.add(new Label("Valued:"),0,7);
        grid.add(valuedField,1,7);

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
                    if (!(fullNameField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapcust " +
                                      "SET fullName = \"" + fullNameField.getText() + "\"" +
                                      "WHERE customerID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(contactNameField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapcust " +
                                "SET contactName= \"" + contactNameField.getText() + "\"" +
                                "WHERE customerID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(addressField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapcust " +
                                "SET address = \"" + addressField.getText() + "\"" +
                                "WHERE customerID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(phoneNumberField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapcust " +
                                "SET phoneNumber = \"" + phoneNumberField.getText() + "\"" +
                                "WHERE customerID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(discountField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapcust " +
                                "SET discount = \"" + discountField.getText() + "\"" +
                                "WHERE customerID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(valuedField.getSelectionModel().isEmpty())) {
                        insertQuery = "UPDATE bapcust " +
                                "SET valued = \"" + valuedField.getValue().toString() + "\"" +
                                "WHERE customerID = \""+ updateField.getValue().toString() +"\"";
                        statement.executeUpdate(insertQuery);
                    }
                    if (!(contactEmailField.getText().trim().isEmpty())) {
                        insertQuery = "UPDATE bapcust " +
                                "SET contactEmail = \"" + contactEmailField.getText() + "\"" +
                                "WHERE customerID = \""+ updateField.getValue().toString() +"\"";
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

        Optional<ObservableList<CustomerAccount>> result = dialog.showAndWait();
    }

}
