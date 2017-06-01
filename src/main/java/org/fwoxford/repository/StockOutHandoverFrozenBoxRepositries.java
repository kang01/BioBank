package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutFrozenBoxForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface StockOutHandoverFrozenBoxRepositries extends DataTablesRepository<StockOutFrozenBoxForDataTableEntity,Long> {
}
