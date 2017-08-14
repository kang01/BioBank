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

    @Modifying
    @Query("update Tranship t set t.transhipState=?2 where t.id=?1")
    void updateTranshipStateById(Long id,String status);

    @Query(value = "select count(*) from tranship s where s.tranship_state !='1090' and s.track_number=?1",nativeQuery = true)
    Long countByTrackNumber(String trackNumber);

    Tranship findByTrackNumber(String trackNumber);
}
