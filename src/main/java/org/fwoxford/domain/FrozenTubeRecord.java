package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FrozenTubeRecord.
 */
@Entity
@Table(name = "tube_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenTubeRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;
    /**
     * 样本临时编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "sample_temp_code", length = 100, nullable = false)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "sample_code", length = 100, nullable = false)
    private String sampleCode;
    /**
     * 样本最多使用次数
     */
    @NotNull
    @Max(value = 20)
    @Column(name = "sample_used_times_most", nullable = false)
    private Integer sampleUsedTimesMost;
    /**
     * 样本已使用次数
     */
    @NotNull
    @Max(value = 20)
    @Column(name = "sample_used_times", nullable = false)
    private Integer sampleUsedTimes;
    /**
     * 冻存管容量值
     */
    @NotNull
    @Max(value = 100)
    @Column(name = "frozen_tube_volumn", nullable = false)
    private Integer frozenTubeVolumn;
    /**
     * 冻存管容量单位
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_tube_volumn_unit", length = 20, nullable = false)
    private String frozenTubeVolumnUnit;
    /**
     * 样本类型编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100, nullable = false)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255, nullable = false)
    private String sampleTypeName;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;
    /**
     * 行数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;
    /**
     * 列数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 冻存管类型编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_tube_type_code", length = 100, nullable = false)
    private String frozenTubeTypeCode;
    /**
     * 冻存管类型名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "frozen_tube_type_name", length = 255, nullable = false)
    private String frozenTubeTypeName;
    /**
     * 是否修改状态（1：是，0：否）
     */
    @NotNull
    @Max(value = 20)
    @Column(name = "is_modify_state", nullable = false)
    private Integer isModifyState;
    /**
     * 是否修改位置（1：是，0：否）
     */
    @NotNull
    @Max(value = 20)
    @Column(name = "is_modify_position", nullable = false)
    private Integer isModifyPosition;
    /**
     * 样本类型
     */
    @ManyToOne
    private SampleType sampleType;
    /**
     * 冻存管类型
     */
    @ManyToOne(optional = false)
    @NotNull
    private FrozenTubeType tubeType;
    /**
     * 冻存盒类型
     */
    @ManyToOne(optional = false)
    @NotNull
    private FrozenBox frozenBox;
    /**
     * 冻存管
     */
    @ManyToOne(optional = false)
    @NotNull
    private FrozenTube frozenTube;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public FrozenTubeRecord projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public FrozenTubeRecord sampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public FrozenTubeRecord sampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public FrozenTubeRecord sampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }

    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public FrozenTubeRecord sampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
        return this;
    }

    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public Integer getFrozenTubeVolumn() {
        return frozenTubeVolumn;
    }

    public FrozenTubeRecord frozenTubeVolumn(Integer frozenTubeVolumn) {
        this.frozenTubeVolumn = frozenTubeVolumn;
        return this;
    }

    public void setFrozenTubeVolumn(Integer frozenTubeVolumn) {
        this.frozenTubeVolumn = frozenTubeVolumn;
    }

    public String getFrozenTubeVolumnUnit() {
        return frozenTubeVolumnUnit;
    }

    public FrozenTubeRecord frozenTubeVolumnUnit(String frozenTubeVolumnUnit) {
        this.frozenTubeVolumnUnit = frozenTubeVolumnUnit;
        return this;
    }

    public void setFrozenTubeVolumnUnit(String frozenTubeVolumnUnit) {
        this.frozenTubeVolumnUnit = frozenTubeVolumnUnit;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public FrozenTubeRecord sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public FrozenTubeRecord sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public FrozenTubeRecord frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public FrozenTubeRecord tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public FrozenTubeRecord tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenTubeRecord memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public FrozenTubeRecord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public FrozenTubeRecord frozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public FrozenTubeRecord frozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public Integer getIsModifyState() {
        return isModifyState;
    }

    public FrozenTubeRecord isModifyState(Integer isModifyState) {
        this.isModifyState = isModifyState;
        return this;
    }

    public void setIsModifyState(Integer isModifyState) {
        this.isModifyState = isModifyState;
    }

    public Integer getIsModifyPosition() {
        return isModifyPosition;
    }

    public FrozenTubeRecord isModifyPosition(Integer isModifyPosition) {
        this.isModifyPosition = isModifyPosition;
        return this;
    }

    public void setIsModifyPosition(Integer isModifyPosition) {
        this.isModifyPosition = isModifyPosition;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public FrozenTubeRecord sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public FrozenTubeType getTubeType() {
        return tubeType;
    }

    public FrozenTubeRecord tubeType(FrozenTubeType frozenTubeType) {
        this.tubeType = frozenTubeType;
        return this;
    }

    public void setTubeType(FrozenTubeType frozenTubeType) {
        this.tubeType = frozenTubeType;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public FrozenTubeRecord frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    public FrozenTube getFrozenTube() {
        return frozenTube;
    }

    public FrozenTubeRecord frozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
        return this;
    }

    public void setFrozenTube(FrozenTube frozenTube) {
        this.frozenTube = frozenTube;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FrozenTubeRecord frozenTubeRecord = (FrozenTubeRecord) o;
        if (frozenTubeRecord.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, frozenTubeRecord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenTubeRecord{" +
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
