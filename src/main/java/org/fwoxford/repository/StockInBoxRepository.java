package org.fwoxford.repository;

import org.fwoxford.domain.StockIn;
import org.fwoxford.domain.StockInBox;

import org.springframework.data.jpa.repository.*;

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

    List<StockInBox> findStockInBoxByStockInCodeAndFrozenBoxCode(String stockInCode, String frozenBoxCode);

    @Modifying
    @Query("update StockInBox t set t.status=?3 where t.stockInCode=?1 and t.frozenBoxCode=?2")
    void updateByStockCodeAndFrozenBoxCode(String stockInCode, String boxCode, String status);

    @Query("select in from StockIn in where in.tranship.transhipCode=?1")
    List<StockIn> findByTranshipCode(String transhipCode);
}
