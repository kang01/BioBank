package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StockOutHandover.
 */
@Entity
@Table(name = "stock_out_handover")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutHandover extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "handover_code", length = 100, nullable = false)
    private String handoverCode;

    @Size(max = 255)
    @Column(name = "receiver_name", length = 255)
    private String receiverName;

    @Size(max = 255)
    @Column(name = "receiver_phone", length = 255)
    private String receiverPhone;

    @Size(max = 255)
    @Column(name = "receiver_organization", length = 255)
    private String receiverOrganization;

    @Column(name = "handover_person_id")
    private Long handoverPersonId;

    @Column(name = "handover_time")
    private LocalDate handoverTime;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne
    private StockOutTask stockOutTask;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutApply stockOutApply;

    @ManyToOne
    private StockOutPlan stockOutPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandoverCode() {
        return handoverCode;
    }

    public StockOutHandover handoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
        return this;
    }

    public void setHandoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public StockOutHandover receiverName(String receiverName) {
        this.receiverName = receiverName;
        return this;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public StockOutHandover receiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
        return this;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverOrganization() {
        return receiverOrganization;
    }

    public StockOutHandover receiverOrganization(String receiverOrganization) {
        this.receiverOrganization = receiverOrganization;
        return this;
    }

    public void setReceiverOrganization(String receiverOrganization) {
        this.receiverOrganization = receiverOrganization;
    }

    public Long getHandoverPersonId() {
        return handoverPersonId;
    }

    public StockOutHandover handoverPersonId(Long handoverPersonId) {
        this.handoverPersonId = handoverPersonId;
        return this;
    }

    public void setHandoverPersonId(Long handoverPersonId) {
        this.handoverPersonId = handoverPersonId;
    }

    public LocalDate getHandoverTime() {
        return handoverTime;
    }

    public StockOutHandover handoverTime(LocalDate handoverTime) {
        this.handoverTime = handoverTime;
        return this;
    }

    public void setHandoverTime(LocalDate handoverTime) {
        this.handoverTime = handoverTime;
    }

    public String getStatus() {
        return status;
    }

    public StockOutHandover status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutHandover memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public StockOutTask getStockOutTask() {
        return stockOutTask;
    }

    public StockOutHandover stockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
        return this;
    }

    public void setStockOutTask(StockOutTask stockOutTask) {
        this.stockOutTask = stockOutTask;
    }

    public StockOutApply getStockOutApply() {
        return stockOutApply;
    }

    public StockOutHandover stockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
        return this;
    }

    public void setStockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
    }

    public StockOutPlan getStockOutPlan() {
        return stockOutPlan;
    }

    public StockOutHandover stockOutPlan(StockOutPlan stockOutPlan) {
        this.stockOutPlan = stockOutPlan;
        return this;
    }

    public void setStockOutPlan(StockOutPlan stockOutPlan) {
        this.stockOutPlan = stockOutPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutHandover StockOutHandover = (StockOutHandover) o;
        if (StockOutHandover.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, StockOutHandover.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutHandover{" +
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
