package org.fwoxford.service.dto.response;

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
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    private String projectCode;
    /**
     * 冻存管编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenTubeCode;
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
     * 冻存盒类型编码
     */
    @NotNull
    @Size(max = 100)
    private String frozenTubeTypeCode;
    /**
     * 冻存盒类型名称
     */
    @NotNull
    @Size(max = 255)
    private String frozenTubeTypeName;
    /**
     * 样本类型编码
     */
    @NotNull
    @Size(max = 100)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @NotNull
    @Size(max = 255)
    private String sampleTypeName;
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
     * 错误类型（6001：位置错误，6002：样本类型错误，6003：其他）
     */
    @Size(max = 20)
    private String errorType;
    /**
     * 状态（3001：正常，3002：空管，3003：空孔；3004：异常）
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 冻存盒类型ID
     */
    private Long frozenTubeTypeId;
    /**
     * 样本类型ID
     */
    private Long sampleTypeId;
    /**
     * 项目ID
     */
    private Long projectId;

    private String frozenBoxCode;

    private Long frozenBoxId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
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

    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }

    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
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

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public void setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
}
