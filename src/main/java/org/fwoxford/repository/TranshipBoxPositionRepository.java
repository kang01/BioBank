package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.TranshipBoxPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipBoxPosition entity.
 */
@SuppressWarnings("unused")
public interface TranshipBoxPositionRepository extends JpaRepository<TranshipBoxPosition,Long> {

    @Query( "select a from TranshipBoxPosition a where a.transhipBox.id = ?1 and a.status != '"+ Constants.INVALID+"'")
    TranshipBoxPosition findByTranshipBoxId(Long id);

    Long countByTranshipBoxIdAndEquipmentCodeAndAreaCodeAndSupportRackCodeAndRowsInShelfAndColumnsInShelf(Long TranshipBoxIid, String equipmentCode, String areaCode, String supportRackCode, String rowsInShelf, String columnsInShelf);
}
