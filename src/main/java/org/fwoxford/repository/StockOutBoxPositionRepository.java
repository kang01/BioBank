package org.fwoxford.repository;

import org.fwoxford.domain.StockOutBoxPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutBoxPosition entity.
 */
@SuppressWarnings("unused")
public interface StockOutBoxPositionRepository extends JpaRepository<StockOutBoxPosition,Long> {
    @Query("delete from StockOutBoxPosition s where s.id in ?1")
    void deleteByIds(List<Long> ids);
}
