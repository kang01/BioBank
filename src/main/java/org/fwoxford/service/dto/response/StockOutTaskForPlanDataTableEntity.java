package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;
import java.lang.ref.SoftReference;
import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutTaskForPlanDataTableEntity {
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String stockOutTaskCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long countOfFrozenBox;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long countOfSample;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private LocalDate createDate;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private LocalDate stockOutDate;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String operators;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String status;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }

    public Long getCountOfFrozenBox() {
        return countOfFrozenBox;
    }

    public void setCountOfFrozenBox(Long countOfFrozenBox) {
        this.countOfFrozenBox = countOfFrozenBox;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getStockOutDate() {
        return stockOutDate;
    }

    public void setStockOutDate(LocalDate stockOutDate) {
        this.stockOutDate = stockOutDate;
    }

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
