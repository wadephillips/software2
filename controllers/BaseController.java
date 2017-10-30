package calendar.controllers;

import calendar.DBFactory;
import javafx.fxml.Initializable;

import javax.sql.DataSource;
import java.net.URL;
import java.util.ResourceBundle;

abstract public class BaseController implements Initializable {

    static final DataSource DATASOURCE = DBFactory.get();

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
