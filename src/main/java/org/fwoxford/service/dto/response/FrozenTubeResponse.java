package org.fwoxford.service.dto.response;

import org.fwoxford.domain.FrozenTubeType;
import org.fwoxford.domain.SampleClassification;
import org.fwoxford.domain.SampleType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/3/22.
 */
public class FrozenTubeResponse {
    /**
     * 冻存管ID
     */
    private Long id;
    /**
     * 样本临时编码
     */
    @NotNull
    @Size(max = 100)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @NotNull
    @Size(max = 100)
    private String sampleCode;
    /**
     * 冻存管
     */
    private FrozenTubeType frozenTubeType;
    /**
     * 样本类型
     */
    private SampleType sampleType;
    /**
     * 行号
     */
    @NotNull
    @Size(max = 20)
    private String tubeRows;
    /**
     * 列号
     */
    @NotNull
    @Size(max = 20)
    private String tubeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 状态（3001：正常，3002：空管，3003：空孔；3004：异常）
     */
    @NotNull
    @Size(max = 20)
    private String status;

    private String frozenBoxCode;

    private Long frozenBoxId;
    /**
     * 是否出库样本标识：1：是
     */
    private Integer stockOutFlag;

    /**
     * 撤销原因
     */
    private String repealReason;

    private SampleClassification sampleClassification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public FrozenTubeType getFrozenTubeType() {
        return frozenTubeType;
    }

    public void setFrozenTubeType(FrozenTubeType frozenTubeType) {
        this.frozenTubeType = frozenTubeType;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
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

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public Integer getStockOutFlag() {
        return stockOutFlag;
    }

    public void setStockOutFlag(Integer stockOutFlag) {
        this.stockOutFlag = stockOutFlag;
    }

    public String getRepealReason() {
        return repealReason;
    }

    public void setRepealReason(String repealReason) {
        this.repealReason = repealReason;
    }

    public SampleClassification getSampleClassification() {
        return sampleClassification;
    }

    public void setSampleClassification(SampleClassification sampleClassification) {
        this.sampleClassification = sampleClassification;
    }
}
