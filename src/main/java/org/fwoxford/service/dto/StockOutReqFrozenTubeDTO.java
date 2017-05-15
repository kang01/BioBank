package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutReqFrozenTube entity.
 */
public class StockOutReqFrozenTubeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    private String tubeColumns;

    private Long frozenBoxId;

    private Long frozenTubeId;

    private Long stockOutRequirementId;

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
    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }
    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public Long getFrozenTubeId() {
        return frozenTubeId;
    }

    public void setFrozenTubeId(Long frozenTubeId) {
        this.frozenTubeId = frozenTubeId;
    }

    public Long getStockOutRequirementId() {
        return stockOutRequirementId;
    }

    public void setStockOutRequirementId(Long stockOutRequirementId) {
        this.stockOutRequirementId = stockOutRequirementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = (StockOutReqFrozenTubeDTO) o;

        if ( ! Objects.equals(id, stockOutReqFrozenTubeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutReqFrozenTubeDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            '}';
    }
}
