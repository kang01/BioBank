package org.fwoxford.service.dto.response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuyu on 2017/5/15.
 */
public class StockOutRequirementReportDTO {

    private Long id;

    private String requirementName;

    private Integer countOfSample;
    private Integer countOfStockOutSample;

    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public Integer getCountOfStockOutSample() {
        return countOfStockOutSample;
    }

    public void setCountOfStockOutSample(Integer countOfStockOutSample) {
        this.countOfStockOutSample = countOfStockOutSample;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Map<String, String> getErrorSamples() {
        return errorSamples;
    }

    public void setErrorSamples(Map<String, String> errorSamples) {
        this.errorSamples = errorSamples;
    }

    private Map<String, String> errorSamples;
}
