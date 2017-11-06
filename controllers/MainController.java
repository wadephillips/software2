package calendar.controllers;

import calendar.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController extends BaseController {

    @FXML
    private Button calendarNavButton;

    @FXML
    private Button customersNavButton;

    @FXML
    private Button reportsNavButton;

    @FXML
    private Pane bodyPane;


    public void showCalendar(ActionEvent actionEvent) throws IOException {
        this.loadContent("monthCalendar.fxml");
    }

    public void loadContent(String fxmlSourceFile) throws IOException {
        System.out.println("loading the contents");
        this.bodyPane.getChildren().clear();
        this.bodyPane.getChildren().addAll((Node) FXMLLoader.load(getClass().getResource("../" + fxmlSourceFile)));
    }

    public void showCustomers(ActionEvent actionEvent) throws IOException {
        this.loadContent("customers.fxml");
    }

    public void showReports(ActionEvent actionEvent) throws IOException {
        this.loadContent("reports.fxml");
    }
}