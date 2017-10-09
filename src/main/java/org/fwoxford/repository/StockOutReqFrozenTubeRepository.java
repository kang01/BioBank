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


    default void deleteByStockOutRequirementId(Long id) {

    };

    @Query("SELECT count(s) FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.status = '1301'")
    Long countByApply(Long id);

    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.frozenBox.id =?2")
    List<StockOutReqFrozenTube> findAllByStockOutApplyIdAndFrozenBoxId(Long applyId, Long frozenBoxId);

    StockOutReqFrozenTube findByFrozenTubeId(Long id);

    List<StockOutReqFrozenTube> findAllByStockOutRequirementIdInAndFrozenBoxIdIn( List<Long> requirementIds, List<Long> frozenBoxIds);

    StockOutReqFrozenTube findByFrozenTubeIdAndStatus(Long id, String status);

    int countByStockOutRequirementId(Long id);

    @Query(value = " select  a.id,count(1) from frozen_box a" +
        "        left join   stock_out_req_frozen_tube c on c.frozen_box_id = a.id " +
        "        where c.stock_out_task_id =?1 and c.status = '1301' and a.status!='2008' " +
        "        group by a.id" ,nativeQuery = true)
    List<Object[]> countByTaskGroupByBox(Long taskId);

    List<StockOutReqFrozenTube> findByStockOutTaskIdAndFrozenBoxId(Long stockOutTaskId, Long frozenBoxId);

    List<StockOutReqFrozenTube> findByStockOutTaskIdAndFrozenTubeIdInAndStatusNot(Long taskId, List<Long> frozenTubeIds, String stauts);

    Long countByStockOutFrozenBoxId(Long id);

    List<StockOutReqFrozenTube> findByStockOutFrozenBoxIdIn(List<Long> frozenBoxIds);

    Long countByStockOutTaskIdAndStatusNotIn(Long taskId, List<String> statusList);

    @Query("select count(t) from StockOutReqFrozenTube t where t.stockOutTask.id=?1 and t.frozenTube.frozenTubeState = '2009' and t.frozenTube.status != '3001'")
    Long countAbnormalTubeByStockOutTaskId(Long taskId);

    // 根据出库盒ID读取出库样本
    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutFrozenBox.id = ?1 AND (?2 IS NULL OR s.status=?2)")
    List<StockOutReqFrozenTube> findByStockOutFrozenBoxId(Long boxId, String status);

    // 根据多个出库盒ID读取出库样本
    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutFrozenBox.id IN ?1 AND (?2 IS NULL OR s.status=?2)")
    List<StockOutReqFrozenTube> findByStockOutFrozenBoxId(List<Long> boxIds, String status);

    void deleteInBatchByStockOutRequirementId(Long id);
}
