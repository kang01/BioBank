package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutFrozenTubeDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Spring Data JPA repository for the StockOutTaskTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutTaskTubeByBoxRepositories extends DataTablesRepository<StockOutFrozenTubeDataTableEntity,Long> {

}
