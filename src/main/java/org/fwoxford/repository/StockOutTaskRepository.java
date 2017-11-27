package org.fwoxford.repository;

import org.apache.tools.ant.filters.ConcatFilter;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.StockOutTask;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutTask entity.
 */
@SuppressWarnings("unused")
public interface StockOutTaskRepository extends JpaRepository<StockOutTask,Long> {

    Page<StockOutTask> findAllByStockOutPlanId(Long id, Pageable pageable);

//    @Query("select t from StockOutTask t left join StockOutHandover a on t.id = a.stockOutTask.id where t.stockOutPlan.id = ?1 and a.id is null and t.status in ('"+ Constants.STOCK_OUT_TASK_COMPLETED+"','"+Constants.STOCK_OUT_TASK_ABNORMAL+"')")
    List<StockOutTask> findByStockOutPlanId(Long id);

    StockOutTask findByStockOutTaskCode(String stockOutTaskCode);

    Long countByStockOutPlanIdAndStatusIn(Long id, List<String> statusList_);
}
