package org.fwoxford.service;

import org.fwoxford.service.dto.response.FrozenPositionListAllDataTableEntity;
import org.fwoxford.service.dto.response.FrozenPositionListSearchForm;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

/**
 * Service Interface for managing StockList.
 */
public interface StockListService {
    DataTablesOutput<FrozenPositionListAllDataTableEntity> getPageStockFrozenPositionList(DataTablesInput input, FrozenPositionListSearchForm searchForm);
}
