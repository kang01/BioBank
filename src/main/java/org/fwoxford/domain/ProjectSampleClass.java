package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectSampleClass.
 */
@Entity
@Table(name = "project_sample")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectSampleClass extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_project_sample")
    @SequenceGenerator(name = "seq_project_sample",sequenceName = "seq_project_sample",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @Size(max = 20)
    @Column(name = "columns_number", length = 20)
    private String columnsNumber;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

    @ManyToOne(optional = false)
    @NotNull
    private SampleType sampleType;

    @ManyToOne
    private SampleClassification sampleClassification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public ProjectSampleClass projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getStatus() {
        return status;
    }

    public ProjectSampleClass status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public ProjectSampleClass memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getColumnsNumber() {
        return columnsNumber;
    }

    public ProjectSampleClass columnsNumber(String columnsNumber) {
        this.columnsNumber = columnsNumber;
        return this;
    }

    public void setColumnsNumber(String columnsNumber) {
        this.columnsNumber = columnsNumber;
    }

    public Project getProject() {
        return project;
    }

    public ProjectSampleClass project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public ProjectSampleClass sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }

    public ProjectSampleClass sampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
        return this;
    }

    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectSampleClass projectSampleClass = (ProjectSampleClass) o;
        if (projectSampleClass.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectSampleClass.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectSampleClass{" +
            "id=" + id +
            ", projectCode='" + projectCode + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", columnsNumber='" + columnsNumber + "'" +
            '}';
    }

    @Size(max = 255)
    @Column(name = "sample_classification_name", length = 255, nullable = false)
    private String sampleClassificationName;

    @Size(max = 100)
    @Column(name = "sample_classification_code", length = 100, nullable = false)
    private String sampleClassificationCode;

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public ProjectSampleClass sampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }
    public ProjectSampleClass sampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }
    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }
}
