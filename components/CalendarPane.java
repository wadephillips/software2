package calendar.components;

import calendar.Main;
import calendar.controllers.MainController;
import calendar.helpers.CalendarType;
import calendar.helpers.KeyValuePair;
import calendar.models.Appointment;
import calendar.models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalendarPane extends VBox {

    /**
     * FXML controls
     */
    @FXML
    public Button showByWeekButton;

    @FXML
    public Button showByMonthButton;

    @FXML
    public Button previousButton;

    @FXML
    public Button nextButton;

    @FXML
    public Label monthYearLabel;

    @FXML
    private TableView<Appointment> appointmentTableView = new TableView<>();

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    
    @FXML
        private TableColumn dateColumn = new TableColumn();
    
    @FXML
        private TableColumn startColumn = new TableColumn();

    @FXML
        private TableColumn endColumn = new TableColumn();

    @FXML
        private TableColumn customerColumn = new TableColumn();

    @FXML
        private TableColumn contactColumn = new TableColumn();

    @FXML
        private TableColumn typeColumn = new TableColumn();

    /**
     * Class properties
     *
     */
    protected static LocalDate displayedMonth;

    private static LocalDate currentDate;

    private LocalDate firstDayOfDisplayedMonth;

    private LocalDate firstDayOfDisplayedWeek;

    private LocalDate lastDayOfDisplayedWeek;

    private int displayedWeekNumber;


    private ArrayList<KeyValuePair> customers = new ArrayList<>();

    private ArrayList<LocalTime> times = new ArrayList<>();

    /**
     *
     * @throws Exception
     */

    public CalendarPane() throws Exception {
        currentDate = LocalDate.now();
        this.firstDayOfDisplayedMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        this.firstDayOfDisplayedWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        this.lastDayOfDisplayedWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        this.initDisplayedWeekNumber();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../calendarPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();
    }

    private void showPreviousWeek() {
        System.out.println("display the previous week");
    }

    private void showNextWeek() {
        System.out.println("display the next week");
    }

    @FXML
    public void showPreviousMonth() {

        System.out.println("Display the previous month");
//        LocalDate firstOfPreviousMonth = this.firstDayOfDisplayedMonth.minusMonths(1);
//        this.displayMonthAndYear(firstOfPreviousMonth);
//        this.firstDayOfDisplayedMonth = firstOfPreviousMonth;
//        this.setGridDates();

    }

    @FXML
    public void showNextMonth() {
        System.out.println("display the next month");
//        LocalDate firstOfNextMonth = this.firstDayOfDisplayedMonth.plusMonths(1);
//        this.displayMonthAndYear(firstOfNextMonth);
//        this.firstDayOfDisplayedMonth = firstOfNextMonth;
//        this.setGridDates();
    }

    public void showCalendarByMonth() throws Exception {
        ActionEvent ae = new ActionEvent();
        this.showCalendarByMonth(ae);
    }

    @FXML
    public void showCalendarByMonth(ActionEvent actionEvent) throws Exception {
        //todo probably don't need to reload the entire pane.  We nca probably just change some things: button actions/displayed monthYear/reload the table
        this.displayMonthAndYear();
        this.showByMonthButton.setDisable(true);
        this.showByMonthButton.setUnderline(true);
        this.showByWeekButton.setUnderline(false);
        this.showByWeekButton.setDisable(false);
        this.setChangeButtons(CalendarType.MONTHLY);
        this.appointmentTableView.getItems().clear();


        //get appointments for the month
        ArrayList<Appointment> apps = Appointment.getAllByYearMonth(this.firstDayOfDisplayedMonth);
        apps.stream().forEach( a -> {
            this.appointments = this.appointmentTableView.getItems();
            this.appointments.add(a);
        });
        //load them into the TableView


//        this.
//        Stage mainStage = Main.getMainStage();
//        FXMLLoader root = new FXMLLoader(getClass().getResource("../navigation.fxml"));
//
//        Scene scene = new Scene(root.load());
//
//        MainController controller = root.getController();
//        CalendarPane calendarPane = new CalendarPane();
//        controller.setBodyPaneChild(calendarPane);
////        controller.loadContent("monthCalendar.fxml");
////
//        mainStage.setScene(scene);
//        mainStage.show();
    }

    @FXML
    public void showCalendarByWeek(ActionEvent actionEvent) throws IOException {
        this.displayWeekYearLabel();
        this.showByWeekButton.setUnderline(true);
        this.showByWeekButton.setDisable(true);
        this.showByMonthButton.setDisable(false);
        this.showByMonthButton.setUnderline(false);
        this.setChangeButtons(CalendarType.WEEKLY);
        this.appointmentTableView.getItems().clear();

        //get appointments for the week
        ArrayList<Appointment> apps = Appointment.getAllByWeek(this.firstDayOfDisplayedWeek,this.lastDayOfDisplayedWeek);
        apps.stream().forEach( a -> {
            System.out.println();
            this.appointments = this.appointmentTableView.getItems();
            this.appointments.add(a);
        });

        //load them into the TableView

//        FXMLLoader weekRoot = new FXMLLoader(getClass().getResource("../calendarWeek.fxml"));
//        System.out.println(weekRoot.getLocation());
//        this.bodyPane.getChildren().setAll(weekRoot);
//        Parent parent = Main.getMainStage().getScene().getRoot();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../calendarWeek.fxml"));
//        this.calendarPane.getChildren().clear();
//        this.calendarPane.getChildren().add((Node) loader.load());




//        this.bodyPane.getChildren().clear();
//        this.bodyPane.getChildren().add((Node) FXMLLoader.load(getClass().getResource("../calendarWeek.fxml")));
    }


    /**
     * Scene Setup Methods
     */

    private void displayMonthAndYear() {
        displayMonthAndYear(this.firstDayOfDisplayedMonth);
    }

    private void displayMonthAndYear(LocalDate date) {
        String display = date.getMonth() + " " + date.getYear();
        display = display.toLowerCase();
        char[] chars = display.toUpperCase().toCharArray();
        char firstChar = chars[0];
        display = firstChar + display.substring(1);
        this.monthYearLabel.setText(display);
    }

    private void displayWeekYearLabel() {
        displayWeekYearLabel(this.firstDayOfDisplayedWeek, this.lastDayOfDisplayedWeek);
    }

    private void displayWeekYearLabel(LocalDate firstDay, LocalDate lastDay) {
        String isSameYear = "";
        if (firstDay.getYear() != lastDay.getYear()) {
            isSameYear = "/" + firstDay.getYear();
        }
        String display = firstDay.getMonthValue() + "/" + firstDay.getDayOfMonth()+ isSameYear + " - " + lastDay.getMonthValue() +
                "/" + lastDay.getMonthValue() + "/" + lastDay.getYear();
        this.monthYearLabel.setText(display);

    }

    private void setChangeButtons(CalendarType type) {
        switch (type) {
            case WEEKLY:
                System.out.println("setting to weekly");
                this.previousButton.setOnAction(event -> this.showPreviousWeek());
                this.nextButton.setOnAction(event -> this.showNextWeek());
                break;
            case MONTHLY:
                System.out.println("setting to monthly");
                this.previousButton.setOnAction(event -> this.showPreviousMonth());
                this.nextButton.setOnAction(event -> this.showNextMonth());
                break;
        }
    }
    /**
     * Initilization Methods
     *
     */
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
    private void initDisplayedWeekNumber() {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        this.displayedWeekNumber = currentDate.get(weekOfYear);
    }

}
