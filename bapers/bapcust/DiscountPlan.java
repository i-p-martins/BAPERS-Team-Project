package bapers.bapcust;

import javafx.beans.property.SimpleStringProperty;

public class DiscountPlan {

    private final SimpleStringProperty discountID = new SimpleStringProperty("");
    private final SimpleStringProperty customerID = new SimpleStringProperty("");
    private final SimpleStringProperty fixed = new SimpleStringProperty("");
    private final SimpleStringProperty taskID = new SimpleStringProperty("");
    private final SimpleStringProperty volume = new SimpleStringProperty("");
    private final SimpleStringProperty discountRate = new SimpleStringProperty("");

    public DiscountPlan(){
        this("","","","","","");
    }

    public DiscountPlan(String discountID, String customerID, String fixed, String taskID, String volume,
                        String discountRate) {
        setDiscountID(discountID);
        setCustomerID(customerID);
        setFixed(fixed);
        setTaskID(taskID);
        setVolume(volume);
        setDiscountRate(discountRate);
    }

    public String getDiscountID() {
        return discountID.get();
    }

    public void setDiscountID(String discountID) {
        this.discountID.set(discountID);
    }

    public String getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(String customerID) {
        this.customerID.set(customerID);
    }

    public String getFixed() {
        return fixed.get();
    }

    public void setFixed(String fixed) {
        this.fixed.set(fixed);
    }

    public String getTaskID() {
        return taskID.get();
    }

    public void setTaskID(String taskID) {
        this.taskID.set(taskID);
    }

    public String getVolume() {
        return volume.get();
    }

    public void setVolume(String volume) {
        this.volume.set(volume);
    }

    public String getDiscountRate() {
        return discountRate.get();
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate.set(discountRate);
    }
}