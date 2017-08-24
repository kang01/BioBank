package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Delegate.
 */
@Entity
@Table(name = "delegate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Delegate extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_delegate")
    @SequenceGenerator(name = "seq_delegate",sequenceName = "seq_delegate",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "delegate_code", length = 100, nullable = false)
    private String delegateCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "delegate_name", length = 255, nullable = false)
    private String delegateName;

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

    public String getDelegateCode() {
        return delegateCode;
    }

    public Delegate delegateCode(String delegateCode) {
        this.delegateCode = delegateCode;
        return this;
    }

    public void setDelegateCode(String delegateCode) {
        this.delegateCode = delegateCode;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public Delegate delegateName(String delegateName) {
        this.delegateName = delegateName;
        return this;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getStatus() {
        return status;
    }

    public Delegate status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public Delegate memo(String memo) {
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
        Delegate delegate = (Delegate) o;
        if (delegate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, delegate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Delegate{" +
            "id=" + id +
            ", delegateCode='" + delegateCode + "'" +
            ", delegateName='" + delegateName + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
