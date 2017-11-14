package calendar.components;

import calendar.helpers.AppointmentType;
import calendar.helpers.KeyValuePair;
import calendar.models.Appointment;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppointmentDialog extends Dialog {

    private ButtonType saveButtonType;

    private AppointmentDialogPane pane;

    private List<KeyValuePair> customers;

    private List<LocalTime> times;

    public AppointmentDialog() {

        try {
            this.pane = new AppointmentDialogPane();
            this.setDialogPane(pane);
            this.saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, saveButtonType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AppointmentDialog(ArrayList<KeyValuePair> customers, ArrayList<LocalTime> times, LocalDate selectedDate, LocalTime selectedTime) {
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

    public ButtonType getSaveButtonType() {
        return this.saveButtonType;
    }

    public Appointment getAppointment(){
        LocalDate date = pane.getDate();
        LocalDateTime start = LocalDateTime.of(date, pane.getStartTime());
        LocalDateTime end = LocalDateTime.of(date, pane.getEndTime());
        long customer = pane.getCustomer().getKey();
        String title = pane.getTitle();
        String description = pane.getDescription().name();
        String location = pane.getLocation();
        String contact = pane.getContact();
        String url = pane.getUrl();

        return new Appointment(customer,title,description,location,contact,url,start,end);
    }

    public void populateComboBoxes(){
//        List<Customer> customers = Customer.findAll();

        this.pane.setComboBoxOptions(this.customers, this.pane.getCustomerComboBox());
        this.pane.setComboBoxOptions(this.times, this.pane.getStartTimeComboBox());
        this.pane.setComboBoxOptions(this.times, this.pane.getEndTimeComboBox());
        this.pane.setComboBoxOptions(Arrays.asList(AppointmentType.values()), this.pane.getDescriptionComboBox());
    }

    public void setCustomers(List<KeyValuePair> customers) {
        this.customers = customers;
    }

    public void setTimes(List<LocalTime> times) {
        this.times = times;
    }
}
