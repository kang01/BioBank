package org.fwoxford.service.dto.response;

import org.fwoxford.service.dto.StockInTubeDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxSplit extends StockInBoxDetail {
    private List<StockInTubeDTO> stockInTubeDTOList;

    public List<StockInTubeDTO> getStockInTubeDTOList() {
        return stockInTubeDTOList;
    }

    public void setStockInTubeDTOList(List<StockInTubeDTO> stockInTubeDTOList) {
        this.stockInTubeDTOList = stockInTubeDTOList;
    }
}
