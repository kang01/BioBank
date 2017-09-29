package org.fwoxford.repository;

import org.fwoxford.domain.StockOutRequirement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutRequirement entity.
 */
@SuppressWarnings("unused")
public interface StockOutRequirementRepository extends JpaRepository<StockOutRequirement,Long> {

    List<StockOutRequirement> findByStockOutApplyId(Long id);

    List<StockOutRequirement> findByIdIn(List<Long> ids);

    List<StockOutRequirement> findByIdInAndStatus(List<Long> ids, String status);

    List<StockOutRequirement> findByIdInAndStatusNot(List<Long> ids, String status);

    Long countByStockOutApplyIdAndStatus(Long id, String status);

    Long countByStockOutApplyId(Long id);

    List<StockOutRequirement> findByStockOutApplyIdAndStatus(Long id, String status);

    StockOutRequirement findByRequirementCode(String requirementCode);

    @Query("select t.id from StockOutRequirement t where t.stockOutApply.id = ?1")
    List<Long> findRequirementByStockOutApplyId(Long id);
}
