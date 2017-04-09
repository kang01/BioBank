package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {

    List<FrozenBox> findAllFrozenBoxByTranshipId(Long transhipId);

    @Query("select box from FrozenBox box where box.frozenBoxCode = ?1")
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);

    @Query(value = "select * from frozen_box box where box.equipment_id = ?1" +
        " and box.area_id = ?2 " +
        " and box.support_rack_id = ?3 " +
        " and box.columns_in_shelf = ?4 " +
        " and box.rows_in_shelf = ?5" , nativeQuery = true)
    List<FrozenBox> countByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    @Query(value = "select b.* from frozen_box b left join tranship s on s.id = b.tranship_id where s.tranship_code  = ?1" ,nativeQuery = true)
    List<FrozenBox> findFrozenBoxByTranshipCode(String transhipCode);

    @Modifying
    @Query("update FrozenBox b set b.status=?2 where b.frozenBoxCode=?1")
    void updateStatusByFrozenBoxCode(String frozenBoxCode, String status);

    List<FrozenBox> findByEquipmentCode(String equipmentCode);

    List<FrozenBox> findByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    FrozenBox findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    List<FrozenBox> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCode(String projectCode, String sampleTypeCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatus(String projectCode, String sampleTypeCode, String frozenBoxStocking);
}
