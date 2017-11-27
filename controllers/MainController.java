package calendar.controllers;

import calendar.Main;
import calendar.components.CalendarPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * The Controller for displaying the main navigational buttons for the application.
 */
public class MainController extends BaseController {

    /**
     * The Calendar Button
     */
    @FXML
    private Button calendarNavButton;

    /**
     * The Customers Button
     */
    @FXML
    private Button customersNavButton;

    /**
     * The Reports Button
     */
    @FXML
    private Button reportsNavButton;

    /**
     * The container for displaying sub views
     */
    @FXML
    protected StackPane bodyPane;


    /**
     * The onAction handler for the calendarNavButton.  Displays the calendar
     * @param actionEvent
     * @throws Exception
     */
    public void showCalendar(ActionEvent actionEvent) throws Exception {
        this.bodyPane.getChildren().clear();
        CalendarPane calendarPane = new CalendarPane();
        calendarPane.showCalendarByMonth();
        this.bodyPane.getChildren().add(calendarPane);

    }


    protected void loadContent(String fxmlSourceFile) throws IOException {
        this.bodyPane.getChildren().clear();
        this.bodyPane.getChildren().addAll((Node) FXMLLoader.load(getClass().getResource("../" + fxmlSourceFile)));
    }

    /**
     * The onAction handler for the customersNavButton.  Displays the main list of Customers.
     * @param actionEvent
     * @throws Exception
     */
    public void showCustomers(ActionEvent actionEvent) throws IOException {
        this.loadContent("customers.fxml");
    }
    /**
     * The onAction handler for the customersNavButton.  Displays the main list of Customers.
     * @throws Exception
     */
    public void showCustomers() throws IOException {
        this.loadContent("customers.fxml");
    }

    /**
     * The onAction handler for the reportsNavButton.  Displays the reports view
     * @param actionEvent
     * @throws Exception
     */
    public void showReports(ActionEvent actionEvent) throws IOException {
        this.loadContent("reports.fxml");
    }

    /**
     * Changes the displayed contents of the body pane
     * @param bodyPaneChild
     */
    void setBodyPaneChild(Pane bodyPaneChild) {
        this.bodyPane.getChildren().clear();
        this.bodyPane.getChildren().add(bodyPaneChild);
    }
}
