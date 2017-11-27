package calendar.controllers;

import calendar.DBFactory;
import calendar.Main;
import calendar.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * An abstract class to extend when creating a controller
 */
abstract public class BaseController implements Initializable {

    /**
     * The connection to the database
     */
    static final DataSource DATASOURCE = DBFactory.get();

    /**
     * The Locale to use with Internationalization
     */
    private final Locale locale;

    /**
     * The Internationalization Resource bundle for the application
     */
    protected final ResourceBundle resourceBundle;

    /**
     * The user's local time zone
     */
    private final TimeZone timeZone;

    /**
     * The constructor
     */
    BaseController() {

        this.locale = Locale.getDefault();
        this.timeZone = TimeZone.getDefault();

        this.resourceBundle = ResourceBundle.getBundle("Calendar", this.locale);
    }

    /**
     * Loads and displays a scene, without making changes to the underlying controller's properties, after a button click.
     * @param clicked The source of the click
     * @param fxmlSourceFile Source of the scene to be loaded.
     * @throws IOException
     */
    @FXML
    public void changeScene(Button clicked, String fxmlSourceFile) throws IOException {
        Stage stage;
        FXMLLoader root;
        stage = Main.getMainStage();
        root = new FXMLLoader(getClass().getResource(fxmlSourceFile));

        Scene scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A helper method to handle output to a log file.  Destination log file must exist before it will function
     * @param action The action that is being logged such as USER_LOGIN
     * @param user The user who is taking action
     * @param destination The log File where this entry should be appended
     * @throws Exception
     */
    void sendToLog(String action, User user, File destination) throws Exception {
        if (destination.exists()){
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(destination, true))){
                String message = action + ": " + user.getUserName() + " @ " + Instant.now().toString();
                writer.write(message);
                writer.newLine();

            }
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

    }
}
