package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/7/20.
 */
public class GeocoderSearchResult {
    private String level;
    private String confidence;
    private String precise;
    private GeocoderLocation location;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getPrecise() {
        return precise;
    }

    public void setPrecise(String precise) {
        this.precise = precise;
    }

    public GeocoderLocation getLocation() {
        return location;
    }

    public void setLocation(GeocoderLocation location) {
        this.location = location;
    }
}
