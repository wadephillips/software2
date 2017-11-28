package calendar.models;

import javafx.beans.property.*;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates an object to represent Customers in the application.  This model also handles interactions with the customer database table
 */
public class Customer extends Model {

    /**
     * The customer's id
     */
    private LongProperty customerId = new SimpleLongProperty();

    /**
     * The customer's name
     */
    private StringProperty customerName = new SimpleStringProperty();

    /**
     * The id of the address that is associated with the customer record
     */
    private LongProperty addressId = new SimpleLongProperty();

    /**
     * Indicates whether or not the user is currently active within the system
     */
    private IntegerProperty active = new SimpleIntegerProperty();

    /**
     * The address object associated with the customer
     */
    private Address address;

    /**
     * A representation of the Customer's address to display to the user
     */
    private StringProperty addressString = new SimpleStringProperty();

    /**
     * The Customer's phone number
     */
    private StringProperty phone = new SimpleStringProperty();


//    public Customer(String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby, long customerId, String customerName, long addressId, int active, Address address) {
//        this(customerId, customerName, addressId, active, createdBy, createDate, lastUpdate, lastUpdateby);
//        this.address = address;
//    }

    /**
     * A constructor for creating new customers that have not already been saved to the database
     * @param customerName
     * @param addressId
     * @param active
     */
    public Customer(String customerName, long addressId, int active) {
        this.setCustomerName(customerName);
        this.setAddressId(addressId);
        this.setActive(active);
        this.checkAndSetCreate();
        this.setUpdate();
    }

    /**
     * A constructor for customers that already exist in the database.
     * @param customerId
     * @param customerName
     * @param addressId
     * @param active
     * @param createdBy
     * @param createDate
     * @param lastUpdate
     * @param lastUpdateby
     */
    public Customer(long customerId, String customerName, long addressId, int active, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.setCustomerId(customerId);
        this.setCustomerName(customerName);
        this.setAddressId(addressId);
        this.setActive(active);
    }

    /**
     * Lookup and return a list of all customers with records in the database
     * @return  A list of customers
     */
    public static List<Customer> findAll() {
        String sql ="SELECT customerId, customerName, cu.addressId, active, cu.createDate as customerCreateDate,\n" +
                "  cu.createdBy as customerCreatedBy, cu.lastUpdate as customerLastUpdate, cu.lastUpdateBy  as customerLastUpdateBy,\n" +
                "  address, address2, city, a.cityId, postalCode, phone, country, c.countryId, a.createDate as addressCreateDate, a.createdBy as addressCreatedBy,\n" +
                "  a.lastUpdate as addressLastUpdate, a.lastUpdateBy as addressLastUpdateBy\n" +
                "FROM customer cu\n" +
                "JOIN address a\n" +
                "ON cu.addressId = a.addressId\n" +
                "JOIN city c\n" +
                "ON a.cityId = c.cityId\n" +
                "JOIN country co\n" +
                "ON c.countryId = co.countryId\n" +
                "WHERE cu.active = 1 \n" +
                "ORDER BY customerId;";
        List<Customer> list = new ArrayList<>();
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);){

            while (rs.next()) {
                list.add(buildCustomerFromDB(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Helper method to create a Customer instance when retrieving a record from the database
     * @param resultSet a row from a database query
     * @return a Customer object
     * @throws SQLException
     */
    private static Customer buildCustomerFromDB(ResultSet resultSet) throws SQLException {
        ZoneId zone = ZoneId.systemDefault();
        AddressBuilder addressBuilder = new AddressBuilder();
        addressBuilder.setAddress(resultSet.getString("address"))
                .setAddress2(resultSet.getString("address2"))
                .setAddressId(resultSet.getLong("addressId"))
                .setCityId(resultSet.getLong("cityId"))
                .setCity(resultSet.getString("city"))
                .setPostalCode(resultSet.getString("postalCode"))
                .setCountry(resultSet.getString("country"))
                .setCountryId(resultSet.getLong("countryId"))
                .setPhone(resultSet.getString("phone"))
                .setCreatedBy(resultSet.getString("addressCreatedBy"))
                .setLastUpdateBy(resultSet.getString("addressLastUpdateBy"))
                .setLastUpdate(resultSet.getTimestamp("addressLastUpdate").toInstant())
                .setCreateDate(ZonedDateTime.ofInstant(resultSet.getTimestamp("addressCreateDate").toInstant(), zone));

        Address address = addressBuilder.build();
        String addressString = buildAddressString(address);

        CustomerBuilder customerBuilder = new CustomerBuilder();
        customerBuilder.setCustomerId(resultSet.getLong("customerId")).setCustomerName(resultSet.getString("customerName"))
                .setAddressId(resultSet.getLong("addressId")).setActive(resultSet.getInt("active"))
                .setCreatedBy(resultSet.getString("customerCreatedBy"))
                .setLastUpdateBy(resultSet.getString("customerLastUpdateBy"))
                .setLastUpdate(resultSet.getTimestamp("customerLastUpdate").toInstant())
                .setCreateDate(ZonedDateTime.ofInstant(resultSet.getTimestamp("customerCreateDate").toInstant(), zone));
        Customer customer = customerBuilder.build();
        customer.setAddress(address);
        customer.setPhone(resultSet.getString("phone"));
        customer.setAddressString(addressString);
        return customer;
    }

    /**
     * Update an Customer record that already exists in the database;
     *
     * @return this
     */

    public Customer update() throws Exception{

        if (this.customerId.get() == 0) {
            return this.save();
        }

        String sql = "UPDATE customer SET customerName = ?, addressId = ?, active = ?, lastUpdateBy = ? WHERE customerId = ?;";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, this.getCustomerName());
            stmt.setLong(2, this.getAddressId());
            stmt.setInt(3, this.getActive());
            stmt.setString(4, this.getLastUpdateby());
            stmt.setLong(5, this.getCustomerId());

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new Exception("The customer record didn't update");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Save a new customer record to the database
     *
     * @return
     */

    public Customer save() throws Exception {

        if (this.customerId.get() > 0) {
            return this.update();
        }

        String sql = "INSERT INTO customer (customerId, customerName, addressId, active, createdBy, createDate, lastUpdate, lastUpdateBy) " +
                "VALUES (?,?,?,?,?,?,?,?);";
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {

                conn.setAutoCommit(false);
                Savepoint savepoint1 = conn.setSavepoint();
                try {
                    this.setCustomerId(this.getNextId());

                    stmt.setLong(1, this.getCustomerId());
                    stmt.setString(2, this.getCustomerName());
                    stmt.setLong(3, this.getAddressId());
                    stmt.setInt(4, this.getActive());
                    stmt.setString(5, super.getCreatedBy());
                    stmt.setTimestamp(6, Timestamp.valueOf(this.getCreateDate().toLocalDateTime()));//datetime
                    final ZonedDateTime updateTime = this.getLastUpdate().atZone(ZoneId.of("UTC"));
                    stmt.setTimestamp(7, Timestamp.valueOf(updateTime.toLocalDateTime())); //timestamp
                    stmt.setString(8, this.getLastUpdateby());

                    stmt.executeUpdate();
                    conn.commit();

                } catch (SQLException e) {
                    e.printStackTrace();
                    conn.rollback(savepoint1);
                }


        } catch(SQLException e){
            e.printStackTrace();

        }

        return this;
    }

    /**
     * Create a string representation of the Customer's address specifically for display to the application user
     * @param address
     * @return
     */
    public static String buildAddressString(Address address) {
        String addressString = "";
        addressString += address.getAddress() + "\n";
        if(!address.getAddress2().equals("")) { addressString += address.getAddress2() + "\n"; }
        addressString += address.getCity() + " " + address.getPostalCode() + " " + address.getCountry() + "\n";
        return addressString;
    }

    /**
     * Getters and Setters
     */

    public long getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(long customerId) {
        this.customerId.set(customerId);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public long getAddressId() {
        return addressId.get();
    }

    public LongProperty addressIdProperty() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId.set(addressId);
    }

    public int getActive() {
        return active.get();
    }

    public boolean isActive() {
        boolean custActive = false;
        if (this.active.get() == 1) { custActive = true; }
        return custActive;
    }

    public IntegerProperty activeProperty() {
        return active;
    }

    public void setActive(int active) {
        this.active.set(active);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddressString(String addressString) {
        this.addressString.set(addressString);
    }

    public String getAddressString() {
        return addressString.get();
    }

    public StringProperty addressStringProperty() {
        return addressString;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public static String findNameById(long customerId) {
        String name = "";
        String sql = "SELECT customerName FROM customer WHERE customerId = ? LIMIT 1 ";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, customerId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("customerName");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}
