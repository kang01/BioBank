package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the StockOutFiles entity.
 */
public class StockOutFilesDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long businessId;

    @NotNull
    @Size(max = 255)
    private String filePath;

    @NotNull
    @Size(max = 255)
    private String fileName;

    @NotNull
    @Size(max = 100)
    private String fileType;

    private Integer fileSize;

    @Lob
    private byte[] files;
    private String filesContentType;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

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
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
    public byte[] getFiles() {
        return files;
    }

    public void setFiles(byte[] files) {
        this.files = files;
    }

    public String getFilesContentType() {
        return filesContentType;
    }

    public void setFilesContentType(String filesContentType) {
        this.filesContentType = filesContentType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutFilesDTO stockOutFilesDTO = (StockOutFilesDTO) o;

        if ( ! Objects.equals(id, stockOutFilesDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutFilesDTO{" +
            "id=" + id +
            ", businessId='" + businessId + "'" +
            ", filePath='" + filePath + "'" +
            ", fileName='" + fileName + "'" +
            ", fileType='" + fileType + "'" +
            ", fileSize='" + fileSize + "'" +
            ", files='" + files + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
