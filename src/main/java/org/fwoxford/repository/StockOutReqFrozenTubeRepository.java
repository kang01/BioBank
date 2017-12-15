package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockOutReqFrozenTube;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutReqFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutReqFrozenTubeRepository extends JpaRepository<StockOutReqFrozenTube,Long> {

    List<StockOutReqFrozenTube> findByStockOutRequirementId(Long id);

    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.status = '"+Constants.STOCK_OUT_SAMPLE_IN_USE+"'")
    List<StockOutReqFrozenTube> findAllByStockOutApplyId(Long id);


    void deleteByStockOutRequirementId(Long id);

    @Query("SELECT count(s) FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.status = ?2")
    Long countByApplyAndStatus(Long id,String status);

    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.frozenBox.id =?2")
    List<StockOutReqFrozenTube> findAllByStockOutApplyIdAndFrozenBoxId(Long applyId, Long frozenBoxId);

    StockOutReqFrozenTube findByFrozenTubeId(Long id);

    List<StockOutReqFrozenTube> findAllByStockOutRequirementIdInAndFrozenBoxIdIn( List<Long> requirementIds, List<Long> frozenBoxIds);

    StockOutReqFrozenTube findByFrozenTubeIdAndStatus(Long id, String status);

    int countByStockOutRequirementId(Long id);

    @Query(value = " select  a.id,count(1) from frozen_box a" +
        "        left join   stock_out_req_frozen_tube c on c.frozen_box_id = a.id " +
        "        where c.stock_out_task_id =?1 and c.status = '"+Constants.STOCK_OUT_SAMPLE_IN_USE+"' and a.status!='"+Constants.FROZEN_BOX_STOCK_OUT_PENDING+"' " +
        "        group by a.id" ,nativeQuery = true)
    List<Object[]> countByTaskGroupByBox(Long taskId);

    @Query("select t from StockOutReqFrozenTube t where t.stockOutTask.id =?1 and t.frozenBox.id = ?2 and t.status = '"+Constants.STOCK_OUT_SAMPLE_IN_USE+"'")
    List<StockOutReqFrozenTube> findByStockOutTaskIdAndFrozenBoxId(Long stockOutTaskId, Long frozenBoxId);

    @Query("select t from StockOutReqFrozenTube t where t.stockOutTask.id =?1 and t.frozenBox.id = ?2 ")
    List<StockOutReqFrozenTube> findByStockOutTaskIdAndFrozenBoxIdAll(Long stockOutTaskId, Long frozenBoxId);

    List<StockOutReqFrozenTube> findByStockOutTaskIdAndFrozenTubeIdInAndStatusNot(Long taskId, List<Long> frozenTubeIds, String stauts);

    @Query("SELECT COUNT(t.id) FROM StockOutReqFrozenTube t WHERE t.stockOutFrozenBox.id = ?1 and t.status !=  '"+Constants.STOCK_OUT_SAMPLE_IN_USE_NOT+"'")
    Long countByStockOutFrozenBoxId(Long id);

    List<StockOutReqFrozenTube> findByStockOutFrozenBoxIdIn(List<Long> frozenBoxIds);

    Long countByStockOutTaskIdAndStatusNotIn(Long taskId, List<String> statusList);

    @Query("select count(t.id) from StockOutReqFrozenTube t where t.stockOutTask.id=?1 and t.frozenTube.frozenTubeState = '"+Constants.FROZEN_BOX_STOCK_OUT_COMPLETED+"' and t.frozenTube.status != '"+Constants.FROZEN_TUBE_NORMAL+"'")
    Long countAbnormalTubeByStockOutTaskId(Long taskId);

    List<StockOutReqFrozenTube> findByStockOutFrozenBoxId(Long boxId);

    // 根据多个出库盒ID读取出库样本
    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutFrozenBox.id IN ?1 AND (?2 IS NULL OR s.status=?2)")
    List<StockOutReqFrozenTube> findByStockOutFrozenBoxId(List<Long> boxIds, String status);

    @Query("select t.frozenBox from StockOutReqFrozenTube t where t.stockOutTask.id = ?1 and t.status = ?2")
    List<FrozenBox> findByStockOutTaskIdAndStatus(Long taskId, String status);

    List<StockOutReqFrozenTube> findByStockOutTaskId(Long taskId);

    Long countByStockOutTaskIdAndFrozenBoxIdAndStatus(Long taskId, Long id, String status);

    @Query("select count(t.id) from StockOutReqFrozenTube t where t.stockOutRequirement.stockOutApply.id = ?1 and t.status in ?2 ")
    Long countUnCompleteSampleByStockOutApplyAndStatusIn(Long id, List<String> statusList_);

    @Query("SELECT count(s.id) FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1")
    Long countByApply(Long id);

    Long countByStockOutRequirementIdAndStatus(Long id, String status);

    @Query("SELECT s.id FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.id = ?1")
    List<Long> findStockOutFrozenTubeIdByStockOutRequirementId(Long id);

    @Query(value = "SELECT cast(rowid as nvarchar2(255)) FROM stock_out_req_frozen_tube s WHERE s.stock_out_requirement_id = ?1" ,nativeQuery = true)
    List<Object> findRowIdByStockOutRequirementId(Long id);

    @Modifying
    @Query(value = "DELETE  FROM stock_out_req_frozen_tube T WHERE ROWID IN ?1",nativeQuery = true)
    void deleteByIdsIn(List<Object> ids);

    @Query("SELECT s FROM StockOutReqFrozenTube s WHERE s.stockOutRequirement.stockOutApply.id = ?1 AND s.sampleTypeId = ?2 AND s.sampleCode in ?3 AND s.status = '"+Constants.STOCK_OUT_SAMPLE_COMPLETED+"'")
    List<StockOutReqFrozenTube> findByStockOutApplyIdAndSampleTypeAndSampleCodeIn(Long id, Long sampleTypeId, List<String> sampleCodeStr);
}
