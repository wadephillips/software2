package calendar.controllers;

import calendar.Main;
import calendar.components.CalendarPane;
import calendar.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
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
                    //record user login in log
                    try {
                        File log = new File("logs/logins.txt");
                        this.sendToLog(user, log);
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
//                    System.out.println("controller");
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
            this.setNoMatchErrorMessage(e.getMessage());
        }finally{
            password.clear();
        }

    }

    private void checkForUpcomingAppointments(User user) {
        String username = user.getUserName();
//        Instant now = Instant.now();
//        Instant soon = now.plus(15, ChronoUnit.MINUTES);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd kk:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusMinutes(15);
        // FIXME: 11/15/17 this curently doesn't work because the add appt form is saving a localDateTime and now() returns GMT
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
            System.out.println(f.format(now) + " - " + f.format(soon) + resultSet);
            if (resultSet.first()) {
                int i = 0;
                String body = "";
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    System.out.println("hi");
                    i++;
                    System.out.println(resultSet.getString("customerName") +"-" + resultSet.getString("start"));
                    body += "You have an appointment with " + resultSet.getString("customerName") +
                            " at " + formatter.format(resultSet.getTimestamp("start").toLocalDateTime()) + "\n";
                }
                if(i > 0 ){
                    Main.popup.accept("You have upcoming appointments!", body);
                    //todo get rid of this comment if everything is working
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                    alert.setHeaderText("You have upcoming appointments!");
//                    alert.setContentText(body);
//
//
//                    alert.showAndWait();
                }

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendToLog(User user, File destination) throws Exception {
        if (destination.exists()){
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(destination, true))){
                String message = "USER_LOGIN: " + user.getUserName() + " @ " + Instant.now().toString();
                writer.write(message);
                writer.newLine();

            }
        }

    }

    private void setNoMatchErrorMessage(String message) {
        messageLabel.setText(message);
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
