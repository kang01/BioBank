package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FrozenBoxType.
 */
@Entity
@Table(name = "frozen_box_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenBoxType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 冻存盒类型编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "frozen_box_type_code", length = 100, nullable = false)
    private String frozenBoxTypeCode;
    /**
     * 冻存盒类型名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "frozen_box_type_name", length = 255, nullable = false)
    private String frozenBoxTypeName;
    /**
     * 冻存盒行数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_box_type_rows", length = 20, nullable = false)
    private String frozenBoxTypeRows;
    /**
     * 冻存盒列数
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "frozen_box_type_columns", length = 20, nullable = false)
    private String frozenBoxTypeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public FrozenBoxType frozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
        return this;
    }

    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }

    public String getFrozenBoxTypeName() {
        return frozenBoxTypeName;
    }

    public FrozenBoxType frozenBoxTypeName(String frozenBoxTypeName) {
        this.frozenBoxTypeName = frozenBoxTypeName;
        return this;
    }

    public void setFrozenBoxTypeName(String frozenBoxTypeName) {
        this.frozenBoxTypeName = frozenBoxTypeName;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }

    public FrozenBoxType frozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
        return this;
    }

    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }

    public FrozenBoxType frozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
        return this;
    }

    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
    }

    public String getMemo() {
        return memo;
    }

    public FrozenBoxType memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public FrozenBoxType status(String status) {
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
        FrozenBoxType frozenBoxType = (FrozenBoxType) o;
        if (frozenBoxType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, frozenBoxType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenBoxType{" +
            "id=" + id +
            ", frozenBoxTypeCode='" + frozenBoxTypeCode + "'" +
            ", frozenBoxTypeName='" + frozenBoxTypeName + "'" +
            ", frozenBoxTypeRows='" + frozenBoxTypeRows + "'" +
            ", frozenBoxTypeColumns='" + frozenBoxTypeColumns + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
