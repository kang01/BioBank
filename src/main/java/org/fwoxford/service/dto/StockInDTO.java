package org.fwoxford.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockIn entity.
 */
public class StockInDTO extends AbstractAuditingDTO implements Serializable {
    private Long id;
    /**
     * 入库编码
     */
    @Size(max = 100)
    private String stockInCode;
    /**
     * 项目编码
     */
    @Size(max = 100)
    private String projectCode;
    /**
     * 项目点编码
     */
    @Size(max = 100)
    private String projectSiteCode;
    /**
     * 接受日期
     */
    private LocalDate receiveDate;
    /**
     * 接受人ID
     */
    @Max(value = 100)
    private Long receiveId;
    /**
     * 接受人姓名
     */
    @Size(max = 100)
    private String receiveName;
    private String receiver;
    /**
     * 入库类型：（首次入库，移位入库，调整入库）
     */
    @Size(max = 20)
    private String stockInType;
    /**
     * 入库人1ID
     */
    @Max(value = 100)
    private Long storeKeeperId1;
    /**
     * 入库人1姓名
     */
    @Size(max = 100)
    private String storeKeeper1;
    /**
     * 入库人2ID
     */
    @Max(value = 100)
    private Long storeKeeperId2;
    /**
     * 入库人2姓名
     */
    @Size(max = 100)
    private String storeKeeper2;
    /**
     * 入库日期
     */
    private LocalDate stockInDate;
    /**
     * 样本数量
     */
    private Integer countOfSample;
    /**
     * 签名人ID
     */
    @Max(value = 100)
    private Long signId;
    /**
     * 签名人姓名
     */
    @Size(max = 100)
    private String signName;
    /**
     * 签名日期
     */
    private LocalDate signDate;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态 :7001：进行中，7002已入库
     */
    @Size(max = 20)
    private String status;
    /**
     * 转运ID
     */
    private Long transhipId;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目点ID
     */
    private Long projectSiteId;
    /**
     * 上级入库单ID
     */
    private Long parentStockInId;
    /**
     * 转运编码
     */
    private String transhipCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
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
    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }
    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getStockInType() {
        return stockInType;
    }

    public void setStockInType(String stockInType) {
        this.stockInType = stockInType;
    }
    public Long getStoreKeeperId1() {
        return storeKeeperId1;
    }

    public void setStoreKeeperId1(Long storeKeeperId1) {
        this.storeKeeperId1 = storeKeeperId1;
    }
    public String getStoreKeeper1() {
        return storeKeeper1;
    }

    public void setStoreKeeper1(String storeKeeper1) {
        this.storeKeeper1 = storeKeeper1;
    }
    public Long getStoreKeeperId2() {
        return storeKeeperId2;
    }

    public void setStoreKeeperId2(Long storeKeeperId2) {
        this.storeKeeperId2 = storeKeeperId2;
    }
    public String getStoreKeeper2() {
        return storeKeeper2;
    }

    public void setStoreKeeper2(String storeKeeper2) {
        this.storeKeeper2 = storeKeeper2;
    }
    public LocalDate getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
    }
    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }
    public Long getSignId() {
        return signId;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }
    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }

    public void setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
    }

    public Long getParentStockInId() {
        return parentStockInId;
    }

    public void setParentStockInId(Long parentStockInId) {
        this.parentStockInId = parentStockInId;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockInDTO stockInDTO = (StockInDTO) o;

        if ( ! Objects.equals(id, stockInDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockInDTO{" +
            "id=" + id +
            ", stockInCode='" + stockInCode + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", receiveDate='" + receiveDate + "'" +
            ", receiveId='" + receiveId + "'" +
            ", receiveName='" + receiveName + "'" +
            ", stockInType='" + stockInType + "'" +
            ", storeKeeperId1='" + storeKeeperId1 + "'" +
            ", storeKeeper1='" + storeKeeper1 + "'" +
            ", storeKeeperId2='" + storeKeeperId2 + "'" +
            ", storeKeeper2='" + storeKeeper2 + "'" +
            ", stockInDate='" + stockInDate + "'" +
            ", countOfSample='" + countOfSample + "'" +
            ", signId='" + signId + "'" +
            ", signName='" + signName + "'" +
            ", signDate='" + signDate + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
