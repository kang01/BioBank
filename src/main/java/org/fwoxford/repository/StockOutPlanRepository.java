package org.fwoxford.repository;

import org.fwoxford.domain.StockOutPlan;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutPlan entity.
 */
@SuppressWarnings("unused")
public interface StockOutPlanRepository extends JpaRepository<StockOutPlan,Long> {
    @Query("SELECT p FROM StockOutPlan p WHERE p.stockOutApply.id = ?1 AND (?2 IS NULL OR p.id <> ?2) AND p.status = 'XXXX'")
    Long countByStockOutApplyId(Long applyId, Long excludeId);
    List<StockOutPlan> findAllByStockOutApplyId(Long applyId);
}
