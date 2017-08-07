package org.fwoxford.repository;

import org.fwoxford.domain.TranshipBox;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipBox entity.
 */
@SuppressWarnings("unused")
public interface TranshipBoxRepositories extends DataTablesRepository<TranshipBox,Long> {

}
