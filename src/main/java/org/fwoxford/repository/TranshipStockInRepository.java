package org.fwoxford.repository;

import org.fwoxford.domain.TranshipStockIn;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipStockIn entity.
 */
@SuppressWarnings("unused")
public interface TranshipStockInRepository extends JpaRepository<TranshipStockIn,Long> {

    List<TranshipStockIn> findByStockInCode(String stockInCode);

    TranshipStockIn findByTranshipCode(String transhipCode);
}
