package bapers.bapacct;

import javafx.beans.property.SimpleStringProperty;

public class CustomerAccount {

    private final SimpleStringProperty id = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty contactName = new SimpleStringProperty("");
    private final SimpleStringProperty contactEmail = new SimpleStringProperty("");
    private final SimpleStringProperty address = new SimpleStringProperty("");
    private final SimpleStringProperty phoneNumber = new SimpleStringProperty("");
    private final SimpleStringProperty discountPlan = new SimpleStringProperty("");
    private final SimpleStringProperty valued = new SimpleStringProperty("");

    public CustomerAccount(){
        this("", "", "", "","", "", "", "");
    }

    public CustomerAccount(String id, String name, String contactName, String contactEmail, String address, String phoneNumber,
                           String discountPlan, String valued) {
        setId(id);
        setName(name);
        setContactName(contactName);
        setContactEmail(contactEmail);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        setDiscountPlan(discountPlan);
        setValued(valued);
    }

    public String getId(){
        return id.get();
    }

    public void setId(String i){
        id.set(i);
    }

    public String getName(){
        return name.get();
    }

    public void setName(String n){
        name.set(n);
    }

    public String getContactName(){
        return contactName.get();
    }

    public void setContactName(String cn){
        contactName.set(cn);
    }

    public String getContactEmail() {
        return contactEmail.get();
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail.set(contactEmail);
    }

    public String getAddress(){
        return address.get();
    }

    public void setAddress(String ad){
        address.set(ad);
    }

    public String getPhoneNumber(){
        return phoneNumber.get();
    }

    public void setPhoneNumber(String pn){
        phoneNumber.set(pn);
    }

    public String getDiscountPlan() {
        return discountPlan.get();
    }

    public void setDiscountPlan(String dp) {
       discountPlan.set(dp);
    }

    public String getValued() {
        return valued.get();
    }

    public void setValued(String v) {
        valued.set(v);
    }

}