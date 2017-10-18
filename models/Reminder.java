package calendar.models;

import java.time.ZonedDateTime;

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
}
