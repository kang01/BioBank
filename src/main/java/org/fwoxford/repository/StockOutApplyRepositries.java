package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutApplyForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;


public interface StockOutApplyRepositries extends DataTablesRepository<StockOutApplyForDataTableEntity,Long> {
}
