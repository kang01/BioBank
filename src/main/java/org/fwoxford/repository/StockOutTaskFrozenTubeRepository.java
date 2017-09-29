package org.fwoxford.repository;

import org.fwoxford.domain.StockOutTaskFrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockOutTaskFrozenTube entity.
 */
@SuppressWarnings("unused")
public interface StockOutTaskFrozenTubeRepository extends JpaRepository<StockOutTaskFrozenTube,Long> {

    void deleteByStockOutTaskId(Long id);

    @Query("select count(t.id) from StockOutTaskFrozenTube t where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenBox.id=?1")
    Long countByFrozenBox(Long frozenBoxId);

    @Query("select count(t.id) from StockOutTaskFrozenTube t where t.stockOutTask.id=?1 and t.status != '1802'")
    Long countByStockOutTaskId(Long id);

    @Query(value = "select  count(count(c.FROZEN_BOX_ID)) from STOCK_OUT_TASK_TUBE a\n" +
        "left join STOCK_OUT_PLAN_TUBE b on a.STOCK_OUT_PLAN_FROZEN_TUBE_ID = b.ID\n" +
        "left join STOCK_OUT_REQ_FROZEN_TUBE c on c.id = b.STOCK_OUT_REQ_FROZEN_TUBE_ID\n" +
        "left join FROZEN_BOX d on d.id = c.FROZEN_BOX_ID where a.STOCK_OUT_TASK_ID = ?1 " +
        "GROUP BY c.FROZEN_BOX_ID ",nativeQuery = true)
    Long countFrozenBoxByStockOutTaskId(Long id);

    @Query("select t from StockOutTaskFrozenTube t where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenBox.frozenBoxCode=?1 and t.stockOutTask.id = ?2")
    List<StockOutTaskFrozenTube> findByFrozenBoxAndTask(String frozenBoxCode,Long id);

    @Query("select t from StockOutTaskFrozenTube t where t.stockOutTask.id=?1 and t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenTube.id=?2")
    StockOutTaskFrozenTube findByStockOutTaskAndFrozenTube(Long taskId, Long id);

    StockOutTaskFrozenTube findByStockOutPlanFrozenTubeId(Long id);

    /**
     * 查询出库样本样本数量
     * @param id
     * @param taskId
     * @return
     */
    @Query("select count(t.id) from StockOutTaskFrozenTube t " +
        " where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenBox.id=?1 " +
        " and t.stockOutTask.id = ?2")
    Long countSampleByFrozenBoxAndTask(Long id, Long taskId);

    /**
     * 查询待装盒的冻存盒列表的出库样本量
     * @param id
     * @param taskId
     * @return
     */
     @Query("select count(t.id) from StockOutTaskFrozenTube t " +
        " where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenTube.frozenBox.id=?1 " +
        " and t.stockOutTask.id = ?2 and t.status='1801' and t.stockOutPlanFrozenTube.stockOutReqFrozenTube.frozenTube.frozenBox.status !='2008'")
    Long countByFrozenBoxAndTask(Long id, Long taskId);

     @Query(value = " select  a.id,count(1) from frozen_box a" +
         "        left join   stock_out_req_frozen_tube c on c.frozen_box_id = a.id " +
         "        where c.stock_out_task_id =?1 and a.status = '1301' and a.status!='2008' " +
         "        group by a.id" ,nativeQuery = true)
    List<Object[]> countByTaskGroupByBox(Long taskId);

    @Modifying
    @Query("update StockOutTaskFrozenTube t set t.status = '1803' where t.id in ?1")
    void updateByStockOutFrozenTubeIds(List<Long> taskTubes);

    Long countByStockOutTaskIdAndStatusNot(Long taskId, String status);

    Long countByStockOutTaskIdAndStatusNotIn(Long taskId, List<String> taskStatus);

    @Query("select count(t.id) from StockOutTaskFrozenTube t " +
        "  where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.stockOutRequirement.id=?1 " +
        "  and t.status = ?2 ")
    Long countByStockOutRequirementIdAndStatus(Long id, String status);

    @Query("select count(t.id) from StockOutTaskFrozenTube t " +
            " where t.stockOutPlanFrozenTube.stockOutReqFrozenTube.stockOutRequirement.stockOutApply.id=?1 " +
            " and t.status = ?2 ")
    Long countByStockOutApplyIdAndStatus(Long id, String status);
}
