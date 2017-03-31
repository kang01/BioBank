package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StorageIn.
 */
@Entity
@Table(name = "storage_in")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StorageIn extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;
    /**
     * 项目点编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100, nullable = false)
    private String projectSiteCode;
    /**
     * 接收日期
     */
    @NotNull
    @Column(name = "receive_date", nullable = false)
    private LocalDate receiveDate;
    /**
     * 接收人ID
     */
    @NotNull
    @Max(value = 100)
    @Column(name = "receive_id", nullable = false)
    private Long receiveId;
    /**
     * 接收人姓名
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "receive_name", length = 100, nullable = false)
    private String receiveName;
    /**
     * 入库类型 ：8001：首次入库，8002：移位入库，8003：调整入库
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "storage_in_type", length = 20, nullable = false)
    private String storageInType;
    /**
     * 入库人1ID
     */
    @NotNull
    @Max(value = 100)
    @Column(name = "storage_in_person_id_1", nullable = false)
    private Long storageInPersonId1;
    /**
     * 入库人1名称
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "storage_in_person_name_1", length = 100, nullable = false)
    private String storageInPersonName1;
    /**
     * 入库人2ID
     */
    @NotNull
    @Max(value = 100)
    @Column(name = "storage_in_person_id_2", nullable = false)
    private Long storageInPersonId2;
    /**
     * 入库人2名称
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "storange_in_person_name_2", length = 100, nullable = false)
    private String storangeInPersonName2;
    /**
     * 入库日期
     */
    @NotNull
    @Column(name = "storage_in_date", nullable = false)
    private LocalDate storageInDate;
    /**
     * 样本数量
     */
    @NotNull
    @Column(name = "sample_number", nullable = false)
    private Integer sampleNumber;
    /**
     * 签名人ID
     */
    @NotNull
    @Max(value = 100)
    @Column(name = "sign_id", nullable = false)
    private Long signId;
    /**
     * 签名人姓名
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "sign_name", length = 100, nullable = false)
    private String signName;
    /**
     * 签名日期
     */
    @NotNull
    @Column(name = "sign_date", nullable = false)
    private LocalDate signDate;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态 :7001：进行中，7002已入库
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 转运
     */
    @ManyToOne(optional = false)
    @NotNull
    private Tranship tranship;
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

    public String getProjectCode() {
        return projectCode;
    }

    public StorageIn projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public StorageIn project_site_code(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String project_site_code) {
        this.projectSiteCode = projectSiteCode;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public StorageIn receiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
        return this;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public StorageIn receiveId(Long receiveId) {
        this.receiveId = receiveId;
        return this;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public StorageIn receiveName(String receiveName) {
        this.receiveName = receiveName;
        return this;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getStorageInType() {
        return storageInType;
    }

    public StorageIn storageInType(String storageInType) {
        this.storageInType = storageInType;
        return this;
    }

    public void setStorageInType(String storageInType) {
        this.storageInType = storageInType;
    }

    public Long getStorageInPersonId1() {
        return storageInPersonId1;
    }

    public StorageIn storageInPersonId1(Long storageInPersonId1) {
        this.storageInPersonId1 = storageInPersonId1;
        return this;
    }

    public void setStorageInPersonId1(Long storageInPersonId1) {
        this.storageInPersonId1 = storageInPersonId1;
    }

    public String getStorageInPersonName1() {
        return storageInPersonName1;
    }

    public StorageIn storageInPersonName1(String storageInPersonName1) {
        this.storageInPersonName1 = storageInPersonName1;
        return this;
    }

    public void setStorageInPersonName1(String storageInPersonName1) {
        this.storageInPersonName1 = storageInPersonName1;
    }

    public Long getStorageInPersonId2() {
        return storageInPersonId2;
    }

    public StorageIn storageInPersonId2(Long storageInPersonId2) {
        this.storageInPersonId2 = storageInPersonId2;
        return this;
    }

    public void setStorageInPersonId2(Long storageInPersonId2) {
        this.storageInPersonId2 = storageInPersonId2;
    }

    public String getStorangeInPersonName2() {
        return storangeInPersonName2;
    }

    public StorageIn storangeInPersonName2(String storangeInPersonName2) {
        this.storangeInPersonName2 = storangeInPersonName2;
        return this;
    }

    public void setStorangeInPersonName2(String storangeInPersonName2) {
        this.storangeInPersonName2 = storangeInPersonName2;
    }

    public LocalDate getStorageInDate() {
        return storageInDate;
    }

    public StorageIn storageInDate(LocalDate storageInDate) {
        this.storageInDate = storageInDate;
        return this;
    }

    public void setStorageInDate(LocalDate storageInDate) {
        this.storageInDate = storageInDate;
    }

    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public StorageIn sampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
        return this;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public Long getSignId() {
        return signId;
    }

    public StorageIn signId(Long signId) {
        this.signId = signId;
        return this;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public String getSignName() {
        return signName;
    }

    public StorageIn signName(String signName) {
        this.signName = signName;
        return this;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public StorageIn signDate(LocalDate signDate) {
        this.signDate = signDate;
        return this;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }

    public String getMemo() {
        return memo;
    }

    public StorageIn memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public StorageIn status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tranship getTranship() {
        return tranship;
    }

    public StorageIn tranship(Tranship tranship) {
        this.tranship = tranship;
        return this;
    }

    public void setTranship(Tranship tranship) {
        this.tranship = tranship;
    }

    public Project getProject() {
        return project;
    }

    public StorageIn project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public StorageIn projectSite(ProjectSite projectSite) {
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
        StorageIn storageIn = (StorageIn) o;
        if (storageIn.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storageIn.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StorageIn{" +
            "id=" + id +
            ", projectCode='" + projectCode + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", receiveDate='" + receiveDate + "'" +
            ", receiveId='" + receiveId + "'" +
            ", receiveName='" + receiveName + "'" +
            ", storageInType='" + storageInType + "'" +
            ", storageInPersonId1='" + storageInPersonId1 + "'" +
            ", storageInPersonName1='" + storageInPersonName1 + "'" +
            ", storageInPersonId2='" + storageInPersonId2 + "'" +
            ", storangeInPersonName2='" + storangeInPersonName2 + "'" +
            ", storageInDate='" + storageInDate + "'" +
            ", sampleNumber='" + sampleNumber + "'" +
            ", signId='" + signId + "'" +
            ", signName='" + signName + "'" +
            ", signDate='" + signDate + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
