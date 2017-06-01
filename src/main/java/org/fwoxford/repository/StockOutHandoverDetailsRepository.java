package org.fwoxford.repository;

import org.fwoxford.domain.StockOutHandoverDetails;
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

    @Query("select count(t) from StockOutHandoverDetails t where t.stockOutHandover.id=?1 group by t.stockOutBoxTube.stockOutFrozenBox.id")
    Integer countFrozenBoxByStockOutHandoverId(Long id);
}
