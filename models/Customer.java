package calendar.models;

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
}
