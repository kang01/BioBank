package org.fwoxford.repository;

import org.fwoxford.domain.TranshipBoxPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipBoxPosition entity.
 */
@SuppressWarnings("unused")
public interface TranshipBoxPositionRepository extends JpaRepository<TranshipBoxPosition,Long> {
    @Query(value = "select * from (select rownum  num, a.*  from  tranship_box_pos a where a.tranship_box_id = ?1 )\n" +
        "          where num = (select count(1)  from  tranship_box_pos where tranship_box_id = 1  )",nativeQuery = true)
    TranshipBoxPosition findByTranshipBoxIdLast(Long id);

    Long countByTranshipBoxIdAndEquipmentCodeAndAreaCodeAndSupportRackCodeAndRowsInShelfAndColumnsInShelf(Long TranshipBoxIid, String equipmentCode, String areaCode, String supportRackCode, String rowsInShelf, String columnsInShelf);
}
