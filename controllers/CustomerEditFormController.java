package calendar.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CustomerEditFormController extends MainController {


    @FXML
    private Label title;

    @FXML
    private TextField NameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField address2Field;

    @FXML
    private ChoiceBox cityChoiceBox;

    @FXML
    private TextField postalCodeField;

    @FXML
    private ChoiceBox countryChoiceBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    @FXML
    public void saveCustomer(ActionEvent actionEvent) {
        System.out.println("saving!");
    }

    @FXML
    public void cancelAndReturn(ActionEvent actionEvent) {
        System.out.println("canceling!");
    }

    public void setTitleText(String text) {
        this.title.setText(text);
    }
}
