package calendar.models;

/**
 * A builder class for instantiating a Customer object
 */
public class CustomerBuilder extends ModelBuilder {

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


    /**
     * Builds up and returns a Customer object
     * @return
     */
    public Customer build(){
        return new Customer(this.customerId, this.customerName, this.addressId, this.active,
                this.createdBy, this.createDate, this.lastUpdate, this.lastUpdateBy);
    }

    /**
     * Setters
     */

    public CustomerBuilder setCustomerId(long customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerBuilder setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public CustomerBuilder setAddressId(long addressId) {
        this.addressId = addressId;
        return this;
    }

    public CustomerBuilder setActive(int active) {
        this.active = active;
        return this;
    }
}
