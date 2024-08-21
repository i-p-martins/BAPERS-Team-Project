package bapers.bapgui.view;

import bapers.bapacct.CustomerAccount;
import bapers.bapacct.Job;
import bapers.bapdb.DBConnectivity;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.LabelView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class ReportsViewController implements Initializable {
    public Label userNameLabel;
    public Label userRoleLabel;
    public Label userIDLabel;
    public ScrollPane scroll;
    public Button logoutButton;
    public Button goBackButton;
    public Button produceIndividualReportButton;
    public Button produceSummaryReportButton;
    public Button createInvoiceButton;
    public Button printReportButton;
    private GridPane grid = new GridPane();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userNameLabel.setText(LoginViewController.getUserNameLabel());
        userIDLabel.setText(LoginViewController.getUserIDLabel());
        userRoleLabel.setText(LoginViewController.getUserRoleLabel());
    }
    public static String date = new SimpleDateFormat("dd/mm/yyyy").format(new Date());

    public static String currentYear = new SimpleDateFormat("yyyy").format(new Date());

    private GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int width = gd.getDisplayMode().getWidth();
    private int height = gd.getDisplayMode().getHeight();

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
            //moves to admin home screen
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
            //moves to admin home screen
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

    public void createInvoice(ActionEvent actionEvent) {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

        List<String> orderChoices = new ArrayList<>();
        String invoiceQuery = "SELECT DISTINCT orderID FROM bapjobs";

        try {
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(invoiceQuery);
            while (queryOutput.next()) {
                orderChoices.add(queryOutput.getString("orderID"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("",orderChoices);
        dialog.setTitle("Create Invoice");
        dialog.setHeaderText("Choose which order to create an invoice for");
        dialog.setContentText("Order ID:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String orderQuery = "SELECT customerID FROM bapjobs WHERE orderID = \"" + result.get() +"\"";
            String customerID = "";
            try {
                Statement statement = connectDB.createStatement();

                ResultSet queryOutput = statement.executeQuery(orderQuery);
                queryOutput.next();
                customerID = queryOutput.getString("customerID");
            }
            catch (Exception e){
                e.printStackTrace();
            }

            grid = new GridPane();
            ColumnConstraints column = new ColumnConstraints(600);
            column.setHgrow(Priority.ALWAYS);
            ColumnConstraints column2 = new ColumnConstraints(5);
            grid.getColumnConstraints().addAll(column,column2);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20,150,10,10));

            Label address = new Label("The Lab\n2, Wynyatt Street\nLondon, EC1V 7HU\nPhone: 0207 235 7534");
            grid.add(address,0,1);
            GridPane.setHalignment(address, HPos.RIGHT);

            Label title = new Label("Invoice / " + date);
            grid.add(title,0,3);
            grid.setHalignment(title, HPos.CENTER);

            String customerQuery = "SELECT * FROM bapcust WHERE customerID = \"" + customerID + "\"";
            CustomerAccount account = new CustomerAccount();
            try {
                Statement statement = connectDB.createStatement();

                ResultSet queryOutput = statement.executeQuery(customerQuery);
                while (queryOutput.next()) {
                    account.setId(queryOutput.getString("customerID"));
                    account.setName(queryOutput.getString("fullName"));
                    account.setContactName(queryOutput.getString("contactName"));
                    account.setAddress(queryOutput.getString("address"));
                    account.setPhoneNumber(queryOutput.getString("phoneNumber"));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            grid.add(new Label("Account: "+ account.getId()),0,5);
            grid.add(new Label(
                    "Customer Name:     "+account.getName()+"\n"+
                          "Account No:            "+account.getId()+"\n"+
                          "Contact Name:        "+account.getName()+"\n"+
                          "Address:                  "+account.getAddress()+"\n"+
                          "Phone:                     "+account.getPhoneNumber()),0,8);
            grid.add(new Label("Job No: " + result.get() + "                  Completed: "),0,9);
            grid.add(new Label("Description of work:"),0,11);

            GridPane informationTable = new GridPane();
            ColumnConstraints table = new ColumnConstraints(300);
            informationTable.getColumnConstraints().addAll(table,table);

            Label titleLabel1 = new Label();
            titleLabel1.setStyle("-fx-border-color: black");
            titleLabel1.setMinWidth(300);
            titleLabel1.setText("Task IDs");
            informationTable.add(titleLabel1,0,0);

            Label titleLabel2 = new Label();
            titleLabel2.setStyle("-fx-border-color: black");
            titleLabel2.setMinWidth(300);
            titleLabel2.setText("Price (£)");
            titleLabel2.setTextAlignment(TextAlignment.RIGHT);
            informationTable.add(titleLabel2,1,0);

            String connectQuery = "SELECT * FROM bapjobs WHERE orderID = \"" + result.get() +"\" ORDER BY taskID";
            try{
                Statement statement = connectDB.createStatement();
                ResultSet queryOutput = statement.executeQuery(connectQuery);

                int counter = 1;
                double totalCost = 0;
                double[] totaltaskCost = new double[7];
                String ID = "";
                while (queryOutput.next()){
                    Label label1 = new Label();
                    label1.setStyle("-fx-border-color: black");
                    label1.setMinWidth(300);
                    label1.setText(queryOutput.getString("taskID"));
                    informationTable.add(label1,0,counter);

                    Label label2 = new Label();
                    label2.setStyle("-fx-border-color: black");
                    label2.setMinWidth(300);
                    label2.setText(queryOutput.getString("price"));
                    label2.setTextAlignment(TextAlignment.RIGHT);
                    informationTable.add(label2,1,counter);

                    totalCost += Double.parseDouble(queryOutput.getString("price"));
                    totaltaskCost[Integer.parseInt(queryOutput.getString("taskID"))] += totalCost;
                    ID = queryOutput.getString("customerID");
                    counter+=1;
                }

                Label label3 = new Label();
                label3.setStyle("-fx-border-color: black");
                label3.setMinWidth(300);
                label3.setText("Sub-Total");
                informationTable.add(label3,0,counter);

                Label label4 = new Label();
                label4.setStyle("-fx-border-color: black");
                label4.setMinWidth(300);
                label4.setText(String.format("%.2f", totalCost));
                label4.setTextAlignment(TextAlignment.RIGHT);
                informationTable.add(label4,1,counter);

                String discountQuery = "SELECT * FROM bapdisc WHERE CustomerID = \"" + ID +"\"";
                queryOutput = statement.executeQuery(discountQuery);
                boolean first = true;
                counter+=1;
                while (queryOutput.next()){
                    Label label5 = new Label();
                    label5.setStyle("-fx-border-color: black");
                    label5.setMinWidth(300);
                    if (first) {
                        label5.setText("Discount agreed:");
                    }
                    else{
                        label5.setText("");
                    }
                    informationTable.add(label5,0,counter);

                    try {
                        Label label6 = new Label();
                        label6.setStyle("-fx-border-color: black");
                        label6.setMinWidth(300);
                        if (queryOutput.getString("fixed").equals("Y")) {
                            label6.setText(queryOutput.getString("discountRate"));

                            String temp = queryOutput.getString("discountRate");
                            temp = temp.substring(0, temp.length() - 1);
                            double percent = Double.parseDouble(temp) / 100;
                            totalCost -= (totalCost * percent);
                        } else {
                            try {
                                label6.setText("Task " + queryOutput.getString("taskID") + ": " + queryOutput.getString("discountRate"));
                                Boolean check = true;
                                String temp = queryOutput.getString("discountRate");
                                temp = temp.substring(0, temp.length() - 1);
                                double percent = Double.parseDouble(temp) / 100;
                                double base = totaltaskCost[Integer.parseInt(queryOutput.getString("taskID")) - 1];
                                totaltaskCost[Integer.parseInt(queryOutput.getString("taskID")) - 1] -= (base * percent);
                                if (check) {
                                    totalCost = totaltaskCost[Integer.parseInt(queryOutput.getString("taskID")) - 1];
                                    check = false;
                                } else {
                                    totalCost += totaltaskCost[Integer.parseInt(queryOutput.getString("taskID")) - 1];
                                }
                            } catch (NullPointerException nullpointer) {}
                            try {
                                label6.setText(queryOutput.getString("volume") + queryOutput.getString("discountRate"));
                                double V = totalCost;
                                String test = queryOutput.getString("volume");
                                test.replaceAll("£", "");
                                if (test.charAt(0) == '£') {
                                    test.replace("V", "V||V");
                                }
                                if (Boolean.parseBoolean(test)) {
                                    String temp = queryOutput.getString("discountRate");
                                    temp = temp.substring(0, temp.length() - 1);
                                    double percent = Double.parseDouble(temp) / 100;
                                    totalCost -= (totalCost * percent);
                                }
                            } catch (NullPointerException nullpointer) { }
                        }
                        label6.setTextAlignment(TextAlignment.RIGHT);
                        informationTable.add(label6, 1, counter);
                        counter++;
                    }
                    catch(Exception e){
                        Label label6 = new Label();
                        label6.setStyle("-fx-border-color: black");
                        label6.setMinWidth(300);
                        informationTable.add(label6, 1, counter);
                        break;
                    }
                }

                Label label7 = new Label();
                label7.setStyle("-fx-border-color: black");
                label7.setMinWidth(300);
                label7.setText("");
                informationTable.add(label7,0,counter);

                Label label8 = new Label();
                label8.setStyle("-fx-border-color: black");
                label8.setMinWidth(300);
                label8.setText(String.format("%.2f", totalCost));
                informationTable.add(label8,1,counter);

                totalCost = totalCost + (totalCost*0.2);

                Label label9 = new Label();
                label9.setStyle("-fx-border-color: black");
                label9.setMinWidth(300);
                label9.setText("Total payable (VAT at 20%)");
                informationTable.add(label9,0,counter+1);

                Label label10 = new Label();
                label10.setStyle("-fx-border-color: black");
                label10.setMinWidth(300);
                label10.setText(String.format("%.2f", totalCost));
                informationTable.add(label10,1,counter+1);
            }

            catch (Exception e){
                e.printStackTrace();
            }


            grid.add(informationTable,0,13);
            GridPane.setHalignment(informationTable, HPos.RIGHT);
            grid.add(new Label("Make a payment within 30 days by cash or card."),0,14);

            scroll.setContent(grid);

        }
    }

    public void produceIndividualReport(ActionEvent actionEvent) {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

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

        i = 2020;
        List<String> year = new ArrayList<>();
        while (i<=Integer.parseInt(currentYear)){
            year.add(String.valueOf(i));
            i++;
        }

        Dialog<ObservableList<Job>> dialog = new Dialog<>();
        dialog.setTitle("Generate Individual Performance Report");
        dialog.setHeaderText("Select the time frame for the report");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox startDayField = new ComboBox();
        ComboBox startMonthField = new ComboBox();
        ComboBox startYearField = new ComboBox();

        ComboBox stopDayField = new ComboBox();
        ComboBox stopMonthField = new ComboBox();
        ComboBox stopYearField = new ComboBox();

        grid.add(new Label("Start Date:"),0,0);
        startDayField.getItems().addAll(day);
        grid.add(startDayField,1,0);
        grid.add(new Label("/"),2,0);
        startMonthField.getItems().addAll(month);
        grid.add(startMonthField, 3,0);
        grid.add(new Label("/"),4,0);
        startYearField.getItems().addAll(year);
        grid.add(startYearField, 5,0);

        grid.add(new Label("End Date:"),0,1);
        stopDayField.getItems().addAll(day);
        grid.add(stopDayField,1,1);
        grid.add(new Label("/"),2,1);
        stopMonthField.getItems().addAll(month);
        grid.add(stopMonthField, 3,1);
        grid.add(new Label("/"),4,1);
        stopYearField.getItems().addAll(year);
        grid.add(stopYearField, 5,1);

        dialog.getDialogPane().setContent(grid);

        GridPane reportGrid = new GridPane();
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {

                ColumnConstraints column = new ColumnConstraints(600);
                column.setHgrow(Priority.ALWAYS);
                ColumnConstraints column2 = new ColumnConstraints(5);
                reportGrid.getColumnConstraints().addAll(column, column2);
                reportGrid.setHgap(10);
                reportGrid.setVgap(10);
                reportGrid.setPadding(new Insets(20, 10, 10, 10));

                Label label1 = new Label("Individual Performance Report");
                reportGrid.add(label1, 0, 1);
                reportGrid.setHalignment(label1, HPos.CENTER);

                String startDate = startYearField.getValue().toString() + "-"
                        + startMonthField.getValue().toString() + "-"
                        + startDayField.getValue().toString();

                String stopDate = stopYearField.getValue().toString() + "-"
                        + stopMonthField.getValue().toString() + "-"
                        + stopDayField.getValue().toString();

                Label label2 = new Label("Period: " + startDate + " - " + stopDate);
                reportGrid.add(label2, 0, 2);
                reportGrid.setHalignment(label2, HPos.CENTER);

                GridPane ReportTable = new GridPane();
                ColumnConstraints table = new ColumnConstraints(85);
                ReportTable.getColumnConstraints().addAll(table, table, table, table, table, table, table);

                Label columnTitle1 = new Label();
                columnTitle1.setStyle("-fx-border-color: black");
                columnTitle1.setMinWidth(85);
                columnTitle1.setText("Name");
                ReportTable.add(columnTitle1, 0, 0);

                Label columnTitle2 = new Label();
                columnTitle2.setStyle("-fx-border-color: black");
                columnTitle2.setMinWidth(85);
                columnTitle2.setText("Task IDs");
                ReportTable.add(columnTitle2, 1, 0);

                Label columnTitle3 = new Label();
                columnTitle3.setStyle("-fx-border-color: black");
                columnTitle3.setMinWidth(85);
                columnTitle3.setText("Department");
                ReportTable.add(columnTitle3, 2, 0);

                Label columnTitle4 = new Label();
                columnTitle4.setStyle("-fx-border-color: black");
                columnTitle4.setMinWidth(85);
                columnTitle4.setText("Date");
                ReportTable.add(columnTitle4, 3, 0);

                Label columnTitle5 = new Label();
                columnTitle5.setStyle("-fx-border-color: black");
                columnTitle5.setMinWidth(85);
                columnTitle5.setText("Start time");
                ReportTable.add(columnTitle5, 4, 0);

                Label columnTitle6 = new Label();
                columnTitle6.setStyle("-fx-border-color: black");
                columnTitle6.setMinWidth(85);
                columnTitle6.setText("Time taken");
                ReportTable.add(columnTitle6, 5, 0);

                Label columnTitle7 = new Label();
                columnTitle7.setStyle("-fx-border-color: black");
                columnTitle7.setMinWidth(85);
                columnTitle7.setText("Total");
                ReportTable.add(columnTitle7, 6, 0);

                String query = "SELECT bapuser.name AS 'Name', bapjobs.taskID AS 'task IDs', baptasks.location AS 'Department', bapjobs.date AS 'Date', bapjobs.timeStarted AS 'Start time', bapjobs.timeTaken AS 'Time taken'\n" +
                        "FROM bapjobs\n" +
                        "INNER JOIN bapuser ON bapjobs.userID = bapuser.ID\n" +
                        "INNER JOIN baptasks ON bapjobs.taskID = baptasks.ID\n" +
                        "WHERE (bapjobs.date BETWEEN \"" + startDate + "\" AND \"" + stopDate + "\")\n" +
                        "ORDER BY bapuser.name";

                try {
                    Statement statement = connectDB.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    int rowIndex = 1;
                    ArrayList<Integer> total = new ArrayList<>();
                    ArrayList<Integer> rowSpan = new ArrayList<>();
                    rowSpan.add(1);
                    total.add(0);
                    int rowSpanIndex = -1;
                    String tempName = "";
                    while (resultSet.next()) {
                        Label infoColumn1 = new Label();
                        infoColumn1.setStyle("-fx-border-color: black");
                        infoColumn1.setMinWidth(85);
                        if (!tempName.equals(resultSet.getString("Name"))) {
                            infoColumn1.setText(resultSet.getString("Name"));
                            tempName = resultSet.getString("Name");
                            rowSpan.add(1);
                            total.add(0);
                            rowSpanIndex++;
                        } else {
                            infoColumn1.setText("");
                            rowSpan.set(rowSpanIndex, (rowSpan.get(rowSpanIndex)+1));
                        }
                        ReportTable.add(infoColumn1, 0, rowIndex);

                        Label infoColumn2 = new Label();
                        infoColumn2.setStyle("-fx-border-color: black");
                        infoColumn2.setMinWidth(85);
                        infoColumn2.setText(resultSet.getString("task IDs"));
                        ReportTable.add(infoColumn2, 1, rowIndex);

                        Label infoColumn3 = new Label();
                        infoColumn3.setStyle("-fx-border-color: black");
                        infoColumn3.setMinWidth(85);
                        infoColumn3.setText(resultSet.getString("Department"));
                        ReportTable.add(infoColumn3, 2, rowIndex);

                        Label infoColumn4 = new Label();
                        infoColumn4.setStyle("-fx-border-color: black");
                        infoColumn4.setMinWidth(85);
                        infoColumn4.setText(resultSet.getString("Date"));
                        ReportTable.add(infoColumn4, 3, rowIndex);

                        Label infoColumn5 = new Label();
                        infoColumn5.setStyle("-fx-border-color: black");
                        infoColumn5.setMinWidth(85);
                        infoColumn5.setText(resultSet.getString("Start time"));
                        ReportTable.add(infoColumn5, 4, rowIndex);

                        Label infoColumn6 = new Label();
                        infoColumn6.setStyle("-fx-border-color: black");
                        infoColumn6.setMinWidth(85);
                        try {
                            total.set(rowSpanIndex, (total.get(rowSpanIndex) + Integer.parseInt(resultSet.getString("Time taken"))));
                        }
                        catch (Exception e){}

                        infoColumn6.setText(resultSet.getString("Time taken"));
                        ReportTable.add(infoColumn6, 5, rowIndex);

                        rowIndex++;
                    }

                    int tempTotal = 0;
                    int k = 1;
                    System.out.println(rowSpan.size());
                    try {
                        for (int j = 0; j < rowSpan.size()-1; j++) {
                            System.out.println("test: " + j + " " + k + " " + rowSpan.get(j));
                            Label totalColumn = new Label();
                            totalColumn.setStyle("-fx-border-color: black");
                            totalColumn.setMinWidth(85);
                            totalColumn.setMinHeight(rowSpan.get(j)*19.3);
                            totalColumn.setText(String.valueOf(total.get(j)));
                            ReportTable.add(totalColumn, 6, k, 1, rowSpan.get(j));
                            tempTotal += total.get(j);
                            k += rowSpan.get(j);
                        }
                    }
                    catch (Exception e){e.printStackTrace();}

                    Label totalColumn1 = new Label();
                    totalColumn1.setStyle("-fx-border-color: black");
                    totalColumn1.setMinWidth(510);
                    totalColumn1.setText("Total effort:");
                    ReportTable.add(totalColumn1, 0, rowIndex, 6,1);

                    Label totalColumn2 = new Label();
                    totalColumn2.setStyle("-fx-border-color: black");
                    totalColumn2.setMinWidth(85);
                    totalColumn2.setText(String.valueOf(tempTotal));
                    ReportTable.add(totalColumn2, 6, rowIndex);
                }

                catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("One of your inputs is incorrect");
                    e.printStackTrace();
                    return null;
                }



                reportGrid.add(ReportTable, 0, 5);
                reportGrid.setHalignment(ReportTable, HPos.CENTER);

            }
            return null;
        });
        Optional<ObservableList<Job>> result = dialog.showAndWait();

        scroll.setContent(reportGrid);
    }

    public void produceCustomerReport(ActionEvent actionEvent) {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

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

        i = 2020;
        List<String> year = new ArrayList<>();
        while (i<=Integer.parseInt(currentYear)){
            year.add(String.valueOf(i));
            i++;
        }

        List<String> customerIdChoices = new ArrayList<>();
        String customerQuery = "SELECT customerID FROM bapcust;";

        try{
            Statement statement = connectDB.createStatement();

            ResultSet queryOutput = statement.executeQuery(customerQuery);
            while (queryOutput.next()){
                customerIdChoices.add(queryOutput.getString("customerID"));
            }
        }
        catch (Exception e){
        }


        Dialog<ObservableList<Job>> dialog = new Dialog<>();
        dialog.setTitle("Generate Individual Performance Report");
        dialog.setHeaderText("Select the time frame for the report");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        ComboBox startDayField = new ComboBox();
        ComboBox startMonthField = new ComboBox();
        ComboBox startYearField = new ComboBox();

        ComboBox stopDayField = new ComboBox();
        ComboBox stopMonthField = new ComboBox();
        ComboBox stopYearField = new ComboBox();

        ComboBox customerIDField = new ComboBox();

        grid.add(new Label("Customer ID:"),0,0);
        customerIDField.getItems().addAll(customerIdChoices);
        grid.add(customerIDField,1,0);

        grid.add(new Label("Start Date:"),0,1);
        startDayField.getItems().addAll(day);
        grid.add(startDayField,1,1);
        grid.add(new Label("/"),2,1);
        startMonthField.getItems().addAll(month);
        grid.add(startMonthField, 3,1);
        grid.add(new Label("/"),4,1);
        startYearField.getItems().addAll(year);
        grid.add(startYearField, 5,1);

        grid.add(new Label("End Date:"),0,2);
        stopDayField.getItems().addAll(day);
        grid.add(stopDayField,1,2);
        grid.add(new Label("/"),2,2);
        stopMonthField.getItems().addAll(month);
        grid.add(stopMonthField, 3,2);
        grid.add(new Label("/"),4,2);
        stopYearField.getItems().addAll(year);
        grid.add(stopYearField, 5,2);

        dialog.getDialogPane().setContent(grid);

        GridPane reportGrid = new GridPane();
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {

                ColumnConstraints column = new ColumnConstraints(600);
                column.setHgrow(Priority.ALWAYS);
                ColumnConstraints column2 = new ColumnConstraints(5);
                reportGrid.getColumnConstraints().addAll(column, column2);
                reportGrid.setHgap(10);
                reportGrid.setVgap(10);
                reportGrid.setPadding(new Insets(20, 10, 10, 10));

                Label label1 = new Label("Customer Sales Report");
                reportGrid.add(label1, 0, 1);
                reportGrid.setHalignment(label1, HPos.CENTER);

                String startDate = startYearField.getValue().toString() + "-"
                        + startMonthField.getValue().toString() + "-"
                        + startDayField.getValue().toString();

                String stopDate = stopYearField.getValue().toString() + "-"
                        + stopMonthField.getValue().toString() + "-"
                        + stopDayField.getValue().toString();

                Label label2 = new Label("Period: " + startDate + " - " + stopDate);
                reportGrid.add(label2, 0, 2);
                reportGrid.setHalignment(label2, HPos.CENTER);

                reportGrid.add(new Label("Customer ID: " + customerIDField.getValue().toString()),0,4);

                GridPane ReportTable = new GridPane();
                ColumnConstraints table = new ColumnConstraints(85);
                ReportTable.getColumnConstraints().addAll(table, table, table, table, table, table, table);



                Label columnTitle1 = new Label();
                columnTitle1.setStyle("-fx-border-color: black");
                columnTitle1.setMinWidth(85);
                columnTitle1.setText("Name");
                ReportTable.add(columnTitle1, 0, 0);

                Label columnTitle2 = new Label();
                columnTitle2.setStyle("-fx-border-color: black");
                columnTitle2.setMinWidth(85);
                columnTitle2.setText("Task IDs");
                ReportTable.add(columnTitle2, 1, 0);

                Label columnTitle3 = new Label();
                columnTitle3.setStyle("-fx-border-color: black");
                columnTitle3.setMinWidth(85);
                columnTitle3.setText("Department");
                ReportTable.add(columnTitle3, 2, 0);

                Label columnTitle4 = new Label();
                columnTitle4.setStyle("-fx-border-color: black");
                columnTitle4.setMinWidth(85);
                columnTitle4.setText("Date");
                ReportTable.add(columnTitle4, 3, 0);

                Label columnTitle5 = new Label();
                columnTitle5.setStyle("-fx-border-color: black");
                columnTitle5.setMinWidth(85);
                columnTitle5.setText("Start time");
                ReportTable.add(columnTitle5, 4, 0);

                Label columnTitle6 = new Label();
                columnTitle6.setStyle("-fx-border-color: black");
                columnTitle6.setMinWidth(85);
                columnTitle6.setText("Time taken");
                ReportTable.add(columnTitle6, 5, 0);

                Label columnTitle7 = new Label();
                columnTitle7.setStyle("-fx-border-color: black");
                columnTitle7.setMinWidth(85);
                columnTitle7.setText("Total");
                ReportTable.add(columnTitle7, 6, 0);

                String query = "SELECT bapuser.name AS 'Name', bapjobs.taskID AS 'task IDs', baptasks.location AS 'Department', bapjobs.date AS 'Date', bapjobs.timeStarted AS 'Start time', bapjobs.timeTaken AS 'Time taken'\n" +
                        "FROM bapjobs\n" +
                        "INNER JOIN bapuser ON bapjobs.userID = bapuser.ID\n" +
                        "INNER JOIN baptasks ON bapjobs.taskID = baptasks.ID\n" +
                        "WHERE (bapjobs.CustomerID = \""+customerIDField.getValue().toString()+"\" AND (bapjobs.date BETWEEN \"" + startDate + "\" AND \"" + stopDate + "\"))\n" +
                        "ORDER BY bapuser.name";

                try {
                    Statement statement = connectDB.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    int rowIndex = 1;
                    ArrayList<Integer> total = new ArrayList<>();
                    ArrayList<Integer> rowSpan = new ArrayList<>();
                    rowSpan.add(1);
                    total.add(0);
                    int rowSpanIndex = -1;
                    String tempName = "";
                    while (resultSet.next()) {
                        Label infoColumn1 = new Label();
                        infoColumn1.setStyle("-fx-border-color: black");
                        infoColumn1.setMinWidth(85);
                        if (!tempName.equals(resultSet.getString("Name"))) {
                            infoColumn1.setText(resultSet.getString("Name"));
                            tempName = resultSet.getString("Name");
                            rowSpan.add(1);
                            total.add(0);
                            rowSpanIndex++;
                        } else {
                            infoColumn1.setText("");
                            rowSpan.set(rowSpanIndex, (rowSpan.get(rowSpanIndex)+1));
                        }
                        ReportTable.add(infoColumn1, 0, rowIndex);

                        Label infoColumn2 = new Label();
                        infoColumn2.setStyle("-fx-border-color: black");
                        infoColumn2.setMinWidth(85);
                        infoColumn2.setText(resultSet.getString("task IDs"));
                        ReportTable.add(infoColumn2, 1, rowIndex);

                        Label infoColumn3 = new Label();
                        infoColumn3.setStyle("-fx-border-color: black");
                        infoColumn3.setMinWidth(85);
                        infoColumn3.setText(resultSet.getString("Department"));
                        ReportTable.add(infoColumn3, 2, rowIndex);

                        Label infoColumn4 = new Label();
                        infoColumn4.setStyle("-fx-border-color: black");
                        infoColumn4.setMinWidth(85);
                        infoColumn4.setText(resultSet.getString("Date"));
                        ReportTable.add(infoColumn4, 3, rowIndex);

                        Label infoColumn5 = new Label();
                        infoColumn5.setStyle("-fx-border-color: black");
                        infoColumn5.setMinWidth(85);
                        infoColumn5.setText(resultSet.getString("Start time"));
                        ReportTable.add(infoColumn5, 4, rowIndex);

                        Label infoColumn6 = new Label();
                        infoColumn6.setStyle("-fx-border-color: black");
                        infoColumn6.setMinWidth(85);
                        try {
                            total.set(rowSpanIndex, (total.get(rowSpanIndex) + Integer.parseInt(resultSet.getString("Time taken"))));
                        }
                        catch (Exception e){}

                        infoColumn6.setText(resultSet.getString("Time taken"));
                        ReportTable.add(infoColumn6, 5, rowIndex);

                        rowIndex++;
                    }

                    int tempTotal = 0;
                    int k = 1;
                    System.out.println(rowSpan.size());
                    try {
                        for (int j = 0; j < rowSpan.size()-1; j++) {
                            System.out.println("test: " + j + " " + k + " " + rowSpan.get(j));
                            Label totalColumn = new Label();
                            totalColumn.setStyle("-fx-border-color: black");
                            totalColumn.setMinWidth(85);
                            totalColumn.setMinHeight(rowSpan.get(j)*19.3);
                            totalColumn.setText(String.valueOf(total.get(j)));
                            ReportTable.add(totalColumn, 6, k, 1, rowSpan.get(j));
                            tempTotal += total.get(j);
                            k += rowSpan.get(j);
                        }
                    }
                    catch (Exception e){e.printStackTrace();}

                    Label totalColumn1 = new Label();
                    totalColumn1.setStyle("-fx-border-color: black");
                    totalColumn1.setMinWidth(515);
                    totalColumn1.setText("Total effort:");
                    ReportTable.add(totalColumn1, 0, rowIndex, 6,1);

                    Label totalColumn2 = new Label();
                    totalColumn2.setStyle("-fx-border-color: black");
                    totalColumn2.setMinWidth(85);
                    totalColumn2.setText(String.valueOf(tempTotal));
                    ReportTable.add(totalColumn2, 6, rowIndex);
                }

                catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("One of your inputs is incorrect");
                    e.printStackTrace();
                    return null;
                }



                reportGrid.add(ReportTable, 0, 6);
                reportGrid.setHalignment(ReportTable, HPos.CENTER);

            }
            return null;
        });
        Optional<ObservableList<Job>> result = dialog.showAndWait();

        scroll.setContent(reportGrid);
    }

    public void produceSummaryReport(ActionEvent actionEvent) {
        DBConnectivity connectivity = new DBConnectivity();
        Connection connectDB = connectivity.DBConnectivity();

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

        i = 2020;
        List<String> year = new ArrayList<>();
        while (i<=Integer.parseInt(currentYear)){
            year.add(String.valueOf(i));
            i++;
        }

        Dialog<ObservableList<Job>> dialog = new Dialog<>();
        dialog.setTitle("Generate Summary Performance Report");
        dialog.setHeaderText("Select the time frame for the report");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,10,10,10));

        ComboBox startDayField = new ComboBox();
        ComboBox startMonthField = new ComboBox();
        ComboBox startYearField = new ComboBox();

        ComboBox stopDayField = new ComboBox();
        ComboBox stopMonthField = new ComboBox();
        ComboBox stopYearField = new ComboBox();

        grid.add(new Label("Start Date:"),0,0);
        startDayField.getItems().addAll(day);
        grid.add(startDayField,1,0);
        grid.add(new Label("/"),2,0);
        startMonthField.getItems().addAll(month);
        grid.add(startMonthField, 3,0);
        grid.add(new Label("/"),4,0);
        startYearField.getItems().addAll(year);
        grid.add(startYearField, 5,0);

        grid.add(new Label("End Date:"),0,1);
        stopDayField.getItems().addAll(day);
        grid.add(stopDayField,1,1);
        grid.add(new Label("/"),2,1);
        stopMonthField.getItems().addAll(month);
        grid.add(stopMonthField, 3,1);
        grid.add(new Label("/"),4,1);
        stopYearField.getItems().addAll(year);
        grid.add(stopYearField, 5,1);

        dialog.getDialogPane().setContent(grid);

        GridPane reportGrid = new GridPane();
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType){
                int index = 0;

                ColumnConstraints column = new ColumnConstraints(600);
                column.setHgrow(Priority.ALWAYS);
                ColumnConstraints column2 = new ColumnConstraints(5);
                reportGrid.getColumnConstraints().addAll(column,column2);
                reportGrid.setHgap(10);
                reportGrid.setVgap(10);
                reportGrid.setPadding(new Insets(20,10,10,10));

                Label label1 = new Label("Summary Performance Report");
                reportGrid.add(label1, 0,1);
                reportGrid.setHalignment(label1, HPos.CENTER);

                String startDate =  startYearField.getValue().toString() + "/"
                        + startMonthField.getValue().toString() + "/"
                        + startDayField.getValue().toString();

                String stopDate = stopYearField.getValue().toString() + "/"
                        + stopMonthField.getValue().toString() + "/"
                        + stopDayField.getValue().toString();

                Label label2 = new Label("Period: " + startDate + " - " + stopDate);
                reportGrid.add(label2, 0, 2);
                reportGrid.setHalignment(label2, HPos.CENTER);

                Label label3 = new Label("Day shift 1 (5am - 2:30pm)");
                reportGrid.add(label3, 0, 4);
                reportGrid.setHalignment(label3, HPos.CENTER);

                GridPane DayShift1Table = new GridPane();
                ColumnConstraints table = new ColumnConstraints(120);
                DayShift1Table.getColumnConstraints().addAll(table,table);

                Label columnTitle1 = new Label();
                columnTitle1.setStyle("-fx-border-color: black");
                columnTitle1.setMinWidth(120);
                columnTitle1.setText("Date");
                DayShift1Table.add(columnTitle1,0,0);

                Label columnTitle2 = new Label();
                columnTitle2.setStyle("-fx-border-color: black");
                columnTitle2.setMinWidth(120);
                columnTitle2.setText("Copy Room");
                DayShift1Table.add(columnTitle2,1,0);

                Label columnTitle3 = new Label();
                columnTitle3.setStyle("-fx-border-color: black");
                columnTitle3.setMinWidth(120);
                columnTitle3.setText("Development");
                DayShift1Table.add(columnTitle3,2,0);

                Label columnTitle4 = new Label();
                columnTitle4.setStyle("-fx-border-color: black");
                columnTitle4.setMinWidth(120);
                columnTitle4.setText("Finishing");
                DayShift1Table.add(columnTitle4,3,0);

                Label columnTitle5 = new Label();
                columnTitle5.setStyle("-fx-border-color: black");
                columnTitle5.setMinWidth(120);
                columnTitle5.setText("Packing");
                DayShift1Table.add(columnTitle5,4,0);

                Integer[] total = {0,0,0,0};

                String query = "SELECT bapjobs.date AS 'Date',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Copy Room' THEN bapjobs.timeTaken END) AS 'Copy Room',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Development Area' THEN bapjobs.timeTaken END) AS 'Development',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Finishing Room' THEN bapjobs.timeTaken END) AS 'Finishing',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Packing Departments' THEN bapjobs.timeTaken END) AS 'Packing'\n" +
                        "FROM bapjobs\n" +
                        "INNER JOIN baptasks ON bapjobs.taskID = baptasks.ID\n" +
                        "WHERE (bapjobs.date BETWEEN '" + startDate  + "' AND '" + stopDate + "') AND (bapjobs.timeStarted >= '5:00:00' AND bapjobs.timeStarted < '14:30:00')\n" +
                        "GROUP BY bapjobs.date";

                try{
                    Statement statement = connectDB.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    int rowIndex = 1;
                    while(resultSet.next()){
                        Label infoColumn1 = new Label();
                        infoColumn1.setStyle("-fx-border-color: black");
                        infoColumn1.setMinWidth(120);
                        infoColumn1.setText(resultSet.getString("Date"));
                        DayShift1Table.add(infoColumn1,0,rowIndex);

                        Label infoColumn2 = new Label();
                        infoColumn2.setStyle("-fx-border-color: black");
                        infoColumn2.setMinWidth(120);
                        try{
                            infoColumn2.setText(resultSet.getString("Copy Room").substring(0,resultSet.getString("Copy Room").length()-2) + " min");
                            total[0] += Integer.parseInt(resultSet.getString("Copy Room").substring(0,resultSet.getString("Copy Room").length()-2));
                        }
                            catch(NullPointerException nullPointer){}
                        DayShift1Table.add(infoColumn2,1,rowIndex);


                        Label infoColumn3 = new Label();
                        infoColumn3.setStyle("-fx-border-color: black");
                        infoColumn3.setMinWidth(120);
                        try {
                            infoColumn3.setText(resultSet.getString("Development").substring(0, resultSet.getString("Development").length() - 2) + " min");
                            total[1] += Integer.parseInt(resultSet.getString("Development").substring(0,resultSet.getString("Development").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift1Table.add(infoColumn3,2,rowIndex);

                        Label infoColumn4 = new Label();
                        infoColumn4.setStyle("-fx-border-color: black");
                        infoColumn4.setMinWidth(120);
                        try{
                            infoColumn4.setText(resultSet.getString("Finishing").substring(0,resultSet.getString("Finishing").length()-2) + " min");
                            total[2] += Integer.parseInt(resultSet.getString("Finishing").substring(0,resultSet.getString("Finishing").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift1Table.add(infoColumn4,3,rowIndex);

                        Label infoColumn5 = new Label();
                        infoColumn5.setStyle("-fx-border-color: black");
                        infoColumn5.setMinWidth(120);
                        try{
                            infoColumn5.setText(resultSet.getString("Packing").substring(0,resultSet.getString("Packing").length()-2) + " min");
                            total[3] += Integer.parseInt(resultSet.getString("Packing").substring(0,resultSet.getString("Packing").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift1Table.add(infoColumn5,4,rowIndex);

                        rowIndex++;
                    }

                    Label columnTotal1 = new Label();
                    columnTotal1.setStyle("-fx-border-color: black");
                    columnTotal1.setMinWidth(120);
                    columnTotal1.setText("Total");
                    DayShift1Table.add(columnTotal1,0,rowIndex);

                    Label columnTotal2 = new Label();
                    columnTotal2.setStyle("-fx-border-color: black");
                    columnTotal2.setMinWidth(120);
                    columnTotal2.setText(total[0] + "min");
                    DayShift1Table.add(columnTotal2,1,rowIndex);

                    Label columnTotal3 = new Label();
                    columnTotal3.setStyle("-fx-border-color: black");
                    columnTotal3.setMinWidth(120);
                    columnTotal3.setText(total[1] + "min");
                    DayShift1Table.add(columnTotal3,2,rowIndex);

                    Label columnTotal4 = new Label();
                    columnTotal4.setStyle("-fx-border-color: black");
                    columnTotal4.setMinWidth(120);
                    columnTotal4.setText(total[2] + "min");
                    DayShift1Table.add(columnTotal4,3,rowIndex);

                    Label columnTotal5 = new Label();
                    columnTotal5.setStyle("-fx-border-color: black");
                    columnTotal5.setMinWidth(120);
                    columnTotal5.setText(total[3] + "min");
                    DayShift1Table.add(columnTotal5,4,rowIndex);
                }

                catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("One of your inputs is incorrect");
                    e.printStackTrace();
                    return null;
                }

                reportGrid.add(DayShift1Table, 0,5);
                reportGrid.setHalignment(DayShift1Table, HPos.CENTER);

                Label label4 = new Label("Day shift 2 (2:30pm - 10pm)");
                reportGrid.add(label4, 0, 7);
                reportGrid.setHalignment(label4, HPos.CENTER);

                GridPane DayShift2Table = new GridPane();
                DayShift2Table.getColumnConstraints().addAll(table,table);

                Label column2Title1 = new Label();
                column2Title1.setStyle("-fx-border-color: black");
                column2Title1.setMinWidth(120);
                column2Title1.setText("Date");
                DayShift2Table.add(column2Title1,0,0);

                Label column2Title2 = new Label();
                column2Title2.setStyle("-fx-border-color: black");
                column2Title2.setMinWidth(120);
                column2Title2.setText("Copy Room");
                DayShift2Table.add(column2Title2,1,0);

                Label column2Title3 = new Label();
                column2Title3.setStyle("-fx-border-color: black");
                column2Title3.setMinWidth(120);
                column2Title3.setText("Development");
                DayShift2Table.add(column2Title3,2,0);

                Label column2Title4 = new Label();
                column2Title4.setStyle("-fx-border-color: black");
                column2Title4.setMinWidth(120);
                column2Title4.setText("Finishing");
                DayShift2Table.add(column2Title4,3,0);

                Label column2Title5 = new Label();
                column2Title5.setStyle("-fx-border-color: black");
                column2Title5.setMinWidth(120);
                column2Title5.setText("Packing");
                DayShift2Table.add(column2Title5,4,0);

                Integer[] total2 = {0,0,0,0};

                String query2 = "SELECT bapjobs.date AS 'Date',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Copy Room' THEN bapjobs.timeTaken END) AS 'Copy Room',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Development Area' THEN bapjobs.timeTaken END) AS 'Development',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Finishing Room' THEN bapjobs.timeTaken END) AS 'Finishing',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Packing Departments' THEN bapjobs.timeTaken END) AS 'Packing'\n" +
                        "FROM bapjobs\n" +
                        "INNER JOIN baptasks ON bapjobs.taskID = baptasks.ID\n" +
                        "WHERE (bapjobs.date BETWEEN '" + startDate  + "' AND '" + stopDate + "') AND (bapjobs.timeStarted >= '14:30:00' AND bapjobs.timeStarted < '22:00:00')\n" +
                        "GROUP BY bapjobs.date";

                try{
                    Statement statement = connectDB.createStatement();
                    ResultSet resultSet = statement.executeQuery(query2);

                    int rowIndex = 1;
                    while(resultSet.next()){
                        Label infoColumn1 = new Label();
                        infoColumn1.setStyle("-fx-border-color: black");
                        infoColumn1.setMinWidth(120);
                        infoColumn1.setText(resultSet.getString("Date"));
                        DayShift2Table.add(infoColumn1,0,rowIndex);

                        Label infoColumn2 = new Label();
                        infoColumn2.setStyle("-fx-border-color: black");
                        infoColumn2.setMinWidth(120);
                        try{
                            infoColumn2.setText(resultSet.getString("Copy Room").substring(0,resultSet.getString("Copy Room").length()-2) + " min");
                            total2[0] += Integer.parseInt(resultSet.getString("Copy Room").substring(0,resultSet.getString("Copy Room").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift2Table.add(infoColumn2,1,rowIndex);


                        Label infoColumn3 = new Label();
                        infoColumn3.setStyle("-fx-border-color: black");
                        infoColumn3.setMinWidth(120);
                        try {
                            infoColumn3.setText(resultSet.getString("Development").substring(0, resultSet.getString("Development").length() - 2) + " min");
                            total2[1] += Integer.parseInt(resultSet.getString("Development").substring(0,resultSet.getString("Development").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift2Table.add(infoColumn3,2,rowIndex);

                        Label infoColumn4 = new Label();
                        infoColumn4.setStyle("-fx-border-color: black");
                        infoColumn4.setMinWidth(120);
                        try{
                            infoColumn4.setText(resultSet.getString("Finishing").substring(0,resultSet.getString("Finishing").length()-2) + " min");
                            total2[2] += Integer.parseInt(resultSet.getString("Finishing").substring(0,resultSet.getString("Finishing").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift2Table.add(infoColumn4,3,rowIndex);

                        Label infoColumn5 = new Label();
                        infoColumn5.setStyle("-fx-border-color: black");
                        infoColumn5.setMinWidth(120);
                        try{
                            infoColumn5.setText(resultSet.getString("Packing").substring(0,resultSet.getString("Packing").length()-2) + " min");
                            total2[3] += Integer.parseInt(resultSet.getString("Packing").substring(0,resultSet.getString("Packing").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift2Table.add(infoColumn5,4,rowIndex);

                        rowIndex++;
                    }

                    Label columnTotal1 = new Label();
                    columnTotal1.setStyle("-fx-border-color: black");
                    columnTotal1.setMinWidth(120);
                    columnTotal1.setText("Total");
                    DayShift2Table.add(columnTotal1,0,rowIndex);

                    Label columnTotal2 = new Label();
                    columnTotal2.setStyle("-fx-border-color: black");
                    columnTotal2.setMinWidth(120);
                    columnTotal2.setText(String.valueOf(total2[0]) + "min");
                    DayShift2Table.add(columnTotal2,1,rowIndex);

                    Label columnTotal3 = new Label();
                    columnTotal3.setStyle("-fx-border-color: black");
                    columnTotal3.setMinWidth(120);
                    columnTotal3.setText(String.valueOf(total2[1]) + "min");
                    DayShift2Table.add(columnTotal3,2,rowIndex);

                    Label columnTotal4 = new Label();
                    columnTotal4.setStyle("-fx-border-color: black");
                    columnTotal4.setMinWidth(120);
                    columnTotal4.setText(String.valueOf(total2[2]) + "min");
                    DayShift2Table.add(columnTotal4,3,rowIndex);

                    Label columnTotal5 = new Label();
                    columnTotal5.setStyle("-fx-border-color: black");
                    columnTotal5.setMinWidth(120);
                    columnTotal5.setText(String.valueOf(total2[3]) + "min");
                    DayShift2Table.add(columnTotal5,4,rowIndex);
                }

                catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("One of your inputs is incorrect");
                    e.printStackTrace();
                    return null;
                }

                reportGrid.add(DayShift2Table, 0,8);
                reportGrid.setHalignment(DayShift1Table, HPos.CENTER);

                Label label5 = new Label("Night shift (10pm - 5am)");
                reportGrid.add(label5, 0, 10);
                reportGrid.setHalignment(label5, HPos.CENTER);

                GridPane DayShift3Table = new GridPane();
                DayShift3Table.getColumnConstraints().addAll(table,table);

                Label column3Title1 = new Label();
                column3Title1.setStyle("-fx-border-color: black");
                column3Title1.setMinWidth(120);
                column3Title1.setText("Date");
                DayShift3Table.add(column3Title1,0,0);

                Label column3Title2 = new Label();
                column3Title2.setStyle("-fx-border-color: black");
                column3Title2.setMinWidth(120);
                column3Title2.setText("Copy Room");
                DayShift3Table.add(column3Title2,1,0);

                Label column3Title3 = new Label();
                column3Title3.setStyle("-fx-border-color: black");
                column3Title3.setMinWidth(120);
                column3Title3.setText("Development");
                DayShift3Table.add(column3Title3,2,0);

                Label column3Title4 = new Label();
                column3Title4.setStyle("-fx-border-color: black");
                column3Title4.setMinWidth(120);
                column3Title4.setText("Finishing");
                DayShift3Table.add(column3Title4,3,0);

                Label column3Title5 = new Label();
                column3Title5.setStyle("-fx-border-color: black");
                column3Title5.setMinWidth(120);
                column3Title5.setText("Packing");
                DayShift3Table.add(column3Title5,4,0);

                Integer[] total3 = {0,0,0,0};

                String query3 = "SELECT bapjobs.date AS 'Date',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Copy Room' THEN bapjobs.timeTaken END) AS 'Copy Room',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Development Area' THEN bapjobs.timeTaken END) AS 'Development',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Finishing Room' THEN bapjobs.timeTaken END) AS 'Finishing',\n" +
                        "SUM(CASE WHEN baptasks.location = 'Packing Departments' THEN bapjobs.timeTaken END) AS 'Packing'\n" +
                        "FROM bapjobs\n" +
                        "INNER JOIN baptasks ON bapjobs.taskID = baptasks.ID\n" +
                        "WHERE (bapjobs.date BETWEEN '" + startDate  + "' AND '" + stopDate + "') AND (bapjobs.timeStarted >= '22:00:00' AND bapjobs.timeStarted < '5:00:00')\n" +
                        "GROUP BY bapjobs.date";

                try{
                    Statement statement = connectDB.createStatement();
                    ResultSet resultSet = statement.executeQuery(query3);

                    int rowIndex = 1;
                    while(resultSet.next()){
                        Label infoColumn1 = new Label();
                        infoColumn1.setStyle("-fx-border-color: black");
                        infoColumn1.setMinWidth(120);
                        infoColumn1.setText(resultSet.getString("Date"));
                        DayShift3Table.add(infoColumn1,0,rowIndex);

                        Label infoColumn2 = new Label();
                        infoColumn2.setStyle("-fx-border-color: black");
                        infoColumn2.setMinWidth(120);
                        try{
                            infoColumn2.setText(resultSet.getString("Copy Room").substring(0,resultSet.getString("Copy Room").length()-2) + " min");
                            total3[0] += Integer.parseInt(resultSet.getString("Copy Room").substring(0,resultSet.getString("Copy Room").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift3Table.add(infoColumn2,1,rowIndex);


                        Label infoColumn3 = new Label();
                        infoColumn3.setStyle("-fx-border-color: black");
                        infoColumn3.setMinWidth(120);
                        try {
                            infoColumn3.setText(resultSet.getString("Development").substring(0, resultSet.getString("Development").length() - 2) + " min");
                            total3[1] += Integer.parseInt(resultSet.getString("Development").substring(0,resultSet.getString("Development").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift3Table.add(infoColumn3,2,rowIndex);

                        Label infoColumn4 = new Label();
                        infoColumn4.setStyle("-fx-border-color: black");
                        infoColumn4.setMinWidth(120);
                        try{
                            infoColumn4.setText(resultSet.getString("Finishing").substring(0,resultSet.getString("Finishing").length()-2) + " min");
                            total3[2] += Integer.parseInt(resultSet.getString("Finishing").substring(0,resultSet.getString("Finishing").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift3Table.add(infoColumn4,3,rowIndex);

                        Label infoColumn5 = new Label();
                        infoColumn5.setStyle("-fx-border-color: black");
                        infoColumn5.setMinWidth(120);
                        try{
                            infoColumn5.setText(resultSet.getString("Packing").substring(0,resultSet.getString("Packing").length()-2) + " min");
                            total3[3] += Integer.parseInt(resultSet.getString("Packing").substring(0,resultSet.getString("Packing").length()-2));
                        }
                        catch(NullPointerException nullPointer){}
                        DayShift3Table.add(infoColumn5,4,rowIndex);

                        rowIndex++;
                    }

                    Label columnTotal1 = new Label();
                    columnTotal1.setStyle("-fx-border-color: black");
                    columnTotal1.setMinWidth(120);
                    columnTotal1.setText("Total");
                    DayShift3Table.add(columnTotal1,0,rowIndex);

                    Label columnTotal2 = new Label();
                    columnTotal2.setStyle("-fx-border-color: black");
                    columnTotal2.setMinWidth(120);
                    columnTotal2.setText(String.valueOf(total3[0]) + "min");
                    DayShift3Table.add(columnTotal2,1,rowIndex);

                    Label columnTotal3 = new Label();
                    columnTotal3.setStyle("-fx-border-color: black");
                    columnTotal3.setMinWidth(120);
                    columnTotal3.setText(String.valueOf(total3[1]) + "min");
                    DayShift3Table.add(columnTotal3,2,rowIndex);

                    Label columnTotal4 = new Label();
                    columnTotal4.setStyle("-fx-border-color: black");
                    columnTotal4.setMinWidth(120);
                    columnTotal4.setText(String.valueOf(total3[2]) + "min");
                    DayShift3Table.add(columnTotal4,3,rowIndex);

                    Label columnTotal5 = new Label();
                    columnTotal5.setStyle("-fx-border-color: black");
                    columnTotal5.setMinWidth(120);
                    columnTotal5.setText(String.valueOf(total3[3]) + "min");
                    DayShift3Table.add(columnTotal5,4,rowIndex);
                }

                catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Input Error");
                    alert.setContentText("One of your inputs is incorrect");
                    e.printStackTrace();
                    return null;
                }

                reportGrid.add(DayShift3Table, 0,11);
                reportGrid.setHalignment(DayShift3Table, HPos.CENTER);

                Label label6 = new Label("For period (" + startDate + " - " + stopDate + ")");
                reportGrid.add(label6, 0, 13);
                reportGrid.setHalignment(label6, HPos.CENTER);

                GridPane periodTable = new GridPane();
                periodTable.getColumnConstraints().addAll(table,table);

                Label column4Title1 = new Label();
                column4Title1.setStyle("-fx-border-color: black");
                column4Title1.setMinWidth(120);
                column4Title1.setText("Date");
                periodTable.add(column4Title1,0,0);

                Label column4Title2 = new Label();
                column4Title2.setStyle("-fx-border-color: black");
                column4Title2.setMinWidth(120);
                column4Title2.setText("Copy Room");
                periodTable.add(column4Title2,1,0);

                Label column4Title3 = new Label();
                column4Title3.setStyle("-fx-border-color: black");
                column4Title3.setMinWidth(120);
                column4Title3.setText("Development");
                periodTable.add(column4Title3,2,0);

                Label column4Title4 = new Label();
                column4Title4.setStyle("-fx-border-color: black");
                column4Title4.setMinWidth(120);
                column4Title4.setText("Finishing");
                periodTable.add(column4Title4,3,0);

                Label column4Title5 = new Label();
                column4Title5.setStyle("-fx-border-color: black");
                column4Title5.setMinWidth(120);
                column4Title5.setText("Packing");
                periodTable.add(column4Title5,4,0);

                Label dayShift1Total = new Label();
                dayShift1Total.setStyle("-fx-border-color: black");
                dayShift1Total.setMinWidth(120);
                dayShift1Total.setText("Day Shift 1");
                periodTable.add(dayShift1Total,0,1);

                Label dayShift1CopyTotal = new Label();
                dayShift1CopyTotal.setStyle("-fx-border-color: black");
                dayShift1CopyTotal.setMinWidth(120);
                dayShift1CopyTotal.setText(String.valueOf(total[0])+ "min");
                periodTable.add(dayShift1CopyTotal,1,1);

                Label dayShift1DevTotal = new Label();
                dayShift1DevTotal.setStyle("-fx-border-color: black");
                dayShift1DevTotal.setMinWidth(120);
                dayShift1DevTotal.setText(String.valueOf(total[1])+ "min");
                periodTable.add(dayShift1DevTotal,2,1);

                Label dayShift1FinishTotal = new Label();
                dayShift1FinishTotal.setStyle("-fx-border-color: black");
                dayShift1FinishTotal.setMinWidth(120);
                dayShift1FinishTotal.setText(String.valueOf(total[2])+ "min");
                periodTable.add(dayShift1FinishTotal,3,1);

                Label dayShift1PackTotal = new Label();
                dayShift1PackTotal.setStyle("-fx-border-color: black");
                dayShift1PackTotal.setMinWidth(120);
                dayShift1PackTotal.setText(String.valueOf(total[3])+ "min");
                periodTable.add(dayShift1PackTotal,4,1);

                Label dayShift2Total = new Label();
                dayShift2Total.setStyle("-fx-border-color: black");
                dayShift2Total.setMinWidth(120);
                dayShift2Total.setText("Day Shift 2");
                periodTable.add(dayShift2Total,0,2);

                Label dayShift2CopyTotal = new Label();
                dayShift2CopyTotal.setStyle("-fx-border-color: black");
                dayShift2CopyTotal.setMinWidth(120);
                dayShift2CopyTotal.setText(String.valueOf(total2[0])+ "min");
                periodTable.add(dayShift2CopyTotal,1,2);

                Label dayShift2DevTotal = new Label();
                dayShift2DevTotal.setStyle("-fx-border-color: black");
                dayShift2DevTotal.setMinWidth(120);
                dayShift2DevTotal.setText(String.valueOf(total2[1])+ "min");
                periodTable.add(dayShift2DevTotal,2,2);

                Label dayShift2FinishTotal = new Label();
                dayShift2FinishTotal.setStyle("-fx-border-color: black");
                dayShift2FinishTotal.setMinWidth(120);
                dayShift2FinishTotal.setText(String.valueOf(total2[2])+ "min");
                periodTable.add(dayShift2FinishTotal,3,2);

                Label dayShift2PackTotal = new Label();
                dayShift2PackTotal.setStyle("-fx-border-color: black");
                dayShift2PackTotal.setMinWidth(120);
                dayShift2PackTotal.setText(String.valueOf(total2[3])+ "min");
                periodTable.add(dayShift2PackTotal,4,2);

                Label nightShiftTotal = new Label();
                nightShiftTotal.setStyle("-fx-border-color: black");
                nightShiftTotal.setMinWidth(120);
                nightShiftTotal.setText("Night Shift");
                periodTable.add(nightShiftTotal,0,3);

                Label nightShiftCopyTotal = new Label();
                nightShiftCopyTotal.setStyle("-fx-border-color: black");
                nightShiftCopyTotal.setMinWidth(120);
                nightShiftCopyTotal.setText(String.valueOf(total3[0])+ "min");
                periodTable.add(nightShiftCopyTotal,1,3);

                Label nightShiftDevTotal = new Label();
                nightShiftDevTotal.setStyle("-fx-border-color: black");
                nightShiftDevTotal.setMinWidth(120);
                nightShiftDevTotal.setText(String.valueOf(total3[1])+ "min");
                periodTable.add(nightShiftDevTotal,2,3);

                Label nightShiftFinishTotal = new Label();
                nightShiftFinishTotal.setStyle("-fx-border-color: black");
                nightShiftFinishTotal.setMinWidth(120);
                nightShiftFinishTotal.setText(String.valueOf(total3[2])+ "min");
                periodTable.add(nightShiftFinishTotal,3,3);

                Label nightShiftPackTotal = new Label();
                nightShiftPackTotal.setStyle("-fx-border-color: black");
                nightShiftPackTotal.setMinWidth(120);
                nightShiftPackTotal.setText(String.valueOf(total3[3])+ "min");
                periodTable.add(nightShiftPackTotal,4,3);

                Label finalTotal = new Label();
                finalTotal.setStyle("-fx-border-color: black");
                finalTotal.setMinWidth(120);
                finalTotal.setText("Total");
                periodTable.add(finalTotal,0,4);

                Label finalCopyTotal = new Label();
                finalCopyTotal.setStyle("-fx-border-color: black");
                finalCopyTotal.setMinWidth(120);
                finalCopyTotal.setText(String.valueOf(total[0]+total2[0]+total3[0])+ "min");
                periodTable.add(finalCopyTotal,1,4);

                Label finalDevTotal = new Label();
                finalDevTotal.setStyle("-fx-border-color: black");
                finalDevTotal.setMinWidth(120);
                finalDevTotal.setText(String.valueOf(total[1]+total2[1]+total3[1])+ "min");
                periodTable.add(finalDevTotal ,2,4);

                Label finalFinishTotal = new Label();
                finalFinishTotal.setStyle("-fx-border-color: black");
                finalFinishTotal.setMinWidth(120);
                finalFinishTotal.setText(String.valueOf(total[2]+total2[2]+total3[2])+ "min");
                periodTable.add(finalFinishTotal,3,4);

                Label finalPackTotal = new Label();
                finalPackTotal.setStyle("-fx-border-color: black");
                finalPackTotal.setMinWidth(120);
                finalPackTotal.setText(String.valueOf(total[3]+total2[3]+total3[3]) + "min");
                periodTable.add(finalPackTotal,4,4);

                reportGrid.add(periodTable, 0,14);
                reportGrid.setHalignment(periodTable, HPos.CENTER);

            }
            return null;
        });

        Optional<ObservableList<Job>> result = dialog.showAndWait();

        scroll.setContent(reportGrid);
    }

    public void printReport(ActionEvent actionEvent) {
        PrinterJob job = PrinterJob.createPrinterJob();
        PageLayout pageLayout = job.getJobSettings().getPageLayout();
        job.getJobSettings().setPageLayout(pageLayout);
        if (job!=null){
            Scale scale = new Scale(0.75,0.75);
            grid.getTransforms().add(scale);
            job.showPrintDialog(grid.getScene().getWindow());
            job.printPage(grid);
            job.endJob();
        }
        else{
            System.out.println("Failed to print");
        }
    }
}
