package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {
    @Query(value = "select t.* from frozen_box t left join tranship_box p on t.id=p.frozen_box_id where p.tranship_id=?1",nativeQuery = true)
    List<FrozenBox> findAllFrozenBoxByTranshipId(Long transhipId);

    @Query("select box from FrozenBox box where box.frozenBoxCode = ?1")
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);

    @Query(value = "select * from frozen_box box where box.equipment_id = ?1" +
        " and box.area_id = ?2 " +
        " and box.support_rack_id = ?3 " +
        " and box.columns_in_shelf = ?4 " +
        " and box.rows_in_shelf = ?5 and box.status!='2005'" , nativeQuery = true)
    List<FrozenBox> countByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    @Modifying
    @Query("update FrozenBox b set b.status=?2 where b.frozenBoxCode=?1")
    void updateStatusByFrozenBoxCode(String frozenBoxCode, String status);

    List<FrozenBox> findByEquipmentCode(String equipmentCode);

    List<FrozenBox> findByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    FrozenBox findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    List<FrozenBox> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCode(String projectCode, String sampleTypeCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatus(String projectCode, String sampleTypeCode, String frozenBoxStocking);

    List<FrozenBox> findByFrozenBoxCodeInAndStatusIn(List<String> frozenBoxCodeStr, List<String> statusStr);

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
        " and f.sample_type_id in ?3" +
        " and f.sample_classification_id in ?4" +
        " and s.stock_in_code =?5" +
        " and f.frozen_box_type_id=?6 " +
        " and f.status=?7" +
        " and f.sample_number<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by f.sample_number asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleTypeIdAndSampleClassificationId(String frozenBoxCode, String projectCode, Long sampleTypeId,
                                            List<Long> sampleClassificationIdStr, String stockInCode, Long frozenBoxTypeId, String frozenBoxStocked);
    @Query(value = "select f.* from frozen_box f left join stock_in_box s on f.id=s.frozen_box_id " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_type_id in ?3" +
        " and s.stock_in_code =?4" +
        " and f.frozen_box_type_id=?5 " +
        " and f.status=?6" +
        " and f.sample_number<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by f.sample_number asc",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleTypeIdAndAll(String frozenBoxCode, String projectCode, Long sampleTypeCode, String stockInCode, Long frozenBoxTypeId, String status);

}
