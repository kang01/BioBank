package org.fwoxford.repository;

import org.fwoxford.domain.StockInTubes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInTubes entity.
 */
@SuppressWarnings("unused")
public interface StockInTubesRepository extends JpaRepository<StockInTubes,Long> {

}
