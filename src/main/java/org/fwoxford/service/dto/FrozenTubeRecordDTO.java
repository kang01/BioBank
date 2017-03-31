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
    /**
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    private String projectCode;
    /**
     * 样本临时编码
     */
    @NotNull
    @Size(max = 100)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @NotNull
    @Size(max = 100)
    private String sampleCode;
    /**
     * 样本最多使用次数
     */
    @NotNull
    @Max(value = 20)
    private Integer sampleUsedTimesMost;
    /**
     * 样本已使用次数
     */
    @NotNull
    @Max(value = 20)
    private Integer sampleUsedTimes;
    /**
     * 冻存管容量值
     */
    @NotNull
    @Max(value = 100)
    private Integer frozenTubeVolumn;
    /**
     * 冻存管容量单位
     */
    @NotNull
    @Size(max = 20)
    private String frozenTubeVolumnUnit;
    /**
     * 样本类型编码
     */
    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @NotNull
    @Size(max = 255)
    private String sampleTypeName;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 行数
     */
    @NotNull
    @Size(max = 20)
    private String tubeRows;
    /**
     * 列数
     */
    @NotNull
    @Size(max = 20)
    private String tubeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 冻存管类型编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenTubeTypeCode;
    /**
     * 冻存管类型名称
     */
    @NotNull
    @Size(max = 255)
    private String frozenTubeTypeName;
    /**
     * 是否修改状态（1：是，0：否）
     */
    @NotNull
    @Max(value = 20)
    private Integer isModifyState;
    /**
     * 是否修改位置（1：是，0：否）
     */
    @NotNull
    @Max(value = 20)
    private Integer isModifyPosition;
    /**
     * 样本类型Id
     */
    private Long sampleTypeId;
    /**
     * 冻存管类型Id
     */
    private Long tubeTypeId;
    /**
     * 冻存盒类型Id
     */
    private Long frozenBoxId;
    /**
     * 冻存管Id
     */
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
    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }
    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
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
            ", sampleUsedTimesMost='" + sampleUsedTimesMost + "'" +
            ", sampleUsedTimes='" + sampleUsedTimes + "'" +
            ", frozenTubeVolumn='" + frozenTubeVolumn + "'" +
            ", frozenTubeVolumnUnit='" + frozenTubeVolumnUnit + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + "'" +
            ", frozenTubeTypeName='" + frozenTubeTypeName + "'" +
            ", isModifyState='" + isModifyState + "'" +
            ", isModifyPosition='" + isModifyPosition + "'" +
            '}';
    }
}
