package bapers.bapproc;

import javafx.beans.property.SimpleStringProperty;

public class Task {
    private final SimpleStringProperty id = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty location = new SimpleStringProperty("");
    private final SimpleStringProperty price = new SimpleStringProperty("");
    private final SimpleStringProperty duration = new SimpleStringProperty("");

    public Task(){this("","","","","");}

    public Task(String id, String description, String location, String price, String duration){
        setId(id);
        setDescription(description);
        setLocation(location);
        setPrice(price);
        setDuration(duration);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDuration() {
        return duration.get();
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }
}