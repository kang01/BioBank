package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/31.
 */
public class StockOutHandoverDataTableEntity {
    /**
     * 交接ID
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String handoverCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String stockOutTaskCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String purposeOfSample;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private LocalDate handoverTime;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String handoverPerson;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long countOfSample;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandoverCode() {
        return handoverCode;
    }

    public void setHandoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
    }

    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }

    public String getPurposeOfSample() {
        return purposeOfSample;
    }

    public void setPurposeOfSample(String purposeOfSample) {
        this.purposeOfSample = purposeOfSample;
    }

    public LocalDate getHandoverTime() {
        return handoverTime;
    }

    public void setHandoverTime(LocalDate handoverTime) {
        this.handoverTime = handoverTime;
    }

    public String getHandoverPerson() {
        return handoverPerson;
    }

    public void setHandoverPerson(String handoverPerson) {
        this.handoverPerson = handoverPerson;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
