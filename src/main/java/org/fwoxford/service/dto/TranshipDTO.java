package org.fwoxford.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Tranship entity.
 */
public class TranshipDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;
    /**
     * 转运日期
     */
    private LocalDate transhipDate;
    /**
     * 项目编码
     */
    @Size(max = 100)
    private String projectCode;
    /**
     * 项目名称
     */
    @Size(max = 255)
    private String projectName;
    /**
     * 项目点编码
     */
    @Size(max = 100)
    private String projectSiteCode;
    /**
     * 项目点名称
     */
    @Size(max = 255)
    private String projectSiteName;
    /**
     * 运单号
     */
    @Size(max = 100)
    private String trackNumber;
    /**
     * 转运批次
     */
    @Size(max = 100)
    private String transhipBatch;
    /**
     * 运单状态：1001：进行中，1002：待入库，1003：已入库，1004：已作废
     */
    @Size(max = 20)
    private String transhipState;
    /**
     * 接收人
     */
    @Size(max = 100)
    private String receiver;
    /**
     * 接收日期
     */
    private LocalDate receiveDate;
    /**
     * 样本数量
     */
    private Integer sampleNumber;
    /**
     * 冻存盒数量
     */
    private Integer frozenBoxNumber;
    /**
     * 空管数
     */
    @NotNull
    private Integer emptyTubeNumber;
    /**
     * 空孔数
     */
    private Integer emptyHoleNumber;
    /**
     * 样本满意度
     */
    @Max(value = 20)
    private Integer sampleSatisfaction;
    /**
     * 有效样本数
     */
    private Integer effectiveSampleNumber;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态：0000：无效，0001：有效
     */
    @Size(max = 20)
    private String status;
    /**
     * 转运编码
     */
    @Size(max = 255)
    private String transhipCode;
    /**
     * 项目Id
     */
    private Long projectId;
    /**
     * 项目点Id
     */
    private Long projectSiteId;

    private List<FrozenBoxDTO> frozenBoxDTOList;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getTranshipDate() {
        return transhipDate;
    }

    public void setTranshipDate(LocalDate transhipDate) {
        this.transhipDate = transhipDate;
    }
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }
    public String getTranshipBatch() {
        return transhipBatch;
    }

    public void setTranshipBatch(String transhipBatch) {
        this.transhipBatch = transhipBatch;
    }
    public String getTranshipState() {
        return transhipState;
    }

    public void setTranshipState(String transhipState) {
        this.transhipState = transhipState;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }
    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }
    public Integer getFrozenBoxNumber() {
        return frozenBoxNumber;
    }

    public void setFrozenBoxNumber(Integer frozenBoxNumber) {
        this.frozenBoxNumber = frozenBoxNumber;
    }
    public Integer getEmptyTubeNumber() {
        return emptyTubeNumber;
    }

    public void setEmptyTubeNumber(Integer emptyTubeNumber) {
        this.emptyTubeNumber = emptyTubeNumber;
    }
    public Integer getEmptyHoleNumber() {
        return emptyHoleNumber;
    }

    public void setEmptyHoleNumber(Integer emptyHoleNumber) {
        this.emptyHoleNumber = emptyHoleNumber;
    }
    public Integer getSampleSatisfaction() {
        return sampleSatisfaction;
    }

    public void setSampleSatisfaction(Integer sampleSatisfaction) {
        this.sampleSatisfaction = sampleSatisfaction;
    }
    public Integer getEffectiveSampleNumber() {
        return effectiveSampleNumber;
    }

    public void setEffectiveSampleNumber(Integer effectiveSampleNumber) {
        this.effectiveSampleNumber = effectiveSampleNumber;
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
    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }

    public void setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
    }

    public List<FrozenBoxDTO> getFrozenBoxDTOList() {
        return frozenBoxDTOList;
    }

    public void setFrozenBoxDTOList(List<FrozenBoxDTO> frozenBoxDTOList) {
        this.frozenBoxDTOList = frozenBoxDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranshipDTO transhipDTO = (TranshipDTO) o;

        if ( ! Objects.equals(id, transhipDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TranshipDTO{" +
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
            '}';
    }
}
