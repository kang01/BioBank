package org.fwoxford.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProjectSite.
 */
@Entity
@Table(name = "project_site")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectSite extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 项目点编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100, nullable = false)
    private String projectSiteCode;
    /**
     * 项目点名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "project_site_name", length = 255, nullable = false)
    private String projectSiteName;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 项目点
     */
    @OneToMany(mappedBy = "projectSite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProjectRelate> projectRelates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public ProjectSite projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public ProjectSite projectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
        return this;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getMemo() {
        return memo;
    }

    public ProjectSite memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public ProjectSite status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<ProjectRelate> getProjectRelates() {
        return projectRelates;
    }

    public ProjectSite projectRelates(Set<ProjectRelate> projectRelates) {
        this.projectRelates = projectRelates;
        return this;
    }

    public ProjectSite addProjectRelate(ProjectRelate projectRelate) {
        this.projectRelates.add(projectRelate);
        projectRelate.setProjectSite(this);
        return this;
    }

    public ProjectSite removeProjectRelate(ProjectRelate projectRelate) {
        this.projectRelates.remove(projectRelate);
        projectRelate.setProjectSite(null);
        return this;
    }

    public void setProjectRelates(Set<ProjectRelate> projectRelates) {
        this.projectRelates = projectRelates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectSite projectSite = (ProjectSite) o;
        if (projectSite.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectSite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectSite{" +
            "id=" + id +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", projectSiteName='" + projectSiteName + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
