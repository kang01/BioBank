package org.fwoxford.service.dto.response;

import io.swagger.models.auth.In;
import org.fwoxford.domain.SampleType;
import org.fwoxford.service.dto.StockInTubeDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxForDataMoved {

    private Long frozenBoxId;
    private String frozenBoxCode;
    private Integer countOfSample;
    private String frozenBoxColumns;
    private String frozenBoxRows;
    private SampleType sampleType;
    private List<StockInTubeForDataMoved> stockInFrozenTubeList;

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

    public String getFrozenBoxColumns() {
        return frozenBoxColumns;
    }

    public void setFrozenBoxColumns(String frozenBoxColumns) {
        this.frozenBoxColumns = frozenBoxColumns;
    }

    public String getFrozenBoxRows() {
        return frozenBoxRows;
    }

    public void setFrozenBoxRows(String frozenBoxRows) {
        this.frozenBoxRows = frozenBoxRows;
    }

    public SampleType getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleType sampleType) {
        this.sampleType = sampleType;
    }

    public List<StockInTubeForDataMoved> getStockInFrozenTubeList() {
        return stockInFrozenTubeList;
    }

    public void setStockInFrozenTubeList(List<StockInTubeForDataMoved> stockInFrozenTubeList) {
        this.stockInFrozenTubeList = stockInFrozenTubeList;
    }
}
