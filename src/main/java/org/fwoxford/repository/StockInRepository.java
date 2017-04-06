package org.fwoxford.repository;

import org.fwoxford.domain.StockIn;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the StockIn entity.
 */
@SuppressWarnings("unused")
public interface StockInRepository extends JpaRepository<StockIn,Long> {

}
