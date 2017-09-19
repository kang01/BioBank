package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PositionChange.
 */
@Entity
@Table(name = "position_change")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PositionChange extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_position_change")
    @SequenceGenerator(name = "seq_position_change",sequenceName = "seq_position_change",allocationSize = 1,initialValue = 1)
    private Long id;

    @Size(max = 1024)
    @Column(name = "change_reason", length = 1024)
    private String changeReason;

    @NotNull
    @Size(max = 20)
    @Column(name = "change_type", length = 20, nullable = false)
    private String changeType;

    @Column(name = "whether_freezing_and_thawing")
    private Boolean whetherFreezingAndThawing;

    @Column(name = "operator_id_1")
    private Long operatorId1;

    @Column(name = "operator_id_2")
    private Long operatorId2;

    @Column(name = "position_change_date")
    private LocalDate positionChangeDate;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public PositionChange changeReason(String changeReason) {
        this.changeReason = changeReason;
        return this;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeType() {
        return changeType;
    }

    public PositionChange changeType(String changeType) {
        this.changeType = changeType;
        return this;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Boolean isWhetherFreezingAndThawing() {
        return whetherFreezingAndThawing;
    }

    public PositionChange whetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
        return this;
    }

    public void setWhetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
    }

    public Long getOperatorId1() {
        return operatorId1;
    }

    public PositionChange operatorId1(Long operatorId1) {
        this.operatorId1 = operatorId1;
        return this;
    }

    public void setOperatorId1(Long operatorId1) {
        this.operatorId1 = operatorId1;
    }

    public Long getOperatorId2() {
        return operatorId2;
    }

    public PositionChange operatorId2(Long operatorId2) {
        this.operatorId2 = operatorId2;
        return this;
    }

    public void setOperatorId2(Long operatorId2) {
        this.operatorId2 = operatorId2;
    }

    public LocalDate getPositionChangeDate() {
        return positionChangeDate;
    }
    public PositionChange positionChangeDate(LocalDate positionChangeDate) {
        this.positionChangeDate = positionChangeDate;
        return this;
    }
    public void setPositionChangeDate(LocalDate positionChangeDate) {
        this.positionChangeDate = positionChangeDate;
    }

    public String getStatus() {
        return status;
    }

    public PositionChange status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public PositionChange memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PositionChange positionChange = (PositionChange) o;
        if (positionChange.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, positionChange.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionChange{" +
            "id=" + id +
            ", changeReason='" + changeReason + "'" +
            ", changeType='" + changeType + "'" +
            ", whetherFreezingAndThawing='" + whetherFreezingAndThawing + "'" +
            ", operatorId1='" + operatorId1 + "'" +
            ", operatorId2='" + operatorId2 + "'" +
            ", positionChangeDate='" + positionChangeDate + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
