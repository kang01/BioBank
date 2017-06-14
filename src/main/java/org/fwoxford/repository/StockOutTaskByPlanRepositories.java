package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutTaskForPlanDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Spring Data JPA repository for the StockOutTask entity.
 */
@SuppressWarnings("unused")
public interface StockOutTaskByPlanRepositories extends DataTablesRepository<StockOutTaskForPlanDataTableEntity,Long> {

}
