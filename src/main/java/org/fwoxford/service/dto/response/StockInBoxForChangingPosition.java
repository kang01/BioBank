package org.fwoxford.service.dto.response;

import org.fwoxford.domain.SampleType;

import java.util.List;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxForChangingPosition extends StockInBoxForShelf {

    private List<StockInTubeForBox> stockInFrozenTubeList;

    public List<StockInTubeForBox> getStockInFrozenTubeList() {
        return stockInFrozenTubeList;
    }

    public void setStockInFrozenTubeList(List<StockInTubeForBox> stockInFrozenTubeList) {
        this.stockInFrozenTubeList = stockInFrozenTubeList;
    }
}
