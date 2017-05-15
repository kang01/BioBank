package org.fwoxford.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutHandover entity.
 */
public class StockOutHandoverDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String handoverCode;

    @Size(max = 255)
    private String receiverName;

    @Size(max = 255)
    private String receiverPhone;

    @Size(max = 255)
    private String receiverOrganization;

    private Long handoverPersonId;

    private LocalDate handoverTime;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long stockOutTaskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getHandoverCode() {
        return handoverCode;
    }

    public void setHandoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
    }
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
    public String getReceiverOrganization() {
        return receiverOrganization;
    }

    public void setReceiverOrganization(String receiverOrganization) {
        this.receiverOrganization = receiverOrganization;
    }
    public Long getHandoverPersonId() {
        return handoverPersonId;
    }

    public void setHandoverPersonId(Long handoverPersonId) {
        this.handoverPersonId = handoverPersonId;
    }
    public LocalDate getHandoverTime() {
        return handoverTime;
    }

    public void setHandoverTime(LocalDate handoverTime) {
        this.handoverTime = handoverTime;
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

    public Long getStockOutTaskId() {
        return stockOutTaskId;
    }

    public void setStockOutTaskId(Long stockOutTaskId) {
        this.stockOutTaskId = stockOutTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutHandoverDTO stockOutHandoverDTO = (StockOutHandoverDTO) o;

        if ( ! Objects.equals(id, stockOutHandoverDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutHandoverDTO{" +
            "id=" + id +
            ", handoverCode='" + handoverCode + "'" +
            ", receiverName='" + receiverName + "'" +
            ", receiverPhone='" + receiverPhone + "'" +
            ", receiverOrganization='" + receiverOrganization + "'" +
            ", handoverPersonId='" + handoverPersonId + "'" +
            ", handoverTime='" + handoverTime + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
