package calendar.models;

import calendar.ModelDAO;
import javafx.beans.property.*;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wadelp on 10/17/17.
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

    private Address address;

    private StringProperty addressString = new SimpleStringProperty();

    private StringProperty phone = new SimpleStringProperty();

    public Customer(){
        super();

    }

    public Customer(String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby, long customerId, String customerName, long addressId, int active, Address address) {
        this(customerId, customerName, addressId, active, createdBy, createDate, lastUpdate, lastUpdateby);
        this.address = address;
    }

    public Customer(long customerId, String customerName, long addressId, int active, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.setCustomerId(customerId);
        this.setCustomerName(customerName);
        this.setAddressId(addressId);
        this.setActive(active);
    }

    public Customer(String customerName, long addressId, int active) {
        this.setCustomerName(customerName);
        this.setAddressId(addressId);
        this.setActive(active);
        this.checkAndSetCreate();
        this.checkAndSetUpdate();
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

    public static List<Customer> findAll() {
        String sql ="SELECT customerId, customerName, cu.addressId, active, cu.createDate as customerCreateDate,\n" +
                "  cu.createdBy as customerCreatedBy, cu.lastUpdate as customerLastUpdate, cu.lastUpdateBy  as customerLastUpdateBy,\n" +
                "  address, address2, city, postalCode, phone, country, a.createDate as addressCreateDate, a.createdBy as addressCreatedBy,\n" +
                "  a.lastUpdate as addressLastUpdate, a.lastUpdateBy as addressLastUpdateBy\n" +
                "FROM customer cu\n" +
                "JOIN address a\n" +
                "ON cu.addressId = a.addressId\n" +
                "JOIN city c\n" +
                "ON a.cityId = c.cityId\n" +
                "JOIN country co\n" +
                "ON c.countryId = co.countryId\n" +
                "ORDER BY customerId;";
        List<Customer> list = new ArrayList<>();
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);){

            while (rs.next()) {
//                System.out.println(rs);
                list.add(buildCustomerFromDB(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Customer buildCustomerFromDB(ResultSet resultSet) throws SQLException {
        ZoneId zone = ZoneId.systemDefault();
        AddressBuilder addressBuilder = new AddressBuilder();
//        System.out.println("builder: " + resultSet.getString("country"));
        addressBuilder.setAddress(resultSet.getString("address"))
                .setAddress2(resultSet.getString("address2"))
                .setAddressId(resultSet.getLong("addressId"))
//                .setCity(resultSet.getLong("cityId"))
                .setCity(resultSet.getString("city"))
                .setPostalCode(resultSet.getString("postalCode"))
                .setCountry(resultSet.getString("country"))
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

    public Customer save() {

        if (this.customerId.get() > 0) {
            //todo throw an exception
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
                    stmt.setTimestamp(6, Timestamp.from(this.getCreateDate().toInstant()));//datetime
                    stmt.setTimestamp(7, Timestamp.from(this.getLastUpdate())); //timestamp
                    stmt.setString(8, this.getLastUpdateby());

                    stmt.executeUpdate();
                    conn.commit();

                } catch (SQLException e) {
                    e.printStackTrace();
                    conn.rollback(savepoint1);
                }


        } catch(SQLException e){
            //            System.out.println(sql);
            e.printStackTrace();

        }

        return this;
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

    public IntegerProperty activeProperty() {
        return active;
    }

    public void setActive(int active) {
        this.active.set(active);
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public static String buildAddressString(Address address) {
        String addressString = "";
        addressString += address.getAddress() + "\n";
        if(!address.getAddress2().equals("")) { addressString += address.getAddress2() + "\n"; }
        System.out.println(address.getCountry());
        addressString += address.getCity() + " " + address.getPostalCode() + " " + address.getCountry() + "\n";
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

}
