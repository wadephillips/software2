package calendar.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarNavigationController extends BaseCalendarController {


    @FXML
    public Button showByWeekButton;

    @FXML
    public Button showByMonthButton;

    @FXML
    public Button previousMonthButton;

    @FXML
    public Button nextMonthButton;

    @FXML
    public Label monthYearLabel;


    public CalendarNavigationController() {
        super();
    }

    @FXML
    public void showCalendarByMonth(ActionEvent actionEvent) {
//        this.bodyPane =
    }

    @FXML
    public void showCalendarByWeek(ActionEvent actionEvent) {

    }

    @FXML
    public void showPreviousMonth(ActionEvent actionEvent) {
        System.out.println("Display the previous month");
    }

    @FXML
    public void showNextMonth(ActionEvent actionEvent) {
        System.out.println("display the next month");
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
        super.initialize(location, resources);
        this.displayMonthAndYear();
    }

    private void displayMonthAndYear() {
        String display = this.firstDayOfCurrentMonth.getMonth() + " " + this.firstDayOfCurrentMonth.getYear();
        display = display.toLowerCase();
        char[] chars = display.toUpperCase().toCharArray();
        char firstChar = chars[0];
        display = firstChar + display.substring(1);
        this.monthYearLabel.setText(display);
//        this.monthYearLabel.setAlignment(Pos.CENTER);
//        this.monthYearLabel.setContentDisplay(ContentDisplay.CENTER);
    }
}