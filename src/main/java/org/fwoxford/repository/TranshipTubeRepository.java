package org.fwoxford.repository;

import org.fwoxford.domain.TranshipTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipTube entity.
 */
@SuppressWarnings("unused")
public interface TranshipTubeRepository extends JpaRepository<TranshipTube,Long> {

    Long countByColumnsInTubeAndRowsInTubeAndStatusAndTranshipBoxIdAndFrozenTubeId(String tubeColumns, String tubeRows, String status, Long transhipBoxId, Long frozenTubeId);

    @Query(value = " select t.* from tranship_tube t"
        + " where rowid in (select row_id from (select b.frozen_tube_id,max(rowid) row_id from tranship_tube b group by b.frozen_tube_id ))"
        + " and t.tranship_box_id = ?1 " , nativeQuery = true)
    List<TranshipTube> findByTranshipBoxIdLast(Long id);
}
