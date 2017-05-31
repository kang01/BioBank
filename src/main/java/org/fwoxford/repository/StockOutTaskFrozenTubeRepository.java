package org.fwoxford.repository;

import org.fwoxford.domain.StockOutTaskFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutTaskFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutTaskFrozenTubeRepository extends JpaRepository<StockOutTaskFrozenTube,Long> {

    void deleteByStockOutTaskId(Long id);

    @Query("select count(t) from StockOutTaskFrozenTube t where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenBox.id=?1")
    Long countByFrozenBox(Long frozenBoxId);

    @Query("select count(t) from StockOutTaskFrozenTube t where t.stockOutTask.id=?1")
    Long countByStockOutTaskId(Long id);

    @Query(value = "select  count(count(c.FROZEN_BOX_ID)) from STOCK_OUT_TASK_TUBE a\n" +
        "left join STOCK_OUT_PLAN_TUBE b on a.STOCK_OUT_PLAN_FROZEN_TUBE_ID = b.ID\n" +
        "left join STOCK_OUT_REQ_FROZEN_TUBE c on c.id = b.STOCK_OUT_REQ_FROZEN_TUBE_ID\n" +
        "left join FROZEN_BOX d on d.id = c.FROZEN_BOX_ID where a.STOCK_OUT_TASK_ID = ?1 " +
        "GROUP BY c.FROZEN_BOX_ID ",nativeQuery = true)
    Long countFrozenBoxByStockOutTaskId(Long id);

    @Query("select t from StockOutTaskFrozenTube t where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenBox.frozenBoxCode=?1")
    List<StockOutTaskFrozenTube> findByFrozenBox(String frozenBoxCode);

    @Query("select t from StockOutTaskFrozenTube t where t.stockOutTask.id=?1 and t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenTube.id=?2")
    StockOutTaskFrozenTube findByStockOutTaskAndFrozenTube(Long taskId, Long id);

    StockOutTaskFrozenTube findByStockOutPlanFrozenTubeId(Long id);
}
