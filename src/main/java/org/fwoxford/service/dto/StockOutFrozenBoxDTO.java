package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutFrozenBox entity.
 */
public class StockOutFrozenBoxDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long frozenBoxId;

    private Long stockOutBoxPositionId;

    private Long stockOutTaskId;

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

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public Long getStockOutBoxPositionId() {
        return stockOutBoxPositionId;
    }

    public void setStockOutBoxPositionId(Long stockOutBoxPositionId) {
        this.stockOutBoxPositionId = stockOutBoxPositionId;
    }

    public Long getStockOutTaskId() {
        return stockOutTaskId;
    }

    public void setStockOutTaskId(Long stockOutTaskId) {
        this.stockOutTaskId = stockOutTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = (StockOutFrozenBoxDTO) o;

        if ( ! Objects.equals(id, stockOutFrozenBoxDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutFrozenBoxDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
