package org.fwoxford.repository;

import org.fwoxford.domain.StockOutReqFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutReqFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutReqFrozenTubeRepository extends JpaRepository<StockOutReqFrozenTube,Long> {

}
