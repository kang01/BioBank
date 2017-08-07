package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PositionDestroy.
 */
@Entity
@Table(name = "position_destroy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PositionDestroy extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1024)
    @Column(name = "destroy_reason", length = 1024)
    private String destroyReason;

    @NotNull
    @Size(max = 20)
    @Column(name = "destroy_type", length = 20, nullable = false)
    private String destroyType;

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

    public String getDestroyReason() {
        return destroyReason;
    }

    public PositionDestroy destroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
        return this;
    }

    public void setDestroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
    }

    public String getDestroyType() {
        return destroyType;
    }

    public PositionDestroy destroyType(String destroyType) {
        this.destroyType = destroyType;
        return this;
    }

    public void setDestroyType(String destroyType) {
        this.destroyType = destroyType;
    }

    public Long getOperatorId1() {
        return operatorId1;
    }

    public PositionDestroy operatorId1(Long operatorId1) {
        this.operatorId1 = operatorId1;
        return this;
    }

    public void setOperatorId1(Long operatorId1) {
        this.operatorId1 = operatorId1;
    }

    public Long getOperatorId2() {
        return operatorId2;
    }

    public PositionDestroy operatorId2(Long operatorId2) {
        this.operatorId2 = operatorId2;
        return this;
    }

    public void setOperatorId2(Long operatorId2) {
        this.operatorId2 = operatorId2;
    }

    public String getStatus() {
        return status;
    }

    public PositionDestroy status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public PositionDestroy memo(String memo) {
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
        PositionDestroy positionDestroy = (PositionDestroy) o;
        if (positionDestroy.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, positionDestroy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionDestroy{" +
            "id=" + id +
            ", destroyReason='" + destroyReason + "'" +
            ", destroyType='" + destroyType + "'" +
            ", operatorId1='" + operatorId1 + "'" +
            ", operatorId2='" + operatorId2 + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
