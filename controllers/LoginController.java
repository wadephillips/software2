package calendar.controllers;

import calendar.Main;
import calendar.models.User;
import calendar.models.UserBuilder;
import com.sun.xml.internal.rngom.parse.host.Base;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController extends BaseController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    public void submitLogin(ActionEvent actionEvent) throws Exception {
        String sql = "SELECT * FROM user WHERE userName = ?;";
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ){
            stmt.setString(1, this.username.getText());
            ResultSet rs = stmt.executeQuery();
            if( rs.next()) {
                String pass = rs.getString("password");
                System.out.println(pass);
                System.out.println( this.password.getText());
                if (pass.equals(this.password.getText()) ){
                    
                    User user = User.buildUserFromDB(rs);
                    Main.setLoggedInUser(user);

                } else {
                    throw new Exception("Username and password combination is incorrect.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("hi");
        alert.setHeaderText("Thanks for trying to login.");
        User userMain = Main.getLoggedInUser();
        alert.setContentText("You are atempting to login as " + userMain.getUserName() + " with the password: " + userMain.getPassword());

        Optional<ButtonType> result = alert.showAndWait();


    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
