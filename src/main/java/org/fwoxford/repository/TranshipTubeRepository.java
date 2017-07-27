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
        "   select row_number() over(partition by frozen_tube_id order by CREATED_DATE desc) rn, a.* from  tranship_tube a where tranship_box_id = ?1 and status !='0000'" +
        ") where rn = 1 " , nativeQuery = true)

    List<TranshipTube> findByTranshipBoxIdLast(Long id);

    TranshipTube findByTranshipBoxIdAndFrozenTubeId(Long transhipBoxId, Long frozenTubeId);

    @Query(value = " select * from (" +
        "   select row_number() over(partition by frozen_tube_id order by CREATED_DATE desc) rn, a.* from  tranship_tube a where tranship_box_id = ?1 and frozen_tube_id = ?2 and status !='0000'" +
        ") where rn = 1 " , nativeQuery = true)
    TranshipTube findByTranshipBoxIdAndFrozenTubeIdLast(Long id, Long id1);

    @Query(value = "select count(distinct t.id) from tranship_tube t " +
        " left join frozen_tube f on t.frozen_tube_id = f.id " +
        " left join tranship_box tb on  t.tranship_box_id = tb.id " +
        " left join tranship s on  tb.tranship_id = s.id " +
        " where s.tranship_code = ?1 and f.frozen_tube_state = '2002'",nativeQuery = true)
    Long countUnStockInTubeByTranshipCode(String transhipCode);

    @Query(value = "select count(distinct t.id) from tranship_tube t " +
        " left join frozen_tube f on t.frozen_tube_id = f.id " +
        " left join tranship_box tb on  t.tranship_box_id = ?1 " +
        " where  f.frozen_tube_state = '2002'",nativeQuery = true)
    Long countUnStockInTubeByTranshipBox(Long transhipBoxId);

    @Query(value = "select count(distinct t.id) from tranship_tube t " +
        " left join frozen_tube f on t.frozen_tube_id = f.id " +
        " left join tranship_box tb on  t.tranship_box_id = tb.id " +
        " left join tranship s on  tb.tranship_id = s.id " +
        " where s.tranship_code = ?1 and f.frozen_tube_state = ?2",nativeQuery = true)
    Long countUnStockInTubeByTranshipCodeAndStatus(String transhipCode, String status);
}
