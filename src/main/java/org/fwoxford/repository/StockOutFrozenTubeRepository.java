package org.fwoxford.repository;

import org.fwoxford.domain.StockOutFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutFrozenTubeRepository extends JpaRepository<StockOutFrozenTube,Long> {
    @Query(value = "select count(*) from stock_out_tube t where t.stock_out_frozen_box_id=?1" ,nativeQuery = true)
    Long countByFrozenBox(Long boxId);

    @Query("SELECT s.id FROM StockOutFrozenTube s WHERE s.stockOutFrozenBox.stockOutTask.id = ?1")
    List<Long> findByTask(Long id);

    @Query("delete from StockOutFrozenTube s WHERE s.stockOutFrozenBox.stockOutTask.id = ?1")
    void deleteByTask(Long id);

    void deleteByIdIn(List<Long> tubeIds);

    @Query("select count(t) from StockOutFrozenTube t where t.stockOutFrozenBox.stockOutTask.id=?1")
    Long countByStockOutTaskId(Long id);
}
