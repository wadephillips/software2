package calendar.controllers;

import calendar.models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomersTableController extends MainController {
    @FXML
    private TableView<Customer> customerTableView = new TableView<Customer>();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    @FXML
    private TableColumn customerIdColumn = new TableColumn();
    @FXML
    private TableColumn customerNameColumn = new TableColumn();
    @FXML
    private TableColumn customerPhoneColumn = new TableColumn();
    @FXML
    private  TableColumn customerAddressString = new TableColumn();


    public CustomersTableController() {
    }

    public void loadCustomers() {

        ArrayList<Customer> allCustomers = (ArrayList<Customer>) Customer.findAll();
//        System.out.println(allCustomers);
        this.customers.addAll(allCustomers);

        for (Customer customer : this.customers
                ) {
//            System.out.println(customer.getCustomerId() + customer.getCustomerName() + customer.getCountry());
            this.customers = this.customerTableView.getItems();
            this.customers.add(customer);
        }

    }

    public Customer getSelectedCustomer() {
        //get the selected customer
        int index = this.customerTableView.getSelectionModel().getSelectedIndex();
        //get the customer
        Customer customer = customers.get(index);
        //return the customer
        return customer;
    }

    public TableView<Customer> getCustomerTableView() {
        return customerTableView;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
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
        this.loadCustomers();
    }
}