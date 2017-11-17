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

    private LongProperty countryId = new SimpleLongProperty();

    private StringProperty country = new SimpleStringProperty();


    /**
     *
     */
    private StringProperty phone = new SimpleStringProperty();

    public Address(){
        super();

    }

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

    public Address(String address, String address2, long cityId, String postalCode, String phone) {
        super();
        this.address.set(address);
        this.address2.set(address2);
        this.cityId.set(cityId);
        this.postalCode.set(postalCode);
        this.phone.set(phone);
        super.checkAndSetCreate();
        super.checkAndSetUpdate();
//        System.out.println("hellox: " + createDate + createdBy + lastUpdate +lastUpdateby);
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
//                System.out.println(field.getName());
            }
            while (resultSet.next()){
//                System.out.println(resultSet.getString(2));
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
     * @return this
     */

    public Address update() throws Exception {

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
     * method to help save changes the entity to the database.
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
//                System.out.println(super.getCreateDate());
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
            //            System.out.println(sql);
            e.printStackTrace();

        }

        return this;
    }

    /**
     * method to delete the entities record from the database.
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
