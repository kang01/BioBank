package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PositionMove.
 */
@Entity
@Table(name = "position_move")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PositionMove extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1024)
    @Column(name = "move_reason", length = 1024)
    private String moveReason;

    @Size(max = 1024)
    @Column(name = "move_affect", length = 1024)
    private String moveAffect;

    @Column(name = "whether_freezing_and_thawing")
    private Boolean whetherFreezingAndThawing;

    @NotNull
    @Size(max = 20)
    @Column(name = "move_type", length = 20, nullable = false)
    private String moveType;

    @Column(name = "operator_id_1")
    private Long operatorId1;

    @Column(name = "operator_id_2")
    private Long operatorId2;

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

    public String getMoveReason() {
        return moveReason;
    }

    public PositionMove moveReason(String moveReason) {
        this.moveReason = moveReason;
        return this;
    }

    public void setMoveReason(String moveReason) {
        this.moveReason = moveReason;
    }

    public String getMoveAffect() {
        return moveAffect;
    }

    public PositionMove moveAffect(String moveAffect) {
        this.moveAffect = moveAffect;
        return this;
    }

    public void setMoveAffect(String moveAffect) {
        this.moveAffect = moveAffect;
    }

    public Boolean isWhetherFreezingAndThawing() {
        return whetherFreezingAndThawing;
    }

    public PositionMove whetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
        return this;
    }

    public void setWhetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
    }

    public String getMoveType() {
        return moveType;
    }

    public PositionMove moveType(String moveType) {
        this.moveType = moveType;
        return this;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public Long getOperatorId1() {
        return operatorId1;
    }

    public PositionMove operatorId1(Long operatorId1) {
        this.operatorId1 = operatorId1;
        return this;
    }

    public void setOperatorId1(Long operatorId1) {
        this.operatorId1 = operatorId1;
    }

    public Long getOperatorId2() {
        return operatorId2;
    }

    public PositionMove operatorId2(Long operatorId2) {
        this.operatorId2 = operatorId2;
        return this;
    }

    public void setOperatorId2(Long operatorId2) {
        this.operatorId2 = operatorId2;
    }

    public String getStatus() {
        return status;
    }

    public PositionMove status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public PositionMove memo(String memo) {
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
        PositionMove positionMove = (PositionMove) o;
        if (positionMove.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, positionMove.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionMove{" +
            "id=" + id +
            ", moveReason='" + moveReason + "'" +
            ", moveAffect='" + moveAffect + "'" +
            ", whetherFreezingAndThawing='" + whetherFreezingAndThawing + "'" +
            ", moveType='" + moveType + "'" +
            ", operatorId1='" + operatorId1 + "'" +
            ", operatorId2='" + operatorId2 + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
