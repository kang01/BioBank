package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutHandoverDetails entity.
 */
public class StockOutHandoverDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long stockOutHandoverId;

    private Long stockOutFrozenTubeId;

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

    public Long getStockOutHandoverId() {
        return stockOutHandoverId;
    }

    public void setStockOutHandoverId(Long stockOutHandoverId) {
        this.stockOutHandoverId = stockOutHandoverId;
    }

    public Long getStockOutFrozenTubeId() {
        return stockOutFrozenTubeId;
    }

    public void setStockOutFrozenTubeId(Long stockOutFrozenTubeId) {
        this.stockOutFrozenTubeId = stockOutFrozenTubeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO = (StockOutHandoverDetailsDTO) o;

        if ( ! Objects.equals(id, stockOutHandoverDetailsDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutHandoverDetailsDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}