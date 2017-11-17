package calendar.controllers;

import calendar.helpers.KeyValuePair;
import calendar.models.Address;
import calendar.models.City;
import calendar.models.Country;
import calendar.models.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController extends MainController {

    private CustomersTableController customersTableController = new CustomersTableController();

    private CustomerEditFormController customerEditFormController = new CustomerEditFormController();

    public CustomersController(){
        super();
    }


    public void loadCustomers(){

        customersTableController.loadCustomers();
    }

    @FXML
    public void editCustomer () {

        Customer customer = this.customersTableController.getSelectedCustomer();
        Address address = customer.getAddress();
        try {
//
            this.bodyPane.getChildren().clear();
            FXMLLoader root = new FXMLLoader(getClass().getResource("../customerEditForm.fxml"));
            this.bodyPane.getChildren().addAll((Node) root.load());

//            this.customerEditFormController = root.getController();
            this.customerEditFormController = root.getController();

            Button saveButton = this.customerEditFormController.getSaveButton();
            saveButton.setDisable(true);
            saveButton.setVisible(false);

            Button updateButton = this.customerEditFormController.getUpdateButton();
            updateButton.setVisible(true);
            updateButton.setDisable(false);
            this.customerEditFormController.setTitleText("Edit Customer");
            this.customerEditFormController.initAddChoiceBoxes();
            this.customerEditFormController.setCustomerId(customer.getCustomerId());
            this.customerEditFormController.setAddressId(address.getAddressId());
            this.customerEditFormController.setNameField(customer.getCustomerName());
            this.customerEditFormController.getActiveCheckBox().setSelected(customer.isActive());

            this.customerEditFormController.setAddressField(address.getAddress());
            this.customerEditFormController.setAddress2Field(address.getAddress2());
            this.customerEditFormController.setPhoneField(address.getPhone());
            this.customerEditFormController.setPostalCodeField(address.getPostalCode());

            //do we need to get the keyvaluepair form the Observable list and set it as the value?
            KeyValuePair city = new KeyValuePair(0,"");
            for (KeyValuePair c: this.customerEditFormController.getCities()
                 ) {
                if (c.getKey() == address.getCityId()) {
                    city = c;
                    System.out.println(city.toString());
                }
            }
            this.customerEditFormController.getCityChoiceBox().setValue(city);

            KeyValuePair country = new KeyValuePair(0,"");
            for (KeyValuePair c: this.customerEditFormController.getCountries()
                 ) {
                if (c.getKey() == address.getCountryId()) {
                    country = c;
                }
            }
            this.customerEditFormController.getCountryChoiceBox().setValue(country);
//            this.customerEditFormController.
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        try {

            this.bodyPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../customersTable.fxml"));
            this.bodyPane.getChildren().addAll((Node) loader.load() );
            this.customersTableController = loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAddCustomerView(ActionEvent actionEvent) {
        try {
//
            this.bodyPane.getChildren().clear();
            FXMLLoader root = new FXMLLoader(getClass().getResource("../customerEditForm.fxml"));
            this.bodyPane.getChildren().addAll((Node) root.load());
//            this.customerEditFormController = root.getController();
            CustomerEditFormController controller = root.getController();
            controller.setTitleText("Add Customer");
            controller.initAddChoiceBoxes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
