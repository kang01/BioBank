package org.fwoxford.repository;

import org.fwoxford.domain.StockOutPlan;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutPlan entity.
 */
@SuppressWarnings("unused")
public interface StockOutPlanRepository extends JpaRepository<StockOutPlan,Long> {

}
