package calendar.components;

import calendar.Main;
import calendar.controllers.MainController;
import calendar.helpers.AppointmentType;
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
    private Button editAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

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

        this.appointmentTableView.setOnMouseReleased(event -> {
            this.editAppointmentButton.setDisable(false);
            this.deleteAppointmentButton.setDisable(false);
        });

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
                Appointment appt = appointment.get();
                compareAndInsertInTable(appt);
            } else {
                System.out.println("Nothing to do right now");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void editAppointment(ActionEvent actionEvent) {
        try {
            Appointment selectedAppt = this.appointmentTableView.getSelectionModel().getSelectedItem();
            AppointmentDialog dialog = new AppointmentDialog(this.customers, this.times, LocalDate.now(), LocalTime.now());
            AppointmentDialogPane adp = dialog.getPane();
            adp.setTitleTextField(selectedAppt.getTitle());
            adp.getDescriptionComboBox().setValue((AppointmentType.valueOf(selectedAppt.getDescription())));
            adp.setLocationTextField(selectedAppt.getLocation());
            adp.setContactTextField(selectedAppt.getContact());
            adp.setUrlTextField(selectedAppt.getUrl());

            Optional<KeyValuePair> result = this.customers.stream().filter(c -> c.getKey() == selectedAppt.getCustomerId()).findFirst();
            if (result.isPresent()) {
                adp.getCustomerComboBox().setValue(result.get());
            }

            adp.getApptDatePicker().setValue(selectedAppt.getStart().toLocalDate());
            adp.getStartTimeComboBox().setValue(selectedAppt.getStart().toLocalTime());
            adp.getEndTimeComboBox().setValue(selectedAppt.getEnd().toLocalTime());


            long appointmentId = selectedAppt.getAppointmentId();
            ButtonType saveButtonType = dialog.getSaveButtonType();

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    Appointment appointment = dialog.getAppointment();
                    appointment.setAppointmentId(appointmentId);
                    appointment.save();
                    return appointment;
                } else {
                    System.out.println("cancel");
                    return null;
                }
            });

            Optional<Appointment> appointment = dialog.showAndWait();
            if (appointment.isPresent()){
                int initalIndex = this.appointmentTableView.getSelectionModel().getSelectedIndex();
                this.appointments.remove(initalIndex);
                compareAndInsertInTable(appointment.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void compareAndInsertInTable(Appointment appt) {
        Optional<Appointment> apptAfter = appointments.stream().filter(a -> a.getStart().compareTo(appt.getStart()) == 1).findFirst(); //todo next what if it should be last in the TableView?
        this.appointments = this.appointmentTableView.getItems();
        if(apptAfter.isPresent()){
            int index = appointments.indexOf(apptAfter);
            this.appointments.add(index, appt);
        } else {
            this.appointments.add(appt);
        }
    }

    @FXML
    private void deleteAppointment(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("You're about to delete this appointment forever, this cannot be undone. ");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Appointment appointment = this.appointmentTableView.getSelectionModel().getSelectedItem();
            boolean deleted = appointment.delete();
            if(deleted){
                this.appointments = this.appointmentTableView.getItems();
                this.appointments.remove(appointment);
            }
        }
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
