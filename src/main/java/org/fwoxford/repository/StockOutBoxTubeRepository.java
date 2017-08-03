package org.fwoxford.repository;

import org.fwoxford.domain.StockOutBoxTube;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutBoxTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutBoxTubeRepository extends JpaRepository<StockOutBoxTube,Long> {
    @Query("select count(t) from StockOutBoxTube t where t.stockOutFrozenBox.frozenBox.id=?1 ")
    Long countByFrozenBox(Long id);

    StockOutBoxTube findByFrozenTubeId(Long id);

    @Query("select count(t) from StockOutBoxTube t where t.stockOutFrozenBox.id=?1")
    Long countByStockOutFrozenBoxId(Long id);

    Page<StockOutBoxTube> findByStockOutFrozenBoxIdIn(List<Long> ids, Pageable pageable);

    @Modifying
    @Query("update StockOutBoxTube t set t.frozenTubeState = '2009' where t.stockOutFrozenBox.id=?1")
    void updateByStockOutFrozenBox(Long id);

    List<StockOutBoxTube> findByStockOutFrozenBoxId(Long id);

    StockOutBoxTube findByStockOutTaskFrozenTubeId(Long id);

    @Query("select t.stockOutTaskFrozenTube.id from StockOutBoxTube t where t.stockOutFrozenBox.id=?1 ")
    List<Long> findTaskFrozenTubeByStockOutFrozenBoxId(Long id);

    @Query("select t.stockOutTaskFrozenTube.stockOutPlanFrozenTube.id from StockOutBoxTube t where t.stockOutFrozenBox.id=?1 ")
    List<Long> findPlanFrozenTubeByStockOutFrozenBoxId(Long id);

    @Query("select count(t) from StockOutBoxTube t where t.stockOutTaskFrozenTube.stockOutTask.id=?1 and t.frozenTubeState = '2009'")
    Long countByStockOutTaskId(Long taskId);

    @Query("select count(t) from StockOutBoxTube t where t.stockOutTaskFrozenTube.stockOutTask.id=?1 and t.frozenTubeState = '2009' and t.frozenTube.status != '3001'")
    Long countAbnormalTubeByStockOutTaskId(Long taskId);
}
