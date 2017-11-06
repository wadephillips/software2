package calendar.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomersController extends MainController {

    private final CustomersTableController customersTableController = new CustomersTableController();

    private final CustomerEditFormController customerEditFormController = new CustomerEditFormController();

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
//            this.loadContent("customerEditForm.fxml");
            //todo figure out how to get a copy of the subcontroller
//            this.customerEditFormController.setTitleText("Add Customer");
//            System.out.println(this.customerEditFormController.getTitle().getText());
            this.bodyPane.getChildren().clear();
            FXMLLoader root = new FXMLLoader(getClass().getResource("../customerEditForm.fxml"));
            this.bodyPane.getChildren().addAll((Node) root.load());
            CustomerEditFormController controller = root.getController();
            controller.setTitleText("Add Customer");
            controller.initAddChoiceBoxes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
