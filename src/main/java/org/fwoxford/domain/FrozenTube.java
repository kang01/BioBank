package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FrozenTube.
 */
@Entity
@Table(name = "frozen_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenTube extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "frozen_tube_code", length = 100, nullable = false)
    private String frozenTubeCode;

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
    @Column(name = "frozen_tube_type_code", length = 100, nullable = false)
    private String frozenTubeTypeCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "frozen_tube_type_name", length = 255, nullable = false)
    private String frozenTubeTypeName;

    @NotNull
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100, nullable = false)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255, nullable = false)
    private String sampleTypeName;

    @NotNull
    @Max(value = 100)
    @Column(name = "sample_used_times_most", nullable = false)
    private Integer sampleUsedTimesMost;

    @NotNull
    @Max(value = 100)
    @Column(name = "sample_used_times", nullable = false)
    private Integer sampleUsedTimes;

    @NotNull
    @Max(value = 400)
    @Column(name = "frozen_tube_volumns", nullable = false)
    private Integer frozenTubeVolumns;

    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_tube_volumns_unit", length = 20, nullable = false)
    private String frozenTubeVolumnsUnit;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @Size(max = 20)
    @Column(name = "error_type", length = 20)
    private String errorType;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "frozen_tube_type_id")
    private FrozenTubeType frozenTubeType;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "sample_type_id")
    private SampleType sampleType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public FrozenTube projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public FrozenTube frozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
        return this;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public FrozenTube sampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public FrozenTube sampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public FrozenTube frozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public FrozenTube frozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public FrozenTube sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public FrozenTube sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public FrozenTube sampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }

    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public FrozenTube sampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
        return this;
    }

    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public Integer getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }

    public FrozenTube frozenTubeVolumns(Integer frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
        return this;
    }

    public void setFrozenTubeVolumns(Integer frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }

    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }

    public FrozenTube frozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
        return this;
    }

    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public FrozenTube tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public FrozenTube tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getErrorType() {
        return errorType;
    }

    public FrozenTube errorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getStatus() {
        return status;
    }

    public FrozenTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FrozenTubeType getFrozenTubeType() {
        return frozenTubeType;
    }

    public FrozenTube frozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
        return this;
    }

    public void setFrozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public FrozenTube sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FrozenTube frozenTube = (FrozenTube) o;
        if (frozenTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, frozenTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenTube{" +
            "id=" + id +
            ", projectCode='" + projectCode + "'" +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + "'" +
            ", frozenTubeTypeName='" + frozenTubeTypeName + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", sampleUsedTimesMost='" + sampleUsedTimesMost + "'" +
            ", sampleUsedTimes='" + sampleUsedTimes + "'" +
            ", frozenTubeVolumns='" + frozenTubeVolumns + "'" +
            ", frozenTubeVolumnsUnit='" + frozenTubeVolumnsUnit + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", memo='" + memo + "'" +
            ", errorType='" + errorType + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
