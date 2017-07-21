package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/7/20.
 */
public class GeocoderSearchAddressResult {
    private String formatted_address;
    private String business;
    private String cityCode;
    private GeocoderLocation location;
    private AddressComponent addressComponent;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public GeocoderLocation getLocation() {
        return location;
    }

    public void setLocation(GeocoderLocation location) {
        this.location = location;
    }

    public AddressComponent getAddressComponent() {
        return addressComponent;
    }

    public void setAddressComponent(AddressComponent addressComponent) {
        this.addressComponent = addressComponent;
    }
}
