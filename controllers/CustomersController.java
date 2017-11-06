package calendar.controllers;

import calendar.models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomersController extends MainController {

    @FXML
    private TableView<Customer> customerTableView = new TableView<>();;

    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    private TableColumn customerIdColumn = new TableColumn();

    @FXML
    private TableColumn customerNameColumn = new TableColumn();




    public CustomersController(){
        super();
    }
    public void loadCustomers(){

        ArrayList<Customer> allCustomers = (ArrayList<Customer>) Customer.findAll();
        System.out.println(allCustomers);

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, Long>("customerId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
//        System.out.println(this.customerTableView.getItems());
//        this.customers.addAll(Customer.findAll());
        //todo why wont htis table fucking populate

//        for (Customer customer: allCustomers
//             ) {
//            System.out.println(customer.getCustomerId()+customer.getCustomerName());
//            this.customers = this.customerTableView.getItems();
//            this.customers.add(customer);
//        }
        this.customers.addAll(allCustomers);
        System.out.println(this.customers);
        this.customerTableView.getItems().setAll(this.customers);

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
            this.loadCustomers();
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
