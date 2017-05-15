package org.fwoxford.repository;

import org.fwoxford.domain.StockOutPlanFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutPlanFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutPlanFrozenTubeRepository extends JpaRepository<StockOutPlanFrozenTube,Long> {

}
