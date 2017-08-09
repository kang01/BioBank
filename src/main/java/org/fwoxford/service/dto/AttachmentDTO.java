package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Attachment entity.
 */
public class AttachmentDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Long businessId;

    @Size(max = 20)
    private String businessType;

    private Long fileId1;

    private Long fileId2;

    @Size(max = 255)
    private String fileTitle;

    @Size(max = 255)
    private String fileName;

    @Size(max = 1024)
    private String description;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private String smallImage;
    private String bigImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public Long getFileId1() {
        return fileId1;
    }

    public void setFileId1(Long fileId1) {
        this.fileId1 = fileId1;
    }
    public Long getFileId2() {
        return fileId2;
    }

    public void setFileId2(Long fileId2) {
        this.fileId2 = fileId2;
    }
    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getBigImage() {
        return bigImage;
    }
    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }
}
