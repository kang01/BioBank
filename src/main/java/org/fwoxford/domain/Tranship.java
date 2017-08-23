package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Tranship.
 */
@Entity
@Table(name = "tranship")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tranship extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tranship")
    @SequenceGenerator(name = "seq_tranship",sequenceName = "seq_tranship",allocationSize = 1,initialValue = 1)
    private Long id;
    /**
     * 转运日期
     */
    @Column(name = "tranship_date", nullable = true)
    private LocalDate transhipDate;
    /**
     * 项目编码
     */
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = true)
    private String projectCode;
    /**
     * 项目名称
     */
    @Size(max = 255)
    @Column(name = "project_name", length = 255, nullable = true)
    private String projectName;
    /**
     * 项目点编码
     */
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100, nullable = true)
    private String projectSiteCode;
    /**
     * 项目点名称
     */
    @Size(max = 255)
    @Column(name = "project_site_name", length = 255, nullable = true)
    private String projectSiteName;
    /**
     * 运单号
     */
    @Size(max = 100)
    @Column(name = "track_number", length = 100, nullable = true)
    private String trackNumber;
    /**
     * 转运批次
     */
    @Size(max = 100)
    @Column(name = "tranship_batch", length = 100, nullable = true)
    private String transhipBatch;
    /**
     * 运单状态：1001：进行中，1002：待入库，1003：已入库，1090：已作废
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "tranship_state", length = 20, nullable = false)
    private String transhipState;
    /**
     * 接收人ID
     */
    @Column(name = "receiver_id")
    private Long receiverId;
    /**
     * 接收人
     */
    @Size(max = 100)
    @Column(name = "receiver", length = 100, nullable = true)
    private String receiver;
    /**
     * 接收日期
     */
    @Column(name = "receive_date", nullable = true)
    private LocalDate receiveDate;
    /**
     * 样本数量
     */
    @NotNull
    @Column(name = "sample_number", nullable = false)
    private Integer sampleNumber;
    /**
     * 冻存盒数量
     */
    @NotNull
    @Column(name = "frozen_box_number", nullable = false)
    private Integer frozenBoxNumber;
    /**
     * 空管数
     */
    @NotNull
    @Column(name = "empty_tube_number", nullable = false)
    private Integer emptyTubeNumber;
    /**
     * 空孔数
     */
    @NotNull
    @Column(name = "empty_hole_number", nullable = false)
    private Integer emptyHoleNumber;
    /**
     * 样本满意度
     */
    @Max(value = 20)
    @Column(name = "sample_satisfaction")
    private Integer sampleSatisfaction;
    /**
     * 有效样本数
     */
    @NotNull
    @Column(name = "effective_sample_number", nullable = false)
    private Integer effectiveSampleNumber;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态：0000：无效，0001：有效
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 转运编码
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "tranship_code", length = 255, nullable = false)
    private String transhipCode;
    /**
     * 临时设备编码
     */
    @Size(max = 255)
    @Column(name = "temp_equipment_code", length = 255)
    private String tempEquipmentCode;
    /**
     * 临时区域编码
     */
    @Size(max = 255)
    @Column(name = "temp_area_code", length = 255)
    private String tempAreaCode;
    /**
     * 临时设备ID
     */
    @Column(name = "temp_equipment_id")
    private Long tempEquipmentId;

    /**
     * 临时区域ID
     */
    @Column(name = "temp_area_id")
    private Long tempAreaId;
    /**
     * 项目
     */
    @ManyToOne(optional = true)
    private Project project;
    /**
     * 项目点
     */
    @ManyToOne(optional = true)
    private ProjectSite projectSite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTranshipDate() {
        return transhipDate;
    }

    public Tranship transhipDate(LocalDate transhipDate) {
        this.transhipDate = transhipDate;
        return this;
    }
    public void setTranshipDate(LocalDate transhipDate) {
        this.transhipDate = transhipDate;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public Tranship projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public Tranship projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public Tranship projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public Tranship projectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
        return this;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public Tranship trackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
        return this;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getTranshipBatch() {
        return transhipBatch;
    }

    public Tranship transhipBatch(String transhipBatch) {
        this.transhipBatch = transhipBatch;
        return this;
    }

    public void setTranshipBatch(String transhipBatch) {
        this.transhipBatch = transhipBatch;
    }

    public String getTranshipState() {
        return transhipState;
    }

    public Tranship transhipState(String transhipState) {
        this.transhipState = transhipState;
        return this;
    }

    public void setTranshipState(String transhipState) {
        this.transhipState = transhipState;
    }

    public Long getReceiverId() {
        return receiverId;
    }
    public Tranship receiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiver() {
        return receiver;
    }

    public Tranship receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public Tranship receiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
        return this;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public Tranship sampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
        return this;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public Integer getFrozenBoxNumber() {
        return frozenBoxNumber;
    }

    public Tranship frozenBoxNumber(Integer frozenBoxNumber) {
        this.frozenBoxNumber = frozenBoxNumber;
        return this;
    }

    public void setFrozenBoxNumber(Integer frozenBoxNumber) {
        this.frozenBoxNumber = frozenBoxNumber;
    }

    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }

    public Tranship emptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
        return this;
    }

    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }

    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }

    public Tranship emptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
        return this;
    }

    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }

    public Integer getSampleSatisfaction() {
        return sampleSatisfaction;
    }

    public Tranship sampleSatisfaction(Integer sampleSatisfaction) {
        this.sampleSatisfaction = sampleSatisfaction;
        return this;
    }

    public void setSampleSatisfaction(Integer sampleSatisfaction) {
        this.sampleSatisfaction = sampleSatisfaction;
    }

    public Integer getEffectiveSampleNumber() {
        return effectiveSampleNumber;
    }

    public Tranship effectiveSampleNumber(Integer effectiveSampleNumber) {
        this.effectiveSampleNumber = effectiveSampleNumber;
        return this;
    }

    public void setEffectiveSampleNumber(Integer effectiveSampleNumber) {
        this.effectiveSampleNumber = effectiveSampleNumber;
    }

    public String getMemo() {
        return memo;
    }

    public Tranship memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public Tranship status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public Tranship transhipCode(String transhipCode) {
        this.transhipCode = transhipCode;
        return this;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public String getTempEquipmentCode() {
        return tempEquipmentCode;
    }
    public Tranship tempEquipmentCode(String tempEquipmentCode) {
        this.tempEquipmentCode = tempEquipmentCode;
        return this;
    }
    public void setTempEquipmentCode(String tempEquipmentCode) {
        this.tempEquipmentCode = tempEquipmentCode;
    }

    public String getTempAreaCode() {
        return tempAreaCode;
    }
    public Tranship tempAreaCode(String tempAreaCode) {
        this.tempAreaCode = tempAreaCode;
        return this;
    }

    public void setTempAreaCode(String tempAreaCode) {
        this.tempAreaCode = tempAreaCode;
    }

    public Long getTempEquipmentId() {
        return tempEquipmentId;
    }
    public Tranship tempEquipmentId(Long tempEquipmentId) {
        this.tempEquipmentId = tempEquipmentId;
        return this;
    }
    public void setTempEquipmentId(Long tempEquipmentId) {
        this.tempEquipmentId = tempEquipmentId;
    }

    public Long getTempAreaId() {
        return tempAreaId;
    }
    public Tranship tempAreaId(Long tempAreaId) {
        this.tempAreaId = tempAreaId;
        return this;
    }
    public void setTempAreaId(Long tempAreaId) {
        this.tempAreaId = tempAreaId;
    }

    public Project getProject() {
        return project;
    }

    public Tranship project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public Tranship projectSite(ProjectSite projectSite) {
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
        Tranship tranship = (Tranship) o;
        if (tranship.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tranship.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tranship{" +
            "id=" + id +
            ", transhipDate='" + transhipDate + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectName='" + projectName + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", projectSiteName='" + projectSiteName + "'" +
            ", trackNumber='" + trackNumber + "'" +
            ", transhipBatch='" + transhipBatch + "'" +
            ", transhipState='" + transhipState + "'" +
            ", receiver='" + receiver + "'" +
            ", receiverId='" + receiverId + "'" +
            ", receiveDate='" + receiveDate + "'" +
            ", sampleNumber='" + sampleNumber + "'" +
            ", frozenBoxNumber='" + frozenBoxNumber + "'" +
            ", emptyTubeNumber='" + emptyTubeNumber + "'" +
            ", emptyHoleNumber='" + emptyHoleNumber + "'" +
            ", sampleSatisfaction='" + sampleSatisfaction + "'" +
            ", effectiveSampleNumber='" + effectiveSampleNumber + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", transhipCode='" + transhipCode + "'" +
            ", tempEquipmentCode='" + tempEquipmentCode + "'" +
            ", tempAreaCode='" + tempAreaCode + "'" +
            ", tempEquipmentId='" + tempEquipmentId + "'" +
            ", transhipCode='" + tempAreaId + "'" +
            '}';
    }
}
