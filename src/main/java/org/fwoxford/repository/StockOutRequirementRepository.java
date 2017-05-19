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
}
