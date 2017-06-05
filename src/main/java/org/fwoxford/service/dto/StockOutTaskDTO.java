package org.fwoxford.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutTask entity.
 */
public class StockOutTaskDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Long stockOutHeadId1;

    private Long stockOutHeadId2;

    @NotNull
    private LocalDate stockOutDate;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    private String stockOutTaskCode;

    @NotNull
    private Integer usedTime;

    private Long stockOutPlanId;

    private String stockOutPlanCode;

    private String stockOutHeader1;

    private String stockOutHeader2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getStockOutHeadId1() {
        return stockOutHeadId1;
    }

    public void setStockOutHeadId1(Long stockOutHeadId1) {
        this.stockOutHeadId1 = stockOutHeadId1;
    }
    public Long getStockOutHeadId2() {
        return stockOutHeadId2;
    }

    public void setStockOutHeadId2(Long stockOutHeadId2) {
        this.stockOutHeadId2 = stockOutHeadId2;
    }
    public LocalDate getStockOutDate() {
        return stockOutDate;
    }

    public void setStockOutDate(LocalDate stockOutDate) {
        this.stockOutDate = stockOutDate;
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
    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }
    public Integer getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public Long getStockOutPlanId() {
        return stockOutPlanId;
    }

    public void setStockOutPlanId(Long stockOutPlanId) {
        this.stockOutPlanId = stockOutPlanId;
    }

    public String getStockOutPlanCode() {
        return stockOutPlanCode;
    }

    public void setStockOutPlanCode(String stockOutPlanCode) {
        this.stockOutPlanCode = stockOutPlanCode;
    }

    public String getStockOutHeader1() {
        return stockOutHeader1;
    }

    public void setStockOutHeader1(String stockOutHeader1) {
        this.stockOutHeader1 = stockOutHeader1;
    }

    public String getStockOutHeader2() {
        return stockOutHeader2;
    }

    public void setStockOutHeader2(String stockOutHeader2) {
        this.stockOutHeader2 = stockOutHeader2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutTaskDTO stockOutTaskDTO = (StockOutTaskDTO) o;

        if ( ! Objects.equals(id, stockOutTaskDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutTaskDTO{" +
            "id=" + id +
            ", stockOutHeadId1='" + stockOutHeadId1 + "'" +
            ", stockOutHeadId2='" + stockOutHeadId2 + "'" +
            ", stockOutDate='" + stockOutDate + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", stockOutTaskCode='" + stockOutTaskCode + "'" +
            ", usedTime='" + usedTime + "'" +
            ", stockOutPlanCode='" + stockOutPlanCode + "'" +
            ", stockOutHeader2='" + stockOutHeader2 + "'" +
            ", stockOutHeader1='" + stockOutHeader1 + "'" +
            '}';
    }
}
