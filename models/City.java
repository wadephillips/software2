package calendar.models;


import calendar.ModelDAO;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wadelp on 10/17/17.
 */
public class City extends Model {

    /**
     * The id of the city
     */
    private long cityId;

    /**
     * The name of the city
     */
    private String city;

    /**
     * The id of the Country that the City belongs to
     */
    private long countryId;


    public City(long cityId, String city, long countryId, String createdBy, Timestamp createDate, Timestamp lastUpdate, String lastUpdateBy){
        super();

    }

    public City(long cityId, String city, long countryId, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
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

    public static City find(int id) {
        String sql = "SELECT * FROM city WHERE cityId = ? ";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.first()){

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.city;
    }

    /**
     * method to retrieve all instances of the entity from the database
     */

    public static ArrayList<City> findAll() {
        ZoneId zone = ZoneId.systemDefault();
        String sql = "SELECT * FROM city;";
        ArrayList<City> cities = new ArrayList<>();
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);){

            while (resultSet.next()){
                    City city = new City(resultSet.getLong("cityId"),
                            resultSet.getString("city"),
                            resultSet.getLong("countryId"),
                            resultSet.getString("createdBy"),
                            ZonedDateTime.ofInstant(resultSet.getTimestamp("createDate").toInstant(), zone),
                            resultSet.getTimestamp("lastUpdate").toInstant(),
                            resultSet.getString("lastUpdateBy")
                    );
                    cities.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
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

    public long getCityId() {
        return cityId;
    }

    public String getCity() {
        return city;
    }
}
