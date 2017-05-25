package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutPlansForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Created by gengluying on 2017/5/25.
 */
public interface StockOutPlanRepositories  extends DataTablesRepository<StockOutPlansForDataTableEntity,Long> {
}
