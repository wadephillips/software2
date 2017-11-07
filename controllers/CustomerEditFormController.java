package calendar.controllers;


import calendar.models.City;
import calendar.models.Country;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerEditFormController extends MainController {


    @FXML
    private Label title;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField address2Field;

    @FXML
    private ChoiceBox<KeyValuePair> cityChoiceBox = new ChoiceBox<>();

    @FXML
    private TextField postalCodeField;

    @FXML
    private ChoiceBox<KeyValuePair> countryChoiceBox = new ChoiceBox<>();

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private List<KeyValuePair> cities;

    private List<KeyValuePair> countries;



    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    @FXML
    public void createCustomer(ActionEvent actionEvent) {
        System.out.println("saving!");
        Instant instant = Instant.now();
        String name = this.nameField.getText();
        String phone = this.phoneField.getText();
        String address = this.addressField.getText();
        String address2 = this.address2Field.getText();
        System.out.println(this.cityChoiceBox.getValue().toString()+ ", " + this.cityChoiceBox.getValue().getKey());
        String postalCode = this.postalCodeField.getText();
//        long countryId  = this.countryChoiceBox.getValue();
        String sqlCustomer = "INSERT INTO ;";
    }

    @FXML
    public void cancelAndReturn(ActionEvent actionEvent) throws IOException {
        Button cancel = (Button) actionEvent.getSource();
        this.changeScene(cancel,"../navigation.fxml");
        System.out.println("canceling!");
    }

    public void setTitleText(String text) {
        this.title.setText(text);
    }

    public void initAddChoiceBoxes(){
        List<City> cities = City.findAll();
        List<Country> countries = Country.findAll();
//        this.countries = countries.stream().collect(Collectors.toMap(x -> x, x -> x.getCountryId()));
//        this.cities = cities.stream().collect(Collectors.toMap(x -> x, x -> x.getCityId()));
        extractKeyValuePairs(cities, countries);
//
        System.out.println(this.countries);
        setChoiceBoxOptions(this.cities, this.cityChoiceBox);
        setChoiceBoxOptions(this.countries, this.countryChoiceBox);
    }

    public void extractKeyValuePairs(List<City> cities, List<Country> countries) {
        for (City city :
                cities) {
            System.out.println(city.getCityId());
            //todo getting a null poitner execption here farts
            this.cities.add(new KeyValuePair(city.getCityId(), city.getCity()));
        }
        for (Country country :
                countries) {
            this.countries.add(new KeyValuePair(country.getCountryId(), country.getCountry()));
        }
    }

    private void setChoiceBoxOptions(List<KeyValuePair> list, ChoiceBox box){
            box.getItems().addAll(list);
    }



}

class KeyValuePair {
    private final long key;

    private final String value;

    public KeyValuePair(long key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return value;
    }
}
