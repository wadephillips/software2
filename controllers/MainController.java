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

public class MainController extends BaseController {

    @FXML
    private Button calendarNavButton;

    @FXML
    private Button customersNavButton;

    @FXML
    private Button reportsNavButton;

    @FXML
    protected StackPane bodyPane;


    public void showCalendar(ActionEvent actionEvent) throws IOException {
//        this.loadContent("calendarPane.fxml");
        this.loadContent("monthCalendar.fxml");
    }

    public void loadContent(String fxmlSourceFile) throws IOException {
        this.bodyPane.getChildren().clear();
        this.bodyPane.getChildren().addAll((Node) FXMLLoader.load(getClass().getResource("../" + fxmlSourceFile)));
    }

    public void showCustomers(ActionEvent actionEvent) throws IOException {
        this.loadContent("customers.fxml");
    }
    public void showCustomers() throws IOException {
        this.loadContent("customers.fxml");
    }

    public void showReports(ActionEvent actionEvent) throws IOException {
        this.loadContent("reports.fxml");
    }

    public void setBodyPaneChild(CalendarPane bodyPaneChild) {
        this.bodyPane.getChildren().clear();
        this.bodyPane.getChildren().add(bodyPaneChild);
    }
}
