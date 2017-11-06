package calendar.models;

import calendar.ModelDAO;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private StringProperty postalCode = new SimpleStringProperty();

    /**
     *
     */
    private StringProperty phone = new SimpleStringProperty();

    public Address(){
        super();

    }

    public Address(long addressId, String address, String address2, long cityId, String postalCode, String phone, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.setAddressId(addressId);
        this.setAddress(address);
        this.setAddress2(address2);
        this.setCityId(cityId);
        this.setPostalCode(postalCode);
        this.setPhone(phone);
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

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
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