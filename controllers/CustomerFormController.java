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

/**
 * Controller to handle the logic for the Add Customer and Edit Customer forms.
 */
public class CustomerFormController extends MainController {

    /**
     * The title of the form
     */
    @FXML
    private Label title;

    /**
     * The name of the customer
     */
    @FXML
    private TextField nameField;

    /**
     * A checkbox to indicate whether or not the customer is active or not
     */
    @FXML
    private CheckBox activeCheckBox;

    /**
     * A phone number of the customer
     */
    @FXML
    private TextField phoneField;

    /**
     * The street address of the customer
     */
    @FXML
    private TextField addressField;

    /**
     * An additional field for address information
     */
    @FXML
    private TextField address2Field;

    /**
     * The city where the customer is located
     */
    @FXML
    private ChoiceBox<KeyValuePair> cityChoiceBox = new ChoiceBox<>();

    /**
     * The customer's postal code
     */
    @FXML
    private TextField postalCodeField;

    /**
     * The customer's country
     */
    @FXML
    private ChoiceBox<KeyValuePair> countryChoiceBox = new ChoiceBox<>();

    /**
     * The button displayed to save changes made to an existing customer
     */
    @FXML
    private Button updateButton;

    /**
     * The button displayed to save a new customer
     */
    @FXML
    private Button saveButton;

    /**
     * The button for canceling out of the add or edit customer forms
     */
    @FXML
    private Button cancelButton;

    /**
     * A container Pane to hold the add or edit customer form
     */
    @FXML
    private StackPane bodyPane;

    /**
     * The list of cities that are displayed to the user when creating or editing a customer
     */
    private List<KeyValuePair> cities = new ArrayList<>();

    /**
     * The list of countries that are displayed to the user when creating or editing a customer
     */
    private List<KeyValuePair> countries = new ArrayList<>();

    /**
     * The addresses database id
     */
    private long addressId;

    /**
     * The customer's id in the customer database table
     */
    private long customerId;


    /**
     * The onAction handler for the saveButton.
     * Takes the information from the form and sends it to the Address and Customer Models for saving.
     * @param actionEvent
     */
    @FXML
    public void createCustomer(ActionEvent actionEvent) {

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

    /**
     * The onAction handler for the updateButton.
     * Takes the information from the form and sends it to the Address and Customer Models for updating.
     * @param actionEvent
     */
    @FXML
    public void updateCustomer(ActionEvent actionEvent) {

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

    /**
     * Gets Customer information from the add or edit form and returns a Customer object
     * @param custAddress the address of the customer from the form.
     * @return a Customer object
     */
    Customer extractCustomer(Address custAddress) {
        String name = this.nameField.getText();
        int active = 0;
        if (this.activeCheckBox.isSelected()) {
            active = 1;
        }
        return new Customer(name, custAddress.getAddressId(), active);
    }

    /**
     * Gets Address information from the add or edit form and returns an Address object
     * @return a Address object
     */
    Address extractAddress() {
        String phone = this.phoneField.getText();
        String address = this.addressField.getText();
        String address2 = this.address2Field.getText();
        long cityId = this.cityChoiceBox.getValue().getKey();
        String postalCode = this.postalCodeField.getText();
        long countryId  = this.countryChoiceBox.getValue().getKey();

        return new Address(address, address2, cityId, postalCode, phone);
    }

    /**
     * The onAction handler for the cancelButton on the add and edit forms
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void cancelAndReturn(ActionEvent actionEvent) throws IOException {
        Button cancel = (Button) actionEvent.getSource();
        System.out.println("canceling!");
        this.returnToCustomersScene();
    }

    /**
     * Sets the title to be displayed at the top of the add or edit customer form
     * @param text
     */
    public void setTitleText(String text) {
        this.title.setText(text);
    }

    /**
     * Gets a list of cities and countries to be added to the appropriate ChoiceBoxes on the form
     */
    public void initAddChoiceBoxes(){
        List<City> cities = City.findAll();
        List<Country> countries = Country.findAll();
        extractKeyValuePairs(cities, countries);

        setChoiceBoxOptions(this.cities, this.cityChoiceBox);
        setChoiceBoxOptions(this.countries, this.countryChoiceBox);
    }

    /**
     * Helper method that transforms the lists of Cities and Countries into KeyValuePairs
     * @param cities
     * @param countries
     */
    private void extractKeyValuePairs(List<City> cities, List<Country> countries) {
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

    /**
     * Displays the appropriate Country for the address on the add/edit form based on the selected City
     * @param cityId
     */
    @FXML
    private void displayCountry(long cityId) {
        long countryId = City.lookupCountryId(cityId);
        if (countryId > 0) {
            KeyValuePair country = countries.stream().filter(x -> x.getKey() == countryId).findFirst().get();
            this.countryChoiceBox.setValue(country);
        }
    }

    /**
     * Loads and displays the main customer scene, without making changes to the underlying controller's properties, after a button click.
     */
    public void returnToCustomersScene() throws IOException {
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
     * Validates the fields on the add/edit form that relate to the Customer's Address.
     * @return True if the address information is valid and complete, false if any required information is missing.
     * @throws Exception
     */
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

    /**
     * Validates the fields on the add/edit form that relate to the Customer.
     * @return True if the Customer information is valid and complete, false if any required information is missing.
     * @throws Exception
     */
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
        });

        this.saveButton.addEventFilter(ActionEvent.ACTION, handler);
        this.updateButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * An event handler to validate Customer and Address fields on the Add/Edit forms.  If any of the information is
     * invalid an Alert is presented to the user with a description of the issue and the the save or update action is
     * not performed.
     */
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

    /**
     * Getters and Setters for instance variables
     */

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
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

}


