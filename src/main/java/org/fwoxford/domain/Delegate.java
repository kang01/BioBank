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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "delegate_code", length = 100, nullable = false)
    private String delegate_code;

    @NotNull
    @Size(max = 255)
    @Column(name = "delegate_name", length = 255, nullable = false)
    private String delegate_name;

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

    public String getDelegate_code() {
        return delegate_code;
    }

    public Delegate delegate_code(String delegate_code) {
        this.delegate_code = delegate_code;
        return this;
    }

    public void setDelegate_code(String delegate_code) {
        this.delegate_code = delegate_code;
    }

    public String getDelegate_name() {
        return delegate_name;
    }

    public Delegate delegate_name(String delegate_name) {
        this.delegate_name = delegate_name;
        return this;
    }

    public void setDelegate_name(String delegate_name) {
        this.delegate_name = delegate_name;
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
            ", delegate_code='" + delegate_code + "'" +
            ", delegate_name='" + delegate_name + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
