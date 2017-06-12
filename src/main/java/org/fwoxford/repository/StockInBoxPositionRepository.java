package org.fwoxford.repository;

import org.fwoxford.domain.StockInBoxPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInBoxPosition entity.
 */
@SuppressWarnings("unused")
public interface StockInBoxPositionRepository extends JpaRepository<StockInBoxPosition,Long> {

    List<StockInBoxPosition> findByStockInBoxIdAndStatus(Long stockInBoxId, String status);
}
