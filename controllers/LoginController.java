package calendar.controllers;

import calendar.Main;
import calendar.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends BaseController {

    @FXML
    private Label loginHeader;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField username;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField password;

    @FXML
    private Label messageLabel;

    public LoginController() {
        super();
    }

    public void localize() {
        String login = this.resourceBundle.getString("login");
//        System.out.println(login);
        this.loginHeader.setText(login);
        this.usernameLabel.setText(this.resourceBundle.getString("usernameLabel"));
        this.passwordLabel.setText(this.resourceBundle.getString("passwordLabel"));
    }


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
//                System.out.println(pass);
//                System.out.println( this.password.getText());
                if (pass.equals(this.password.getText()) ){

                    User user = User.buildUserFromDB(rs);
                    Main.setLoggedInUser(user);
                    //change to Calendar scene.
                    Button btn = (Button) actionEvent.getSource();
                    this.changeScene(btn, "../monthCalendar.fxml");

                } else {
                    setNoMatchErrorMessage();
//                    throw new Exception("");
//                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                    alert.setTitle("hi");
//                    alert.setHeaderText("Thanks for trying to login.");
//                    User userMain = Main.getLoggedInUser();
//                    alert.setContentText("You are atempting to login as " + userMain.getUserName() + " with the password: " + userMain.getPassword());
//
//                    Optional<ButtonType> result = alert.showAndWait();

                }
            } else {
                this.setNoMatchErrorMessage();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            password.clear();
        }
//


    }

    public void setNoMatchErrorMessage() {
        messageLabel.setText("Username and password combination is incorrect. Please try again.");
        messageLabel.setVisible(true);
    }

    @FXML
    public void hideMessage(MouseEvent mouseEvent) {
        if (this.messageLabel.isVisible()) {
            this.messageLabel.setText("");
            this.messageLabel.setVisible(false);
        }
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
        this.localize();
    }
}
