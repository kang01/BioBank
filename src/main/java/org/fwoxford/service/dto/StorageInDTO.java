package org.fwoxford.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StorageIn entity.
 */
public class StorageInDTO extends AbstractAuditingDTO implements Serializable {
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    /**
     * 项目编码
     */
    @Size(max = 100)
    private String projectCode;
    /**
     * 项目点编码
     */
    @Size(max = 100)
    private String projectSiteCode;
    /**
     * 接受日期
     */
    private LocalDate receiveDate;
    /**
     * 接受人ID
     */
    @Max(value = 100)
    private Long receiveId;
    /**
     * 接受人姓名
     */
    @Size(max = 100)
    private String receiveName;
    /**
     * 入库类型：（首次入库，移位入库，调整入库）
     */
    @NotNull
    @Size(max = 20)
    private String storageInType;
    /**
     * 入库人1ID
     */
    @Max(value = 100)
    private Long storageInPersonId1;
    /**
     * 入库人1姓名
     */
    @Size(max = 100)
    private String storageInPersonName1;
    /**
     * 入库人2ID
     */
    @Max(value = 100)
    private Long storageInPersonId2;
    /**
     * 入库人2姓名
     */
    @Size(max = 100)
    private String storangeInPersonName2;
    /**
     * 入库日期
     */
    @JsonView(DataTablesOutput.View.class)
    private LocalDate storageInDate;
    /**
     * 样本数量
     */
    @Max(value = 100)
    @JsonView(DataTablesOutput.View.class)
    private Integer sampleNumber;
    /**
     * 签名人ID
     */
    @Max(value = 100)
    private Long signId;
    /**
     * 卡名人姓名
     */
    @Size(max = 100)
    @JsonView(DataTablesOutput.View.class)
    private String signName;
    /**
     * 签名日期
     */
    private LocalDate signDate;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态
     */
    @Size(max = 20)
    @JsonView(DataTablesOutput.View.class)
    private String status;
    /**
     * 转运ID
     */
    private Long transhipId;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目点ID
     */
    private Long projectSiteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }
    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }
    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }
    public String getStorageInType() {
        return storageInType;
    }

    public void setStorageInType(String storageInType) {
        this.storageInType = storageInType;
    }
    public Long getStorageInPersonId1() {
        return storageInPersonId1;
    }

    public void setStorageInPersonId1(Long storageInPersonId1) {
        this.storageInPersonId1 = storageInPersonId1;
    }
    public String getStorageInPersonName1() {
        return storageInPersonName1;
    }

    public void setStorageInPersonName1(String storageInPersonName1) {
        this.storageInPersonName1 = storageInPersonName1;
    }
    public Long getStorageInPersonId2() {
        return storageInPersonId2;
    }

    public void setStorageInPersonId2(Long storageInPersonId2) {
        this.storageInPersonId2 = storageInPersonId2;
    }
    public String getStorangeInPersonName2() {
        return storangeInPersonName2;
    }

    public void setStorangeInPersonName2(String storangeInPersonName2) {
        this.storangeInPersonName2 = storangeInPersonName2;
    }
    public LocalDate getStorageInDate() {
        return storageInDate;
    }

    public void setStorageInDate(LocalDate storageInDate) {
        this.storageInDate = storageInDate;
    }
    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }
    public Long getSignId() {
        return signId;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }
    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
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

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StorageInDTO storageInDTO = (StorageInDTO) o;

        if ( ! Objects.equals(id, storageInDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StorageInDTO{" +
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
