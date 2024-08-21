package bapers.bappaym;

import javafx.beans.property.SimpleStringProperty;

import java.util.regex.Pattern;

public class Payment {

    private final SimpleStringProperty id = new SimpleStringProperty("");
    private final SimpleStringProperty CustomerID = new SimpleStringProperty("");
    private final SimpleStringProperty OrderID = new SimpleStringProperty("");
    private final SimpleStringProperty Price = new SimpleStringProperty("");
    private final SimpleStringProperty dateDue = new SimpleStringProperty("");
    private final SimpleStringProperty paymentComplete = new SimpleStringProperty("");
    private final SimpleStringProperty dateOfPayment = new SimpleStringProperty("");
    private final SimpleStringProperty cardType = new SimpleStringProperty("");
    private final SimpleStringProperty cardExpiryDate = new SimpleStringProperty("");
    private final SimpleStringProperty cardDigits = new SimpleStringProperty("");

    public Payment(){
        this("","","","","","","","",
                "","");
    }

    public Payment(String id, String CustomerID, String orderID, String Price, String dateDue, String paymentComplete,
                   String dateOfPayment, String cardType, String cardExpiryDate, String cardDigits){
        setId(id);
        setCustomerID(CustomerID);
        setOrderID(orderID);
        setPrice(Price);
        setDateDue(dateDue);
        setPaymentComplete(paymentComplete);
        setDateOfPayment(dateOfPayment);
        setCardType(cardType);
        setCardExpiryDate(cardExpiryDate);
        setCardDigits(cardDigits);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getCustomerID() {
        return CustomerID.get();
    }

    public void setCustomerID(String customerID) {
        this.CustomerID.set(customerID);
    }

    public String getOrderID() {
        return OrderID.get();
    }

    public void setOrderID(String orderID) {
        this.OrderID.set(orderID);
    }

    public String getPrice() {
        return Price.get();
    }

    public void setPrice(String price) {
        this.Price.set(price);
    }

    public String getDateDue() {
        return dateDue.get();
    }

    public void setDateDue(String dateDue) {
        this.dateDue.set(dateDue);
    }

    public String getPaymentComplete() {
        return paymentComplete.get();
    }

    public void setPaymentComplete(String paymentComplete) {
        this.paymentComplete.set(paymentComplete);
    }

    public String getDateOfPayment() {
        return dateOfPayment.get();
    }

    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment.set(dateOfPayment);
    }

    public String getCardType() {
        return cardType.get();
    }

    public void setCardType(String cardType) {
        this.cardType.set(cardType);
    }

    public String getCardExpiryDate() {
        return cardExpiryDate.get();
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate.set(cardExpiryDate);
    }

    public String getCardDigits() {
        return cardDigits.get();
    }

    public void setCardDigits(String cardDigits) {
        this.cardDigits.set(cardDigits);
    }
}