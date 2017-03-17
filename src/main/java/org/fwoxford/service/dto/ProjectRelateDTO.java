package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProjectRelate entity.
 */
public class ProjectRelateDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private Long projectId;

    private String projectProjectCode;

    private Long projectSiteId;

    private String projectSiteProjectSiteCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectProjectCode() {
        return projectProjectCode;
    }

    public void setProjectProjectCode(String projectProjectCode) {
        this.projectProjectCode = projectProjectCode;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }

    public void setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
    }

    public String getProjectSiteProjectSiteCode() {
        return projectSiteProjectSiteCode;
    }

    public void setProjectSiteProjectSiteCode(String projectSiteProjectSiteCode) {
        this.projectSiteProjectSiteCode = projectSiteProjectSiteCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectRelateDTO projectRelateDTO = (ProjectRelateDTO) o;

        if ( ! Objects.equals(id, projectRelateDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectRelateDTO{" +
            "id=" + id +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
