package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SampleType entity.
 */
public class SampleTypeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;

    @NotNull
    @Size(max = 255)
    private String sampleTypeName;

    @Size(max = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    private String status;

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
    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }
    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
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

        SampleTypeDTO sampleTypeDTO = (SampleTypeDTO) o;

        if ( ! Objects.equals(id, sampleTypeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SampleTypeDTO{" +
            "id=" + id +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", frontColor='" + frontColor + "'" +
            ", backColor='" + backColor + "'" +
            '}';
    }
}
