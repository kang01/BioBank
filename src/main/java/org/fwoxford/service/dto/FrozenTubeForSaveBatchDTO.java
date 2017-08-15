package org.fwoxford.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/5/4.
 */
public class FrozenTubeForSaveBatchDTO {

    private Long id;
    /**
     * 样本临时编码
     */
    @Size(max = 100)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @Size(max = 100)
    private String sampleCode;
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
     * 状态：3001：正常，3002：空管，3003：空孔；3004：异常
     */
    @Size(max = 20)
    private String status;
    /**
     * 冻存盒ID
     */
    private Long frozenBoxId;
    /**
     * 冻存盒编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenBoxCode;
    /**
     * 样本类型ID
     */
    private Long sampleTypeId;
    /**
     *样本分类ID
     */
    private Long sampleClassificationId;

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

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }
    private Boolean isHemolysis;
    private Boolean isBloodLipid;
    private Long patientId;

    public Boolean getHemolysis() {
        return isHemolysis;
    }

    public void setHemolysis(Boolean hemolysis) {
        this.isHemolysis = hemolysis;
    }

    public Boolean getBloodLipid() {
        return isBloodLipid;
    }

    public void setBloodLipid(Boolean bloodLipid) {
        this.isBloodLipid = bloodLipid;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
