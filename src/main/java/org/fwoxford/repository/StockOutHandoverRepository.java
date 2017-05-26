package org.fwoxford.repository;

import org.fwoxford.domain.StockOutHandover;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutHandover entity.
 */
@SuppressWarnings("unused")
public interface StockOutHandoverRepository extends JpaRepository<StockOutHandover,Long> {

    Long countByStockOutTaskId(Long id);
}
