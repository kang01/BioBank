package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FrozenTubeType.
 */
@Entity
@Table(name = "frozen_tube_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenTubeType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_tube_type_code", length = 100, nullable = false)
    private String frozenTubeTypeCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "frozen_tube_type_name", length = 255, nullable = false)
    private String frozenTubeTypeName;

    @NotNull
    @Max(value = 20)
    @Column(name = "sample_used_times_most", nullable = false)
    private Integer sampleUsedTimesMost;

    @NotNull
    @Column(name = "frozen_tube_volumn", nullable = false)
    private Integer frozenTubeVolumn;

    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_tube_volumn_unit", length = 20, nullable = false)
    private String frozenTubeVolumnUnit;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public FrozenTubeType frozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public FrozenTubeType frozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public FrozenTubeType sampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }

    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getFrozenTubeVolumn() {
        return frozenTubeVolumn;
    }

    public FrozenTubeType frozenTubeVolumn(Integer frozenTubeVolumn) {
        this.frozenTubeVolumn = frozenTubeVolumn;
        return this;
    }

    public void setFrozenTubeVolumn(Integer frozenTubeVolumn) {
        this.frozenTubeVolumn = frozenTubeVolumn;
    }

    public String getFrozenTubeVolumnUnit() {
        return frozenTubeVolumnUnit;
    }

    public FrozenTubeType frozenTubeVolumnUnit(String frozenTubeVolumnUnit) {
        this.frozenTubeVolumnUnit = frozenTubeVolumnUnit;
        return this;
    }

    public void setFrozenTubeVolumnUnit(String frozenTubeVolumnUnit) {
        this.frozenTubeVolumnUnit = frozenTubeVolumnUnit;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenTubeType memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public FrozenTubeType status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FrozenTubeType frozenTubeType = (FrozenTubeType) o;
        if (frozenTubeType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, frozenTubeType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenTubeType{" +
            "id=" + id +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + "'" +
            ", frozenTubeTypeName='" + frozenTubeTypeName + "'" +
            ", sampleUsedTimesMost='" + sampleUsedTimesMost + "'" +
            ", frozenTubeVolumn='" + frozenTubeVolumn + "'" +
            ", frozenTubeVolumnUnit='" + frozenTubeVolumnUnit + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
