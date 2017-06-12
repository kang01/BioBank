package org.fwoxford.repository;

import org.fwoxford.domain.StockInBoxPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInBoxPosition entity.
 */
@SuppressWarnings("unused")
public interface StockInBoxPositionRepository extends JpaRepository<StockInBoxPosition,Long> {

    List<StockInBoxPosition> findByStockInBoxIdAndStatus(Long stockInBoxId, String status);

    @Query(value = "select * from stock_in_box_pos a where a.stock_in_box_id = ?1 and a.status != '0000' order by CREATED_DATE desc " +
        " FETCH FIRST 1 ROWS ONLY  " , nativeQuery = true)
    StockInBoxPosition findByStockInBoxIdLast(Long id);
}
