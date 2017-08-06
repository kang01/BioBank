package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PositionChange entity.
 */
public class PositionChangeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 1024)
    private String changeReason;

    @Size(max = 20)
    private String changeType;

    private Boolean whetherFreezingAndThawing;

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
    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }
    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
    public Boolean getWhetherFreezingAndThawing() {
        return whetherFreezingAndThawing;
    }

    public void setWhetherFreezingAndThawing(Boolean whetherFreezingAndThawing) {
        this.whetherFreezingAndThawing = whetherFreezingAndThawing;
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

        PositionChangeDTO positionChangeDTO = (PositionChangeDTO) o;

        if ( ! Objects.equals(id, positionChangeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionChangeDTO{" +
            "id=" + id +
            ", changeReason='" + changeReason + "'" +
            ", changeType='" + changeType + "'" +
            ", whetherFreezingAndThawing='" + whetherFreezingAndThawing + "'" +
            ", operatorId1='" + operatorId1 + "'" +
            ", operatorId2='" + operatorId2 + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
    private String password1;

    private String password2;

    private Long changeId1;

    private Long changeId2;

    public String getPassword1() {
        return password1;
    }

    public PositionChangeDTO setPassword1(String password1) {
        this.password1 = password1;
        return this;
    }

    public String getPassword2() {
        return password2;
    }

    public PositionChangeDTO setPassword2(String password2) {
        this.password2 = password2;
        return this;
    }

    public Long getChangeId1() {
        return changeId1;
    }

    public PositionChangeDTO setChangeId1(Long changeId1) {
        this.changeId1 = changeId1;
        return this;
    }

    public Long getChangeId2() {
        return changeId2;
    }

    public PositionChangeDTO setChangeId2(Long changeId2) {
        this.changeId2 = changeId2;
        return this;
    }
}
