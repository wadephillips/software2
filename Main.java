package calendar;

import calendar.controllers.LoginController;
import calendar.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sql.DataSource;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class Main extends Application {
    

    /**
     * Holds the Locale to be used by the application
     */
    private static Locale locale;

    /**
     * Holds the Time Zone Id to be used by the application
     */
    private static ZoneId zone;

    /**
     * Holds a User object representing the logged in user.
     */
    private static User loggedInUser;

    /**
     * Holds the primary stage
     */
    private static Stage mainStage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.locale = Locale.getDefault();
        this.zone = ZoneId.systemDefault();

        // Uncomment one of the next two lines to test localization
        // Locale locale = new Locale("en", "GB");
        // Locale locale = new Locale("es", "ES");

        // Uncomment the following line to test localization
        // Locale.setDefault(locale);

        Main.mainStage = primaryStage;
        FXMLLoader root = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root.load());

        LoginController controller = root.<LoginController>getController();
        controller.localize();

        primaryStage.setTitle("ACME Calendar");
        primaryStage.setScene(scene);
        primaryStage.show();
        popup.accept("Welcome to Acme Calendar", "You put the A in Acme");
    }

    /**
     * The main method
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Provides standardized popups for the application;
     */
    public static BiConsumer<String, String> popup = (title, body) -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acme Calendar");
        alert.setHeaderText(title);
        alert.setContentText(body);
        alert.showAndWait();
    };

    /**
     * Provides standardized alerts for the application
     */
    public static BiConsumer<String, String> alert = (title, body) -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acme Calendar");
        alert.setHeaderText(title);
        alert.setContentText(body);
        alert.showAndWait();
    };

    /**
     * Getters and Setters
     */

    public static void setLoggedInUser(User loggedInUser) {
        Main.loggedInUser = loggedInUser;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ZoneId getZone() { return zone; }
}
