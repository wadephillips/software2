package calendar.controllers;

import calendar.Main;
import calendar.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
                if (pass.equals(this.password.getText()) ){

                    User user = User.buildUserFromDB(rs);
                    Main.setLoggedInUser(user);
                    //change to Calendar scene.
                    Button btn = (Button) actionEvent.getSource();
//                    this.changeScene(btn, "../navigation.fxml");
                    Stage stage;
                    FXMLLoader root;
                    stage = Main.getMainStage();
                    root = new FXMLLoader(getClass().getResource("../navigation.fxml"));

                    Scene scene = new Scene(root.load());
                    System.out.println("controller");
                    MainController controller = root.getController();
                    controller.loadContent("../monthCalendar.fxml");



                    stage.setScene(scene);
                    stage.show();

                } else {
                    setNoMatchErrorMessage();

                }
            } else {
                this.setNoMatchErrorMessage();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            password.clear();
        }

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
