package calendar.models;

import calendar.ModelDAO;

import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by wadelp on 10/17/17.
 */
public class Reminder extends Model {

    /**
     * The reminder's id
     */
    private int reminderId;

    /**
     * The date on which the reminder occurs
     */
    private ZonedDateTime reminderDate;

    /**
     * The lenght of time a reminder should be snoozed for in milliseconds
     */
    private long snoozeIncrement;

    /**
     * The id for the type of snooze
     */
    private long snoozeIncrementTypeId;


    /**
     * todo what is this for?
     */
    private String reminderCol;
    public Reminder(){
        super();
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
