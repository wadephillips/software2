package calendar.models;


import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Creates an object to represent a city in the application.  This model also handles interactions with the city database table.
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

    /**
     * The constructor for updating a city that already exists in the database
     * @param cityId
     * @param city
     * @param countryId
     * @param createdBy
     * @param createDate
     * @param lastUpdate
     * @param lastUpdateby
     */
    public City(long cityId, String city, long countryId, String createdBy, ZonedDateTime createDate, Instant lastUpdate, String lastUpdateby) {
        super(createdBy, createDate, lastUpdate, lastUpdateby);
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
    }


    /**
     * Lookup a specific city in the database by id
     *
     * @param id The id of the city to be found.
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
     * Retrieve all city records from the database
     *
     * @return a list of Cities
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
     * Get the id country country where a known city is located.
     * @param cityId the id of the city.
     * @return the id of the city's country.
     */
    public static long lookupCountryId(long cityId) {
        long countryId = 0;
        String sql = "SELECT countryId FROM city WHERE cityId = ?";

        try(Connection conn = DATASOURCE.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, cityId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.first()){
                countryId = resultSet.getLong("countryId");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryId;
    }

    /**
     * Getters
     */

    public long getCityId() {
        return cityId;
    }

    public String getCity() {
        return city;
    }

    /**
     * Returns the city's name
     *
     * @return the city's name.
     */
    @Override
    public String toString() {
        return this.city;
    }
}
