package calendar.models;

/**
 * Created by wadelp on 10/17/17.
 */
public class User extends Model {

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


    public User(){
        super();
    }

    /**
     * method to return an empty version of the entity.
     *
     * @return
     */
    @Override
    public Model create() {
        return null;
    }

    /**
     * method to retrieve an instance of the entity from the database.
     *
     * @return
     */
    @Override
    public Model find() {
        return null;
    }

    /**
     * method to persist changes on the entity to the database.
     *
     * @return
     */
    @Override
    public Model update() {
        return null;
    }

    /**
     * method to help save changes the entity to the database.
     *
     * @return
     */
    @Override
    public Model save() {
        return null;
    }

    /**
     * method to delete the entitie's record from the database.
     *
     * @return
     */
    @Override
    public Model delete() {
        return null;
    }


}
