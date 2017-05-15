package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Delegate entity.
 */
public class DelegateDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String delegate_code;

    @NotNull
    @Size(max = 255)
    private String delegate_name;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
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

    public void setDelegate_code(String delegate_code) {
        this.delegate_code = delegate_code;
    }
    public String getDelegate_name() {
        return delegate_name;
    }

    public void setDelegate_name(String delegate_name) {
        this.delegate_name = delegate_name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DelegateDTO delegateDTO = (DelegateDTO) o;

        if ( ! Objects.equals(id, delegateDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DelegateDTO{" +
            "id=" + id +
            ", delegate_code='" + delegate_code + "'" +
            ", delegate_name='" + delegate_name + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
