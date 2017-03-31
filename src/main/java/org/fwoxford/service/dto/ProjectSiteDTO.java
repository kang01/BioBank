package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProjectSite entity.
 */
public class ProjectSiteDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 项目点编码
     */
    @NotNull
    @Size(max = 100)
    private String projectSiteCode;
    /**
     * 项目点名称
     */
    @NotNull
    @Size(max = 255)
    private String projectSiteName;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }
    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectSiteDTO projectSiteDTO = (ProjectSiteDTO) o;

        if ( ! Objects.equals(id, projectSiteDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectSiteDTO{" +
            "id=" + id +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", projectSiteName='" + projectSiteName + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
