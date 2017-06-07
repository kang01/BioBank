package org.fwoxford.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the StockOutHandOver entity.
 */
public class StockOutHandoverDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String handoverCode;

    @Size(max = 255)
    private String receiverName;

    @Size(max = 255)
    private String receiverPhone;

    @Size(max = 255)
    private String receiverOrganization;

    private Long handoverPersonId;
    private String handoverPersonName;

    private LocalDate handoverTime;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long stockOutTaskId;

    private Long stockOutApplyId;

    private Long stockOutPlanId;

    private String stockOutTaskCode;

    private String stockOutApplyCode;

    private String stockOutPlanCode;

    private String password;

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

    public Long getStockOutApplyId() {
        return stockOutApplyId;
    }

    public void setStockOutApplyId(Long stockOutApplyId) {
        this.stockOutApplyId = stockOutApplyId;
    }

    public Long getStockOutPlanId() {
        return stockOutPlanId;
    }

    public void setStockOutPlanId(Long stockOutPlanId) {
        this.stockOutPlanId = stockOutPlanId;
    }

    public String getHandoverPersonName() {
        return handoverPersonName;
    }

    public void setHandoverPersonName(String handoverPersonName) {
        this.handoverPersonName = handoverPersonName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }

    public String getStockOutApplyCode() {
        return stockOutApplyCode;
    }

    public void setStockOutApplyCode(String stockOutApplyCode) {
        this.stockOutApplyCode = stockOutApplyCode;
    }

    public String getStockOutPlanCode() {
        return stockOutPlanCode;
    }

    public void setStockOutPlanCode(String stockOutPlanCode) {
        this.stockOutPlanCode = stockOutPlanCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOutHandoverDTO stockOutHandOverDTO = (StockOutHandoverDTO) o;

        if ( ! Objects.equals(id, stockOutHandOverDTO.id)) { return false; }

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
            ", handoverCode='" + handoverCode + '\'' +
            ", receiverName='" + receiverName + '\'' +
            ", receiverPhone='" + receiverPhone + '\'' +
            ", receiverOrganization='" + receiverOrganization + '\'' +
            ", handoverPersonId=" + handoverPersonId +
            ", handoverPersonName='" + handoverPersonName + '\'' +
            ", handoverTime=" + handoverTime +
            ", status='" + status + '\'' +
            ", memo='" + memo + '\'' +
            ", stockOutTaskId=" + stockOutTaskId +
            ", stockOutApplyId=" + stockOutApplyId +
            ", stockOutPlanId=" + stockOutPlanId +
            ", stockOutTaskCode=" + stockOutTaskCode +
            ", stockOutApplyCode=" + stockOutApplyCode +
            ", stockOutPlanCode=" + stockOutPlanCode +
            '}';
    }
}
