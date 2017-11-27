package calendar.models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Creates an object to represent an Address in the application.  This model also handles interactions with the address
 * database table
 */
public class Address extends Model {

    /**
     * The id of this address
     */
    private LongProperty addressId = new SimpleLongProperty();

    /**
     * The primary street address field
     */
    private StringProperty address = new SimpleStringProperty();

    /**
     * The secondary street address field
     */
    private StringProperty address2 = new SimpleStringProperty();

    /**
     * The id of the City where this address is located.  A foreign key on the city table
     */
    private LongProperty cityId = new SimpleLongProperty();

    /**
     * The name of the city
     */
    private StringProperty  city = new SimpleStringProperty();

    /**
     * The address' postal code
     */
    private StringProperty postalCode = new SimpleStringProperty();

    /**
     * The id of the country where this address is located.
     */
    private LongProperty countryId = new SimpleLongProperty();

    /**
     * The name of the country where the address is located
     */
    private StringProperty country = new SimpleStringProperty();


    /**
     * The phone number associated with this address
     */
    private StringProperty phone = new SimpleStringProperty();

    /**
     * A constructor
     */
    public Address(){
        super();
    }

    /**
     * A constructor for creating new addresses that don't already exist in the database
     * @param address
     * @param address2
     * @param cityId
     * @param postalCode
     * @param phone
     */
    public Address(String address, String address2, long cityId, String postalCode, String phone) {
        super();
        this.address.set(address);
        this.address2.set(address2);
        this.cityId.set(cityId);
        this.postalCode.set(postalCode);
        this.phone.set(phone);
        super.checkAndSetCreate();
        super.setUpdate();
    }

    /**
     * A constructor for addresses that already exist in the database.
     * @param addressId
     * @param address
     * @param address2
     * @param cityId
     * @param postalCode
     * @param phone
     * @param createdBy
     * @param createDate
     * @param lastUpdate
     * @param lastUpdateby
     * @param city
     * @param country
     * @param countryId
     */
    public Address(long addressId, String address, String address2, long cityId, String postalCode, String phone, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby, String city, String country, long countryId) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.setAddressId(addressId);
        this.setAddress(address);
        this.setAddress2(address2);
        this.setCityId(cityId);
        this.setPostalCode(postalCode);
        this.setPhone(phone);
        this.setCity(city);
        this.setCountry(country);
        this.setCountryId(countryId);
    }


    /**
     * Lookup a specific address in the database by id
     *
     * @param id The id of the address to found
     * @return
     */

    public Address find(int id) {
        String sql = "SELECT * FROM address WHERE addressId = " + id + ";";
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery(sql)){
            for (Field field : this.getFields()){
            }
            while (resultSet.next()){
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *  Updates the address in the database
     *
     * @return this
     */

    public Address update() throws Exception {

        if (this.addressId.get() == 0) {
            return this.save();
        }

        String sql = "UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdateBy = ? " +
                "WHERE addressId = ? ;";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, this.getAddress());
            stmt.setString(2, this.getAddress2());
            stmt.setLong(3, this.getCityId());
            stmt.setString(4, this.getPostalCode());
            stmt.setString(5, this.getPhone());
            stmt.setString(6, super.getLastUpdateby());
            stmt.setLong(7, this.getAddressId());

            int result = stmt.executeUpdate();

            if (result == 0) {
                throw new Exception("The database was unable to update the address");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Inserts a new address record in the database
     *
     * @return
     */

    public Address save() throws Exception {

        if (this.addressId.get() > 0) {
            return this.update();
        }

        String sql = "INSERT INTO address (addressId, address, address2, cityId, postalCode, phone, createdBy, createDate, lastUpdate, lastUpdateBy) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?);";
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {

            conn.setAutoCommit(false);
            Savepoint savepoint1 = conn.setSavepoint();
            try {
                this.setAddressId(this.getNextId());

                stmt.setLong(1, this.getAddressId());
                stmt.setString(2, this.getAddress());
                stmt.setString(3, this.getAddress2());
                stmt.setLong(4, this.getCityId());
                stmt.setString(5, this.getPostalCode());
                stmt.setString(6, this.getPhone());
                stmt.setString(7, super.getCreatedBy());
                stmt.setTimestamp(8, Timestamp.from(super.getCreateDate().toInstant()));//datetime
                stmt.setTimestamp(9, Timestamp.from(super.getLastUpdate())); //timestamp
                stmt.setString(10, super.getLastUpdateby());

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
     * Getters and Setters
     */
    public long getAddressId() {
        return addressId.get();
    }

    public LongProperty addressIdProperty() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId.set(addressId);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getAddress2() {
        return address2.get();
    }

    public StringProperty address2Property() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }

    public long getCityId() {
        return cityId.get();
    }

    public LongProperty cityIdProperty() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId.set(cityId);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public void setCountryId(long countryId) {
        this.countryId.set(countryId);
    }

    public long getCountryId() { return this.countryId.get(); }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }


}
