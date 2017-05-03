package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SampleClassification.
 */
@Entity
@Table(name = "sample_class")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SampleClassification extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "sample_classification_name", length = 255, nullable = false)
    private String sampleClassificationName;

    @NotNull
    @Size(max = 100)
    @Column(name = "sample_classification_code", length = 100, nullable = false)
    private String sampleClassificationCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 100)
    @Column(name = "front_color", length = 100, nullable = false)
    private String frontColor;

    @NotNull
    @Size(max = 100)
    @Column(name = "back_color", length = 100, nullable = false)
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

    public SampleClassification sampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
        return this;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public SampleClassification sampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
        return this;
    }

    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getStatus() {
        return status;
    }

    public SampleClassification status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public SampleClassification memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public SampleClassification frontColor(String frontColor) {
        this.frontColor = frontColor;
        return this;
    }

    public void setFrontColor(String frontColor) {
        this.frontColor = frontColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public SampleClassification backColor(String backColor) {
        this.backColor = backColor;
        return this;
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
        SampleClassification sampleClassification = (SampleClassification) o;
        if (sampleClassification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sampleClassification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SampleClassification{" +
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
