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

    @Query("select count(t.id) from StockOutHandoverDetails t where t.stockOutHandoverBox.stockOutHandover.id = ?1")
    Long countByStockOutHandoverId(Long id);

    @Query("select t from StockOutHandoverDetails t where t.stockOutHandoverBox.stockOutHandover.id = ?1")
    List<StockOutHandoverDetails> findByStockOutHandoverId(Long id);

    @Query(value = "select count(b) from stock_out_handover_box b " +
        "where b.stockOutHandover.id =?1" ,nativeQuery = true)
    Integer countFrozenBoxByStockOutHandoverId(Long id);

    @Query("select count(t) from StockOutHandoverDetails t where t.stockOutHandoverBox.stockOutHandover.stockOutApply.id=?1")
    Long countByStockOutApply(Long id);
}
