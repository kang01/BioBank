package org.fwoxford.repository;

import org.fwoxford.service.dto.response.FrozenBoxForStockOutDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/4/8.
 */
public interface StockOutFrozenBoxForWaitingRepositries extends DataTablesRepository<FrozenBoxForStockOutDataTableEntity,Long> {
}
