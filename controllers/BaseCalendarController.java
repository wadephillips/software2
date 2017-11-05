package calendar.controllers;

import calendar.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

abstract public class BaseCalendarController extends BaseController {

    protected static LocalDate displayedMonth;
    protected static LocalDate currentDate;
    @FXML
    public AnchorPane calendarPane;
    protected LocalDate firstDayOfDisplayedMonth;

    @FXML
    protected Pane bodyPane;

    public BaseCalendarController() {
        currentDate = LocalDate.now();
        this.firstDayOfDisplayedMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());


    }

    @FXML
    public void showCalendarByMonth(ActionEvent actionEvent) throws IOException {
        Stage mainStage = Main.getMainStage();
        FXMLLoader root = new FXMLLoader(getClass().getResource("../navigation.fxml"));

        Scene scene = new Scene(root.load());

        MainController controller = root.getController();
        controller.loadContent("monthCalendar.fxml");

        mainStage.setScene(scene);
        mainStage.show();
    }

    @FXML
    public void showCalendarByWeek(ActionEvent actionEvent) throws IOException {
//        FXMLLoader weekRoot = new FXMLLoader(getClass().getResource("../calendarWeek.fxml"));
//        System.out.println(weekRoot.getLocation());
//        this.bodyPane.getChildren().setAll(weekRoot);
//        Parent parent = Main.getMainStage().getScene().getRoot();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../calendarWeek.fxml"));
            this.calendarPane.getChildren().clear();
            this.calendarPane.getChildren().add((Node) loader.load());




//        this.bodyPane.getChildren().clear();
//        this.bodyPane.getChildren().add((Node) FXMLLoader.load(getClass().getResource("../calendarWeek.fxml")));
    }
}
