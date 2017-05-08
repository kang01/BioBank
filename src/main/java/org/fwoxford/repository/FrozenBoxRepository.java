package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {
    @Query(value = "select t.* from frozen_box t left join tranship_box p on t.id=p.frozen_box_id where p.tranship_id=?1 and t.status!='0000'",nativeQuery = true)
    List<FrozenBox> findAllFrozenBoxByTranshipId(Long transhipId);

    @Query("select box from FrozenBox box where box.frozenBoxCode = ?1 and box.status!='0000'")
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);

    @Query(value = "select * from frozen_box box where box.equipment_id = ?1" +
        " and box.area_id = ?2 " +
        " and box.support_rack_id = ?3 " +
        " and box.columns_in_shelf = ?4 " +
        " and box.rows_in_shelf = ?5 and box.status!='2005' and t.status!='0000'" , nativeQuery = true)
    List<FrozenBox> countByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    @Modifying
    @Query("update FrozenBox b set b.status=?2 where b.frozenBoxCode=?1")
    void updateStatusByFrozenBoxCode(String frozenBoxCode, String status);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.status!='0000'")
    List<FrozenBox> findByEquipmentCode(String equipmentCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2 and box.status!='0000'")
    List<FrozenBox> findByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2  and box.supportRackCode = ?3 " +
        " and box.columnsInShelf = ?4 and box.rowsInShelf = ?5" +
        " and box.status!='0000'")
    FrozenBox findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    @Query(value = "select f.* from frozen_box f where f.equipment_code = ?1 and f.area_code =?2" +
        " and f.support_rack_code = ?3" +
        " and f.status!='0000'" ,nativeQuery = true)
    List<FrozenBox> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    @Query(value = "select f.* from frozen_box f where f.project_code = ?1 and f.sample_type_code =?2 and f.status!='0000'" ,nativeQuery = true)
    List<FrozenBox> findByProjectCodeAndSampleTypeCode(String projectCode, String sampleTypeCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatus(String projectCode, String sampleTypeCode, String status);

    List<FrozenBox> findByFrozenBoxCodeInAndStatusIn(List<String> frozenBoxCodeStr, List<String> statusStr);

    @Query(value = "select f.* from frozen_box f where f.frozen_box_code in ?1 and f.status!='0000'" ,nativeQuery = true)
    List<FrozenBox> findByFrozenBoxCodeIn(List<String> frozenBoxCodeStr);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatusIn(String projectCode, String sampleTypeCode, List<String> statusList);

    @Query(value = "select t.* from frozen_Box t left join tranship_Box b on t.frozen_box_code = b.frozen_box_code \n" +
       "left join tranship s on b.tranship_id = s.id \n" +
       "where t.project_code = ?1 and t.sample_type_code = ?2 and s.tranship_code = ?3 \n" +
       "and t.status = ?4 order by t.sample_number asc",nativeQuery = true)
    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndTranshipCodeAndStatus(String projectCode, String sampleTypeCode, String transhipCode,String status);

    @Query(value = "select t.* from frozen_box t left join stock_in_box b on t.frozen_box_code = b.frozen_box_code \n" +
        "where t.project_code = ?1 and t.sample_type_code = ?2 and b.stock_in_code = ?3 \n" +
        "and t.status = ?4 order by t.sample_number asc",nativeQuery = true)
    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStockInCodeAndStatus(String projectCode, String sampleTypeCode, String stockInCode, String frozenBoxStocking);

    @Query(value = "select f.* from frozen_box f left join stock_in_box s on f.id=s.frozen_box_id " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_classification_id in ?3" +
        " and s.stock_in_code =?4" +
        " and f.frozen_box_type_id=?5 " +
        " and f.status=?6" +
        " and f.sample_number<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by f.sample_number asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleClassificationId(String frozenBoxCode,  Long projectId,
                                            List<Long> sampleClassificationIdStr, String stockInCode, Long frozenBoxTypeId, String frozenBoxStocked);
    @Query(value = "select f.* from frozen_box f left join stock_in_box s on f.id=s.frozen_box_id " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_type_id=?3 " +
        " and s.stock_in_code =?4" +
        " and f.frozen_box_type_id=?5 " +
        " and f.status=?5" +
        " and f.sample_number<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by f.sample_number asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleTypeId(String frozenBoxCode, Long projectId,Long sampleTypeId, String stockInCode, Long frozenBoxTypeId, String status);

    @Query(value = "select box.id ,count(*) as num from frozen_box box  where box.frozen_box_code =?1" +
        " and box.status not in ('2005','0000') group by box.id " , nativeQuery = true)
    List<Object[]> countByFrozenBoxCode(String frozenBoxCode);
}
