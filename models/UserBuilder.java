package calendar.models;

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

    public User build(){
        //todo implement a check to see if the the createDate and updatedAt have been set.  Set automatically them if they haven't been
        super.checkAndSetCreate();
        super.checkAndSetUpdate();
        return new User(this.userId, this.userName, this.password, this.active, this.createdBy, this.createDate,this.lastUpdate, this.lastUpdateBy);
    }
}
