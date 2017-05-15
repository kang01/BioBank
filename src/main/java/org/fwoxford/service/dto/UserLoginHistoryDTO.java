package org.fwoxford.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UserLoginHistory entity.
 */
public class UserLoginHistoryDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String userToken;

    @NotNull
    private Long loginUserId;

    @NotNull
    @Size(max = 255)
    private String businessName;

    @NotNull
    private ZonedDateTime invalidDate;

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
    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public ZonedDateTime getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(ZonedDateTime invalidDate) {
        this.invalidDate = invalidDate;
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

        UserLoginHistoryDTO userLoginHistoryDTO = (UserLoginHistoryDTO) o;

        if ( ! Objects.equals(id, userLoginHistoryDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserLoginHistoryDTO{" +
            "id=" + id +
            ", userToken='" + userToken + "'" +
            ", loginUserId='" + loginUserId + "'" +
            ", businessName='" + businessName + "'" +
            ", invalidDate='" + invalidDate + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
