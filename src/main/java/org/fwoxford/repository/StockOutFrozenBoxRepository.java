package org.fwoxford.repository;

import org.fwoxford.domain.StockOutFrozenBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutFrozenBox entity.
 */
@SuppressWarnings("unused")
public interface StockOutFrozenBoxRepository extends JpaRepository<StockOutFrozenBox,Long> {

}
