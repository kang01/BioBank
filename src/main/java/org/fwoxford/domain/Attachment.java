package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Attachment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_attachment")
    @SequenceGenerator(name = "seq_attachment",sequenceName = "seq_attachment",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @NotNull
    @Size(max = 20)
    @Column(name = "business_type", length = 20, nullable = false)
    private String businessType;

    @NotNull
    @Column(name = "file_id_1", nullable = false)
    private Long fileId1;

    @Column(name = "file_id_2")
    private Long fileId2;

    @Size(max = 255)
    @Column(name = "file_title", length = 255)
    private String fileTitle;

    @Size(max = 255)
    @Column(name = "file_name", length = 255)
    private String fileName;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

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

    public Attachment businessId(Long businessId) {
        this.businessId = businessId;
        return this;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public Attachment businessType(String businessType) {
        this.businessType = businessType;
        return this;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getFileId1() {
        return fileId1;
    }

    public Attachment fileId1(Long fileId1) {
        this.fileId1 = fileId1;
        return this;
    }

    public void setFileId1(Long fileId1) {
        this.fileId1 = fileId1;
    }

    public Long getFileId2() {
        return fileId2;
    }

    public Attachment fileId2(Long fileId2) {
        this.fileId2 = fileId2;
        return this;
    }

    public void setFileId2(Long fileId2) {
        this.fileId2 = fileId2;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public Attachment fileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
        return this;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public Attachment fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public Attachment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public Attachment status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public Attachment memo(String memo) {
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
        Attachment attachment = (Attachment) o;
        if (attachment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, attachment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + id +
            ", businessId='" + businessId + "'" +
            ", businessType='" + businessType + "'" +
            ", fileId1='" + fileId1 + "'" +
            ", fileId2='" + fileId2 + "'" +
            ", fileTitle='" + fileTitle + "'" +
            ", fileName='" + fileName + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
