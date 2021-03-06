package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.StockOutFrozenBox;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
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

    @Query("SELECT s FROM StockOutFrozenBox s  " +
        " WHERE s.stockOutTask.stockOutPlan.stockOutApply.id =?1 and s.status = ?2")
    Page<StockOutFrozenBox> findBoxesByApplyAndStatus(Long id, String status, Pageable pageRequest);

    List<StockOutFrozenBox> findByStockOutTaskIdAndStatus(Long taskId, String status);
    @Query("SELECT s FROM StockOutFrozenBox s  " +
        " WHERE s.id in ?1 and s.status !='"+ Constants.STOCK_OUT_FROZEN_BOX_HANDOVER+"' ")
    List<StockOutFrozenBox> findByIdIn(List<Long> frozenBoxIds);

    StockOutFrozenBox findByFrozenBoxIdAndStockOutTaskId(Long frozenBoxId, Long taskId);

    StockOutFrozenBox findByFrozenBoxCodeAndCreatedDateGreaterThan(String frozenBoxCode, ZonedDateTime zonedDateTime);

    @Query(value = "select * from stock_out_box t where t.frozen_box_code = ?1 and to_char(t.created_date,'yyyy-MM-dd') = ?2",nativeQuery = true)
    StockOutFrozenBox findByFrozenBoxCodeAndCreatedDateLike(String frozenBoxCode, String s);

    @Query("SELECT S FROM StockOutFrozenBox S WHERE S.stockOutTask.stockOutPlan.stockOutApply.applyCode = ?2 " +
            "AND S.frozenBoxCode IN ?1 AND S.frozenBox.status = '"+Constants.FROZEN_BOX_STOCK_OUT_HANDOVER+"'")
    List<StockOutFrozenBox> findByFrozenBoxCodeAndStockOutApply(List<String> boxCodes, String applyCode);
}
