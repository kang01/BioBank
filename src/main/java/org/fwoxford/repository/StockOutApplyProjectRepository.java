package org.fwoxford.repository;

import org.fwoxford.domain.StockOutApplyProject;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutApplyProject entity.
 */
@SuppressWarnings("unused")
public interface StockOutApplyProjectRepository extends JpaRepository<StockOutApplyProject,Long> {

    void deleteByStockOutApplyId(Long id);
}
