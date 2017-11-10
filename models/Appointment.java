package calendar.models;

import calendar.ModelDAO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;

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
    public ArrayList<ModelDAO> findAll() {
        return null;
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
    public Model save() {
        return null;
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
}
