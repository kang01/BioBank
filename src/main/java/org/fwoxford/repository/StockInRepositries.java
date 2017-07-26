package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockInForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface StockInRepositries extends DataTablesRepository<StockInForDataTableEntity,Long> {
}
