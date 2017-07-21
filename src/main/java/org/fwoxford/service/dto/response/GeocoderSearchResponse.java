package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/7/20.
 */
public class GeocoderSearchResponse {
    private String status;
    private GeocoderSearchResult result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeocoderSearchResult getResult() {
        return result;
    }

    public void setResult(GeocoderSearchResult result) {
        this.result = result;
    }
}
