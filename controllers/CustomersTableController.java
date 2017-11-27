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

    /**
     * The TableView for displaying a list of Customers
     */
    @FXML
    private TableView<Customer> customerTableView = new TableView<Customer>();

    /**
     * The list of Customers to be displied in the customerTableView
     */
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     * The customerTableView columns
     */
    @FXML
    private TableColumn customerIdColumn = new TableColumn();

    @FXML
    private TableColumn customerNameColumn = new TableColumn();

    @FXML
    private TableColumn customerPhoneColumn = new TableColumn();

    @FXML
    private  TableColumn customerAddressString = new TableColumn();


    /**
     * The constructor
     */
    public CustomersTableController() {
    }

    /**
     * Loads a list of active customer from the database into the customerTableView
     */
    public void loadCustomers() {

        ArrayList<Customer> allCustomers = (ArrayList<Customer>) Customer.findAll();
        this.customers.addAll(allCustomers);

        for (Customer customer : this.customers
                ) {
            this.customers = this.customerTableView.getItems();
            this.customers.add(customer);
        }

    }

    /**
     * Gets the selected Customer object from the TableView and returns it
     * @return The selected Customer object
     */
    public Customer getSelectedCustomer() {
        //get the selected customer
        int index = this.customerTableView.getSelectionModel().getSelectedIndex();
        //get the customer
        Customer customer = customers.get(index);
        //return the customer
        return customer;
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

    /**
     * Getters
     */
    public TableView<Customer> getCustomerTableView() {
        return customerTableView;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }
}