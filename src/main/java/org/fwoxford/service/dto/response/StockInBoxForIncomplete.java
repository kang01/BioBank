package org.fwoxford.service.dto.response;

import org.fwoxford.service.dto.SampleClassificationDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/5/5.
 */
public class StockInBoxForIncomplete {
    private Long frozenBoxId;
    private String frozenBoxCode;
    private Integer countOfSample;
    private Long sampleTypeId;
    private SampleClassificationDTO sampleClassificationDTO;
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

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public SampleClassificationDTO getSampleClassificationDTO() {
        return sampleClassificationDTO;
    }

    public void setSampleClassificationDTO(SampleClassificationDTO sampleClassificationDTO) {
        this.sampleClassificationDTO = sampleClassificationDTO;
    }

    public List<StockInTubeForBox> getStockInFrozenTubeList() {
        return stockInFrozenTubeList;
    }

    public void setStockInFrozenTubeList(List<StockInTubeForBox> stockInFrozenTubeList) {
        this.stockInFrozenTubeList = stockInFrozenTubeList;
    }
}
