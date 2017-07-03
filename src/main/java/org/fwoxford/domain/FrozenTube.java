package org.fwoxford.domain;

import io.swagger.models.auth.In;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A FrozenTube.
 */
@Entity
@Table(name = "frozen_tube")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenTube extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 项目编码
     */
    @Size(max = 100)
    @Column(name = "project_code", length = 100)
    private String projectCode;

    /**
     * 项目点编码
     */
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100)
    private String projectSiteCode;

    /**
     * 冻存管编码
     */
    @Size(max = 100)
    @Column(name = "frozen_tube_code", length = 100)
    private String frozenTubeCode;
    /**
     * 样本临时编码
     */
    @Size(max = 100)
    @Column(name = "sample_temp_code", length = 100)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @Size(max = 100)
    @Column(name = "sample_code", length = 100)
    private String sampleCode;
    /**
     * 冻存管类型编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_tube_type_code", length = 100, nullable = false)
    private String frozenTubeTypeCode;
    /**
     * 冻存管类型名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "frozen_tube_type_name", length = 255, nullable = false)
    private String frozenTubeTypeName;
    /**
     * 样本类型编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100, nullable = false)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255, nullable = false)
    private String sampleTypeName;
    /**
     * 样本最多使用次数
     */
    @Max(value = 20)
    @Column(name = "sample_used_times_most")
    private Integer sampleUsedTimesMost;
    /**
     * 样本已使用次数
     */
    @Max(value = 20)
    @Column(name = "sample_used_times")
    private Integer sampleUsedTimes;
    /**
     * 冻存管容量值
     */
    @NotNull
    @Column(name = "frozen_tube_volumns", nullable = false)
    private Integer frozenTubeVolumns;

    /**
     * 样本容量值
     */
    @Column(name = "sample_volumns")
    private Double sampleVolumns;

    /**
     * 冻存管容量值单位
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_tube_volumns_unit", length = 20, nullable = false)
    private String frozenTubeVolumnsUnit;
    /**
     * 行数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "tube_rows", length = 20, nullable = false)
    private String tubeRows;
    /**
     * 列数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "tube_columns", length = 20, nullable = false)
    private String tubeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 错误类型：6001：位置错误，6002：样本类型错误，6003：其他
     */
    @Size(max = 20)
    @Column(name = "error_type", length = 20)
    private String errorType;
    /**
     * 状态
     */
    @Size(max = 20)
    @Column(name = "frozen_tube_state", length = 20)
    private String frozenTubeState;
    /**
     * 状态：3001：正常，3002：空管，3003：空孔；3004：异常;3005：半管
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_code", length = 100, nullable = false)
    private String frozenBoxCode;
    /**
     * 项目组中患者ID
     */
    @Column(name = "patient_id")
    private Long patientId;
    /**
     * 患者出生日期
     */
    @Column(name = "dob")
    private ZonedDateTime dob;
    /**
     * 患者性别
     */
    @Size(max = 255)
    @Column(name = "gender", length = 255)
    private String gender;
    /**
     * 疾病类型
     */
    @Size(max = 255)
    @Column(name = "disease_type", length = 255)
    private String diseaseType;

    @Column(name = "is_hemolysis")
    private Boolean isHemolysis;

    @Column(name = "is_blood_lipid")
    private Boolean isBloodLipid;
    /**
     * 就诊类型
     */
    @Size(max = 255)
    @Column(name = "visit_type", length = 255)
    private String visitType;
    /**
     * 就诊日期
     */
    @Column(name = "visit_date")
    private ZonedDateTime visitDate;

    @Column(name = "age")
    private Integer age;

    /**
     * 样本分期
     */
    @Size(max = 255)
    @Column(name = "sample_stage", length = 255)
    private String sampleStage;

    /**
     * 冻存管类型
     */
    @ManyToOne(optional = false)
    private FrozenTubeType frozenTubeType;
    /**
     * 样本类型
     */
    @ManyToOne(optional = false)
    private SampleType sampleType;
    /**
     * 样本分类
     */
    @ManyToOne
    private SampleClassification sampleClassification;
    /**
     * 项目
     */
    @ManyToOne
    private Project project;
    /**
     * 项目点
     */
    @ManyToOne
    private ProjectSite projectSite;
    /**
     * 冻存盒
     */
    @ManyToOne(optional = false)
    private FrozenBox frozenBox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public FrozenTube projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }
    public FrozenTube projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }
    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public FrozenTube frozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
        return this;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public FrozenTube sampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
        return this;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public FrozenTube sampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
        return this;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }
    public FrozenTube sampleClassification(SampleClassification sampleClassification){
        this.sampleClassification = sampleClassification;
        return this;
    }
    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }
    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public FrozenTube frozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
        return this;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public FrozenTube frozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
        return this;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public FrozenTube sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public FrozenTube sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public FrozenTube sampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
        return this;
    }

    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }

    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public FrozenTube sampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
        return this;
    }

    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }

    public Integer getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }

    public FrozenTube frozenTubeVolumns(Integer frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
        return this;
    }

    public void setFrozenTubeVolumns(Integer frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }

    public Double getSampleVolumns() {
        return sampleVolumns;
    }
    public FrozenTube sampleVolumns(Double sampleVolumns){
        this.sampleVolumns = sampleVolumns;
        return this;
    }
    public void setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
    }

    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }

    public FrozenTube frozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
        return this;
    }

    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public FrozenTube tubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
        return this;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public FrozenTube tubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
        return this;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenTube memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getErrorType() {
        return errorType;
    }

    public FrozenTube errorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }

    public FrozenTube frozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
        return this;
    }
    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
    }

    public String getStatus() {
        return status;
    }

    public FrozenTube status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public FrozenTube frozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
        return this;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getPatientId() {
        return patientId;
    }

    public FrozenTube patientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public ZonedDateTime getDob() {
        return dob;
    }

    public FrozenTube dob(ZonedDateTime dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(ZonedDateTime dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public FrozenTube gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public FrozenTube diseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
        return this;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }
    public Boolean isIsHemolysis() {
        return isHemolysis;
    }

    public FrozenTube isHemolysis(Boolean isHemolysis) {
        this.isHemolysis = isHemolysis;
        return this;
    }

    public void setIsHemolysis(Boolean isHemolysis) {
        this.isHemolysis = isHemolysis;
    }

    public Boolean isIsBloodLipid() {
        return isBloodLipid;
    }

    public FrozenTube isBloodLipid(Boolean isBloodLipid) {
        this.isBloodLipid = isBloodLipid;
        return this;
    }

    public void setIsBloodLipid(Boolean isBloodLipid) {
        this.isBloodLipid = isBloodLipid;
    }
    public String getVisitType() {
        return visitType;
    }

    public FrozenTube visitType(String visitType) {
        this.visitType = visitType;
        return this;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public ZonedDateTime getVisitDate() {
        return visitDate;
    }

    public FrozenTube visitDate(ZonedDateTime visitDate) {
        this.visitDate = visitDate;
        return this;
    }

    public void setVisitDate(ZonedDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public Integer getAge() {
        return age;
    }
    public FrozenTube age(Integer age) {
        this.age = age;
        return this;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSampleStage() {
        return sampleStage;
    }
    public FrozenTube sampleStage(String sampleStage) {
        this.sampleStage = sampleStage;
        return this;
    }
    public void setSampleStage(String sampleStage) {
        this.sampleStage = sampleStage;
    }

    public FrozenTubeType getFrozenTubeType() {
        return frozenTubeType;
    }

    public FrozenTube frozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
        return this;
    }

    public void setFrozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public FrozenTube sampleType(SampleType sampleType) {
        this.sampleType = sampleType;
        return this;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public Project getProject() {
        return project;
    }

    public FrozenTube project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }
    public FrozenTube projectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
        return this;
    }
    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    public FrozenBox getFrozenBox() {
        return frozenBox;
    }

    public FrozenTube frozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
        return this;
    }

    public void setFrozenBox(FrozenBox frozenBox) {
        this.frozenBox = frozenBox;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FrozenTube frozenTube = (FrozenTube) o;
        if (frozenTube.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, frozenTube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenTube{" +
            "id=" + id +
            ", projectCode='" + projectCode + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + "'" +
            ", frozenTubeTypeName='" + frozenTubeTypeName + "'" +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", sampleUsedTimesMost='" + sampleUsedTimesMost + "'" +
            ", sampleUsedTimes='" + sampleUsedTimes + "'" +
            ", frozenTubeVolumns='" + frozenTubeVolumns + "'" +
            ", frozenTubeVolumnsUnit='" + frozenTubeVolumnsUnit + "'" +
            ", sampleVolumns='" + sampleVolumns + "'"+
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", memo='" + memo + "'" +
            ", errorType='" + errorType + "'" +
            ", frozenTubeState='" + frozenTubeState + "'" +
            ", status='" + status + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", patientId='" + patientId + "'" +
            ", dob='" + dob + "'" +
            ", gender='" + gender + "'" +
            ", diseaseType='" + diseaseType + "'" +
            ", isHemolysis='" + isHemolysis + "'" +
            ", isBloodLipid='" + isBloodLipid + "'" +
            ", visitType='" + visitType + "'" +
            ", visitDate='" + visitDate + "'" +
            ", age='" + age + "'" +
            ", sampleStage='" + sampleStage + "'" +
            '}';
    }
}
