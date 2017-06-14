package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by gengluying on 2017/5/23.
 */
@Entity
@Table(name = "view_stock_out_handover")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutHandoverForDataTableEntity {
    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "handover_code")
    private String handoverCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "apply_code")
    private String applyCode;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "count_of_sample")
    private Long countOfSample;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "purpose_of_sample")
    private String usage;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name= "handover_time")
    private LocalDate receiveDate;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name= "receiver_name")
    private String receiver;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name= "deliver_name")
    private String deliverName;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name= "status")
    private String status;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name= "memo")
    private String memo;

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

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
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
}
