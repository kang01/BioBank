package org.fwoxford.repository;

import org.fwoxford.service.dto.response.FrozenTubeHistory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTubeHistory entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeHistoryRepositories extends DataTablesRepository<FrozenTubeHistory,Long> {

    List<FrozenTubeHistory> findByFrozenTubeId(Long frozenTubeId);
}
