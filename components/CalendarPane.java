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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

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

        initTimes();
        initCustomers();
    }

    private void showPreviousWeek() {

        try {
            System.out.println("display the previous week");
            this.firstDayOfDisplayedWeek = this.firstDayOfDisplayedWeek.minusWeeks(1);
            this.lastDayOfDisplayedWeek = this.lastDayOfDisplayedWeek.minusWeeks(1);
            this.showCalendarByWeek();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        this.initWeekYearLabel(this.firstDayOfDisplayedMonth, this.lastDayOfDisplayedWeek);
//        this.firstDayOfDisplayedWeek = firstOfPreviousWeek;
//        this.lastDayOfDisplayedWeek = lastOfPreviousWeek;
//        this.setWeekdayLabels();
    }

    private void showNextWeek() {

        try {
            System.out.println("display the next week");
            this.firstDayOfDisplayedWeek = this.firstDayOfDisplayedWeek.plusWeeks(1);
            this.lastDayOfDisplayedWeek = this.lastDayOfDisplayedWeek.plusWeeks(1);
            this.showCalendarByWeek();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        this.initWeekYearLabel(firstOfNextWeek, lastOfNextWeek);
//        this.firstDayOfDisplayedWeek = firstOfNextWeek;
//        this.lastDayOfDisplayedWeek = lastOfNextWeek;
//        this.setWeekdayLabels();
    }

    @FXML
    public void showPreviousMonth() {
        try {
            System.out.println("Display the previous month");
            LocalDate firstOfPreviousMonth = this.firstDayOfDisplayedMonth.minusMonths(1);
            this.displayMonthAndYear(firstOfPreviousMonth);
            this.firstDayOfDisplayedMonth = firstOfPreviousMonth;
//        ArrayList<Appointment> apps = Appointment.getAllByYearMonth(this.firstDayOfDisplayedMonth);
            showCalendarByMonth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showNextMonth() {
        try {
            System.out.println("display the next month");
            LocalDate firstOfNextMonth = this.firstDayOfDisplayedMonth.plusMonths(1);
            this.displayMonthAndYear(firstOfNextMonth);
            this.firstDayOfDisplayedMonth = firstOfNextMonth;
            showCalendarByMonth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCalendarByMonth() throws Exception {
        ActionEvent ae = new ActionEvent();
        this.showCalendarByMonth(ae);
    }

    @FXML
    public void showCalendarByMonth(ActionEvent actionEvent) throws Exception {
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
    }

    private void showCalendarByWeek() throws IOException {
        ActionEvent ae = new ActionEvent();
        this.showCalendarByWeek(ae);
    }

    @FXML
    public void showCalendarByWeek(ActionEvent actionEvent) throws IOException {
        System.out.println(this.firstDayOfDisplayedWeek + " | " + this.lastDayOfDisplayedWeek);
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
    }

    @FXML
    private void addAppointment(ActionEvent actionEvent) {
        try {
//            ZoneId zoneId = Main.getZone();
//            ZoneOffset offset = zoneId.getRules().getOffset(LocalDateTime.now());
//            Comparator<Appointment> comparator = (a1, a2) -> { a1.getStart().toInstant(offset).compareTo(a2.getStart().toInstant(offset))  };
//            for (Appointment a :
//                    appointments) {
//
//                System.out.print(a.getStart() + " | ");
//                    System.out.println(a.getStart().compareTo(LocalDateTime.of(2017, 11, 18, 12,00)));
//            }


            AppointmentDialog dialog = new AppointmentDialog(this.customers, this.times, LocalDate.now(), LocalTime.now());

            ButtonType saveButtonType = dialog.getSaveButtonType();

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    Appointment appointment = dialog.getAppointment();
                    appointment.save();
                    return appointment;
                } else {
                    System.out.println("cancel");
                    return null;
                }
            });

            Optional<Appointment> appointment = dialog.showAndWait();
            if (appointment.isPresent()){
                System.out.println("Insert appt into grid");
                //todo next - need to insert the appt into the tableview or reload the appts
                Appointment appt = appointment.get();
                Appointment apptAfter = appointments.stream().filter(a -> a.getStart().compareTo(appt.getStart()) == 1).findFirst().get();
                int index = appointments.indexOf(apptAfter);
                this.appointments = this.appointmentTableView.getItems();
                this.appointments.add(index, appt);
//                this.insertAppointmentBlob(box, appointment.get());
            } else {
                System.out.println("Nothing to do right now");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editAppointment(ActionEvent actionEvent) {

    }

    @FXML
    private void deleteAppointment(ActionEvent actionEvent) {

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
                "/" + lastDay.getDayOfMonth() + "/" + lastDay.getYear();
        this.monthYearLabel.setText(display);

    }


    private void setChangeButtons(CalendarType type) {
        switch (type) {
            case WEEKLY:
                this.previousButton.setOnAction(event -> this.showPreviousWeek());
                this.nextButton.setOnAction(event -> this.showNextWeek());
                break;
            case MONTHLY:
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
