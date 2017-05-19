package org.fwoxford.repository;

import org.fwoxford.domain.StockOutRequiredSample;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutRequiredSample entity.
 */
@SuppressWarnings("unused")
public interface StockOutRequiredSampleRepository extends JpaRepository<StockOutRequiredSample,Long> {

    List<StockOutRequiredSample> findByStockOutRequirementId(Long id);

    void deleteByStockOutRequirementId(Long id);

    Long countByStockOutRequirementId(Long id);
}
