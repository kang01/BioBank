package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CheckType.
 */
@Entity
@Table(name = "check_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CheckType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_check_type")
    @SequenceGenerator(name = "seq_check_type",sequenceName = "seq_check_type",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "check_type_code", length = 100, nullable = false)
    private String checkTypeCode;

    @Size(max = 100)
    @Column(name = "check_type_name", length = 100)
    private String checkTypeName;

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

    public String getCheckTypeCode() {
        return checkTypeCode;
    }

    public CheckType checkTypeCode(String checkTypeCode) {
        this.checkTypeCode = checkTypeCode;
        return this;
    }

    public void setCheckTypeCode(String checkTypeCode) {
        this.checkTypeCode = checkTypeCode;
    }

    public String getCheckTypeName() {
        return checkTypeName;
    }

    public CheckType checkTypeName(String checkTypeName) {
        this.checkTypeName = checkTypeName;
        return this;
    }

    public void setCheckTypeName(String checkTypeName) {
        this.checkTypeName = checkTypeName;
    }

    public String getStatus() {
        return status;
    }

    public CheckType status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public CheckType memo(String memo) {
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
        CheckType checkType = (CheckType) o;
        if (checkType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, checkType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CheckType{" +
            "id=" + id +
            ", checkTypeCode='" + checkTypeCode + "'" +
            ", checkTypeName='" + checkTypeName + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
