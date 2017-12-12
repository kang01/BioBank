package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.StockOutApply;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutApply entity.
 */
@SuppressWarnings("unused")
public interface StockOutApplyRepository extends JpaRepository<StockOutApply,Long> {

    Long countByParentApplyId(Long id);

    StockOutApply findByApplyCode(String applyCode);

    @Query("SELECT T FROM StockOutApply T WHERE T.status='"+ Constants.STOCK_OUT_APPROVED+"' AND (T.countOfHandOverSample<T.countOfStockSample OR T.countOfHandOverSample is null)")
    List<StockOutApply> findUnHandoverApply();

    List<StockOutApply> findByStatus(String status);
}
