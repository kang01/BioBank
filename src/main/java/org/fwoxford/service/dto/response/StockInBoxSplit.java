package org.fwoxford.service.dto.response;

import org.fwoxford.service.dto.FrozenBoxTypeDTO;
import org.fwoxford.service.dto.StockInTubeDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxSplit extends StockInBoxDetail {

    private Long  frozenBoxTypeId;
    private FrozenBoxTypeDTO frozenBoxType;

    private List<StockInTubeDTO> stockInFrozenTubeList;

    public List<StockInTubeDTO> getStockInFrozenTubeList() {
        return stockInFrozenTubeList;
    }

    public void setStockInFrozenTubeList(List<StockInTubeDTO> stockInFrozenTubeList) {
        this.stockInFrozenTubeList = stockInFrozenTubeList;
    }

    public Long getFrozenBoxTypeId() {
        return frozenBoxTypeId;
    }

    public void setFrozenBoxTypeId(Long frozenBoxTypeId) {
        this.frozenBoxTypeId = frozenBoxTypeId;
    }

    public FrozenBoxTypeDTO getFrozenBoxType() {
        return frozenBoxType;
    }

    public void setFrozenBoxType(FrozenBoxTypeDTO frozenBoxType) {
        this.frozenBoxType = frozenBoxType;
    }
}
