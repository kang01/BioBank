package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SampleType.
 */
@Entity
@Table(name = "sample_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SampleType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 样本类型编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "sample_type_code", length = 100, nullable = false)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "sample_type_name", length = 255, nullable = false)
    private String sampleTypeName;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 前景色
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "front_color", length = 100, nullable = false)
    private String frontColor;
    /**
     * 背景色
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "back_color", length = 100, nullable = false)
    private String backColor;

    @NotNull
    @Max(value = 20)
    @Column(name = "is_mixed",  nullable = false)
    private Integer isMixed;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public SampleType sampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
        return this;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public SampleType sampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
        return this;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getMemo() {
        return memo;
    }

    public SampleType memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public SampleType status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public SampleType frontColor(String frontColor) {
        this.frontColor = frontColor;
        return this;
    }

    public void setFrontColor(String frontColor) {
        this.frontColor = frontColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public SampleType backColor(String backColor) {
        this.backColor = backColor;
        return this;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public Integer getIsMixed() {
        return isMixed;
    }

    public SampleType isMixed(Integer isMixed) {
        this.isMixed = isMixed;
        return this;
    }
    public void setIsMixed(Integer isMixed) {
        this.isMixed = isMixed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SampleType sampleType = (SampleType) o;
        if (sampleType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sampleType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SampleType{" +
            "id=" + id +
            ", sampleTypeCode='" + sampleTypeCode + "'" +
            ", sampleTypeName='" + sampleTypeName + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", frontColor='" + frontColor + "'" +
            ", backColor='" + backColor + "'" +
            ", isMixed='" + isMixed + "'" +
            '}';
    }
}
