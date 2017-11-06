package calendar.controllers;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController extends MainController {

    private final CustomersTableController customersTableController = new CustomersTableController();


    public CustomersController(){
        super();
    }


    public void loadCustomers(){

        //        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, Long>("customerId"));
//        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
//        System.out.println(this.customerTableView.getItems());
//        this.customers.addAll(Customer.findAll());
        //todo why wont this table populate

        ////        this.customers.addAll(allCustomers);
//        System.out.println(this.customers);
//        this.customerTableView.getItems().setAll(this.customers);

        customersTableController.loadCustomers();
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
//            customersTableController.loadCustomers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAddCustomerView(ActionEvent actionEvent) {
        try {
            this.loadContent("customerEditForm.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
