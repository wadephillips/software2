package calendar.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Creates an object to represent a country in the application.  This model also handles interactions with the country database table.
 */
public class Country extends Model {

    /**
     * The country's id
     */
    private long countryId;

    /**
     * The two character country code
     */
    private String country;

    /**
     * A constructor for creating a Country object based on an entry from the country table
     * @param countryId
     * @param country
     * @param createdBy
     * @param createDate
     * @param lastUpdate
     * @param lastUpdateby
     */
    public Country(long countryId, String country, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.countryId = countryId;
        this.country = country;
    }


    /**
     * Retrive a string of country's name
     *
     * @return a string representation of the country.
     */
    @Override
    public String toString() {
        return this.country;
    }

    /**
     * Lookup and retrieve a list containing all countries with a record in the database.
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
     * Getters and Setters
     */

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
