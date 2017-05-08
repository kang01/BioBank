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
}
