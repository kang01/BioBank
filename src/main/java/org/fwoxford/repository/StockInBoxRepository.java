package org.fwoxford.repository;

import org.fwoxford.domain.StockInBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInBox entity.
 */
@SuppressWarnings("unused")
public interface StockInBoxRepository extends JpaRepository<StockInBox,Long> {

    @Query("update StockInBox t set t.status=?2 where t.stockInCode=?1")
    void updateByStockCode(String stockInCode, String status);

    List<StockInBox> findStockInBoxByStockInCode(String stockInCode);
}
