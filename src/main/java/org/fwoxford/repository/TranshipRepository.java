package org.fwoxford.repository;

import org.fwoxford.domain.Tranship;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tranship entity.
 */
@SuppressWarnings("unused")
public interface TranshipRepository extends JpaRepository<Tranship,Long> {

    Tranship findByTranshipCode(String transhipCode);

    @Query("update Tranship t set t.status=?2 where t.id=?1")
    void updateTranshipStateById(Long id,String status);
}
