package calendar.controllers;


import calendar.Main;
import calendar.helpers.KeyValuePair;
import calendar.models.Address;
import calendar.models.City;
import calendar.models.Country;
import calendar.models.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerEditFormController extends MainController {


    @FXML
    private Label title;

    @FXML
    private TextField nameField;

    @FXML
    private CheckBox activeCheckBox;

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
    private Button updateButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private StackPane bodyPane;

    private List<KeyValuePair> cities = new ArrayList<>();

    private List<KeyValuePair> countries = new ArrayList<>();

    private long addressId;

    private long customerId;

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    @FXML
    public void createCustomer(ActionEvent actionEvent) {
        System.out.println("saving!");
//        Instant instant = Instant.now();

        Address custAddress = extractAddress();
        try {
            custAddress.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Customer customer = extractCustomer(custAddress);
        try {
            customer.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.returnToCustomersScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateCustomer(ActionEvent actionEvent) {
        System.out.println("saving!");
//        Instant instant = Instant.now();

        Address custAddress = extractAddress();
        custAddress.setAddressId(this.addressId);
        custAddress.setLastUpdateby(Main.getLoggedInUser().getUserName());
        try {
            custAddress.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Customer customer = extractCustomer(custAddress);
        customer.setCustomerId(this.customerId);
        customer.setLastUpdateby(Main.getLoggedInUser().getUserName());

        try {
            customer.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.returnToCustomersScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Customer extractCustomer(Address custAddress) {
        String name = this.nameField.getText();
        int active = 0;
        if (this.activeCheckBox.isSelected()) {
            active = 1;
        }
        return new Customer(name, custAddress.getAddressId(), active);
    }

    Address extractAddress() {
        String phone = this.phoneField.getText();
        String address = this.addressField.getText();
        String address2 = this.address2Field.getText();
        long cityId = this.cityChoiceBox.getValue().getKey();
        String postalCode = this.postalCodeField.getText();
        long countryId  = this.countryChoiceBox.getValue().getKey();

        return new Address(address, address2, cityId, postalCode, phone);
    }

    @FXML
    public void cancelAndReturn(ActionEvent actionEvent) throws IOException {
        Button cancel = (Button) actionEvent.getSource();
//        this.changeScene(cancel,"../na.fxml");
        System.out.println("canceling!");
        this.returnToCustomersScene();
    }

    public void setTitleText(String text) {
        this.title.setText(text);
    }

    public void initAddChoiceBoxes(){
        List<City> cities = City.findAll();
        List<Country> countries = Country.findAll();
        extractKeyValuePairs(cities, countries);
//
//        System.out.println(this.countries);
        setChoiceBoxOptions(this.cities, this.cityChoiceBox);
        setChoiceBoxOptions(this.countries, this.countryChoiceBox);
    }

    public void extractKeyValuePairs(List<City> cities, List<Country> countries) {
        for (City city :
                cities) {
//            System.out.println(city.getCityId());
            this.cities.add(new KeyValuePair(city.getCityId(), city.toString()));
        }
        for (Country country :
                countries) {
            this.countries.add(new KeyValuePair(country.getCountryId(), country.toString()));
        }
    }

    private void setChoiceBoxOptions(List<KeyValuePair> list, ChoiceBox box){
            box.getItems().addAll(list);
    }

    public void setNameField(String name) {
        this.nameField.setText(name);
    }

    public void setPhoneField(String phone) {
        this.phoneField.setText(phone);
    }

    public void setAddressField(String address) {
        this.addressField.setText(address);
    }

    public void setAddress2Field(String address2) {
        this.address2Field.setText(address2);
    }

    public void setCityChoiceBox(ChoiceBox<KeyValuePair> cityChoiceBox) {
        this.cityChoiceBox = cityChoiceBox;
    }

    public void setPostalCodeField(String postalCode) {
        this.postalCodeField.setText(postalCode);
    }

    public void setCountryChoiceBox(ChoiceBox<KeyValuePair> countryChoiceBox) {
        this.countryChoiceBox = countryChoiceBox;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }


    public ChoiceBox<KeyValuePair> getCityChoiceBox() {
        return cityChoiceBox;
    }

    public ChoiceBox<KeyValuePair> getCountryChoiceBox() {
        return countryChoiceBox;
    }

    public List<KeyValuePair> getCities() {
        return cities;
    }

    public List<KeyValuePair> getCountries() {
        return countries;
    }

    public CheckBox getActiveCheckBox() {
        return activeCheckBox;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    /**
     * Loads and displays a scene, without making changes to the underlying controller's properties, after a button click.
     *
     *
     *
     */
    public void returnToCustomersScene() throws IOException {
//        super.changeScene(clicked, fxmlSourceFile);
        Stage stage;
        FXMLLoader root;
        stage = Main.getMainStage();
        root = new FXMLLoader(getClass().getResource("../navigation.fxml"));

        Scene scene = new Scene(root.load());

        MainController controller = root.getController();
        controller.showCustomers();


        stage.setScene(scene);
        stage.show();
    }
}


