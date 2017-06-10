package org.fwoxford.repository;

import org.fwoxford.domain.StockOutHandover;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutHandover entity.
 */
@SuppressWarnings("unused")
public interface StockOutHandoverRepository extends JpaRepository<StockOutHandover,Long> {

    Long countByStockOutTaskId(Long id);

    StockOutHandover findByStockOutTaskId(Long taskId);

    @Query("select DISTINCT t.stockOutHandover from StockOutHandoverDetails t " +
        "where t.stockOutHandover.stockOutTask.id = ?1 and t.stockOutBoxTube.stockOutFrozenBox.id = ?2")
    StockOutHandover findByStockOutTaskIdAndstockOutBoxId(Long taskId, Long id);
}
