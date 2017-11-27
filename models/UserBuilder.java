package calendar.models;

/**
 * A builder class for instantiating an User
 */
public class UserBuilder extends ModelBuilder {
    /**
     * The user's id
     */
    private long userId;

    /**
     * The username sign in credential
     */
    private String userName;

    /**
     * The users password
     */
    private String password;

    /**
     * Is the user active
     */
    private int active;

    /**
     * Instantiates and returns a User object
     * @return a User object
     */
    public User build(){
        super.checkAndSetCreate();
        return new User(this.userId, this.userName, this.password, this.active, this.createdBy, this.createDate,this.lastUpdate, this.lastUpdateBy);
    }

    /**
     * Setters
     */

    public UserBuilder setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public UserBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setActive(int active) {
        this.active = active;
        return this;
    }


}
