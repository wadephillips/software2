package calendar.helpers;

public enum AppointmentType {
    INITIAL("Initial Appt"),FOLLOW_UP("Follow Up"),SALE("Sales Call"),SURVEY("Follow up survey"),THANKS("Thank You Call");

    private String humanDescription;

    private AppointmentType(String humanDescription) {
        this.humanDescription = humanDescription;
    }

    public String getHumanDescription() {
        return humanDescription;
    }


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return getHumanDescription();
    }
}
