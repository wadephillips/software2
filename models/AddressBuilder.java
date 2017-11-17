package calendar.models;

public class AddressBuilder extends ModelBuilder {

    /**
     *
     */
    private long addressId;

    /**
     *
     */
    private String address;

    /**
     *
     */
    private String address2;

    /**
     *
     */
    private long cityId;

    private String city;

    private long countryId;

    private String country;

    /**
     *
     */
    private String postalCode;

    /**
     *
     */
    private String phone;

    public Address build(){

        return new Address(this.addressId, this.address, this.address2, this.cityId, this.postalCode, this.phone, this.createdBy, this.createDate, this.lastUpdate, this.lastUpdateBy, this.city, this.country, this.countryId);
    }

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
