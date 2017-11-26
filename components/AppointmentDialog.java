package calendar.components;

import calendar.DBFactory;
import calendar.Main;
import calendar.helpers.AppointmentType;
import calendar.helpers.KeyValuePair;
import calendar.models.Appointment;
import calendar.models.Model;
import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import javax.sql.DataSource;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * AppointmentDialog provides a container for an AppointmentDialogPane which provides a form for adding or editing an Appointment.
 */
public class AppointmentDialog extends Dialog {

    /**
     * The type of the saveButton
     */
    private ButtonType saveButtonType;

    /**
     * The DialogPane that is contained by this Dialog
     */
    private AppointmentDialogPane pane;

    /**
     * A list to hold the values available for the customers ComboBox.
     */
    private List<KeyValuePair> customers;

    /**
     * A list to hold the values available for the start and end ComboBoxes.
     */
    private List<LocalTime> times;

    /**
     * The link to the Database for adding and editing Appointment records
     */
    static final DataSource DATASOURCE = DBFactory.get();


    /**
     * A constructor for creating new Appointments
     */
    public AppointmentDialog() {

        try {
            this.pane = new AppointmentDialogPane();
            this.setDialogPane(pane);
            this.saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, saveButtonType);
            final Button saveButton = (Button) this.getDialogPane().lookupButton(saveButtonType);

            //This event filter prevents the submission of the form if required information is missing
            saveButton.addEventFilter(ActionEvent.ACTION, event -> {
                boolean isValid = false;
                boolean hasOverlap = false;
                try{
                    hasOverlap = this.checkForOverlappingAppointment();
                    isValid = this.validateForm();
                } catch (Exception e) {
                    Main.alert.accept("Please correct the following issue(s) with your appointment before submitting",
                            e.getMessage());
                }
                if (!isValid && !hasOverlap) {
                    event.consume();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * A constructor for editing existing Appointments
     *
     * @param customers A list to be used to populate the customers ComboBox
     * @param times A list to be used to populate the start and end times ComboBoxes
     * @param selectedDate The date that should be displayed in the DatePicker
     */
    public AppointmentDialog(List<KeyValuePair> customers, List<LocalTime> times, LocalDate selectedDate) {
        this();
        this.customers = customers;
        this.times = times;
        this.populateComboBoxes();
        this.pane.getApptDatePicker().setValue(selectedDate);
        LocalTime now = LocalTime.now();
        LocalTime soon = now.plusMinutes((75-now.getMinute())%15).truncatedTo(ChronoUnit.MINUTES);
        LocalTime anHour = soon.plusHours(1).truncatedTo(ChronoUnit.MINUTES);
        this.pane.getStartTimeComboBox().setValue(soon);
        this.pane.getEndTimeComboBox().setValue(anHour);
        this.pane.getDescriptionComboBox().setValue(AppointmentType.SALE);

    }

    /**
     * Takes the input from the AppointmentDialog form and returns an Appointment object.
     *
     * @return An Appointment object
     */
    public Appointment getAppointment(){
        LocalDate date = pane.getDate();
        LocalTime startTime = pane.getStartTime();
        LocalTime endTime = pane.getEndTime();
        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(date, endTime);

        long customer = 0;

        try {
            customer = pane.getCustomer().getKey();
        } catch (Exception e) {
                e.printStackTrace();

        }
        String title = pane.getTitle();
        String description = pane.getDescription().name();
        String location = pane.getLocation();
        String contact = pane.getContact();
        String url = pane.getUrl();


        return new Appointment(customer,title,description,location,contact,url,start,end);
    }

    /**
     * Checks the form fields to ensure that all required fields are filled out before creating the object.
     *
     * @return Returns true if all required fields are filled out and false if they are not.
     * @throws Exception  If a required field is not filled out an exception is thrown with a message to be displayed to the user.
     */
    private boolean validateForm() throws Exception {
        boolean isValid = true;
        StringBuilder body = new StringBuilder();
        if(pane.getTitleTextField().getText().equals("")) {
            isValid = false;
            body.append("You must enter a title");
            body.append(System.lineSeparator());
        }
        if(pane.getContactTextField().getText().equals("")) {
            isValid = false;
            body.append("You must enter a contact for this appointment");
            body.append(System.lineSeparator());
        }
        if (pane.getCustomerComboBox().getValue() == null) {
            isValid = false;
            body.append("You must select a customer for this appointment");
            body.append(System.lineSeparator());
        }
        if (pane.getStartTimeComboBox().getValue() == null) {
            isValid = false;
            body.append("You must select a start time");
            body.append(System.lineSeparator());
        }
        if (pane.getEndTimeComboBox().getValue() == null) {
            isValid = false;
            body.append("You must select an end time");
            body.append(System.lineSeparator());
        }
        if (pane.getStartTimeComboBox().getValue().compareTo(LocalTime.of(8,0,0)) < 0) {
            isValid = false;
            body.append("You may not schedule an appointment before business hours");
            body.append(System.lineSeparator());
        }
        if (pane.getEndTimeComboBox().getValue().compareTo(LocalTime.of(17,0,0)) > 0) {
            isValid = false;
            body.append("You may not schedule an appointment after business hours");
            body.append(System.lineSeparator());
        }



        if (!isValid){;
            throw new Exception(body.toString());
        }
        return isValid;
    }

    /**
     * Checks to make sure that the current user doesn't already have an appointment scheduled during the requested time slot.
     *
     * @return Returns true if the user already has appointments that conflict with the requested start or end time.  Otherwise returns false.
     * @throws Exception If there is a scheduling conflict an Exception is thrown with a message describing the conflict.
     */
    private boolean checkForOverlappingAppointment() throws Exception {
        boolean overlap = false;
        LocalDate date = pane.getDate();
        LocalDateTime start = Model.localDateTimeToUTC(LocalDateTime.of(date, pane.getStartTime())).toLocalDateTime();
        LocalDateTime end = Model.localDateTimeToUTC(LocalDateTime.of(date, pane.getEndTime())).toLocalDateTime();

        String sql = "SELECT * FROM appointment WHERE createdBy = ? AND ((start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?))";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Main.getLoggedInUser().getUserName());
            stmt.setTimestamp(2, Timestamp.valueOf(start));
            stmt.setTimestamp(3, Timestamp.valueOf(end));
            stmt.setTimestamp(4, Timestamp.valueOf(start));
            stmt.setTimestamp(5, Timestamp.valueOf(end));

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.first()) {
                overlap = true;
                ZonedDateTime overlapStartUTC = ZonedDateTime.of(resultSet.getTimestamp("start").toLocalDateTime(),
                        ZoneId.of("UTC"));
                ZonedDateTime overlapStart = Model.utcDateTimeToLocal(overlapStartUTC);
                ZonedDateTime overlapEndUTC = ZonedDateTime.of(resultSet.getTimestamp("end").toLocalDateTime(),
                        ZoneId.of("UTC"));
                ZonedDateTime overlapEnd = Model.utcDateTimeToLocal(overlapEndUTC);
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
                throw new Exception("Sorry, you already have an appointment scheduled from " + overlapStart.format(formatter) + " to " +
                        overlapEnd.format(formatter));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return overlap;
    }


    /**
     * Sets the values to be displayed in the ComboBoxes on the form.
     */
    private void populateComboBoxes(){

        this.pane.setComboBoxOptions(this.customers, this.pane.getCustomerComboBox());
        this.pane.setComboBoxOptions(this.times, this.pane.getStartTimeComboBox());
        this.pane.setComboBoxOptions(this.times, this.pane.getEndTimeComboBox());
        this.pane.setComboBoxOptions(Arrays.asList(AppointmentType.values()), this.pane.getDescriptionComboBox());
    }

    /**
     * Sets the list to be used to populate the customers ComboBox
     *
     * @param customers A list of KeyValuePairs with the customer's id as the key and the Customer object as the value.
     */
    public void setCustomers(List<KeyValuePair> customers) {
        this.customers = customers;
    }

    /**
     * Sets the list to be used to populate the start and end times ComboBoxes
     *
     * @param times A list of LocalTime objects.
     *
     */
    public void setTimes(List<LocalTime> times) {
        this.times = times;
    }

    /**
     * Returns the ButtonType for the SaveButton
     *
     * @return Returns the ButtonType for the SaveButton
     */
    public ButtonType getSaveButtonType() {
        return this.saveButtonType;
    }

    /**
     * Returns the AppointmentDialogPane contained within this Dialog.
     *
     * @return Returns the AppointmentDialogPane contained within this Dialog.
     */
    public AppointmentDialogPane getPane() {
        return pane;
    }
}
