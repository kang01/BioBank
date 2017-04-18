package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by zhuyu on 2017/3/31.
 */
public class StockInForDataTable {
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private LocalDate recordDate;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private LocalDate stockInDate;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Integer countOfSample;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Integer countOfBox;

    @NotNull
    @Size(max = 20)
    @JsonView(DataTablesOutput.View.class)
    private String status;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String storeKeeper1;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String storeKeeper2;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String projectCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String projectSiteCode;
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String transhipCode;

    private String stockInCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockInForDataTable response = (StockInForDataTable) o;

        if ( ! Objects.equals(getId(), response.getId())) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockInForDataTable{" +
            "id=" + id +
            ", recordDate=" + recordDate +
            ", stockInDate=" + stockInDate +
            ", countOfSample=" + countOfSample +
            ", countOfBox=" + countOfBox +
            ", status='" + status + '\'' +
            ", storeKeeper1='" + storeKeeper1 + '\'' +
            ", storeKeeper2='" + storeKeeper2 + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectSiteCode='" + projectSiteCode + '\'' +
            ", transhipCode='" + transhipCode + '\'' +
            ", stockInCode='" + stockInCode + '\'' +
            '}';
    }

    /**
     * 入库单ID
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 入库日期
     */
    public LocalDate getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
    }

    /**
     * 样本数量
     */
    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    /**
     * 冻存盒数量
     */
    public Integer getCountOfBox() {
        return countOfBox;
    }

    public void setCountOfBox(Integer countOfBox) {
        this.countOfBox = countOfBox;
    }

    /**
     * 转运状态：1001：进行中，1002：待入库，1003：已入库，1004：已作废
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 库管员1
     */
    public String getStoreKeeper1() {
        return storeKeeper1;
    }

    public void setStoreKeeper1(String storeKeeper1) {
        this.storeKeeper1 = storeKeeper1;
    }

    /**
     * 库管员2
     */
    public String getStoreKeeper2() {
        return storeKeeper2;
    }

    public void setStoreKeeper2(String storeKeeper2) {
        this.storeKeeper2 = storeKeeper2;
    }

    /**
     * 项目编码
     */
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 项目点编码
     */
    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    /**
     * 转运编码
     */
    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    /**
     * 记录日期
     */
    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }
}
