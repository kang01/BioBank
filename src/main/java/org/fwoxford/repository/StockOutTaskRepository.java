package org.fwoxford.repository;

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
}
