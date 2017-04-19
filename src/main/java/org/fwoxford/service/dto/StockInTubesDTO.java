package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockInTubes entity.
 */
public class StockInTubesDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String stockInCode;

    @Size(max = 100)
    private String transhipCode;

    @Size(max = 100)
    private String transhipBatch;

    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;

    @Size(max = 100)
    private String sampleCode;

    @Size(max = 100)
    private String frozenTubeCode;

    @NotNull
    @Size(max = 20)
    private String rowsInTube;

    @NotNull
    @Size(max = 20)
    private String columnsInTube;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private Long stockInId;

    private Long transhipId;

    private Long frozenBoxId;

    private Long frozenTubeId;

    private Long frozenBoxPositionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }
    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }
    public String getTranshipBatch() {
        return transhipBatch;
    }

    public void setTranshipBatch(String transhipBatch) {
        this.transhipBatch = transhipBatch;
    }
    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }
    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }
    public String getRowsInTube() {
        return rowsInTube;
    }

    public void setRowsInTube(String rowsInTube) {
        this.rowsInTube = rowsInTube;
    }
    public String getColumnsInTube() {
        return columnsInTube;
    }

    public void setColumnsInTube(String columnsInTube) {
        this.columnsInTube = columnsInTube;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStockInId() {
        return stockInId;
    }

    public void setStockInId(Long stockInId) {
        this.stockInId = stockInId;
    }

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
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

    public Long getFrozenBoxPositionId() {
        return frozenBoxPositionId;
    }

    public void setFrozenBoxPositionId(Long frozenBoxPositionId) {
        this.frozenBoxPositionId = frozenBoxPositionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockInTubesDTO stockInTubesDTO = (StockInTubesDTO) o;

        if ( ! Objects.equals(id, stockInTubesDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInTubesDTO{" +
            "id=" + id +
            ", stockInCode='" + stockInCode + "'" +
            ", transhipCode='" + transhipCode + "'" +
            ", transhipBatch='" + transhipBatch + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", rowsInTube='" + rowsInTube + "'" +
            ", columnsInTube='" + columnsInTube + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
