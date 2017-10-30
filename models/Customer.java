package calendar.models;

import calendar.ModelDAO;

import java.util.ArrayList;

/**
 * Created by wadelp on 10/17/17.
 */
public class Customer extends Model {

    /**
     * The customer's id
     */
    private long customerId;

    /**
     * The customer's name
     */
    private String customerName;

    /**
     * The id of the address that is associated with the customer record
     */
    private long addressId;

    /**
     * Indicates whether or not the user is currently active within the system
     */
    private int active;


    public Customer(){
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
