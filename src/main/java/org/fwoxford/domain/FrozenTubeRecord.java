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

    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "sample_temp_code", length = 100, nullable = false)
    private String sampleTempCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "sample_code", length = 100, nullable = false)
    private String sampleCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_type_code", length = 100, nullable = false)
    private String frozenTypeCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "frozen_type_name", length = 255, nullable = false)
    private String frozenTypeName;

    @NotNull
    @Max(value = 20)
    @Column(name = "sample_used_times_most", nullable = false)
    private Integer sampleUsedTimesMost;

    @NotNull
    @Max(value = 20)
    @Column(name = "sample_used_times", nullable = false)
    private Integer sampleUsedTimes;

    @NotNull
    @Max(value = 100)
    @Column(name = "frozen_tube_volumn", nullable = false)
    private Integer frozenTubeVolumn;

    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_tube_volumn_unit", length = 20, nullable = false)
    private String frozenTubeVolumnUnit;

    @NotNull
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100, nullable = false)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255, nullable = false)
    private String sampleTypeName;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;

    @NotNull
    @Size(max = 20)
    @Column(name = "is_modify_state", length = 20, nullable = false)
    private String isModifyState;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @NotNull
    @Size(max = 20)
    @Column(name = "is_modify_position", length = 20, nullable = false)
    private String isModifyPosition;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "sample_type_id")
    private SampleType sampleType;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "tube_type_id")
    private FrozenTubeType tubeType;

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

    public String getFrozenTypeCode() {
        return frozenTypeCode;
    }

    public FrozenTubeRecord frozenTypeCode(String frozenTypeCode) {
        this.frozenTypeCode = frozenTypeCode;
        return this;
    }

    public void setFrozenTypeCode(String frozenTypeCode) {
        this.frozenTypeCode = frozenTypeCode;
    }

    public String getFrozenTypeName() {
        return frozenTypeName;
    }

    public FrozenTubeRecord frozenTypeName(String frozenTypeName) {
        this.frozenTypeName = frozenTypeName;
        return this;
    }

    public void setFrozenTypeName(String frozenTypeName) {
        this.frozenTypeName = frozenTypeName;
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

    public String getIsModifyState() {
        return isModifyState;
    }

    public FrozenTubeRecord isModifyState(String isModifyState) {
        this.isModifyState = isModifyState;
        return this;
    }

    public void setIsModifyState(String isModifyState) {
        this.isModifyState = isModifyState;
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

    public String getIsModifyPosition() {
        return isModifyPosition;
    }

    public FrozenTubeRecord isModifyPosition(String isModifyPosition) {
        this.isModifyPosition = isModifyPosition;
        return this;
    }

    public void setIsModifyPosition(String isModifyPosition) {
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
