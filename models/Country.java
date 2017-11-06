package calendar.models;

import calendar.ModelDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by wadelp on 10/17/17.
 */
public class Country extends Model {

    private long countryId;

    private String country;

    public Country(){
        super();

    }

    public Country(long countryId, String country, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.countryId = countryId;
        this.country = country;
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

    public static ArrayList<Country> findAll() {
        ZoneId zone = ZoneId.systemDefault();
        String sql = "SELECT * FROM country;";
        ArrayList<Country> countries = new ArrayList<>();
        try(Connection conn = DATASOURCE.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);){

            while (resultSet.next()){
                Country country = new Country(resultSet.getLong("countryId"),
                        resultSet.getString("country"),
                        resultSet.getString("createdBy"),
                        ZonedDateTime.ofInstant(resultSet.getTimestamp("createDate").toInstant(), zone),
                        resultSet.getTimestamp("lastUpdate").toInstant(),
                        resultSet.getString("lastUpdateBy")
                );
                countries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
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
