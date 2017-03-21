package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SupportRackType.
 */
@Entity
@Table(name = "support_rack_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SupportRackType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "support_rack_type_code", length = 20, nullable = false)
    private String supportRackTypeCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "support_rack_rows", length = 20, nullable = false)
    private String supportRackRows;

    @NotNull
    @Size(max = 20)
    @Column(name = "support_rack_columns", length = 20, nullable = false)
    private String supportRackColumns;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupportRackTypeCode() {
        return supportRackTypeCode;
    }

    public SupportRackType supportRackTypeCode(String supportRackTypeCode) {
        this.supportRackTypeCode = supportRackTypeCode;
        return this;
    }

    public void setSupportRackTypeCode(String supportRackTypeCode) {
        this.supportRackTypeCode = supportRackTypeCode;
    }

    public String getSupportRackRows() {
        return supportRackRows;
    }

    public SupportRackType supportRackRows(String supportRackRows) {
        this.supportRackRows = supportRackRows;
        return this;
    }

    public void setSupportRackRows(String supportRackRows) {
        this.supportRackRows = supportRackRows;
    }

    public String getSupportRackColumns() {
        return supportRackColumns;
    }

    public SupportRackType supportRackColumns(String supportRackColumns) {
        this.supportRackColumns = supportRackColumns;
        return this;
    }

    public void setSupportRackColumns(String supportRackColumns) {
        this.supportRackColumns = supportRackColumns;
    }

    public String getMemo() {
        return memo;
    }

    public SupportRackType memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public SupportRackType status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupportRackType supportRackType = (SupportRackType) o;
        if (supportRackType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, supportRackType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupportRackType{" +
            "id=" + id +
            ", supportRackTypeCode='" + supportRackTypeCode + "'" +
            ", supportRackRows='" + supportRackRows + "'" +
            ", supportRackColumns='" + supportRackColumns + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
