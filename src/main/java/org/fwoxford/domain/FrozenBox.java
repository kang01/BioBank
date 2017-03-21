package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FrozenBox.
 */
@Entity
@Table(name = "frozen_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenBox extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_type_code", length = 100, nullable = false)
    private String frozenBoxTypeCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_box_rows", length = 20, nullable = false)
    private String frozenBoxRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_box_columns", length = 20, nullable = false)
    private String frozenBoxColumns;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "project_name", length = 255, nullable = false)
    private String projectName;

    @NotNull
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100, nullable = false)
    private String projectSiteCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "project_site_name", length = 255, nullable = false)
    private String projectSiteName;

    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false)
    private String equipmentCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "area_code", length = 100, nullable = false)
    private String areaCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100, nullable = false)
    private String supportRackCode;

    @NotNull
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100, nullable = false)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255, nullable = false)
    private String sampleTypeName;

    @NotNull
    @Max(value = 20)
    @Column(name = "sample_number", nullable = false)
    private Integer sampleNumber;

    @NotNull
    @Size(max = 20)
    @Column(name = "is_split", length = 20, nullable = false)
    private String isSplit;

    @Size(max = 255)
    @Column(name = "memo", length = 255)
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @ManyToOne(optional = false)
    @NotNull
    private FrozenBoxType frozenBoxType;

    @ManyToOne(optional = false)
    @NotNull
    private SampleType sampleType;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

    @ManyToOne(optional = false)
    @NotNull
    private ProjectSite projectSite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public FrozenBox frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public FrozenBox frozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
        return this;
    }

    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }

    public String getFrozenBoxRows() {
        return frozenBoxRows;
    }

    public FrozenBox frozenBoxRows(String frozenBoxRows) {
        this.frozenBoxRows = frozenBoxRows;
        return this;
    }

    public void setFrozenBoxRows(String frozenBoxRows) {
        this.frozenBoxRows = frozenBoxRows;
    }

    public String getFrozenBoxColumns() {
        return frozenBoxColumns;
    }

    public FrozenBox frozenBoxColumns(String frozenBoxColumns) {
        this.frozenBoxColumns = frozenBoxColumns;
        return this;
    }

    public void setFrozenBoxColumns(String frozenBoxColumns) {
        this.frozenBoxColumns = frozenBoxColumns;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public FrozenBox projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public FrozenBox projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public FrozenBox projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public FrozenBox projectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
        return this;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public FrozenBox equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public FrozenBox areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public FrozenBox supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public FrozenBox sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public FrozenBox sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public FrozenBox sampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
        return this;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getIsSplit() {
        return isSplit;
    }

    public FrozenBox isSplit(String isSplit) {
        this.isSplit = isSplit;
        return this;
    }

    public void setIsSplit(String isSplit) {
        this.isSplit = isSplit;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenBox memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public FrozenBox status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FrozenBoxType getFrozenBoxType() {
        return frozenBoxType;
    }

    public FrozenBox frozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
        return this;
    }

    public void setFrozenBoxType(FrozenBoxType frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public FrozenBox sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public Project getProject() {
        return project;
    }

    public FrozenBox project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public FrozenBox projectSite(ProjectSite projectSite) {
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
        FrozenBox frozenBox = (FrozenBox) o;
        if (frozenBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, frozenBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenBox{" +
            "id=" + id +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + "'" +
            ", frozenBoxRows='" + frozenBoxRows + "'" +
            ", frozenBoxColumns='" + frozenBoxColumns + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectName='" + projectName + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", projectSiteName='" + projectSiteName + "'" +
            ", equipmentCode='" + equipmentCode + "'" +
            ", areaCode='" + areaCode + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", sampleNumber='" + sampleNumber + "'" +
            ", isSplit='" + isSplit + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
