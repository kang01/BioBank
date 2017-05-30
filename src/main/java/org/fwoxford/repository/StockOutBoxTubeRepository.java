package org.fwoxford.repository;

import org.fwoxford.domain.StockOutBoxTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutBoxTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutBoxTubeRepository extends JpaRepository<StockOutBoxTube,Long> {

}
