package calendar.models;

import calendar.Main;
import javafx.beans.property.*;
import javafx.fxml.FXML;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

/**
 * Creates an object to represent an appointment in the application.  This model also handles interactions with the
 * appointment database table.
 */
public class Appointment extends Model {

    /**
     * The appointment id
     */
    private LongProperty appointmentId = new SimpleLongProperty();

    /**
     * The customer id
     */
    private LongProperty customerId = new SimpleLongProperty();

    /**
     * A short description of the appointment
     */
    private StringProperty title = new SimpleStringProperty();

    /**
     * The appointment type
     */
    private StringProperty description = new SimpleStringProperty();

    /**
     * The location where this meeting is happening
     */
    private StringProperty location = new SimpleStringProperty();

    /**
     * The name of the contact for this appointment
     */
    private StringProperty contact = new SimpleStringProperty();

    /**
     * A place to store a url that is related to the Appointment instance
     */
    private StringProperty url = new SimpleStringProperty();

    /**
     * The date and time when the Appointment begins
     */
    private ObjectProperty<ZonedDateTime> start = new SimpleObjectProperty<>();

    /**
     * The date and time when the Appointment ends
     */
    private ObjectProperty<ZonedDateTime> end = new SimpleObjectProperty<>();

    /**
     * Name of the customer
     */
    private StringProperty customerName = new SimpleStringProperty();

    /**
     * A string used to display the time when the appointment starts
     */
    private StringProperty startTime = new SimpleStringProperty();

    /**
     * A string used to display the time when the appointment ends
     */
    private StringProperty endTime = new SimpleStringProperty();

    /**
     * A string used to display the date when the Appointment is scheduled to occur
     */
    private StringProperty apptDate = new SimpleStringProperty();

    /**
     * Used to format the appointment date for display to the user
     */
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    /**
     * Used to format the appointment start and end times for display to the user.
     */
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);


    /**
     * Instantiate an empty Appointment instance
     */
    public Appointment(){
        super();

    }

    /**
     * The constructor for creating a new Appointment that has not been prevoiusly saved to the database
     *
     * @param customerId
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param url
     * @param start
     * @param end
     */
    public Appointment(long customerId, String title, String description, String location, String contact, String url, LocalDateTime start, LocalDateTime end) {
        this.customerId.set(customerId);
        this.title.set(title);
        this.description.set(description);
        this.location.set(location);
        this.contact.set(contact);
        this.url.set(url);
        this.start.set(this.localDateTimeToUTC(start));
        this.end.set(this.localDateTimeToUTC(end));
        this.formatDateTimes();
        this.checkAndSetCreate();
        this.setUpdate();
    }

    /**
     * The constructor for retrieving an existing Appointment Record from the Database
     * @param appointmentId
     * @param customerId
     * @param customerName
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param url
     * @param start
     * @param end
     * @param createdBy
     * @param createDate
     * @param lastUpdate
     * @param lastUpdateby
     */
    public Appointment(long appointmentId, long customerId, String customerName, String title, String description, String location, String contact, String url, LocalDateTime start, LocalDateTime end, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.appointmentId.set(appointmentId);
        this.customerId.set(customerId);
        this.customerName.set(customerName);
        this.title.set(title);
        this.description.set(description);
        this.location.set(location);
        this.contact.set(contact);
        this.url.set(url);
        this.start.set(ZonedDateTime.of(start, ZoneId.of("UTC")));
        this.end.set(ZonedDateTime.of(end, ZoneId.of("UTC")));
        this.formatDateTimes();
    }

    private void formatDateTimes() {
        this.apptDate.set(this.start.get().format(this.dateFormatter));
        this.startTime.set(this.start.get().format(this.timeFormatter));
        this.endTime.set(this.end.get().format(this.timeFormatter));
    }


    /**
     * Lookup and return a list of all Appointments with records in the database for the month containing the baseDate
     *
     * @param baseDate a date in the month to be displayed
     * @return  A list of Appointments
     */
    public static ArrayList<Appointment> getAllByYearMonth(LocalDate baseDate) {
        String baseYearMonth = baseDate.getYear() + "-"+ baseDate.getMonthValue();

        String consultantName = Main.getLoggedInUser().getUserName();
        String sql = "SELECT a.*, c.customerName FROM appointment as a INNER JOIN customer as c ON a.customerId = c.customerId WHERE a.createdBy = '" + consultantName + "' AND DATE_FORMAT(start, '%Y-%m') = '" + baseYearMonth + "' ORDER BY start;";
        ArrayList<Appointment> list = getAppointments(sql);
        return list;
    }

    /**
     * Lookup and return a list of all Appointments with records in the database for the week beginning at startOfWeek
     * and ending on endOfWeek
     *
     * @param startOfWeek The date that begins the week to be displayed
     * @param endOfWeek The dat that ends the week to be displayed
     * @return  A list of Appointments
     */
    public static ArrayList<Appointment> getAllByWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        String consultantName = Main.getLoggedInUser().getUserName();
        String sql = "SELECT a.*, c.customerName FROM appointment as a INNER JOIN customer as c ON a.customerId = c.customerId WHERE a.createdBy = '" + consultantName + "' AND start >= '" + startOfWeek + "' AND end <= '" + endOfWeek + "' ORDER by start;";
        ArrayList<Appointment> list = getAppointments(sql);
        return list;
    }


    /**
     * A helper method that handles sending a query to the database and returns a list of Appointments
     * @param sql the sql query to be run
     * @return a list of appointments
     */
    private static ArrayList<Appointment> getAppointments(String sql) {
        ZoneId zone = ZoneId.of("UTC");
        ArrayList<Appointment> list = new ArrayList<>();
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){
            int i = 0;
            while (resultSet.next()){
                Appointment appointment = new Appointment(
                        resultSet.getLong("appointmentId"),
                        resultSet.getLong("customerId"),
                        resultSet.getString("c.customerName"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("location"),
                        resultSet.getString("contact"),
                        resultSet.getString("url"),
                        resultSet.getTimestamp("start").toLocalDateTime(),
                        resultSet.getTimestamp("end").toLocalDateTime(),
                        resultSet.getString("a.createdBy"),
                        ZonedDateTime.ofInstant(resultSet.getTimestamp("a.createDate").toInstant(), zone),
                        resultSet.getTimestamp("a.lastUpdate").toInstant(),
                        resultSet.getString("a.lastUpdateBy")
                );
                list.add(appointment);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * Update an Appointment record that already exists in the database;
     *
     * @return this
     */
    private Appointment update() {

        if (this.appointmentId.get() == 0) {
            return this.save();
        }

        String sql = "UPDATE appointment SET customerId = ?, title = ?, description = ?, " +
                "location = ?, contact = ?, url = ?, start = ?, end = ?, lastUpdateBy = ?" +
                "WHERE appointmentId = ? ";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, this.customerId.get());
            stmt.setString(2, this.title.get());
            stmt.setString(3, this.description.get());
            stmt.setString(4, this.location.get());
            stmt.setString(5, this.contact.get());
            stmt.setString(6, this.url.get());
            stmt.setTimestamp(7, Timestamp.valueOf(this.start.get().toLocalDateTime()));
            stmt.setTimestamp(8, Timestamp.valueOf(this.end.get().toLocalDateTime()));

            stmt.setString(9, Main.getLoggedInUser().getUserName());
            stmt.setLong(10, this.appointmentId.get());




            int result = stmt.executeUpdate();

            if (result == 0) {
                throw new RuntimeException(("The appointment didn't update in the db"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Save a new Appointment record to the database
     *
     * @return this
     */
    public Appointment save() {
        if (this.appointmentId.get() > 0) {
            return this.update();
        }

        if(this.customerId.get() <= 0){
            //todo throw and exception
        }

        String sql = "INSERT INTO appointment (appointmentId, customerId, title, description, location, contact, url, start, end," +
                "createdBy, createDate, lastUpdate, lastUpdateBy) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {

            conn.setAutoCommit(false);
            Savepoint savepoint1 = conn.setSavepoint();
            try {
                this.appointmentId.set(this.getNextId());

                stmt.setLong(1, this.appointmentId.get());
                stmt.setLong(2, this.customerId.get());
                stmt.setString(3, this.title.get());
                stmt.setString(4, this.description.get());
                stmt.setString(5, this.location.get());
                stmt.setString(6, this.contact.get());
                stmt.setString(7, this.url.get());
                stmt.setTimestamp(8, Timestamp.valueOf(this.start.get().toLocalDateTime()));
                stmt.setTimestamp(9, Timestamp.valueOf(this.end.get().toLocalDateTime()));


                stmt.setString(10, super.getCreatedBy());
                stmt.setTimestamp(11, Timestamp.valueOf(this.getCreateDate().toLocalDateTime()));//datetime
                final ZonedDateTime zonedDateTime = this.getLastUpdate().atZone(ZoneId.of("UTC"));
                stmt.setTimestamp(12, Timestamp.valueOf(zonedDateTime.toLocalDateTime())); //timestamp
                stmt.setString(13, this.getLastUpdateby());

                stmt.executeUpdate();
                conn.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback(savepoint1);
            }


        } catch(SQLException e){
            e.printStackTrace();

        }

        return this;
    }

    /**
     * Delete the current Appointment instance's record from the database
     *
     * @return boolean Returns true if the record is successfully deleted, and false if it is not deleted
     */
    public boolean delete() {
        boolean deleted = false;

        String sql = "DELETE FROM appointment WHERE appointmentId = " + this.appointmentId.get();

        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement()) {
            int result = stmt.executeUpdate(sql);

            if (result == 1) deleted = true;
            else throw new RuntimeException("Could not delete appointment " + this.getAppointmentId() + " from DB");

            } catch (SQLException e) {
            e.printStackTrace();
        }

        return deleted;
    }

    /**
     * Gets the Appointment start time and returns a ZonedDateTime based on the logged in user's timezone
     * @return the start time in the local time zone
     */
    public ZonedDateTime getStartLocal() {
        return utcDateTimeToLocal(start.get());
    }

    /**
     * Gets the Appointment end time and returns a ZonedDateTime based on the logged in user's timezone
     * @return the end time in the local time zone
     */
    public ZonedDateTime getEndLocal() {
        return utcDateTimeToLocal(end.get());
    }

    /**
     * Returns a properly formatted start time for display based on the user's Locale
     * @return
     */
    @FXML
    public String getStartLocalFormatted() {
        ZonedDateTime zonedDateTime = this.getStartLocal();
        return zonedDateTime.format(timeFormatter);
    }

    /**
     * Returns a properly formatted end time for display based on the user's Locale
     * @return
     */
    @FXML
    public String getEndLocalFormatted() {
        ZonedDateTime zonedDateTime = this.getEndLocal();
        return zonedDateTime.format(timeFormatter);
    }

    /**
     * Returns a properly formatted appointment date for display based on the user's Locale
     * @return
     */
    @FXML
    public String getApptDateLocalFormatted() {
        ZonedDateTime zonedDateTime = this.getStartLocal();
        return zonedDateTime.format(dateFormatter);
    }

    /**
     * Getters and Setters
     */

    public ZonedDateTime getStartUTC() {
        return start.get();
    }

    public ZonedDateTime getEndUTC() {
        return end.get();
    }

    public long getCustomerId() {
        return customerId.get();
    }

    public long getAppointmentId() {
        return appointmentId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getContact() {
        return contact.get();
    }

    public StringProperty contactProperty() {
        return contact;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getApptDate() {
        return apptDate.get();
    }

    public StringProperty apptDateProperty() {
        return apptDate;
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public String getUrl() {
        return url.get();
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId.set(appointmentId);
    }
}

