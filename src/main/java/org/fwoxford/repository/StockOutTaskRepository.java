package org.fwoxford.repository;

import org.fwoxford.domain.StockOutTask;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutTask entity.
 */
@SuppressWarnings("unused")
public interface StockOutTaskRepository extends JpaRepository<StockOutTask,Long> {

}
