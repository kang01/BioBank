package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TaskUserHistory.
 */
@Entity
@Table(name = "task_user_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskUserHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_task_user_history")
    @SequenceGenerator(name = "seq_task_user_history",sequenceName = "seq_task_user_history",allocationSize = 1,initialValue = 1)
    private Long id;

    @Size(max = 255)
    @Column(name = "user_token", length = 255)
    private String userToken;

    @NotNull
    @Column(name = "login_user_id", nullable = false)
    private Long loginUserId;

    @NotNull
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @NotNull
    @Size(max = 255)
    @Column(name = "business_name", length = 255, nullable = false)
    private String businessName;

    @NotNull
    @Column(name = "invalid_date", nullable = false)
    private ZonedDateTime invalidDate;

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

    public String getUserToken() {
        return userToken;
    }

    public TaskUserHistory userToken(String userToken) {
        this.userToken = userToken;
        return this;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Long getLoginUserId() {
        return loginUserId;
    }

    public TaskUserHistory loginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
        return this;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public TaskUserHistory businessId(Long businessId) {
        this.businessId = businessId;
        return this;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public TaskUserHistory businessName(String businessName) {
        this.businessName = businessName;
        return this;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public ZonedDateTime getInvalidDate() {
        return invalidDate;
    }

    public TaskUserHistory invalidDate(ZonedDateTime invalidDate) {
        this.invalidDate = invalidDate;
        return this;
    }

    public void setInvalidDate(ZonedDateTime invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getStatus() {
        return status;
    }

    public TaskUserHistory status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public TaskUserHistory memo(String memo) {
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
        TaskUserHistory TaskUserHistory = (TaskUserHistory) o;
        if (TaskUserHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, TaskUserHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskUserHistory{" +
            "id=" + id +
            ", userToken='" + userToken + "'" +
            ", loginUserId='" + loginUserId + "'" +
            ", businessId='" + businessId + "'" +
            ", businessName='" + businessName + "'" +
            ", invalidDate='" + invalidDate + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
