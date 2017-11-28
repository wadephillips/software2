package calendar.controllers;

import calendar.Main;
import calendar.components.CalendarPane;
import calendar.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

/**
 * The controller for the Login Form
 */
public class LoginController extends BaseController {

    /**
     * The header for the Login Form
     */
    @FXML
    private Label loginHeader;

    /**
     * The label for the usernameField
     */
    @FXML
    private Label usernameLabel;

    /**
     * The field for entering the username
     */
    @FXML
    private TextField username;

    /**
     * The label for the passwordField
     */
    @FXML
    private Label passwordLabel;

    /**
     * The field for entering the password
     */
    @FXML
    private PasswordField password;

    /**
     * A label used to display an error message in the event of bad username & password combination.
     */
    @FXML
    private Label messageLabel;

    /**
     * The Constructor
     */
    public LoginController() {
        super();
    }

    /**
     * Applies internationalization features to the Login form and replaces label texts with the appropriate text from the resource bundle
     */
    public void localize() {
        String login = this.resourceBundle.getString("login");
        this.loginHeader.setText(login);
        final String username = this.resourceBundle.getString("usernameLabel");
        this.usernameLabel.setText(username);
        final String password = this.resourceBundle.getString("passwordLabel");
        this.passwordLabel.setText(password);
    }

    /**
     * The onAction handler for the logInButton.
     * @param actionEvent
     * @throws Exception
     */
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
                    //record user login in log
                    try {
                        File log = new File("logs/logins.txt");

                        super.sendToLog("USER_LOGIN", user, log);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // check for upcomming appointments and alert if present
                    this.checkForUpcomingAppointments(user);
                    //change to Calendar scene.
                    Button btn = (Button) actionEvent.getSource();
//                    this.changeScene(btn, "../navigation.fxml");
                    Stage stage;
                    FXMLLoader root;
                    stage = Main.getMainStage();
                    root = new FXMLLoader(getClass().getResource("../navigation.fxml"));

                    Scene scene = new Scene(root.load());
                    MainController controller = root.getController();
//                    controller.loadContent("monthCalendar.fxml");
                    CalendarPane calendarPane = new CalendarPane();
                    calendarPane.showCalendarByMonth();
                    controller.setBodyPaneChild(calendarPane);


                    stage.setScene(scene);
                    stage.show();

                } else {
                    throw new Exception("Username and password combination is incorrect. Please try again.");

                }
            } else {
                throw new Exception("Username and password combination is incorrect. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e){
            this.setErrorMessage(e.getMessage());
            e.printStackTrace();
        }finally{
            password.clear();
        }

    }

    /**
     * Checks the database for upcoming appointments for user.  If the user has appoitments starting within the next
     * 15 minutes a popup is displayed with the start and end times for the appointment.
     * @param user A user object
     */
    private void checkForUpcomingAppointments(User user) {
        String username = user.getUserName();

        DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm:ss");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime soon = now.plusMinutes(15);


        String sql = "SELECT * FROM appointment a " +
                "INNER JOIN customer c " +
                "ON a.customerId = c.customerId " +
                "WHERE a.createdBy = ? " +
                "AND (a.start >= ? AND a.start <= ?);";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.setString(2, f.format(now));
            stmt.setString(3, f.format(soon));
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.first()) {
                int i = 0;
                String body = "";
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    i++;
                    ZonedDateTime apptTime = ZonedDateTime.of(resultSet.getTimestamp("start").toLocalDateTime(), ZoneId.of("UTC")).withZoneSameInstant(Main.getZone());
                    body += "You have an appointment with " + resultSet.getString("contact") + " from " + resultSet.getString("customerName") +
                            " at " + formatter.format(apptTime) + "\n";
                }
                if(i > 0 ){
                    Main.popup.accept("You have upcoming appointments!", body);
                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets and displays the error message for the login form
     *
     * @param message An error message to be displayed to the user
     */
    private void setErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    /**
     * Clears and hides the messageLabel
     * @param mouseEvent
     */
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
