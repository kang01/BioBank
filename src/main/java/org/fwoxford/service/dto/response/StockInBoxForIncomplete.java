package org.fwoxford.service.dto.response;

import org.fwoxford.domain.SampleType;
import org.fwoxford.service.dto.FrozenBoxTypeDTO;
import org.fwoxford.service.dto.SampleClassificationDTO;
import org.fwoxford.service.dto.SampleTypeDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/5/5.
 */
public class StockInBoxForIncomplete {
    private Long frozenBoxId;
    private String frozenBoxCode;
    private String frozenBoxCode1D;
    private Integer countOfSample;
    private SampleTypeDTO sampleType;
    private FrozenBoxTypeDTO frozenBoxType;
    private SampleClassificationDTO sampleClassification;
    private List<StockInTubeForBox> stockInFrozenTubeList;
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

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public SampleTypeDTO getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleTypeDTO sampleType) {
        this.sampleType = sampleType;
    }

    public SampleClassificationDTO getSampleClassification() {
        return sampleClassification;
    }

    public void setSampleClassification(SampleClassificationDTO sampleClassification) {
        this.sampleClassification = sampleClassification;
    }

    public FrozenBoxTypeDTO getFrozenBoxType() {
        return frozenBoxType;
    }

    public void setFrozenBoxType(FrozenBoxTypeDTO frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }

    public List<StockInTubeForBox> getStockInFrozenTubeList() {
        return stockInFrozenTubeList;
    }

    public void setStockInFrozenTubeList(List<StockInTubeForBox> stockInFrozenTubeList) {
        this.stockInFrozenTubeList = stockInFrozenTubeList;
    }

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
    }

    private Long frozenBoxTypeId;
    private String frozenBoxTypeName;
    private String frozenBoxTypeCode;
    private String frozenBoxTypeRows;
    private String frozenBoxTypeColumns;

    private Long sampleClassificationId;
    private String sampleClassificationName;
    private String sampleClassificationCode;
    private String frontColorForClass;
    private String backColorForClass;

    private Long sampleTypeId;
    private String sampleTypeCode;
    private String sampleTypeName;
    private Integer isMixed;
    private String frontColor;
    private String backColor;

    private Long projectId;
    private String projectName;
    private String projectCode;

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public String getFrozenBoxTypeName() {
        return frozenBoxTypeName;
    }

    public void setFrozenBoxTypeName(String frozenBoxTypeName) {
        this.frozenBoxTypeName = frozenBoxTypeName;
    }

    public String getFrozenBoxTypeCode() {
        return frozenBoxTypeCode;
    }

    public void setFrozenBoxTypeCode(String frozenBoxTypeCode) {
        this.frozenBoxTypeCode = frozenBoxTypeCode;
    }

    public String getFrozenBoxTypeRows() {
        return frozenBoxTypeRows;
    }

    public void setFrozenBoxTypeRows(String frozenBoxTypeRows) {
        this.frozenBoxTypeRows = frozenBoxTypeRows;
    }

    public String getFrozenBoxTypeColumns() {
        return frozenBoxTypeColumns;
    }

    public void setFrozenBoxTypeColumns(String frozenBoxTypeColumns) {
        this.frozenBoxTypeColumns = frozenBoxTypeColumns;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getFrontColorForClass() {
        return frontColorForClass;
    }

    public void setFrontColorForClass(String frontColorForClass) {
        this.frontColorForClass = frontColorForClass;
    }

    public String getBackColorForClass() {
        return backColorForClass;
    }

    public void setBackColorForClass(String backColorForClass) {
        this.backColorForClass = backColorForClass;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
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

    public Integer getIsMixed() {
        return isMixed;
    }

    public void setIsMixed(Integer isMixed) {
        this.isMixed = isMixed;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(String frontColor) {
        this.frontColor = frontColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
