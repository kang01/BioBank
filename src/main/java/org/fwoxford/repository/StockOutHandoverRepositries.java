package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutHandoverForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface StockOutHandoverRepositries extends DataTablesRepository<StockOutHandoverForDataTableEntity,Long> {
}
