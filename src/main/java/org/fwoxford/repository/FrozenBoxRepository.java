package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {
    @Query(value = "select t.* from frozen_box t left join tranship_box p on t.id=p.frozen_box_id where p.tranship_id=?1 and t.status!='0000' and t.status!='2090'",nativeQuery = true)
    List<FrozenBox> findAllFrozenBoxByTranshipId(Long transhipId);

    @Query("select box from FrozenBox box where box.frozenBoxCode = ?1 and box.status!='0000' and box.status!='2090'" )
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);

    @Query(value = "select * from frozen_box box where box.equipment_id = ?1" +
        " and box.area_id = ?2 " +
        " and box.support_rack_id = ?3 " +
        " and box.columns_in_shelf = ?4 " +
        " and box.rows_in_shelf = ?5 and box.status not in ('0000','2003','2090','2009','2010')" , nativeQuery = true)
    List<FrozenBox> findByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    @Modifying
    @Query("update FrozenBox b set b.status=?2 where b.frozenBoxCode=?1")
    void updateStatusByFrozenBoxCode(String frozenBoxCode, String status);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.status in ('"+Constants.FROZEN_BOX_STOCKED+"','"+Constants.FROZEN_BOX_PUT_SHELVES+"') ")
    List<FrozenBox> findByEquipmentCode(String equipmentCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2 and box.status not in ('0000','2003','2090','2009','2010')")
    List<FrozenBox> findByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select count(box) from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2 and box.status not in ('0000','2003','2090','2009','2010')")
    Long countByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2  and box.supportRackCode = ?3 " +
        " and box.columnsInShelf = ?4 and box.rowsInShelf = ?5" +
        " and box.status not in ('0000','2003','2090','2009','2010')")
    FrozenBox findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    @Query(value = "select f.* from frozen_box f where f.equipment_code = ?1 and f.area_code =?2" +
        " and f.support_rack_code = ?3" +
        " and f.status not in ('0000','2003','2090','2009','2010')" ,nativeQuery = true)
    List<FrozenBox> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    @Query(value = "select count(f.id) from frozen_box f where f.equipment_code = ?1 and f.area_code =?2" +
        " and f.support_rack_code = ?3" +
        " and f.status not in ('0000','2003','2090','2009','2010')" ,nativeQuery = true)
    Long countByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    @Query(value = "select f.* from frozen_box f where f.project_code = ?1 and f.sample_type_code =?2 and f.status!='0000' and f.status!='2090'" ,nativeQuery = true)
    List<FrozenBox> findByProjectCodeAndSampleTypeCode(String projectCode, String sampleTypeCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatus(String projectCode, String sampleTypeCode, String status);

    List<FrozenBox> findByFrozenBoxCodeInAndStatusIn(List<String> frozenBoxCodeStr, List<String> statusStr);

    @Query(value = "select f.* from frozen_box f where f.frozen_box_code in ?1 and f.status!='0000' and f.status!='2090'" ,nativeQuery = true)
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
        " and box.status not in ('2090','0000') group by box.id " , nativeQuery = true)
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



//    @Query(value = "select DISTINCT a.* from frozen_box a\n" +
//        " left join stock_out_req_frozen_tube c on c.frozen_box_id = a.id\n" +
//        " left join stock_out_plan_tube b on b.stock_out_req_frozen_tube_id =c.id \n" +
//        " left join stock_out_task_tube e on e.stock_out_plan_frozen_tube_id =b.id\n" +
//        " where e.stock_out_task_id = ?1 " ,nativeQuery = true)
    @Query(value = "select a.* from frozen_box a left join (  " +
        "        select  a.id from frozen_box a  " +
        "            left join   stock_out_req_frozen_tube c on c.frozen_box_id = a.id " +
        "            where c.stock_out_task_id = ?1 " +
        "            group by a.id)b on a.id = b.id where b.id is not null " ,nativeQuery = true)

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

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube left join stock_in_tube inTube on inTube.frozen_tube_id = tube.id left join stock_in_box inBox on inTube.stock_in_box_id = inBox.id  and inBox.stock_in_code =?6 where inTube.frozen_box_code =f.frozen_box_code and tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber from frozen_box f  " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_classification_id in ?3" +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc FETCH FIRST 10 ROWS ONLY",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleClassificationIdInAllStock(String frozenBoxCode, Long projectId,
                                                                              List<Long> sampleClassificationIdStr, Long frozenBoxTypeId, String status, String stockInCode);

    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube left join stock_in_tube inTube on inTube.frozen_tube_id = tube.id left join stock_in_box inBox on inTube.stock_in_box_id = inBox.id  and inBox.stock_in_code =?6 where inTube.frozen_box_code =f.frozen_box_code and tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber " +
        " from frozen_box f " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_type_id=?3 " +
        " and f.frozen_box_type_id=?4 " +
        " and f.status=?5" +
        " and (select count(tube.id) from frozen_tube tube where tube.frozen_box_code = f.frozen_box_code and tube.status!='0000')<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc FETCH FIRST 10 ROWS ONLY",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleTypeIdInAllStock(String frozenBoxCode, Long projectId, Long sampleTypeId, Long frozenBoxTypeId, String status, String stockInCode);

    FrozenBox findBySupportRackIdAndColumnsInShelfAndRowsInShelf(Long id, String columnsInShelf, String rowsInShelf);

    List<FrozenBox> findProjectByEquipmentId(Long id);

    @Query("select count(box) from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2  and box.supportRackCode = ?3 " +
        " and box.columnsInShelf = ?4 and box.rowsInShelf = ?5" +
        " and box.status!='0000'and box.status!='2090'")
    Long countByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    @Modifying
    @Query("update FrozenBox b set b.status=?1 where b.frozenBoxCode in ?2 and b.status not in ('2090','0000')")
    void updateStatusByFrozenBoxCodes(String frozenBoxTranshipComplete, List<String> frozenBoxCodes);
    @Query(value = "SELECT ROWNUM  as id," +
        " cast(temp.equipment_code as varchar2(255)) as equipment_code, " +
        " cast(temp.area_code as varchar2(255)) as area_code, " +
        " cast(temp.support_rack_code as varchar2(255)) as support_rack_code, " +
        " cast(temp.rows_in_shelf as varchar2(255)) as rows_in_shelf, " +
        " cast(temp.columns_in_shelf as varchar2(255)) as columns_in_shelf, " +
        " equipment_id,area_id,support_rack_id, " +
        " cast(temp.frozen_box_code as varchar2(255)) as frozen_box_code, " +
        " frozen_box_id,project_id, " +
        " cast(temp.project_code as varchar2(255)) as project_code, " +
        " project_site_id,sample_type_id, " +
        " cast(temp.sample_type_code as varchar2(255)) as sample_type_code, "+
        " cast(temp.sample_type_name as varchar2(255)) as sample_type_name, "+
        " sample_classification_id, " +
        " cast(temp.sample_classification_code as varchar2(255)) as sample_classification_code, "+
        " cast(temp.sample_classification_name as varchar2(255)) as sample_classification_name, "+
        " created_date,type " +
        " FROM" +
        "                (" +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,104 as type" +
        "                    FROM position_move_record t where t.move_type in (1,2) and frozen_box_id = ?1" +
        "        " +
        "                    UNION" +
        "           " +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,105 as type" +
        "                    FROM position_change_record t where t.change_type in (1,2) and frozen_box_id = ?1" +
        "            " +
        "                    UNION" +
        "            " +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,106 as type" +
        "                    FROM position_destroy_record t where t.destroy_type in (1,2) and frozen_box_id = ?1" +
        "            " +
        "            ) temp ORDER BY created_date DESC",nativeQuery = true)
    List<Object[]> findPositionHistory(Long id);

    @Query(value = "SELECT ROWNUM  as id," +
        " cast(temp.equipment_code as varchar2(255)) as equipment_code, " +
        " cast(temp.area_code as varchar2(255)) as area_code, " +
        " cast(temp.support_rack_code as varchar2(255)) as support_rack_code, " +
        " cast(temp.rows_in_shelf as varchar2(255)) as rows_in_shelf, " +
        " cast(temp.columns_in_shelf as varchar2(255)) as columns_in_shelf, " +
        " equipment_id,area_id,support_rack_id, " +
        " cast(temp.frozen_box_code as varchar2(255)) as frozen_box_code, " +
        " frozen_box_id,project_id, " +
        " cast(temp.project_code as varchar2(255)) as project_code, " +
        " project_site_id,sample_type_id, " +
        " cast(temp.sample_type_code as varchar2(255)) as sample_type_code, "+
        " cast(temp.sample_type_name as varchar2(255)) as sample_type_name, "+
        " sample_classification_id, " +
        " cast(temp.sample_classification_code as varchar2(255)) as sample_classification_code, "+
        " cast(temp.sample_classification_name as varchar2(255)) as sample_classification_name, "+
        " created_date,type " +
        " FROM" +
        "                (" +
        "                    select t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,101 as type" +
        "                    from tranship_box t where frozen_box_id = ?1" +
        "                    UNION" +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,102 as type" +
        "                    FROM stock_in_box t where frozen_box_id = ?1 and t.status = '2004'" +
        "                    UNION" +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,103 as type" +
        "                    FROM stock_out_box t where frozen_box_id = ?1" +
        "            ) temp ORDER BY created_date DESC",nativeQuery = true)
    List<Object[]> findFrozenBoxHistory(Long id);

    FrozenBox findByFrozenBoxCodeAndSampleTypeCode(String boxCode, String sampleTypeCode);

    @Query(value = " select support_rack_id,count(support_rack_id) as noo from frozen_box where status in ('2004','2006')" +
        " and support_rack_id in ?1 " +
        " group by support_rack_id ",nativeQuery = true)
    List<Object[]> findFrozenBoxGroupBySupportRack(List<Long> supportRackIds);

    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,b.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,107 as type" +
        "                    FROM stock_out_handover_box t " +
        "                    LEFT JOIN stock_out_box b on t.stock_out_frozen_box_id = b.id" +
        "                    where b.frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxStockOutHandOverHistory(Long id);
    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,103 as type" +
        "                    FROM stock_out_box t where t.frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxStockOutHistory(Long id);
    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,102 as type" +
        "                    FROM stock_in_box t where t.frozen_box_id = ?1 and t.status = '2004'" ,nativeQuery = true)
    List<Object[]> findFrozenBoxStockInHistory(Long id);
    @Query(value = "select t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,101 as type" +
        "                    from tranship_box t where frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxTranshipHistory(Long id);

    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,104 as type" +
        "                    FROM position_move_record t where t.move_type in (1,2) and frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxMoveHistory(Long id);

    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,105 as type" +
        "                    FROM position_change_record t where t.change_type in (1,2) and frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxChangeHistory(Long id);

    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,106 as type" +
        "                    FROM position_destroy_record t where t.destroy_type in ("+ Constants.DESTROY_TYPE_FOR_TUBE+","+Constants.DESTROY_TYPE_FOR_BOX+") and frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxDestoryHistory(Long id);


    @Query(value = "select t.frozen_box_code from tranship_box t where t.status != '"+Constants.FROZEN_BOX_INVALID+"' and t.status != '"+Constants.INVALID+"' " +
        " and (?1=0 or t.project_id=?1) and (?2=0 or t.sample_type_id=?2) and (?3=0 or t.sample_classification_id=?3)" ,nativeQuery = true)
    List<String> findAllTranshipFrozenBoxCode(Long projectId, Long sampleTypeId, Long sampleClassId);

    @Query(value = "select t.frozen_box_code from stock_in_box t where t.status != '"+Constants.FROZEN_BOX_INVALID+"' and t.status != '"+Constants.INVALID+"' " +
        " and (?1=0 or t.project_id=?1) and (?2=0 or t.sample_type_id=?2) and (?3=0 or t.sample_classification_id=?3)" ,nativeQuery = true)
    List<String> findAllStockInFrozenBoxCode(Long projectId, Long sampleTypeId, Long sampleClassId);

    @Query(value = "select t.frozen_box_code from position_destroy_record t where t.status != '"+Constants.FROZEN_BOX_INVALID+"' and t.status != '"+Constants.INVALID+"' and t.destroy_type='"+Constants.DESTROY_TYPE_FOR_BOX+"' " +
        " and (?1=0 or t.project_id=?1) and (?2=0 or t.sample_type_id=?2) and (?3=0 or t.sample_classification_id=?3)" ,nativeQuery = true)
    List<String> findAllDestroyFrozenBoxCode(Long projectId, Long sampleTypeId, Long sampleClassId);
}

