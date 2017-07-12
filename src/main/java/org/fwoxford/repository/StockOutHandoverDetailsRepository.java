package org.fwoxford.repository;

import org.fwoxford.domain.StockOutHandoverDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutHandoverDetails entity.
 */
@SuppressWarnings("unused")
public interface StockOutHandoverDetailsRepository extends JpaRepository<StockOutHandoverDetails,Long> {
    @Query("select count(t) from StockOutHandoverDetails t where t.stockOutHandover.stockOutTask.id=?1")
    Long countByStockOutTaskId(Long id);

    Long countByStockOutHandoverId(Long id);

    List<StockOutHandoverDetails> findByStockOutHandoverId(Long id);

    @Query(value = "select count(count(stockoutha0_.id)) as col_0_0_ from stock_out_handover_details stockoutha0_ " +
        " cross join stock_out_box_tube stockoutbo1_ " +
        " where stockoutha0_.stock_out_box_tube_id=stockoutbo1_.id " +
        " and stockoutha0_.stock_out_handover_id = ?1" +
        " group by stockoutbo1_.stock_out_frozen_box_id" ,nativeQuery = true)
    Integer countFrozenBoxByStockOutHandoverId(Long id);

    Page<StockOutHandoverDetails> findPageByStockOutHandoverId(Long id, Pageable pageable);

    @Query("select count(t) from StockOutHandoverDetails t where t.stockOutHandover.stockOutApply.id=?1")
    Long countByStockOutApply(Long id);
}
