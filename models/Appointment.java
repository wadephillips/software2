package calendar.models;

import calendar.ModelDAO;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Handles persisting Appointemtns to the database
 */
public class Appointment extends Model {

    private long appointmentId;

    private long customerId;

    private String title;

    private String description;

    private String location;

    private String contact;

    private String url;

    private LocalDateTime start;

    private LocalDateTime end;

    /**
     * Instantiate an empty instance
     */
    public Appointment(){
        super();

    }

    public Appointment(long customerId, String title, String description, String location, String contact, String url, LocalDateTime start, LocalDateTime end) {
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
        this.checkAndSetCreate();
        this.checkAndSetUpdate();
    }

    public Appointment(long appointmentId, long customerId, String title, String description, String location, String contact, String url, LocalDateTime start, LocalDateTime end, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
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
        ZoneId zone = ZoneId.systemDefault();
        String baseYearMonth = baseDate.getYear() + "-"+ baseDate.getMonthValue();
        String sql = "SELECT * FROM appointment WHERE DATE_FORMAT(start, '%Y-%m') = '" + baseYearMonth + "' ORDER BY start;";
//        System.out.println(sql);
        ArrayList<Appointment> list = getAppointments(zone, sql);
        return list;
    }

    public static ArrayList<Appointment> getAllByWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
//        System.out.println("hi");
        ZoneId zone = ZoneId.systemDefault();
//        String baseYearMonth = baseDate.getYear() + "-"+ baseDate.getMonthValue();
        String sql = "SELECT * FROM appointment WHERE start >= '" + startOfWeek + "' AND end <= '" + endOfWeek + "' ORDER by start;";
//        System.out.println(sql);
        ArrayList<Appointment> list = getAppointments(zone, sql);
        return list;
    }

    private static ArrayList<Appointment> getAppointments(ZoneId zone, String sql) {
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
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("location"),
                        resultSet.getString("contact"),
                        resultSet.getString("url"),
                        LocalDateTime.ofInstant(resultSet.getTimestamp("start").toInstant(), zone),
                        LocalDateTime.ofInstant(resultSet.getTimestamp("end").toInstant(), zone),
                        resultSet.getString("createdBy"),
                        ZonedDateTime.ofInstant(resultSet.getTimestamp("createDate").toInstant(), zone),
                        resultSet.getTimestamp("lastUpdate").toInstant(),
                        resultSet.getString("lastUpdateBy")
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
     * @param id
     * @return
     */
    public Model update(int id) {
        return null;
    }

    /**
     * method to help save changes the entity to the database.
     *
     * @return
     */
    public Appointment save() {
        if (this.appointmentId > 0) {
            //todo throw an exception
        }

        if(this.customerId <= 0){
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
                this.appointmentId = this.getNextId();

                stmt.setLong(1, this.appointmentId);
                stmt.setLong(2, this.customerId);
                stmt.setString(3, this.title);
                stmt.setString(4, this.description);
                stmt.setString(5, this.location);
                stmt.setString(6, this.contact);
                stmt.setString(7, this.url);
                stmt.setTimestamp(8, Timestamp.valueOf(this.start));
                stmt.setTimestamp(9, Timestamp.valueOf(this.end));


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
     * @param id
     * @return
     */
    public Model delete(int id) {
        return null;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getAppointmentId() {
        return appointmentId;
    }
}
