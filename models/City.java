package calendar.models;


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


    public City(){
        super();

    }
}
