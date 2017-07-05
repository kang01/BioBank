package org.fwoxford.repository;

import org.fwoxford.domain.StockInTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInTube entity.
 */
@SuppressWarnings("unused")
public interface StockInTubeRepository extends JpaRepository<StockInTube,Long> {

    StockInTube findByFrozenTubeId(Long id);

    List<StockInTube> findByStockInBoxId(Long id);
}
