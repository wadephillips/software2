package calendar.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

abstract public class BaseCalendarController extends BaseController {

    protected static LocalDate displayedMonth;
    protected LocalDate firstDayOfCurrentMonth;

    @FXML
    protected Pane bodyPane;

    public BaseCalendarController() {
        this.firstDayOfCurrentMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());

    }
}
