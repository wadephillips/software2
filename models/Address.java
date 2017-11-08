package calendar.models;

import calendar.ModelDAO;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by wadelp on 10/17/17.
 */
public class Address extends Model {

    /**
     *
     */
    private LongProperty addressId = new SimpleLongProperty();

    /**
     *
     */
    private StringProperty address = new SimpleStringProperty();

    /**
     *
     */
    private StringProperty address2 = new SimpleStringProperty();

    /**
     *
     */
    private LongProperty cityId = new SimpleLongProperty();

    /**
     *
     */
    private StringProperty  city = new SimpleStringProperty();

    /**
     *
     */
    private StringProperty postalCode = new SimpleStringProperty();

    private StringProperty country = new SimpleStringProperty();


    /**
     *
     */
    private StringProperty phone = new SimpleStringProperty();

    public Address(){
        super();

    }

    public Address(long addressId, String address, String address2, long cityId, String postalCode, String phone, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby, String city, String country) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.setAddressId(addressId);
        this.setAddress(address);
        this.setAddress2(address2);
        this.setCityId(cityId);
        this.setPostalCode(postalCode);
        this.setPhone(phone);
        this.setCity(city);
        this.setCountry(country);
    }

    public Address(StringProperty address, StringProperty address2, LongProperty cityId, StringProperty postalCode, StringProperty phone) {
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.checkAndSetCreate();
        this.checkAndSetUpdate();
    }

    public Address(String address, String address2, long cityId, String postalCode, String phone) {
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
        String sql = "SELECT * FROM address WHERE addressId = " + id + ";";
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery(sql)){
            System.out.println();
            for (Field field : this.getFields()){
                System.out.println(field.getName());
            }
            while (resultSet.next()){
                System.out.println(resultSet.getString(2));
            }

        } catch (SQLException e) {
            System.out.println(sql);
            e.printStackTrace();
        }

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

    public Address save() {

        String sql = "INSERT INTO address (addressId, address, address2, cityId, postalCode, phone, createdBy, createDate, lastUpdate, lastUpdateBy) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?);";
        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {

            conn.setAutoCommit(false);
            Savepoint savepoint1 = conn.setSavepoint();
            try {
                this.setAddressId((this.getNextId());

                stmt.setLong(1, this.getAddressId());
                stmt.setString(2, this.getAddress());
                stmt.setString(3, this.getAddress2());
                stmt.setLong(4, this.getCityId());
                stmt.setString(5, this.getPostalCode());
                stmt.setString(6, this.getPhone());
                stmt.setString(7, super.getCreatedBy());
                stmt.setTimestamp(8, Timestamp.from(this.getCreateDate().toInstant()));//datetime
                stmt.setTimestamp(9, Timestamp.from(this.getLastUpdate())); //timestamp
                stmt.setString(10, this.getLastUpdateby());

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
