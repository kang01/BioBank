package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/7/20.
 */
public class GeocoderSearchAddressResponse {
    private String status;
    private GeocoderSearchAddressResult result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeocoderSearchAddressResult getResult() {
        return result;
    }

    public void setResult(GeocoderSearchAddressResult result) {
        this.result = result;
    }
}
