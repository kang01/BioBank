package org.fwoxford.repository;

import org.fwoxford.domain.StockOutRequiredSample;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTubeHistory StockOutRequiredSample.
 */
@SuppressWarnings("unused")
public interface StockOutRequiredSampleRepositories extends DataTablesRepository<StockOutRequiredSample,Long> {

}
