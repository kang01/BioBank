package org.fwoxford.repository;

import org.fwoxford.domain.StockOutBoxPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutBoxPosition entity.
 */
@SuppressWarnings("unused")
public interface StockOutBoxPositionRepository extends JpaRepository<StockOutBoxPosition,Long> {

    StockOutBoxPosition findByStockOutFrozenBoxId(Long id);
}
