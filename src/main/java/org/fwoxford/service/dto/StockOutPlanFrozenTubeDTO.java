package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutPlanFrozenTube entity.
 */
public class StockOutPlanFrozenTubeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long stockOutPlanId;

    private Long stockOutReqFrozenTubeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getStockOutPlanId() {
        return stockOutPlanId;
    }

    public void setStockOutPlanId(Long stockOutPlanId) {
        this.stockOutPlanId = stockOutPlanId;
    }

    public Long getStockOutReqFrozenTubeId() {
        return stockOutReqFrozenTubeId;
    }

    public void setStockOutReqFrozenTubeId(Long stockOutReqFrozenTubeId) {
        this.stockOutReqFrozenTubeId = stockOutReqFrozenTubeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = (StockOutPlanFrozenTubeDTO) o;

        if ( ! Objects.equals(id, stockOutPlanFrozenTubeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutPlanFrozenTubeDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
