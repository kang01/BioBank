package org.fwoxford.repository;

import org.fwoxford.domain.StockOutApply;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutApply entity.
 */
@SuppressWarnings("unused")
public interface StockOutApplyRepository extends JpaRepository<StockOutApply,Long> {

    Long countByParentApplyId(Long id);
}
