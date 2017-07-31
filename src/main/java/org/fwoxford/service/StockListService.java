package org.fwoxford.service;

import org.fwoxford.service.dto.response.*;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing StockList.
 */
public interface StockListService {
    /**
     * 冻存位置清单
     * @param input
     * @param searchForm
     * @return
     */
    DataTablesOutput<FrozenPositionListAllDataTableEntity> getPageStockFrozenPositionList(DataTablesInput input, FrozenPositionListSearchForm searchForm);
    /**
     * 冻存盒清单
     * @param input
     * @param search
     * @return
     */
    DataTablesOutput<FrozenBoxListAllDataTableEntity> getPageStockFrozenBoxList(DataTablesInput input, FrozenBoxListSearchForm search);
    /**
     * 样本清单
     * @param input
     * @param search
     * @return
     */
    DataTablesOutput<FrozenTubeListAllDataTableEntity> getPageStockFrozenTubeList(DataTablesInput input, FrozenTubeListSearchForm search);
    /**
     * 样本历史清单
     * @param frozenTubeId
     * @return
     */
    List<FrozenTubeHistory> findFrozenTubeHistoryDetail(Long frozenTubeId);

    /**
     * 所有设备清单
     * @param input
     * @param search
     * @return
     */
    DataTablesOutput<ShelvesListAllDataTableEntity> getPageShelvesList(DataTablesInput input, FrozenPositionListSearchForm search);

    DataTablesOutput<AreasListAllDataTableEntity> getPageAreaList(DataTablesInput input, FrozenPositionListSearchForm search);

    Map<Long,FrozenTubeHistory> findFrozenTubeHistoryDetailByIds(List<Long> ids);
}
