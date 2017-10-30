package calendar.controllers;

import calendar.DBFactory;
import calendar.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

abstract public class BaseController implements Initializable {

    static final DataSource DATASOURCE = DBFactory.get();
    private final Locale locale;
    protected final ResourceBundle resourceBundle;
    private final TimeZone timeZone;


    public BaseController() {

        this.locale = Locale.getDefault();
        this.timeZone = TimeZone.getDefault();
//        System.out.println(this.locale);

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
