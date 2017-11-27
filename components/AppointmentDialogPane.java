package calendar.components;

import calendar.helpers.AppointmentType;
import calendar.helpers.KeyValuePair;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Provides a form to add or edit an Appointment
 */
public class AppointmentDialogPane extends DialogPane {


    /**
     * Holds the title text for the dialog field, typically either "Add Appointment" or "Edit Appointment"
     */
    @FXML
    private Label dialogTitle;

    /**
     * The title field on the add/edit appointment form.
     */
    @FXML
    private TextField titleTextField;

    /**
     * The AppointmentType field on the add/edit appointment form
     */
    @FXML
    private ComboBox<AppointmentType> descriptionComboBox;


    /**
     * The location TextField on the add/edit appointment form
     */
    @FXML
    private TextField locationTextField;

    /**
     * The contact TextField on the add/edit appointment form
     */
    @FXML
    private TextField contactTextField;

    /**
     * The url TextField on the add/edit appointment form
     */
    @FXML
    private TextField urlTextField;

    /**
     * The customer ComboBox field on the add/edit appointment form
     */
    @FXML
    private ComboBox<KeyValuePair> customerComboBox;

    /**
     * The appointment DatePicker field on the add/edit appointment form
     */
    @FXML
    private DatePicker apptDatePicker;

    /**
     * The startTime ComboBox on the add/edit appointment form
     */
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;

    /**
     * The endTime ComboBox on the add/edit appointment form
     */
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;


    /**
     * The constructor
     * @throws Exception
     */
    public AppointmentDialogPane() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../appointmentDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        loader.load();

    }

    /**
     * Getters and Setters
     *
     */

    public Label getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(Label dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public void setTitleTextField(String titleText) {
        this.titleTextField.setText(titleText);
    }

    public void setLocationTextField(String locationText) {
        this.locationTextField.setText(locationText);
    }

    public ComboBox<AppointmentType> getDescriptionComboBox() {
        return descriptionComboBox;
    }

    public void setDescriptionComboBox(ComboBox<AppointmentType> descriptionComboBox) {
        this.descriptionComboBox = descriptionComboBox;
    }

    public TextField getContactTextField() {
        return contactTextField;
    }

    public void setContactTextField(String contactText) {
        this.contactTextField.setText(contactText);
    }

    public TextField getUrlTextField() {
        return urlTextField;
    }

    public void setUrlTextField(String urlText) {
        this.urlTextField.setText(urlText);
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

    public Enum<AppointmentType> getDescription() {
        return descriptionComboBox.getValue();
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

    /**
     * Add a list of objects to a ComboBox
     * @param list The list
     * @param box The ComboBox
     */
    public void setComboBoxOptions(List list, ComboBox box){
        box.getItems().addAll(list);
    }



}
