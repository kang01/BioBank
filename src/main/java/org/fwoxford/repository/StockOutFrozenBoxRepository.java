package org.fwoxford.repository;

import org.fwoxford.domain.StockOutFrozenBox;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutFrozenBox entity.
 */
@SuppressWarnings("unused")
public interface StockOutFrozenBoxRepository extends JpaRepository<StockOutFrozenBox,Long> {

    @Query("SELECT s FROM StockOutFrozenBox s WHERE s.stockOutTask.id = ?1")
    Page<StockOutFrozenBox> findAllByTask(Long taskId, Pageable pageable);

    void deleteByStockOutTaskId(Long taskId);

    Long countByStockOutTaskId(Long id);

    List<StockOutFrozenBox> findByStockOutTaskId(Long taskId);

    StockOutFrozenBox findByFrozenBoxId(Long id);

    @Query("SELECT s FROM StockOutFrozenBox s left join StockOutBoxTube t on s.id =t.stockOutFrozenBox.id " +
        " left join StockOutHandoverDetails h on t.id = h.stockOutBoxTube.id" +
        " WHERE h.stockOutHandover.id =?1 and s.status = ?2")
    Page<StockOutFrozenBox> findBoxesByHandOverAndStatus(Long id, String status, Pageable pageRequest);

    @Query("SELECT s FROM StockOutFrozenBox s  " +
        " WHERE s.stockOutTask.stockOutPlan.stockOutApply.id =?1 and s.status = ?2")
    Page<StockOutFrozenBox> findBoxesByApplyAndStatus(Long id, String status, Pageable pageRequest);

    List<StockOutFrozenBox> findByStockOutTaskIdAndStatus(Long taskId, String status);
}
