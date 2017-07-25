package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;

import org.fwoxford.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {
    @Query(value = "select t.* from frozen_box t left join tranship_box p on t.id=p.frozen_box_id where p.tranship_id=?1 and t.status!='0000' and t.status!='2005'",nativeQuery = true)
    List<FrozenBox> findAllFrozenBoxByTranshipId(Long transhipId);

    @Query("select box from FrozenBox box where box.frozenBoxCode = ?1 and box.status!='0000' and box.status!='2005'" )
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);

    @Query(value = "select * from frozen_box box where box.equipment_id = ?1" +
        " and box.area_id = ?2 " +
        " and box.support_rack_id = ?3 " +
        " and box.columns_in_shelf = ?4 " +
        " and box.rows_in_shelf = ?5 and box.status!='2005' and t.status!='0000'" , nativeQuery = true)
    List<FrozenBox> findByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    @Modifying
    @Query("update FrozenBox b set b.status=?2 where b.frozenBoxCode=?1")
    void updateStatusByFrozenBoxCode(String frozenBoxCode, String status);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.status in ('2004','2006') ")
    List<FrozenBox> findByEquipmentCode(String equipmentCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2 and box.status!='0000' and box.status!='2005'")
    List<FrozenBox> findByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select count(box) from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2 and box.status!='0000'  and box.status!='2005'")
    Long countByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2  and box.supportRackCode = ?3 " +
        " and box.columnsInShelf = ?4 and box.rowsInShelf = ?5" +
        " and box.status!='0000'and box.status!='2005'")
    FrozenBox findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    @Query(value = "select f.* from frozen_box f where f.equipment_code = ?1 and f.area_code =?2" +
        " and f.support_rack_code = ?3" +
        " and f.status!='0000'  and f.status!='2005'" ,nativeQuery = true)
    List<FrozenBox> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    @Query(value = "select count(f.id) from frozen_box f where f.equipment_code = ?1 and f.area_code =?2" +
        " and f.support_rack_code = ?3" +
        " and f.status!='0000'and f.status!='2005'" ,nativeQuery = true)
    Long countByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    @Query(value = "select f.* from frozen_box f where f.project_code = ?1 and f.sample_type_code =?2 and f.status!='0000' and f.status!='2005'" ,nativeQuery = true)
    List<FrozenBox> findByProjectCodeAndSampleTypeCode(String projectCode, String sampleTypeCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatus(String projectCode, String sampleTypeCode, String status);

    List<FrozenBox> findByFrozenBoxCodeInAndStatusIn(List<String> frozenBoxCodeStr, List<String> statusStr);

    @Query(value = "select f.* from frozen_box f where f.frozen_box_code in ?1 and f.status!='0000' and f.status!='2005'" ,nativeQuery = true)
    List<FrozenBox> findByFrozenBoxCodeIn(List<String> frozenBoxCodeStr);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatusIn(String projectCode, String sampleTypeCode, List<String> statusList);

    @Query(value = "select t.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = t.frozen_box_code  and tube.status!='0000')as sampleNumber  from frozen_Box t left join tranship_Box b on t.frozen_box_code = b.frozen_box_code \n" +
       "left join tranship s on b.tranship_id = s.id \n" +
       "where t.project_code = ?1 and t.sample_type_code = ?2 and s.tranship_code = ?3 \n" +
       "and t.status = ?4 order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndTranshipCodeAndStatus(String projectCode, String sampleTypeCode, String transhipCode,String status);

    @Query(value = "select t.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = t.frozen_box_code  and tube.status!='0000')as sampleNumber from frozen_box t left join stock_in_box b on t.frozen_box_code = b.frozen_box_code \n" +
        "where t.project_code = ?1 and t.sample_type_code = ?2 and b.stock_in_code = ?3 \n" +
        "and t.status = ?4 order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStockInCodeAndStatus(String projectCode, String sampleTypeCode, String stockInCode, String frozenBoxStocking);

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from frozen_box f left join stock_in_box s on f.id=s.frozen_box_id " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_classification_id in ?3" +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " and s.stock_in_code =?6" +
        " order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleClassificationId(String frozenBoxCode, Long projectId,
                                                                    List<Long> sampleClassificationIdStr, Long frozenBoxTypeId, String status, String stockInCode);
    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from frozen_box f left join stock_in_box s on f.id=s.frozen_box_id " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_type_id=?3 " +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " and s.stock_in_code =?6" +
        " order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleTypeId(String frozenBoxCode, Long projectId, Long sampleTypeId, Long frozenBoxTypeId, String status, String stockInCode);

    @Query(value = "select box.id ,count(*) as num from frozen_box box  where box.frozen_box_code =?1" +
        " and box.status not in ('2005','0000') group by box.id " , nativeQuery = true)
    List<Object[]> countByFrozenBoxCode(String frozenBoxCode);

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from frozen_box f left join stock_in_box s on f.id=s.frozen_box_id " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and s.stock_in_code =?3" +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBox(String frozenBoxCode, Long projectId, String stockInCode, Long frozenBoxTypeId, String status);

    @Query("SELECT DISTINCT s FROM FrozenBox s "
        + " left join StockOutReqFrozenTube r on s.id = r.frozenBox.id "
        + " left join StockOutPlanFrozenTube b on b.stockOutReqFrozenTube.id =r.id "
        + " left join StockOutTaskFrozenTube e on e.stockOutPlanFrozenTube.id =b.id "
        + " WHERE r.stockOutRequirement.id in ?1 "
        + " AND  e.stockOutPlanFrozenTube.id is null "
        )
    Page<FrozenBox> findAllByrequirementIds(List<Long> ids, Pageable pageable);


    @Query(value = "select DISTINCT a.* from frozen_box a\n" +
        " left join stock_out_req_frozen_tube c on c.frozen_box_id = a.id\n" +
        " left join stock_out_plan_tube b on b.stock_out_req_frozen_tube_id =c.id \n" +
        " left join stock_out_task_tube e on e.stock_out_plan_frozen_tube_id =b.id\n" +
        " where e.stock_out_task_id = ?1 " ,nativeQuery = true)
    List<FrozenBox> findByStockOutTaskId(Long taskId);

    @Query( "select  DISTINCT a from FrozenBox a" +
        " inner join StockOutReqFrozenTube c on c.frozenBox.id = a.id" +
        " inner join StockOutPlanFrozenTube b on b.stockOutReqFrozenTube.id =c.id" +
        " inner join StockOutTaskFrozenTube e on e.stockOutPlanFrozenTube.id =b.id" +
        " where e.stockOutTask.id = ?1 ")
    Page<FrozenBox> findAllByTask(Long taskId, Pageable pageable);

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from frozen_box f " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.frozen_box_type_id=?3 " +
        " and f.status=?4" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxInAllStock(String frozenBoxCode, Long projectId, Long frozenBoxTypeId, String status);

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from frozen_box f  " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_classification_id in ?3" +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleClassificationIdInAllStock(String frozenBoxCode, Long projectId,
                                                                    List<Long> sampleClassificationIdStr, Long frozenBoxTypeId, String status);

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from frozen_box f " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_type_id=?3 " +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleTypeIdInAllStock(String frozenBoxCode, Long projectId, Long sampleTypeId, Long frozenBoxTypeId, String status);

    FrozenBox findBySupportRackIdAndColumnsInShelfAndRowsInShelf(Long id, String columnsInShelf, String rowsInShelf);

    List<FrozenBox> findProjectByEquipmentId(Long id);

    @Query("select count(box) from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2  and box.supportRackCode = ?3 " +
        " and box.columnsInShelf = ?4 and box.rowsInShelf = ?5" +
        " and box.status!='0000'and box.status!='2005'")
    Long countByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    @Modifying
    @Query("update FrozenBox b set b.status=?1 where b.frozenBoxCode in ?2")
    void updateStatusByFrozenBoxCodes(String frozenBoxTranshipComplete, List<String> frozenBoxCodes);
}
