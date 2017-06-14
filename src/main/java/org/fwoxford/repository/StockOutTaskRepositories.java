package org.fwoxford.repository;

import org.fwoxford.service.dto.response.StockOutTaskForDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutTask entity.
 */
@SuppressWarnings("unused")
public interface StockOutTaskRepositories extends DataTablesRepository<StockOutTaskForDataTableEntity,Long> {

}
