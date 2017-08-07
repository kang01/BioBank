package org.fwoxford.repository;

import org.fwoxford.domain.StockInTranshipBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInTranshipBox entity.
 */
@SuppressWarnings("unused")
public interface StockInTranshipBoxRepository extends JpaRepository<StockInTranshipBox,Long> {

    List<StockInTranshipBox> findByStockInCode(String stockInCode);
}
