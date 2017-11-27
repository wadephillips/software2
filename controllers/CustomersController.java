package calendar.controllers;

import calendar.helpers.KeyValuePair;
import calendar.models.Address;
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

    @FXML
    private Button editCustomerButton;

    private CustomersTableController customersTableController = new CustomersTableController();

    private CustomerFormController customerFormController = new CustomerFormController();

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
            this.bodyPane.getChildren().clear();
            FXMLLoader root = new FXMLLoader(getClass().getResource("../customerEditForm.fxml"));
            this.bodyPane.getChildren().addAll((Node) root.load());

            this.customerFormController = root.getController();

            Button saveButton = this.customerFormController.getSaveButton();
            saveButton.setDisable(true);
            saveButton.setVisible(false);

            Button updateButton = this.customerFormController.getUpdateButton();
            updateButton.setVisible(true);
            updateButton.setDisable(false);
            this.customerFormController.setTitleText("Edit Customer");
            this.customerFormController.initAddChoiceBoxes();
            this.customerFormController.setCustomerId(customer.getCustomerId());
            this.customerFormController.setAddressId(address.getAddressId());
            this.customerFormController.setNameField(customer.getCustomerName());
            this.customerFormController.getActiveCheckBox().setSelected(customer.isActive());

            this.customerFormController.setAddressField(address.getAddress());
            this.customerFormController.setAddress2Field(address.getAddress2());
            this.customerFormController.setPhoneField(address.getPhone());
            this.customerFormController.setPostalCodeField(address.getPostalCode());

            KeyValuePair city = new KeyValuePair(0,"");
            for (KeyValuePair c: this.customerFormController.getCities()
                 ) {
                if (c.getKey() == address.getCityId()) {
                    city = c;
                }
            }
            this.customerFormController.getCityChoiceBox().setValue(city);

            KeyValuePair country = new KeyValuePair(0,"");
            for (KeyValuePair c: this.customerFormController.getCountries()
                 ) {
                if (c.getKey() == address.getCountryId()) {
                    country = c;
                }
            }
            this.customerFormController.getCountryChoiceBox().setValue(country);
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
            this.customersTableController.getCustomerTableView().setOnMouseReleased(event -> {
                this.editCustomerButton.setDisable(false);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAddCustomerView(ActionEvent actionEvent) {
        try {
            this.bodyPane.getChildren().clear();
            FXMLLoader root = new FXMLLoader(getClass().getResource("../customerEditForm.fxml"));
            this.bodyPane.getChildren().addAll((Node) root.load());
            CustomerFormController controller = root.getController();
            controller.setTitleText("Add Customer");
            controller.initAddChoiceBoxes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
