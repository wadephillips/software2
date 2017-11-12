package calendar.controllers;

import calendar.Main;
import calendar.helpers.KeyValuePair;
import calendar.models.Appointment;
import calendar.models.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract public class BaseCalendarController extends BaseController {

    protected static LocalDate displayedMonth;
    protected static LocalDate currentDate;
    @FXML
    public AnchorPane calendarPane;
    protected LocalDate firstDayOfDisplayedMonth;

    @FXML
    protected Pane bodyPane;

    protected ArrayList<KeyValuePair> customers = new ArrayList<>();

    protected ArrayList<LocalTime> times = new ArrayList<>();

    public BaseCalendarController() {
        currentDate = LocalDate.now();
        this.firstDayOfDisplayedMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());


    }

    @FXML
    public void showCalendarByMonth(ActionEvent actionEvent) throws IOException {
        Stage mainStage = Main.getMainStage();
        FXMLLoader root = new FXMLLoader(getClass().getResource("../navigation.fxml"));

        Scene scene = new Scene(root.load());

        MainController controller = root.getController();
        controller.loadContent("monthCalendar.fxml");

        mainStage.setScene(scene);
        mainStage.show();
    }

    @FXML
    public void showCalendarByWeek(ActionEvent actionEvent) throws IOException {
//        FXMLLoader weekRoot = new FXMLLoader(getClass().getResource("../calendarWeek.fxml"));
//        System.out.println(weekRoot.getLocation());
//        this.bodyPane.getChildren().setAll(weekRoot);
//        Parent parent = Main.getMainStage().getScene().getRoot();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../calendarWeek.fxml"));
            this.calendarPane.getChildren().clear();
            this.calendarPane.getChildren().add((Node) loader.load());




//        this.bodyPane.getChildren().clear();
//        this.bodyPane.getChildren().add((Node) FXMLLoader.load(getClass().getResource("../calendarWeek.fxml")));
    }

    public void initCustomers(){
        List<Customer> customers = Customer.findAll();

        for (Customer customer:
                customers) {
            this.customers.add(new KeyValuePair(customer.getCustomerId(), customer.getCustomerName()));

        }
    }

    public void initTimes(){
        LocalTime base = LocalTime.of(8, 0 , 0);
        while(base.getHour() < 17) {
            this.times.add(base);
            base = base.plusMinutes(15);
        }
    }

    public void insertAppointmentBlob(VBox parent, Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

        String formatedAppTime =  formatter.format(appointment.getStart()) + " - " + formatter.format(appointment.getEnd());

        Label blob = new Label(formatedAppTime + "  " + appointment.getCustomerId());
        blob.getStyleClass().add("apptBlob");
//        System.out.println(blob.getStyleClass());

        parent.getChildren().add(blob);

    }
}
