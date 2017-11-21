package calendar.models;

import calendar.Main;
import calendar.ModelDAO;
import javafx.beans.property.*;
import javafx.fxml.FXML;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Handles persisting Appointemtns to the database
 */
public class Appointment extends Model {

    private LongProperty appointmentId = new SimpleLongProperty();

    private LongProperty customerId = new SimpleLongProperty();

    private StringProperty title = new SimpleStringProperty();

    private StringProperty description = new SimpleStringProperty();

    private StringProperty location = new SimpleStringProperty();

    private StringProperty contact = new SimpleStringProperty();

    private StringProperty url = new SimpleStringProperty();

    private ObjectProperty<ZonedDateTime> start = new SimpleObjectProperty<>();

    private ObjectProperty<ZonedDateTime> end = new SimpleObjectProperty<>();

    private StringProperty customerName = new SimpleStringProperty();

    private StringProperty startTime = new SimpleStringProperty();

    private StringProperty endTime = new SimpleStringProperty();

    private SimpleStringProperty apptDate = new SimpleStringProperty();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
//    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("eee, MMM-d", Main.getLocale());


    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);


    /**
     * Instantiate an empty instance
     */
    public Appointment(){
        super();

    }

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
        this.checkAndSetUpdate();
    }

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
     * method to return an empty version of the entity.
     *
     * @return
     */
    public Model create() {
        return null;
    }

    /**
     * method to retrieve an instance of the entity from the database.
     *
     * @param id
     * @return
     */
    public Model find(int id) {
        return null;
    }

    /**
     * method to retrieve all instances of the entity from the database
     */
    public static ArrayList<Appointment> getAllByYearMonth(LocalDate baseDate) {
//        System.out.println("hi");
//        ZoneId zone = ZoneId.systemDefault();
        String baseYearMonth = baseDate.getYear() + "-"+ baseDate.getMonthValue();

        String consultantName = Main.getLoggedInUser().getUserName();
        String sql = "SELECT a.*, c.customerName FROM appointment as a INNER JOIN customer as c ON a.customerId = c.customerId WHERE a.createdBy = '" + consultantName + "' AND DATE_FORMAT(start, '%Y-%m') = '" + baseYearMonth + "' ORDER BY start;";
//        System.out.println(sql);
        ArrayList<Appointment> list = getAppointments(sql);
        return list;
    }

    public static ArrayList<Appointment> getAllByWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
//        System.out.println("hi");
//        ZoneId zone = ZoneId.systemDefault();
//        String baseYearMonth = baseDate.getYear() + "-"+ baseDate.getMonthValue();
        String consultantName = Main.getLoggedInUser().getUserName();
        String sql = "SELECT a.*, c.customerName FROM appointment as a INNER JOIN customer as c ON a.customerId = c.customerId WHERE a.createdBy = '" + consultantName + "' AND start >= '" + startOfWeek + "' AND end <= '" + endOfWeek + "' ORDER by start;";
//        System.out.println(sql);
        ArrayList<Appointment> list = getAppointments(sql);
        return list;
    }

    private static ArrayList<Appointment> getAppointments(String sql) {
        ZoneId zone = ZoneId.of("UTC");
        ArrayList<Appointment> list = new ArrayList<>();
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){
            int i = 0;
            while (resultSet.next()){
//                System.out.println(++i);
                Appointment appointment = new Appointment(
                        resultSet.getLong("appointmentId"),
                        resultSet.getLong("customerId"),
                        resultSet.getString("c.customerName"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("location"),
                        resultSet.getString("contact"),
                        resultSet.getString("url"),
//                        LocalDateTime.ofInstant(resultSet.getTimestamp("start").toInstant(), zone),
                        resultSet.getTimestamp("start").toLocalDateTime(),
//                        LocalDateTime.ofInstant(resultSet.getTimestamp("end").toInstant(), zone),
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
     * method to persist changes on the entity to the database.
     *
     * @return
     */
    private Appointment update() {

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
     * method to help save changes the entity to the database.
     *
     * @return
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
                stmt.setTimestamp(11, Timestamp.from(this.getCreateDate().toInstant()));//datetime
                stmt.setTimestamp(12, Timestamp.from(this.getLastUpdate())); //timestamp
                stmt.setString(13, this.getLastUpdateby());

                stmt.executeUpdate();
                conn.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback(savepoint1);
            }


        } catch(SQLException e){
            //            System.out.println(sql);
            e.printStackTrace();

        }

        return this;
    }

    /**
     * method to delete the entitie's record from the database.
     *
     * @return boolean
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

    public ZonedDateTime getStartLocal() {
        return utcDateTimeToLocal(start.get());
    }

    public ZonedDateTime getEndLocal() {
        return utcDateTimeToLocal(end.get());
    }

    @FXML
    public String getStartLocalFormatted() {
        ZonedDateTime zonedDateTime = this.getStartLocal();
        return zonedDateTime.format(timeFormatter);
    }

    @FXML
    public String getEndLocalFormatted() {
        ZonedDateTime zonedDateTime = this.getEndLocal();
        return zonedDateTime.format(timeFormatter);
    }

    @FXML
    public String getApptDateLocalFormatted() {
        ZonedDateTime zonedDateTime = this.getStartLocal();
        return zonedDateTime.format(dateFormatter);
    }

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

    public SimpleStringProperty apptDateProperty() {
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

