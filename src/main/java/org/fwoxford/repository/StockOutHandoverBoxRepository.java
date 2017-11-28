package org.fwoxford.repository;

import org.fwoxford.domain.StockOutHandoverBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutHandoverBox entity.
 */
@SuppressWarnings("unused")
public interface StockOutHandoverBoxRepository extends JpaRepository<StockOutHandoverBox,Long> {

    List<StockOutHandoverBox> findByStockOutHandoverId(Long id);
}
