package calendar.models;

import calendar.DBFactory;
import calendar.Main;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.*;

/**
 * An abstract class that contains common functions for all Models
 */
abstract public class Model {

    /**
     * The name of the user who created this Model initially
     */
    protected String createdBy;

    /**
     * The date and time when the Model was originally created
     */
    protected ZonedDateTime createDate;

    /**
     * The time when the Model what last updated
     */
    protected Instant lastUpdate;

    /**
     * The user who last updated the Model
     */
    protected String lastUpdateby;

    /**
     * The name of the database table that contains this Model's record
     */
    protected String tableName;

    /**
     * The name of the current class;
     */
    protected Class currentClass;

    /**
     * Fields that exist on the current class
     */
    protected Field[] fields;

    /**
     * The link to the database;
     */
    static final DataSource DATASOURCE = DBFactory.get();

    /**
     * A constructor for instantiating an empty Model
     */
    public Model() {

        this.currentClass = this.getClass();
        this.tableName = this.currentClass.getSimpleName().toLowerCase();
        this.fields = this.currentClass.getDeclaredFields();

    }

    /**
     * A constructor for instantiating a model that already has a record stored in the database
     *
     * @param createdBy
     * @param createDate
     * @param lastUpdate
     * @param lastUpdateby
     */
    public Model(String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        this();
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.lastUpdateby = lastUpdateby;

    }

    /**
     * Find the next available id for the Model and assign it to the object.
     * @return the id
     */
    protected long getNextId() {
        String sql = "SELECT MAX(`" + this.getIdName() + "`) FROM " + this.tableName + ";";
        long nextId = 0;
        try (Connection conn = DATASOURCE.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);) {

            if (rs.first()) {
                int lastId = rs.getInt(1);
                nextId = lastId + 1;
            } else {
                nextId = 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    /**
     * Checks to see if the createDate and createdBy fields have been set and sets them if they have not been.
     * @return this
     */
    protected Model checkAndSetCreate() {
        if (createDate == null) {
            LocalDateTime now = LocalDateTime.now();
            this.createDate = localDateTimeToUTC(now);
        }
        if (createdBy == null) {
            this.createdBy = Main.getLoggedInUser().getUserName();
        }
        return this;
    }

    /**
     * Sets the lastUpdate and lastUpdatedBy values of the object
     * @return this
     */
    protected Model setUpdate() {
        this.lastUpdate = Instant.now();
        this.lastUpdateby = Main.getLoggedInUser().getUserName();
        return this;
    }

    /**
     * Helper method to take in a LocalDateTime and convert it to a UTC ZonedDateTime
     *
     * @param dateTime
     * @return ZonedDateTime
     */
    public static ZonedDateTime localDateTimeToUTC(LocalDateTime dateTime) {
        ZoneOffset offset = Main.getZone().getRules().getOffset(dateTime);
        ZonedDateTime utc = dateTime.atOffset(offset).atZoneSameInstant(ZoneId.of("UTC"));
        return utc;
    }

    /**
     * Helper method to take in a LocalDateTime and convert it to a UTC ZonedDateTime
     *
     * @param dateTime
     * @return ZonedDateTime
     */
    public static ZonedDateTime utcDateTimeToLocal(ZonedDateTime dateTime) {

        ZonedDateTime local = dateTime.withZoneSameInstant(Main.getZone());
        return local;
    }

    /**
     * Getters and Setters
     */

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateby() {
        return lastUpdateby;
    }

    public void setLastUpdateby(String lastUpdateby) {
        this.lastUpdateby = lastUpdateby;
    }

    public String getTableName() {
        return tableName;
    }

    public Field[] getFields() {
        return fields;
    }

    public String getIdName() {
        return this.tableName + "Id";
    }

}
