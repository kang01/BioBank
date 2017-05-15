package org.fwoxford.repository;

import org.fwoxford.domain.StockOutFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutFrozenTubeRepository extends JpaRepository<StockOutFrozenTube,Long> {

}
