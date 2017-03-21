package org.fwoxford.service.dto;

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

    private Long id;

    @NotNull
    @Size(max = 100)
    private String projectCode;

    @NotNull
    @Size(max = 100)
    private String project_site_code;

    @NotNull
    private LocalDate receiveDate;

    @NotNull
    @Max(value = 100)
    private Long receiveId;

    @NotNull
    @Size(max = 100)
    private String receiveName;

    @NotNull
    @Size(max = 20)
    private String storageInType;

    @NotNull
    @Max(value = 100)
    private Long storageInPersonId1;

    @NotNull
    @Size(max = 100)
    private String storageInPersonName1;

    @NotNull
    @Max(value = 100)
    private Long storageInPersonId2;

    @NotNull
    @Size(max = 100)
    private String storangeInPersonName2;

    @NotNull
    private LocalDate storageInDate;

    @NotNull
    @Max(value = 100)
    private Integer sampleNumber;

    @NotNull
    @Max(value = 100)
    private Long signId;

    @NotNull
    @Size(max = 100)
    private String signName;

    @NotNull
    private LocalDate signDate;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

    private Long transhipId;

    private Long projectId;

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
    public String getProject_site_code() {
        return project_site_code;
    }

    public void setProject_site_code(String project_site_code) {
        this.project_site_code = project_site_code;
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
            ", project_site_code='" + project_site_code + "'" +
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
