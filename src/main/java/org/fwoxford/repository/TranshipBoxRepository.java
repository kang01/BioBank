package org.fwoxford.repository;

import org.fwoxford.domain.TranshipBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TranshipBox entity.
 */
@SuppressWarnings("unused")
public interface TranshipBoxRepository extends JpaRepository<TranshipBox,Long> {

    TranshipBox findByTranshipIdAndFrozenBoxId(Long transhipId, Long frozenBoxId);

    void deleteByFrozenBoxId(Long id);
@Query("update TranshipBox t set t.status=?3 where t.tranship.id=?1 and t.frozenBoxCode=?2")
    void updateStatusByTranshipIdAndFrozenBoxCode(Long id, String frozenBoxCode, String frozenBoxStocked);
}
