package org.fwoxford.service.dto;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInTubeDTO {
    private Long frozenTubeId;
    private String frozenTubeCode;
    private String status;
    private String tubeColumns;
    private String tubeRows;
    private Integer isModifyState;
    private Integer isModifyPosition;
    private Long frozenBoxId;
    private String frozenBoxCode;
    private Long sampleTypeId;
    private Long frozenTubeTypeId;

    @Override
    public String toString() {
        return "StockInTubeDTO{" +
            "frozenTubeId=" + frozenTubeId +
            ", frozenTubeCode='" + frozenTubeCode + '\'' +
            ", status='" + status + '\'' +
            ", tubeColumns='" + tubeColumns + '\'' +
            ", tubeRows='" + tubeRows + '\'' +
            ", isModifyState=" + isModifyState +
            ", isModifyPosition=" + isModifyPosition +
            ", frozenBoxId=" + frozenBoxId +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            ", sampleTypeId=" + sampleTypeId +
            ", frozenTubeTypeId=" + frozenTubeTypeId +
            '}';
    }

    public Long getFrozenTubeId() {
        return frozenTubeId;
    }

    public void setFrozenTubeId(Long frozenTubeId) {
        this.frozenTubeId = frozenTubeId;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public Integer getIsModifyState() {
        return isModifyState;
    }

    public void setIsModifyState(Integer isModifyState) {
        this.isModifyState = isModifyState;
    }

    public Integer getIsModifyPosition() {
        return isModifyPosition;
    }

    public void setIsModifyPosition(Integer isModifyPosition) {
        this.isModifyPosition = isModifyPosition;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public void setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
    }
}
