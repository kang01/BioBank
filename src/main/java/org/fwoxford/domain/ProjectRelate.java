package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectRelate.
 */
@Entity
@Table(name = "project_relate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectRelate extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
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
     * 项目
     */
    @ManyToOne(optional = false)
    @NotNull
    private Project project;
    /**
     * 项目点
     */
    @ManyToOne(optional = false)
    @NotNull
    private ProjectSite projectSite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public ProjectRelate memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public ProjectRelate status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public ProjectRelate project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public ProjectRelate projectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
        return this;
    }

    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectRelate projectRelate = (ProjectRelate) o;
        if (projectRelate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectRelate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectRelate{" +
            "id=" + id +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
