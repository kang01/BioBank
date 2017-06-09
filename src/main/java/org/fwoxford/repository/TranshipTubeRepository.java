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

    @Query(value = " select * from (" +
        "   select row_number() over(partition by frozen_tube_id order by CREATED_DATE desc) rn, a.* from  tranship_tube a where tranship_box_id = ?1 " +
        ") where rn = 1 " , nativeQuery = true)

    List<TranshipTube> findByTranshipBoxIdLast(Long id);
}
