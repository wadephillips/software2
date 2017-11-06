package calendar.controllers;


import calendar.models.City;
import calendar.models.Country;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private ChoiceBox cityChoiceBox = new ChoiceBox();

    @FXML
    private TextField postalCodeField;

    @FXML
    private ChoiceBox countryChoiceBox = new ChoiceBox();

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
    public void cancelAndReturn(ActionEvent actionEvent) throws IOException {

        this.loadContent("customers.fxml");
        System.out.println("canceling!");
    }

    public void setTitleText(String text) {
        this.title.setText(text);
    }

    public void initAddChoiceBoxes(){
        setChoiceBoxOptions(City.findAll(), cityChoiceBox);
        setChoiceBoxOptions(Country.findAll(), countryChoiceBox);
    }
    private void setChoiceBoxOptions(List list, ChoiceBox box){
            box.getItems().addAll(list);
    }



}
