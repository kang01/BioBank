package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Relations entity.
 */
public class RelationsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String projectCode;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private Long frozenBoxTypeId;

    private Long frozenTubeTypeId;

    private Long sampleTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public void setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelationsDTO relationsDTO = (RelationsDTO) o;

        if ( ! Objects.equals(id, relationsDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RelationsDTO{" +
            "id=" + id +
            ", projectCode='" + projectCode + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
