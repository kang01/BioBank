package org.fwoxford.repository;

import org.fwoxford.domain.StockIn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockIn entity.
 */
@SuppressWarnings("unused")
public interface StockInRepository extends JpaRepository<StockIn,Long> {

    StockIn findStockInByStockInCode(String stockInCode);

    StockIn findStockInByTranshipId(Long id);

    @Query("select s from StockIn s where s.tranship.transhipCode=?1")
    List<StockIn> findByTranshipCode(String transhipCode);

    @Query("select count(s) from StockIn s where s.tranship.transhipCode=?1")
    int countByTranshipCode(String transhipCode);
}
