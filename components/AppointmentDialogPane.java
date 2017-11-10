package calendar.components;

import calendar.helpers.KeyValuePair;
import calendar.models.Appointment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDialogPane extends DialogPane {

    @FXML
    private Label titleLabel;

    @FXML
    private ComboBox<KeyValuePair> customerComboBox;

    @FXML
    private DatePicker apptDatePicker;

    @FXML
    private ComboBox<LocalTime> startTimeComboBox;

    @FXML
    private ComboBox<LocalTime> endTimeComboBox;




    public AppointmentDialogPane() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../appointmentDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();

    }


    public KeyValuePair getCustomer() {
        return customerComboBox.getValue();
    }

    public LocalDate getDate() {
        return apptDatePicker.getValue();
    }

    public LocalTime getStartTime() {
        return startTimeComboBox.getValue();
    }
    public LocalTime getEndTime() {
        return endTimeComboBox.getValue();
    }

}
