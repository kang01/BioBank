package org.fwoxford.repository;

import org.fwoxford.domain.StockOutPlanFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutPlanFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutPlanFrozenTubeRepository extends JpaRepository<StockOutPlanFrozenTube,Long> {

    List<StockOutPlanFrozenTube> findByStockOutPlanId(Long id);

    @Query("select count(t) from StockOutPlanFrozenTube t where t.stockOutReqFrozenTube.frozenBox.id=?1 and t.status ='1501'")
    Long countByFrozenBoxId(Long id);

    @Query("select t from StockOutPlanFrozenTube t where t.stockOutPlan.id = ?1 and  t.stockOutReqFrozenTube.frozenBox.id=?2 and t.status ='1501'")
    List<StockOutPlanFrozenTube> findByStockOutPlanIdAndFrozenBoxId(Long planId, Long frozenBoxId);

    StockOutPlanFrozenTube findByStockOutReqFrozenTubeId(Long id);
}
