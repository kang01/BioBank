package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SupportRack.
 */
@Entity
@Table(name = "support_rack")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SupportRack extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_support_rack")
    @SequenceGenerator(name = "seq_support_rack",sequenceName = "seq_support_rack",allocationSize = 1,initialValue = 1)
    private Long id;
    /**
     * 冻存架类型编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "support_rack_type_code", length = 100, nullable = false)
    private String supportRackTypeCode;
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
     * 冻存架编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "support_rack_code", length = 100, nullable = false)
    private String supportRackCode;
    /**
     * 冻存架类型
     */
    @ManyToOne(optional = false)
    @NotNull
    private SupportRackType supportRackType;
    /**
     * 区域
     */
    @ManyToOne(optional = false)
    @NotNull
    private Area area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupportRackTypeCode() {
        return supportRackTypeCode;
    }

    public SupportRack supportRackTypeCode(String supportRackTypeCode) {
        this.supportRackTypeCode = supportRackTypeCode;
        return this;
    }

    public void setSupportRackTypeCode(String supportRackTypeCode) {
        this.supportRackTypeCode = supportRackTypeCode;
    }

    public String getMemo() {
        return memo;
    }

    public SupportRack memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public SupportRack status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupportRackCode() {
        return supportRackCode;
    }

    public SupportRack supportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
        return this;
    }

    public void setSupportRackCode(String supportRackCode) {
        this.supportRackCode = supportRackCode;
    }

    public SupportRackType getSupportRackType() {
        return supportRackType;
    }

    public SupportRack supportRackType(SupportRackType supportRackType) {
        this.supportRackType = supportRackType;
        return this;
    }

    public void setSupportRackType(SupportRackType supportRackType) {
        this.supportRackType = supportRackType;
    }

    public Area getArea() {
        return area;
    }

    public SupportRack area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupportRack supportRack = (SupportRack) o;
        if (supportRack.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, supportRack.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupportRack{" +
            "id=" + id +
            ", supportRackTypeCode='" + supportRackTypeCode + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", supportRackCode='" + supportRackCode + "'" +
            '}';
    }
}
