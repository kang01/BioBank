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

    @Size(max = 100)
    private String sampleTempCode;

    private Long frozenTubeId;

    private Long frozenBoxPositionId;

    private Long transhipBoxId;

    private Long stockInBoxId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
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

    public Long getTranshipBoxId() {
        return transhipBoxId;
    }

    public void setTranshipBoxId(Long transhipBoxId) {
        this.transhipBoxId = transhipBoxId;
    }

    public Long getStockInBoxId() {
        return stockInBoxId;
    }

    public void setStockInBoxId(Long stockInBoxId) {
        this.stockInBoxId = stockInBoxId;
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
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", rowsInTube='" + rowsInTube + "'" +
            ", columnsInTube='" + columnsInTube + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            '}';
    }
}
