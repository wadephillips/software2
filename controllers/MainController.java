package calendar.controllers;

import calendar.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController extends BaseController {

    @FXML
    private Button calendarNavButton;

    @FXML
    private Button customersNavButton;

    @FXML
    private Button reportsNavButton;


    public void showCalendar(ActionEvent actionEvent) {
//        this.changeScene();
//        Main.getMainStage();
    }

    public void showCustomers(ActionEvent actionEvent) {
    }

    public void showReports(ActionEvent actionEvent) {
    }
}
