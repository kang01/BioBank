package org.fwoxford.repository;

import org.fwoxford.domain.StockOutFrozenBox;

import org.springframework.data.domain.Page;
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

}
