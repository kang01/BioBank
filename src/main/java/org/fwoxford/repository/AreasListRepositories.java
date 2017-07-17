package org.fwoxford.repository;

import org.fwoxford.service.dto.response.AreasListAllDataTableEntity;
import org.fwoxford.service.dto.response.FrozenTubeHistory;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTubeHistory entity.
 */
@SuppressWarnings("unused")
public interface AreasListRepositories extends DataTablesRepository<AreasListAllDataTableEntity,Long> {
}
