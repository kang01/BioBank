package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FrozenTubeRecord entity.
 */
public class FrozenTubeRecordDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String projectCode;

    @NotNull
    @Size(max = 100)
    private String sampleTempCode;

    @NotNull
    @Size(max = 100)
    private String sampleCode;

    @NotNull
    @Size(max = 100)
    private String frozenTypeCode;

    @NotNull
    @Size(max = 255)
    private String frozenTypeName;

    @NotNull
    @Max(value = 20)
    private Integer sampleUsedTimesMost;

    @NotNull
    @Max(value = 20)
    private Integer sampleUsedTimes;

    @NotNull
    @Max(value = 100)
    private Integer frozenTubeVolumn;

    @NotNull
    @Size(max = 20)
    private String frozenTubeVolumnUnit;

    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    private String sampleTypeName;

    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 20)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    private String tubeColumns;

    @NotNull
    @Size(max = 20)
    private String isModifyState;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    @NotNull
    @Size(max = 20)
    private String isModifyPosition;

    private Long sampleTypeId;

    private Long tubeTypeId;

    private Long frozenBoxId;

    private Long frozenTubeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }
    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
    public String getFrozenTypeCode() {
        return frozenTypeCode;
    }

    public void setFrozenTypeCode(String frozenTypeCode) {
        this.frozenTypeCode = frozenTypeCode;
    }
    public String getFrozenTypeName() {
        return frozenTypeName;
    }

    public void setFrozenTypeName(String frozenTypeName) {
        this.frozenTypeName = frozenTypeName;
    }
    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }
    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }
    public Integer getFrozenTubeVolumn() {
        return frozenTubeVolumn;
    }

    public void setFrozenTubeVolumn(Integer frozenTubeVolumn) {
        this.frozenTubeVolumn = frozenTubeVolumn;
    }
    public String getFrozenTubeVolumnUnit() {
        return frozenTubeVolumnUnit;
    }

    public void setFrozenTubeVolumnUnit(String frozenTubeVolumnUnit) {
        this.frozenTubeVolumnUnit = frozenTubeVolumnUnit;
    }
    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }
    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }
    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
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
    public String getIsModifyState() {
        return isModifyState;
    }

    public void setIsModifyState(String isModifyState) {
        this.isModifyState = isModifyState;
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
    public String getIsModifyPosition() {
        return isModifyPosition;
    }

    public void setIsModifyPosition(String isModifyPosition) {
        this.isModifyPosition = isModifyPosition;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public Long getTubeTypeId() {
        return tubeTypeId;
    }

    public void setTubeTypeId(Long frozenTubeTypeId) {
        this.tubeTypeId = frozenTubeTypeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FrozenTubeRecordDTO frozenTubeRecordDTO = (FrozenTubeRecordDTO) o;

        if ( ! Objects.equals(id, frozenTubeRecordDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenTubeRecordDTO{" +
            "id=" + id +
            ", projectCode='" + projectCode + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTypeCode='" + frozenTypeCode + "'" +
            ", frozenTypeName='" + frozenTypeName + "'" +
            ", sampleUsedTimesMost='" + sampleUsedTimesMost + "'" +
            ", sampleUsedTimes='" + sampleUsedTimes + "'" +
            ", frozenTubeVolumn='" + frozenTubeVolumn + "'" +
            ", frozenTubeVolumnUnit='" + frozenTubeVolumnUnit + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", isModifyState='" + isModifyState + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", isModifyPosition='" + isModifyPosition + "'" +
            '}';
    }
}
