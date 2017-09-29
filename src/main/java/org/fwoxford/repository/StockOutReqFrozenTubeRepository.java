package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockOutReqFrozenTube;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutReqFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutReqFrozenTubeRepository extends JpaRepository<StockOutReqFrozenTube,Long> {

    List<StockOutReqFrozenTube> findByStockOutRequirementId(Long id);

    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.status = '1301'")
    List<StockOutReqFrozenTube> findAllByStockOutApplyId(Long id);


    void deleteByStockOutRequirementId(Long id);

    @Query("SELECT count(s) FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.status = '1301'")
    Long countByApply(Long id);

    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.frozenBox.id =?2")
    List<StockOutReqFrozenTube> findAllByStockOutApplyIdAndFrozenBoxId(Long applyId, Long frozenBoxId);

    StockOutReqFrozenTube findByFrozenTubeId(Long id);

    List<StockOutReqFrozenTube> findAllByStockOutRequirementIdInAndFrozenBoxId( List<Long> requirementIds, Long frozenBoxId);

    StockOutReqFrozenTube findByFrozenTubeIdAndStatus(Long id, String status);

    int countByStockOutRequirementId(Long id);

    // 根据出库盒ID读取出库样本
    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutFrozenBox.id = ?1 AND (?2 IS NULL OR s.status=?2)")
    List<StockOutReqFrozenTube> findByStockOutFrozenBoxId(Long boxId, String status);

    // 根据多个出库盒ID读取出库样本
    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutFrozenBox.id IN ?1 AND (?2 IS NULL OR s.status=?2)")
    List<StockOutReqFrozenTube> findByStockOutFrozenBoxId(List<Long> boxIds, String status);
}
