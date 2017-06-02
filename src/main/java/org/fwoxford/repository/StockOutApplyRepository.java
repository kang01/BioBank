package org.fwoxford.repository;

import org.fwoxford.domain.StockOutApply;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutApply entity.
 */
@SuppressWarnings("unused")
public interface StockOutApplyRepository extends JpaRepository<StockOutApply,Long> {
    @Query("select t from StockOutApply t left join StockOutHandover h on t.id = h.stockOutApply.id where  h.stockOutApply.id is null or h.status = '2101'")
    List<StockOutApply> findAllNotHandOverApply();
}
