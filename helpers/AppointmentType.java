package calendar.helpers;

/**
 * An Enum to describe different types of appointments
 */
public enum AppointmentType {
    /**
     * The main declaration
     */
    INITIAL("Initial Appt"),FOLLOW_UP("Follow Up"),SALE("Sales Call"),SURVEY("Follow up survey"),THANKS("Thank You Call");

    /**
     * An easier version to read
     */
    private String descriptionForHumans;

    /**
     * The Constructor
     * @param descriptionForHumans A very short description that is easier to read
     */
    AppointmentType(String descriptionForHumans) {
        this.descriptionForHumans = descriptionForHumans;
    }

    /**
     * A getter to return the more friendly version of the constant
     * @return
     */
    public String getDescriptionForHumans() {
        return descriptionForHumans;
    }


    /**
     * Returns the human friendly version of this enum constant, as contained in the
     * declaration.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return getDescriptionForHumans();
    }
}
