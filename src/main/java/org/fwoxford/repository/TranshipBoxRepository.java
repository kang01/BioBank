package org.fwoxford.repository;

import org.fwoxford.domain.Tranship;
import org.fwoxford.domain.TranshipBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipBox entity.
 */
@SuppressWarnings("unused")
public interface TranshipBoxRepository extends JpaRepository<TranshipBox,Long> {

    TranshipBox findByTranshipIdAndFrozenBoxId(Long transhipId, Long frozenBoxId);

    @Modifying
    @Query("update TranshipBox t set t.status=?3 where t.tranship.id=?1 and t.frozenBoxCode=?2")
    void updateStatusByTranshipIdAndFrozenBoxCode(Long id, String frozenBoxCode, String status);

    @Query(value = "select t.* from tranship_box t where t.tranship_id =?1 and t.status!='0000'",nativeQuery = true)
    List<TranshipBox> findByTranshipId(Long id);

    @Query(value = "select t.* from tranship_box t where t.frozen_box_code =?1 and t.status!='0000'",nativeQuery = true)
    TranshipBox findByFrozenBoxCode(String frozenBoxCode);
}
