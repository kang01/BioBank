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
}
