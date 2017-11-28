package calendar.models;

/**
 * A builder class for instantiating an Address
 */
public class AddressBuilder extends ModelBuilder {

    /**
     * The address id
     */
    private long addressId;

    /**
     * The primary street address field
     */
    private String address;

    /**
     * The secondary street address field
     */
    private String address2;

    /**
     * The id of the City where this address is located.  A foreign key on the city table
     */
    private long cityId;

    /**
     * The name of the city
     */
    private String city;

    /**
     * The address' postal code
     */
    private String postalCode;

    /**
     * The id of the country where this address is located.
     */
    private long countryId;

    /**
     * The name of the country where the address is located
     */
    private String country;


    /**
     * The phone number associated with this address
     */
    private String phone;

    /**
     * Instantiates and returns a new Address object
     * @return
     */
    public Address build(){

        return new Address(this.addressId, this.address, this.address2, this.cityId, this.postalCode, this.phone, this.createdBy, this.createDate, this.lastUpdate, this.lastUpdateBy, this.city, this.country, this.countryId);
    }

    /**
     * Setters
     */

    public AddressBuilder setAddressId(long addressId) {
        this.addressId = addressId;
        return this;
    }

    public AddressBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public AddressBuilder setAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public AddressBuilder setCityId(long cityId) {
        this.cityId = cityId;
        return this;
    }

    public AddressBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public AddressBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public AddressBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public AddressBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    public AddressBuilder setCountryId(long countryId) {
        this.countryId = countryId;
        return this;
    }
}
