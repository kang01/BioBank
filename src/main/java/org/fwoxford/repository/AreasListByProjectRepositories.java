package org.fwoxford.repository;

import org.fwoxford.service.dto.response.AreasListByProjectDataTableEntity;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

/**
 * Spring Data JPA repository for the AreasListByProjectDataTableEntity entity.
 */
@SuppressWarnings("unused")
public interface AreasListByProjectRepositories extends DataTablesRepository<AreasListByProjectDataTableEntity,Long> {
}
