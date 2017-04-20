package org.fwoxford.repository;

import org.fwoxford.domain.StockInTubes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInTubes entity.
 */
@SuppressWarnings("unused")
public interface StockInTubesRepository extends JpaRepository<StockInTubes,Long> {

    StockInTubes findByStockInCodeAndFrozenTubeId(String stockInCode, Long id);

    List<StockInTubes> findByTranshipCode(String transhipCode);

    List<StockInTubes> findByTranshipCodeAndFrozenBoxCode(String transhipCode, String frozenBoxCode);

    List<StockInTubes> findByStockInCodeAndFrozenBoxCode(String stockInCode, String frozenBoxCode);
}
