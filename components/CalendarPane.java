package calendar.components;

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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class CalendarPane extends VBox {

    /**
     * Calendar controls
     */
    @FXML
    public Button showByWeekButton;

    @FXML
    public Button showByMonthButton;

    @FXML
    private Button addAppointmentButton;

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


    /**
     * The TableView for displaying appointments by month or week
     */
    @FXML
    private TableView<Appointment> appointmentTableView = new TableView<>();

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /**
     * Table Columns
     */

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
     * Class Variables
     */
    protected static LocalDate currentDate;

    /**
     * Instance Variables
     */

    protected LocalDate displayedMonth;

    private LocalDate firstDayOfDisplayedMonth;

    private LocalDate firstDayOfDisplayedWeek;

    private LocalDate lastDayOfDisplayedWeek;

//    private int displayedWeekNumber;


    /**
     * Lists for populating ComboBoxes on the Add/Edit form
     */
    private ArrayList<KeyValuePair> customers = new ArrayList<>();

    private ArrayList<LocalTime> times = new ArrayList<>();

    /**
     *The constructor
     * @throws Exception
     */

    public CalendarPane() throws Exception {
        //Set the class variables
        currentDate = LocalDate.now();
        //set the instance variables
        this.firstDayOfDisplayedMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        this.firstDayOfDisplayedWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        this.lastDayOfDisplayedWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
//        this.initDisplayedWeekNumber();

        //load the calendar view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../calendarPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();

        //set click handler to enable edit and delete buttons if an appointment is selected
        this.appointmentTableView.setOnMouseReleased(event -> {
            this.editAppointmentButton.setDisable(false);
            this.deleteAppointmentButton.setDisable(false);
        });

        initTimes();
        initCustomers();
    }

    /**
     * The onAction handler for previousButton when viewing the calendar by week.
     * Displays the previous calendar week.
     */
    @FXML
    private void showPreviousWeek() {

        try {
            this.firstDayOfDisplayedWeek = this.firstDayOfDisplayedWeek.minusWeeks(1);
            this.lastDayOfDisplayedWeek = this.lastDayOfDisplayedWeek.minusWeeks(1);
            this.showCalendarByWeek();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The onAction handler for nextButton when viewing the calendar by week.
     * Displays the next calendar week.
     */
    @FXML
    private void showNextWeek() {

        try {
            this.firstDayOfDisplayedWeek = this.firstDayOfDisplayedWeek.plusWeeks(1);
            this.lastDayOfDisplayedWeek = this.lastDayOfDisplayedWeek.plusWeeks(1);
            this.showCalendarByWeek();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The onAction handler for previousButton when viewing the calendar by month.
     * Displays the previous calendar month.
     */
    @FXML
    public void showPreviousMonth() {
        try {
            LocalDate firstOfPreviousMonth = this.firstDayOfDisplayedMonth.minusMonths(1);
            this.displayMonthAndYear(firstOfPreviousMonth);
            this.firstDayOfDisplayedMonth = firstOfPreviousMonth;
            showCalendarByMonth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The onAction handler for nextButton when viewing the calendar by month.
     * Displays the next calendar month.
     */
    @FXML
    public void showNextMonth() {
        try {
            LocalDate firstOfNextMonth = this.firstDayOfDisplayedMonth.plusMonths(1);
            this.displayMonthAndYear(firstOfNextMonth);
            this.firstDayOfDisplayedMonth = firstOfNextMonth;
            showCalendarByMonth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the calendar by month.
     * @throws Exception
     */
    public void showCalendarByMonth() throws Exception {
        ActionEvent ae = new ActionEvent();
        this.showCalendarByMonth(ae);
    }

    /**
     * The onAction handler for showByWeekButton.
     * Displays the calendar by week.
     * @throws Exception
     */
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

    /**
     * Displays the calendar by week.
     * @throws Exception
     */
    private void showCalendarByWeek() throws IOException {
        ActionEvent ae = new ActionEvent();
        this.showCalendarByWeek(ae);
    }

    /**
     * The onAction handler for showByWeekButton.
     * Displays the calendar by week.
     * @throws Exception
     */
    @FXML
    public void showCalendarByWeek(ActionEvent actionEvent) throws IOException {
        // show the user what week they are viewing
        this.displayWeekYearLabel();
        //disable the Week button and enable the Month button
        this.showByWeekButton.setUnderline(true);
        this.showByWeekButton.setDisable(true);
        this.showByMonthButton.setDisable(false);
        this.showByMonthButton.setUnderline(false);
        //set the previousButton and nextButton to change by week
        this.setChangeButtons(CalendarType.WEEKLY);
        //clear the table view
        this.appointmentTableView.getItems().clear();

        //get appointments for the week
        ArrayList<Appointment> apps = Appointment.getAllByWeek(this.firstDayOfDisplayedWeek,this.lastDayOfDisplayedWeek);
        //display appointments in the tableView
        apps.stream().forEach( a -> {
            this.appointments = this.appointmentTableView.getItems();
            this.appointments.add(a);
        });
    }

    /**
     * The onAction handler for the addAppointmentButton.  Displays the Add Appointment dialog.
     * @param actionEvent
     */
    @FXML
    private void addAppointment(ActionEvent actionEvent) {
        try {
            //load up the Add Appointment Dialog
            AppointmentDialog dialog = new AppointmentDialog(this.customers, this.times, LocalDate.now(), 0);

            //handle dialog button controls
            ButtonType saveButtonType = dialog.getSaveButtonType();

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) { //save
                    Appointment appointment = dialog.getAppointment();
                    String customerName = Customer.findNameById(appointment.getCustomerId());
                    appointment.setCustomerName(customerName);
                    return appointment;
                } else { //cancel
                    return null;
                }
            });

            Optional<Appointment> appointment = dialog.showAndWait();
            if (appointment.isPresent()){
                //save appointment to the database
                Appointment appt = appointment.get();
                appt.save();

                //insert into correct place in table
                compareAndInsertInTable(appt);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * The onAction handler for the editAppointmentButton.  Displays the Edit Appointment dialog.
     * @param actionEvent
     */
    @FXML
    private void editAppointment(ActionEvent actionEvent) {
        try {
            //get the selected appointment
            Appointment selectedAppt = this.appointmentTableView.getSelectionModel().getSelectedItem();

            long appointmentId = selectedAppt.getAppointmentId();

            //load the Edit Appointment dialog
            AppointmentDialog dialog = new AppointmentDialog(this.customers, this.times, LocalDate.now(), appointmentId );
            AppointmentDialogPane adp = dialog.getPane();

            //load the selected appointment into the dialog
            adp.getDialogTitle().setText("Edit Appointment");
            adp.setTitleTextField(selectedAppt.getTitle());
            adp.getDescriptionComboBox().setValue((AppointmentType.valueOf(selectedAppt.getDescription())));
            adp.setLocationTextField(selectedAppt.getLocation());
            adp.setContactTextField(selectedAppt.getContact());
            adp.setUrlTextField(selectedAppt.getUrl());

            Optional<KeyValuePair> result = this.customers.stream().filter(c -> c.getKey() == selectedAppt.getCustomerId()).findFirst();
            if (result.isPresent()) {
                adp.getCustomerComboBox().setValue(result.get());
            }

            adp.getApptDatePicker().setValue(selectedAppt.getStartLocal().toLocalDate());
            adp.getStartTimeComboBox().setValue(selectedAppt.getStartLocal().toLocalTime());
            adp.getEndTimeComboBox().setValue(selectedAppt.getEndLocal().toLocalTime());


            //insert event handler here?

            //handle dialog button inputs
            ButtonType saveButtonType = dialog.getSaveButtonType();

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) { //save
                    Appointment appointment = dialog.getAppointment();
                    appointment.setAppointmentId(appointmentId);
                    String customerName = Customer.findNameById(appointment.getCustomerId());
                    appointment.setCustomerName(customerName);
                    return appointment;
                } else {//cancel
                    return null;
                }
            });

            Optional<Appointment> appointment = dialog.showAndWait();
            if (appointment.isPresent()){
                appointment.get().save();
                //remove selected appointment from the TableView
                int initialIndex = this.appointmentTableView.getSelectionModel().getSelectedIndex();
                this.appointments.remove(initialIndex);

                //insert updated appointment into TableView in the correct order
                compareAndInsertInTable(appointment.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to search through the list of appointments backing the TableView and add an appointment based on
     * it's start time.
     * @param appt the appointment to be inserted into the appointments list
     */
    private void compareAndInsertInTable(Appointment appt) {
        Optional<Appointment> apptAfter = appointments.stream().filter(a -> a.getStartLocal().compareTo(appt.getStartLocal()) == 1).findFirst();
        this.appointments = this.appointmentTableView.getItems();
        if(apptAfter.isPresent()){
            int index = appointments.indexOf(apptAfter.get());
            this.appointments.add(index, appt);
        } else {
            this.appointments.add(appt);
        }
    }

    /**
     * The onAction handler for the deleteAppointmentButton.  Displays the a Confirmation Alert and deletes the selected
     * appointment on confirmation.
     * @param actionEvent
     */
    @FXML
    private void deleteAppointment(ActionEvent actionEvent) {
        //initialize the Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("You're about to delete this appointment forever, this cannot be undone. ");

        //Display the Alert
        Optional<ButtonType> result = alert.showAndWait();

        //if delete is confirmed delete the selected appointment
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

    /**
     * Displays the current month and year at the top of the calendar
     */
    private void displayMonthAndYear() {
        displayMonthAndYear(this.firstDayOfDisplayedMonth);
    }

    /**
     * Set the month and year to be displayed at the top of the calendar.
     * @param date  Any day of the month that should be displayed to the user
     */
    private void displayMonthAndYear(LocalDate date) {
        String display = date.getMonth() + " " + date.getYear();
        display = display.toLowerCase();
        char[] chars = display.toUpperCase().toCharArray();
        char firstChar = chars[0];
        display = firstChar + display.substring(1);
        this.monthYearLabel.setText(display);
    }

    /**
     * Displays the start and end date of the current week and year at the top of the calendar
     */
    private void displayWeekYearLabel() {
        displayWeekYearLabel(this.firstDayOfDisplayedWeek, this.lastDayOfDisplayedWeek);
    }

    /**
     * Set the week to be displayed at the top of the calendar.
     * @param firstDay The first day of the week that is being displayed.
     * @param lastDay  The last day of the week that is being displayed
     */
    private void displayWeekYearLabel(LocalDate firstDay, LocalDate lastDay) {
        String isSameYear = "";
        if (firstDay.getYear() != lastDay.getYear()) {
            isSameYear = "/" + firstDay.getYear();
        }
        String display = firstDay.getMonthValue() + "/" + firstDay.getDayOfMonth()+ isSameYear + " - " + lastDay.getMonthValue() +
                "/" + lastDay.getDayOfMonth() + "/" + lastDay.getYear();
        this.monthYearLabel.setText(display);

    }


    /**
     * Sets the onAction event handler for the previousButton and the nextButton
     * @param type The type of calendar that is being displayed
     */
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
     * Initialization Methods
     */

    /**
     * Get a list of current customers to be passed to add & edit appointment dialogs
     */
    public void initCustomers(){
        List<Customer> customers = Customer.findAll();

        for (Customer customer:
                customers) {
            this.customers.add(new KeyValuePair(customer.getCustomerId(), customer.getCustomerName()));

        }
    }

    /**
     * Create a list of times to be used as start and end times by the add & edit appointment dialogs
     */
    public void initTimes(){
        LocalTime base = LocalTime.of(8, 0 , 0);
        while(base.getHour() < 17) {
            this.times.add(base);
            base = base.plusMinutes(15);
        }
    }

    //todo can we delete this?
//    private void initDisplayedWeekNumber() {
//        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
//        this.displayedWeekNumber = currentDate.get(weekOfYear);
//    }





}
