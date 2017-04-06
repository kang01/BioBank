package org.fwoxford.repository;

import org.fwoxford.domain.StockInBox;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the StockInBox entity.
 */
@SuppressWarnings("unused")
public interface StockInBoxRepository extends JpaRepository<StockInBox,Long> {

}
