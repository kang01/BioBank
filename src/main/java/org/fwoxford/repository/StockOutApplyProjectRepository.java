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

    List<StockOutApplyProject> findByStockOutApplyId(Long id);
    @Query(value = "select p.* from stock_out_apply_project p " +
        " left join stock_out_apply a on p.stock_out_apply_id = a.id " +
        " left join stock_out_requirement r on r.stock_out_apply_id = a.id " +
        " where r.id =?1 " ,nativeQuery = true)
    List<StockOutApplyProject> findByStockRequirementId(Long id);

    @Query(value = "select count(*) from stock_out_apply_project p " +
        " left join stock_out_apply a on p.stock_out_apply_id = a.id " +
        " left join stock_out_requirement r on r.stock_out_apply_id = a.id " +
        " where r.id =?1 " ,nativeQuery = true)
    Long countByStockRequirementId(Long id);
}
