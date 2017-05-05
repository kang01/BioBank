package org.fwoxford.repository;

import org.fwoxford.domain.StockInBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInBox entity.
 */
@SuppressWarnings("unused")
public interface StockInBoxRepository extends JpaRepository<StockInBox,Long> {
    @Modifying
    @Query("update StockInBox t set t.status=?2 where t.stockInCode=?1")
    void updateByStockCode(String stockInCode, String status);

    List<StockInBox> findStockInBoxByStockInCode(String stockInCode);

    StockInBox findStockInBoxByStockInCodeAndFrozenBoxCode(String stockInCode, String frozenBoxCode);

    @Modifying
    @Query("update StockInBox t set t.status=?3 where t.stockInCode=?1 and t.frozenBoxCode=?2")
    void updateByStockCodeAndFrozenBoxCode(String stockInCode, String boxCode, String status);
}
