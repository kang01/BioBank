package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PositionDestroy entity.
 */
public class PositionDestroyDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 1024)
    private String destroyReason;

    @NotNull
    @Size(max = 20)
    private String destroyType;

    private Long operatorId1;

    private Long operatorId2;

    @NotNull
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
    public String getDestroyReason() {
        return destroyReason;
    }

    public void setDestroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
    }
    public String getDestroyType() {
        return destroyType;
    }

    public void setDestroyType(String destroyType) {
        this.destroyType = destroyType;
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

        PositionDestroyDTO positionDestroyDTO = (PositionDestroyDTO) o;

        if ( ! Objects.equals(id, positionDestroyDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PositionDestroyDTO{" +
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
