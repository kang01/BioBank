package org.fwoxford.service.dto.response;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by gengluying on 2017/8/30.
 */
public class SampleReportForStockInAndOut {
    private String stockInDate;
    private Long countOfSample;

    public String getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(String stockInDate) {
        this.stockInDate = stockInDate;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }
}
