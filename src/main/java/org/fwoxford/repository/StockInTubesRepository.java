package org.fwoxford.repository;

import org.fwoxford.domain.StockInTubes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInTubes entity.
 */
@SuppressWarnings("unused")
public interface StockInTubesRepository extends JpaRepository<StockInTubes,Long> {
    @Query("select s from StockInTubes s " +
        "where s.transhipBox.tranship.transhipCode =?1 and s.transhipBox.frozenBoxCode =?2")
    List<StockInTubes> findByTranshipCodeAndFrozenBoxCode(String transhipCode, String frozenBoxCode);

    @Query(value = "select s.* from stock_in_tubes s left join stock_in_box b on s.stock_in_box_id = b.id " +
        "where b.stock_in_code =?1 and b.frozen_box_code =?2 or b.frozen_box_code = null and s.status =?3", nativeQuery = true)
    List<StockInTubes> findByStockInCodeAndFrozenBoxCodeAndStatus(String stockInCode, String frozenBoxCode,String status);

    @Query(value = "select s.* from stock_in_tubes s left join stock_in_box b on s.stock_in_box_id = b.id " +
        "where b.stock_in_code =?1 and b.frozen_box_code =?2" +
        " and s.status =?3", nativeQuery = true)
    List<StockInTubes> findByStockInCodeAndFrozenBoxCodeNotNullAndStatus(String stockInCode, String frozenBoxCode, String frozenBoxStocked);

    @Query(value = "select count(*) from stock_in_tubes s left join stock_in_box b on s.stock_in_box_id = b.id " +
        "where b.stock_in_code =?1 and b.frozen_box_code =?2 or b.frozen_box_code = null and s.status =?3", nativeQuery = true)
    Long countByStockInCodeAndFrozenBoxCodeAndStatus(String stockInCode, String frozenBoxCode, String frozenBoxStocking);

    @Query(value = "select count(*) from stock_in_tubes s left join stock_in_box b on s.stock_in_box_id = b.id " +
        "where b.stock_in_code =?1 and b.frozen_box_code =?2" +
        " and s.status =?3", nativeQuery = true)
    Long countByStockInCodeAndFrozenBoxCodeNotNullAndStatus(String stockInCode, String frozenBoxCode, String status);
}
