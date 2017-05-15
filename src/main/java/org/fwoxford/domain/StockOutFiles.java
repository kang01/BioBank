package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutFiles.
 */
@Entity
@Table(name = "stock_out_files")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutFiles extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_path", length = 255, nullable = false)
    private String filePath;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;

    @NotNull
    @Size(max = 100)
    @Column(name = "file_type", length = 100, nullable = false)
    private String fileType;

    @Column(name = "file_size")
    private Integer fileSize;

    @Lob
    @Column(name = "files")
    private byte[] files;

    @Column(name = "files_content_type")
    private String filesContentType;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
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

    public StockOutFiles businessId(Long businessId) {
        this.businessId = businessId;
        return this;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getFilePath() {
        return filePath;
    }

    public StockOutFiles filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public StockOutFiles fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public StockOutFiles fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public StockOutFiles fileSize(Integer fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFiles() {
        return files;
    }

    public StockOutFiles files(byte[] files) {
        this.files = files;
        return this;
    }

    public void setFiles(byte[] files) {
        this.files = files;
    }

    public String getFilesContentType() {
        return filesContentType;
    }

    public StockOutFiles filesContentType(String filesContentType) {
        this.filesContentType = filesContentType;
        return this;
    }

    public void setFilesContentType(String filesContentType) {
        this.filesContentType = filesContentType;
    }

    public String getStatus() {
        return status;
    }

    public StockOutFiles status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutFiles memo(String memo) {
        this.memo = memo;
        return this;
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
        StockOutFiles stockOutFiles = (StockOutFiles) o;
        if (stockOutFiles.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutFiles.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutFiles{" +
            "id=" + id +
            ", businessId='" + businessId + "'" +
            ", filePath='" + filePath + "'" +
            ", fileName='" + fileName + "'" +
            ", fileType='" + fileType + "'" +
            ", fileSize='" + fileSize + "'" +
            ", files='" + files + "'" +
            ", filesContentType='" + filesContentType + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
