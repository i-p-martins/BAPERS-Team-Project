package bapers.bapadmn;

import bapers.I_Staff;
import javafx.beans.property.SimpleStringProperty;

public class Staff_ImplClass implements I_Staff {

    private final SimpleStringProperty id = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty role = new SimpleStringProperty("");
    private final SimpleStringProperty username = new SimpleStringProperty("");
    private final SimpleStringProperty password = new SimpleStringProperty("");
    private final SimpleStringProperty lastLogin = new SimpleStringProperty("");

    public Staff_ImplClass(){this("","","","","","");}

    public Staff_ImplClass(String id, String name, String role, String username, String password, String lastLogin){
        setId(id);
        setName(name);
        setRole(role);
        setUsername(username);
        setPassword(password);
        setLastLogin(lastLogin);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getLastLogin() {
        return lastLogin.get();
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin.set(lastLogin);
    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }

    @Override
    public void forgotPass() {

    }
}