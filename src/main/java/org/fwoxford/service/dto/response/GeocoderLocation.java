package org.fwoxford.service.dto.response;

import java.math.BigDecimal;

/**
 * Created by gengluying on 2017/7/20.
 */
public class GeocoderLocation {
    private BigDecimal lng;
    private BigDecimal lat;

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }
}
