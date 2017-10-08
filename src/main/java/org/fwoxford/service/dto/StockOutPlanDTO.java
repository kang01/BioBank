package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutPlan entity.
 */
public class StockOutPlanDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String stockOutPlanCode;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    private String applyNumber;

    private Long stockOutApplyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStockOutPlanCode() {
        return stockOutPlanCode;
    }

    public void setStockOutPlanCode(String stockOutPlanCode) {
        this.stockOutPlanCode = stockOutPlanCode;
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
    public String getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }

    public Long getStockOutApplyId() {
        return stockOutApplyId;
    }

    public void setStockOutApplyId(Long stockOutApplyId) {
        this.stockOutApplyId = stockOutApplyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutPlanDTO stockOutPlanDTO = (StockOutPlanDTO) o;

        if ( ! Objects.equals(id, stockOutPlanDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutPlanDTO{" +
            "id=" + id +
            ", stockOutPlanCode='" + stockOutPlanCode + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", applyNumber='" + applyNumber + "'" +
            '}';
    }
    private LocalDate stockOutPlanDate;

    public LocalDate getStockOutPlanDate() {
        return stockOutPlanDate;
    }

    public void setStockOutPlanDate(LocalDate stockOutPlanDate) {
        this.stockOutPlanDate = stockOutPlanDate;
    }
}
