package calendar.components;

import calendar.helpers.KeyValuePair;
import calendar.models.Appointment;
import calendar.models.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentDialogPane extends DialogPane {

    @FXML
    private Label titleLabel;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField contactTextField;

    @FXML
    private TextField urlTextField;

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

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public void setTitleTextField(TextField titleTextField) {
        this.titleTextField = titleTextField;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void setDescriptionTextArea(TextArea descriptionTextArea) {
        this.descriptionTextArea = descriptionTextArea;
    }

    public TextField getLocationTextField() {
        return locationTextField;
    }

    public void setLocationTextField(TextField locationTextField) {
        this.locationTextField = locationTextField;
    }

    public TextField getContactTextField() {
        return contactTextField;
    }

    public void setContactTextField(TextField contactTextField) {
        this.contactTextField = contactTextField;
    }

    public TextField getUrlTextField() {
        return urlTextField;
    }

    public void setUrlTextField(TextField urlTextField) {
        this.urlTextField = urlTextField;
    }

    public ComboBox<KeyValuePair> getCustomerComboBox() {
        return customerComboBox;
    }

    public void setCustomerComboBox(ComboBox<KeyValuePair> customerComboBox) {
        this.customerComboBox = customerComboBox;
    }

    public DatePicker getApptDatePicker() {
        return apptDatePicker;
    }

    public void setApptDatePicker(DatePicker apptDatePicker) {
        this.apptDatePicker = apptDatePicker;
    }

    public ComboBox<LocalTime> getStartTimeComboBox() {
        return startTimeComboBox;
    }

    public void setStartTimeComboBox(ComboBox<LocalTime> startTimeComboBox) {
        this.startTimeComboBox = startTimeComboBox;
    }

    public ComboBox<LocalTime> getEndTimeComboBox() {
        return endTimeComboBox;
    }

    public void setEndTimeComboBox(ComboBox<LocalTime> endTimeComboBox) {
        this.endTimeComboBox = endTimeComboBox;
    }

    public String getTitle() {
        return titleTextField.getText();
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }

    public String getLocation() {
        return this.locationTextField.getText();
    }

    public String getContact() {
        return this.contactTextField.getText();
    }

    public String getUrl() {
        return this.urlTextField.getText();
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

    public void setComboBoxOptions(List list, ComboBox box){
        box.getItems().addAll(list);
    }
}
