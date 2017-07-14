package org.fwoxford.service.dto;


import org.fwoxford.service.dto.response.PositionMoveForBox;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the PositionMove entity.
 */
public class PositionMoveDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 1024)
    private String moveReason;

    @Size(max = 1024)
    private String moveAffect;

    private Boolean whetherFreezingAndThawing;

    @Size(max = 20)
    private String moveType;

    private Long operatorId1;

    private Long operatorId2;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
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

    public void setMoveReason(String moveReason) {
        this.moveReason = moveReason;
    }
    public String getMoveAffect() {
        return moveAffect;
    }

    public void setMoveAffect(String moveAffect) {
        this.moveAffect = moveAffect;
    }
    public Boolean getWhetherFreezingAndThawing() {
        return whetherFreezingAndThawing;
    }

    public void setWhetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
    }
    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }
    public Long getOperatorId1() {
        return operatorId1;
    }

    public void setOperatorId1(Long operatorId1) {
        this.operatorId1 = operatorId1;
    }
    public Long getOperatorId2() {
        return operatorId2;
    }

    public void setOperatorId2(Long operatorId2) {
        this.operatorId2 = operatorId2;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMemo() {
        return memo;
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

        PositionMoveDTO positionMoveDTO = (PositionMoveDTO) o;

        if ( ! Objects.equals(id, positionMoveDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionMoveDTO{" +
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
