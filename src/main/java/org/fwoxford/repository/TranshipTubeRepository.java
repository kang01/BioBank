package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.TranshipTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipTube entity.
 */
@SuppressWarnings("unused")
public interface TranshipTubeRepository extends JpaRepository<TranshipTube,Long> {

    Long countByColumnsInTubeAndRowsInTubeAndStatusAndTranshipBoxIdAndFrozenTubeId(String tubeColumns, String tubeRows, String status, Long transhipBoxId, Long frozenTubeId);

    TranshipTube findByTranshipBoxIdAndFrozenTubeId(Long transhipBoxId, Long frozenTubeId);

    @Query(value = "select count(distinct t.id) from tranship_tube t " +
        " left join frozen_tube f on t.frozen_tube_id = f.id " +
        " left join tranship_box tb on  t.tranship_box_id = tb.id " +
        " left join tranship s on  tb.tranship_id = s.id " +
        " left join stock_in_tube st on  st.frozen_tube_id = f.id " +
        " where s.tranship_code = ?1 and st.frozen_tube_state = ?2",nativeQuery = true)
    Long countUnStockInTubeByTranshipCodeAndStatus(String transhipCode, String status);

    @Modifying
    @Query("update TranshipTube t set t.frozenTubeState = ?1  where t.frozenBoxCode in ?2 and t.status not in  ('"+ Constants.INVALID+"','"+Constants.FROZEN_BOX_INVALID+"')")
    void updateFrozenTubeStateByFrozenBoxCodesAndTranshipCode(String status, List<String> frozenBoxCodes);

    List<TranshipTube> findByTranshipBoxIdAndStatusNotIn(Long id,List<String> status);

    @Modifying
    @Query("update TranshipTube t set t.status = ?1  where t.transhipBox.id = ?2")
    void updateStatusByTranshipBoxId(String status, Long id);

    @Query(value = "select count(1) from tranship_tube t where t.tranship_box_id in ?1 and t.status=?2" ,nativeQuery = true)
    int countByTranshipBoxIdsStrAndStatus(List<Long> boxIds, String status);

    @Query(value = "select t.sample_temp_code,count(t.sample_temp_code) as noo from tranship_tube t " +
            " where t.tranship_box_id in ?1 and t.status not in ('"+ Constants.INVALID+"','"+Constants.FROZEN_BOX_INVALID+"') and t.sample_code is null " +
            " GROUP BY t.sample_temp_code" ,nativeQuery = true)
    List<Object[]> countByTranshipBoxIdsAndGroupBySampleTempCode(List<Long> boxIds);

    @Query(value = "select t.sample_code,count(t.sample_code) as noo from tranship_tube t " +
            " where t.tranship_box_id in ?1 and t.status not in ('"+ Constants.INVALID+"','"+Constants.FROZEN_BOX_INVALID+"') " +
            " GROUP BY t.sample_code " ,nativeQuery = true)
    List<Object[]> countByTranshipBoxIdsAndGroupBySampleCode(List<Long> boxIds);
}
