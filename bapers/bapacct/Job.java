package bapers.bapacct;

import bapers.I_DiscountChain;
import bapers.bapproc.TasksCollection;

import java.sql.Date;
import javafx.beans.property.SimpleStringProperty;

public class Job {

    private final SimpleStringProperty customerID = new SimpleStringProperty("");
    private final SimpleStringProperty taskID = new SimpleStringProperty("");
    private final SimpleStringProperty userID = new SimpleStringProperty("");
    private final SimpleStringProperty ID = new SimpleStringProperty("");
    private final SimpleStringProperty urgency = new SimpleStringProperty("");
    private final SimpleStringProperty stipDeadline = new SimpleStringProperty("");
    private final SimpleStringProperty price = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty timeStarted = new SimpleStringProperty("");
    private final SimpleStringProperty timeAccepted = new SimpleStringProperty("");
    private final SimpleStringProperty timeTaken = new SimpleStringProperty("");
    private final SimpleStringProperty orderID = new SimpleStringProperty("");

    public Job(){
        this("","","","","","","","","","","","");
    }

    public Job(String customerID, String taskID, String userID, String ID, String orderID, String urgency, String stipDeadline,
               String price, String date, String timeStarted, String timeAccepted, String timeTaken){

        setCustomerID(customerID);
        setTaskID(taskID);
        setUserID(userID);
        setID(ID);
        setOrderID(orderID);
        setUrgency(urgency);
        setStipDeadline(stipDeadline);
        setPrice(price);
        setDate(date);
        setTimeStarted(timeStarted);
        setTimeAccepted(timeAccepted);
        setTimeTaken(timeTaken);
    }

    public String getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(String customerID) {
        this.customerID.set(customerID);
    }

    public String getTaskID() {
        return taskID.get();
    }

    public void setTaskID(String taskID) {
        this.taskID.set(taskID);
    }

    public String getUserID() {
        return userID.get();
    }

    public void setUserID(String userID) {
        this.userID.set(userID);
    }

    public String getID() {
        return ID.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getUrgency() {
        return urgency.get();
    }

    public void setUrgency(String urgency) {
        this.urgency.set(urgency);
    }

    public String getStipDeadline() {
        return stipDeadline.get();
    }

    public void setStipDeadline(String stipDeadline) {
        this.stipDeadline.set(stipDeadline);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTimeStarted() {
        return timeStarted.get();
    }

    public void setTimeStarted(String timeStarted) {
        this.timeStarted.set(timeStarted);
    }

    public String getTimeTaken() {
        return timeTaken.get();
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken.set(timeTaken);
    }

    public String getOrderID() {
        return orderID.get();
    }

    public void setOrderID(String orderID) {
        this.orderID.set(orderID);
    }

    public String getTimeAccepted() {
        return timeAccepted.get();
    }

    public void setTimeAccepted(String timeAccepted) {
        this.timeAccepted.set(timeAccepted);
    }
}