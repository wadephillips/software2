package calendar.controllers;


import calendar.Main;
import calendar.helpers.KeyValuePair;
import calendar.models.Address;
import calendar.models.City;
import calendar.models.Country;
import calendar.models.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    @FXML
    private void displayCountry(long cityId) {
        long countryId = City.lookupCountryId(cityId);
        if (countryId > 0) {
            KeyValuePair country = countries.stream().filter(x -> x.getKey() == countryId).findFirst().get();
            this.countryChoiceBox.setValue(country);
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

        this.cityChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            this.displayCountry(cities.get(newValue.intValue()).getKey());
            System.out.println(observable);
//          return cities.get(newValue.intValue());
        });

        this.saveButton.addEventFilter(ActionEvent.ACTION, handler);
        this.updateButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    private EventHandler<ActionEvent> handler = event -> {
        boolean isValidCustomer = false;
        boolean isValidAddress = false;
        try{
            isValidCustomer = this.validateCustomer();
            isValidAddress = this.validateAddress();
        } catch (Exception e) {
            Main.alert.accept("Please correct the following issue(s) with this customer entry before submitting",
                    e.getMessage());
        }
        if (!isValidCustomer || !isValidAddress) {
            event.consume();
        }
    };


    private boolean validateAddress() throws Exception {
        boolean isValid = true;
        StringBuilder body = new StringBuilder();

        if (this.phoneField.getText().equals("")){
            isValid = false;
            body.append("You must enter a phone number.");
            body.append(System.lineSeparator());
        }
        if (this.addressField.getText().equals("")) {
            isValid = false;
            body.append("You must enter an address.");
            body.append(System.lineSeparator());
        }
        if (this.cityChoiceBox.getValue() == null) {
            isValid = false;
            body.append("You must select a city");
            body.append(System.lineSeparator());
        }
        if (this.postalCodeField.getText().equals("")) {
            isValid = false;
            body.append("You must enter a postal code");
            body.append(System.lineSeparator());
        }


        if (!isValid) {
            throw new Exception(body.toString());
        }

        return isValid;
    }

    private boolean validateCustomer() throws Exception {
        boolean isValid = true;
        StringBuilder body = new StringBuilder();

        if (this.nameField.getText().equals("")) {
            isValid = false;
            body.append("You must enter a Customer name");
            body.append(System.lineSeparator());
        }
        if (!isValid) {
            throw new Exception(body.toString());
        }

        return isValid;
    }
}


