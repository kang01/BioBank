package org.fwoxford.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.domain.SampleType;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Created by gengluying on 2017/4/1.
 */
public class StockInForDataDetail {
    /**
     * 入庫ID
     */
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 项目点编码
     */
    private String projectSiteCode;
    /**
     * 接受日期
     */
    private LocalDate receiveDate;
    /**
     * 接收人
     */
    private String receiver;
    /**
     * 入库日期
     */
    private LocalDate stockInDate;
    /**
     * 状态
     */
    private String status;
    /**
     * 入库人1
     */
    private String storeKeeper1;
    /**
     * 入库人2
     */
    private String storeKeeper2;
    /**
     * 转运编码
     */
    private String transhipCode;
    /**
     * 入库编码
     */
    private String stockInCode;

    @Override
    public String toString() {
        return "StockInForDataDetail{" +
            "id=" + id +
            ", projectId='" + projectId + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", receiveDate=" + receiveDate +
            ", receiver='" + receiver + '\'' +
            ", stockInDate=" + stockInDate +
            ", status='" + status + '\'' +
            ", storeKeeper1='" + storeKeeper1 + '\'' +
            ", storeKeeper2='" + storeKeeper2 + '\'' +
            ", transhipCode='" + transhipCode + '\'' +
            ", stockInCode='" + stockInCode + '\'' +
            '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
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

    public LocalDate getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoreKeeper1() {
        return storeKeeper1;
    }

    public void setStoreKeeper1(String storeKeeper1) {
        this.storeKeeper1 = storeKeeper1;
    }

    public String getStoreKeeper2() {
        return storeKeeper2;
    }

    public void setStoreKeeper2(String storeKeeper2) {
        this.storeKeeper2 = storeKeeper2;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }
}
