package org.fwoxford.repository;

import org.fwoxford.domain.StockOutRequiredSample;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<StockOutRequiredSample> findAllByStockOutRequirementId(Long id, Pageable pageable);
}
