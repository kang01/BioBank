package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;

import org.fwoxford.service.dto.FrozenBoxDTO;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {

    List<FrozenBox> findAllFrozenBoxByTranshipId(Long transhipId);

    @Query("select box from FrozenBox box where box.frozenBoxCode = ?1")
    Optional<FrozenBox> findFrozenBoxDetailsByBoxCode(String frozenBoxCode);

    @Query(value = "select * from frozen_box box where box.equipment_id = ?1" +
        " and box.area_id = ?2 " +
        " and box.support_rack_id = ?3 " +
        " and box.columns_in_shelf = ?4 " +
        " and box.rows_in_shelf = ?5" , nativeQuery = true)
    List<FrozenBox> countByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    @Query("select box from FrozenBox box where box.tranship.transhipCode = ?1")
    List<FrozenBox> findFrozenBoxByTranshipCode(String transhipCode);
}
