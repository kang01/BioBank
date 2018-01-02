package org.fwoxford.service.dto;

import org.fwoxford.service.dto.response.SampleCountByTypeForm;

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
public class TranshipDTO implements Serializable {

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
     * 运单状态：1001：进行中，1002：待入库，1003：已入库，1090：已作废
     */
    @Size(max = 20)
    private String transhipState;
    /**
     * 接收人ID
     */
    private Long receiverId;
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
     * 临时设备编码
     */
    private String tempEquipmentCode;
    /**
     * 临时区域编码
     */
    private String tempAreaCode;
    /**
     * 临时设备ID
     */
    private Long tempEquipmentId;

    /**
     * 临时区域ID
     */
    private Long tempAreaId;
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

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
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

    public String getTempEquipmentCode() {
        return tempEquipmentCode;
    }

    public void setTempEquipmentCode(String tempEquipmentCode) {
        this.tempEquipmentCode = tempEquipmentCode;
    }

    public String getTempAreaCode() {
        return tempAreaCode;
    }

    public void setTempAreaCode(String tempAreaCode) {
        this.tempAreaCode = tempAreaCode;
    }

    public Long getTempEquipmentId() {
        return tempEquipmentId;
    }

    public void setTempEquipmentId(Long tempEquipmentId) {
        this.tempEquipmentId = tempEquipmentId;
    }

    public Long getTempAreaId() {
        return tempAreaId;
    }

    public void setTempAreaId(Long tempAreaId) {
        this.tempAreaId = tempAreaId;
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
            ", tempAreaId='" + tempAreaId + "'" +
            ", receiveType='" + receiveType + "'" +
//            ", returnBackPeople='" + returnBackPeople + "'" +
            ", stockOutApplyId='" + stockOutApplyId + "'" +
            ", delegateId='" + delegateId + "'" +
            '}';
    }

    private List<SampleCountByTypeForm> sampleCountByTypeForms ;

    public List<SampleCountByTypeForm> getSampleCountByTypeForms() {
        return sampleCountByTypeForms;
    }

    public void setSampleCountByTypeForms(List<SampleCountByTypeForm> sampleCountByTypeForms) {
        this.sampleCountByTypeForms = sampleCountByTypeForms;
    }

    private String receiveType;

//    private String returnBackPeople;

    private Long stockOutApplyId;

    private Long delegateId;

    private String applyPersonName;

    private String applyCode;

    private String delegateName;

    private String invalidReason;

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

//    public String getReturnBackPeople() {
//        return returnBackPeople;
//    }
//
//    public void setReturnBackPeople(String returnBackPeople) {
//        this.returnBackPeople = returnBackPeople;
//    }

    public Long getStockOutApplyId() {
        return stockOutApplyId;
    }

    public void setStockOutApplyId(Long stockOutApplyId) {
        this.stockOutApplyId = stockOutApplyId;
    }

    public Long getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(Long delegateId) {
        this.delegateId = delegateId;
    }

    public String getApplyPersonName() {
        return applyPersonName;
    }

    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    private Long checkTypeId;
    private String checkTypeName;

    public Long getCheckTypeId() {
        return checkTypeId;
    }

    public void setCheckTypeId(Long checkTypeId) {
        this.checkTypeId = checkTypeId;
    }

    public String getCheckTypeName() {
        return checkTypeName;
    }

    public void setCheckTypeName(String checkTypeName) {
        this.checkTypeName = checkTypeName;
    }
}
