package bapers.bapgui.view;

import bapers.bapacct.CustomerAccount;
import bapers.bapdb.DBConnectivity;
import bapers.bappaym.Payment;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.util.List;

public class PaymentsViewController implements Initializable{
    public TableView<Payment> tbData = new TableView<>();
    public TableColumn paymentID;
    public TableColumn customerAccountID;
    public TableColumn jobID;
    public TableColumn price;
    public TableColumn dateDue;
    public TableColumn paymentComplete;
    public TableColumn dateOfPayment;
    public TableColumn cardType;
    public TableColumn cardExpiryDate;
    public TableColumn cardDigits;

    public Label userNameLabel;
    public Label userRoleLabel;
    public Label userIDLabel;

    public Button logoutButton;
    public Button goBackButton;
    public Button acceptPaymentButton;
    public Button createDuePaymentButton;
    public Button acceptCardPaymentButton;

    public TextField searchBox;

    private ObservableList<Payment> data = tbData.getItems();

    public static String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public static String currentYear = new SimpleDateFormat("yyyy").format(new Date());

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(LoginViewController.getUserNameLabel());
        userIDLabel.setText(LoginViewController.getUserIDLabel());
        userRoleLabel.setText(LoginViewController.getUserRoleLabel());

        displayDatabase();
        FilteredList<Payment> filteredData = new FilteredList<>(data, p -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(payment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return payment.getId().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getCustomerID().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getOrderID().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getPrice().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getDateDue().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getPaymentComplete().contains(lowerCaseFilter) ||
                        payment.getDateOfPayment().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getCardType().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getCardExpiryDate().toLowerCase().contains(lowerCaseFilter) ||
                        payment.getCardDigits().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Payment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tbData.comparatorProperty());
        tbData.setItems(sortedData);
    }

    private void displayDatabase(){
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        String connectQuery = "SELECT PaymentID,CustomerID,OrderID,(SELECT SUM(price) FROM bappay) AS price ,dateDue,paymentComplete,dateOfPayment,cardType,cardExpiryDate,cardDigits FROM bappay group by OrderID;";

        try{

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            data.clear();
            while (queryOutput.next()){
                data.add(new Payment(queryOutput.getString("PaymentID"),
                        queryOutput.getString("CustomerID"),
                        queryOutput.getString("OrderID"),
                        queryOutput.getString("Price"),
                        queryOutput.getString("dateDue"),
                        queryOutput.getString("paymentComplete"),
                        queryOutput.getString("dateOfPayment"),
                        queryOutput.getString("cardType"),
                        queryOutput.getString("cardExpiryDate"),
                        queryOutput.getString("cardDigits")
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

    public void acceptPayment(ActionEvent actionEvent) {
        ObservableList<Payment> data = tbData.getItems();
        List<String> choices = new ArrayList<>();
        for (int i = 0; i<data.size(); i++){
            choices.add(data.get(i).getOrderID());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Complete Payment");
        dialog.setHeaderText("Choose which payment to complete");
        dialog.setContentText("Order ID");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            DBConnectivity connectivity = new DBConnectivity();
            Connection connectDB = connectivity.DBConnectivity();

            String updateQuery = "UPDATE bappay SET paymentComplete = \"Y\", dateOfPayment = \"" + date + "\" WHERE OrderID = \"" + result.get() +"\"";

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

    public void createDuePayment(ActionEvent actionEvent) {
    }

    public void acceptCardPayment(ActionEvent actionEvent) {
        System.out.println(date);
        ObservableList<Payment> data = tbData.getItems();

        int i = 1;
        List<String> day = new ArrayList<>();
        while (i<=31){
            if (String.valueOf(i).length()<2) {
                day.add("0"+i);
            }
            else{
                day.add(String.valueOf(i));
            }
            i++;
        }

        i = 1;
        List<String> month = new ArrayList<>();
        while (i<=12){
            if (String.valueOf(i).length()<2) {
                month.add("0"+i);
            }
            else{
                month.add(String.valueOf(i));
            }
            i++;
        }

        i = 2021;
        List<String> year = new ArrayList<>();
        while (i<=(Integer.parseInt(currentYear)+3)){
            year.add(String.valueOf(i));
            i++;
        }

        List<String> choices = new ArrayList<>();
        for (Payment datum : data) {
            choices.add(datum.getOrderID());
        }

        Dialog<ObservableList<Payment>> dialog = new Dialog<>();
        dialog.setTitle("Complete Card Payment");
        dialog.setHeaderText("Choose which payment to complete");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox IDField = new ComboBox();

        TextField cardTypeField = new TextField();
        cardTypeField.setMinWidth(25);
        TextField cardDigitsField = new TextField();
        cardDigitsField.setMinWidth(25);

        ComboBox yearField = new ComboBox();
        ComboBox monthField = new ComboBox();

        IDField.getItems().addAll(choices);
        grid.add(new Label("Order ID:"), 0, 0);
        grid.add(IDField,1,0);
        grid.add(new Label("Card Type:"),0,1);
        grid.add(cardTypeField,1,1);
        grid.add(new Label("Card Expiry Date:"),0,2);
        monthField.getItems().addAll(month);
        grid.add(monthField,1,2);
        grid.add(new Label("/"),2,2);
        yearField.getItems().addAll(year);
        grid.add(yearField,3,2);
        grid.add(new Label("Card Digits:"),0,3);
        grid.add(cardDigitsField,1,3);


        //Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        //confirmButton.setDisable(true);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){
                DBConnectivity connectivity = new DBConnectivity();
                Connection connectDB = connectivity.DBConnectivity();

                String insertQuery = "UPDATE bappay SET paymentComplete = \"Y\", dateOfPayment = \"" + date + "\", " +
                        "cardType = \"" + cardTypeField.getText() + "\", cardExpiryDate = \"" +
                        yearField.getValue().toString() + "-" + monthField.getValue().toString() + "-01" +
                        "\", cardDigits = \"" + cardDigitsField.getText() + "\"" +
                        " WHERE OrderID= \"" + IDField.getValue().toString() + "\";";
                try{
                    System.out.println("test");
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
            }
            return null;
        });

        Optional<ObservableList<Payment>> result = dialog.showAndWait();
    }
}
