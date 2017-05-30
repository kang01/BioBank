package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutBoxTube entity.
 */
public class StockOutBoxTubeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long stockOutFrozenBoxId;

    private Long frozenTubeId;

    private Long stockOutTaskFrozenTubeId;

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

    public Long getStockOutFrozenBoxId() {
        return stockOutFrozenBoxId;
    }

    public void setStockOutFrozenBoxId(Long stockOutFrozenBoxId) {
        this.stockOutFrozenBoxId = stockOutFrozenBoxId;
    }

    public Long getFrozenTubeId() {
        return frozenTubeId;
    }

    public void setFrozenTubeId(Long frozenTubeId) {
        this.frozenTubeId = frozenTubeId;
    }

    public Long getStockOutTaskFrozenTubeId() {
        return stockOutTaskFrozenTubeId;
    }

    public void setStockOutTaskFrozenTubeId(Long stockOutTaskFrozenTubeId) {
        this.stockOutTaskFrozenTubeId = stockOutTaskFrozenTubeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutBoxTubeDTO stockOutBoxTubeDTO = (StockOutBoxTubeDTO) o;

        if ( ! Objects.equals(id, stockOutBoxTubeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutBoxTubeDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
