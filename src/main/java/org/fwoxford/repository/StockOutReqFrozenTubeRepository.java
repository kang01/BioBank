package org.fwoxford.repository;

import org.fwoxford.domain.StockOutReqFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutReqFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutReqFrozenTubeRepository extends JpaRepository<StockOutReqFrozenTube,Long> {

    List<StockOutReqFrozenTube> findByStockOutRequirementId(Long id);

    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.status = '1301'")
    List<StockOutReqFrozenTube> findAllByStockOutApplyId(Long id);

    void deleteByStockOutRequirementId(Long id);
}
