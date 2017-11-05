package calendar.controllers;

import calendar.models.Customer;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class CustomersController extends MainController {


    private void loadCustomers(){

        List<Customer> customers = Customer.findAll();

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
            this.loadContent("customersTable.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAddCustomerView(ActionEvent actionEvent) {
        try {
            this.loadContent("addCustomer.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
