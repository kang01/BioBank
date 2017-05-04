package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SampleClassification entity.
 */
public class SampleClassificationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String sampleClassificationName;

    @NotNull
    @Size(max = 100)
    private String sampleClassificationCode;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    private String frontColor;

    @NotNull
    @Size(max = 100)
    private String backColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }
    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
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
    public String getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(String frontColor) {
        this.frontColor = frontColor;
    }
    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SampleClassificationDTO sampleClassificationDTO = (SampleClassificationDTO) o;

        if ( ! Objects.equals(id, sampleClassificationDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SampleClassificationDTO{" +
            "id=" + id +
            ", sampleClassificationName='" + sampleClassificationName + "'" +
            ", sampleClassificationCode='" + sampleClassificationCode + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            ", frontColor='" + frontColor + "'" +
            ", backColor='" + backColor + "'" +
            '}';
    }
}
