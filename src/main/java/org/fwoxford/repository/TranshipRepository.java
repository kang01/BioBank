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

    @Query(value = "select t.sample_type_id as sampleTypeId,t.sample_classification_id as sampleClassificationId," +
        " t.sample_type_name  as sampleTypeName," +
        " t.sample_classification_name as sampleClassificationName," +
        "count(t.id) as countOfTube from tranship_tube t " +
        " left join tranship_box b on t.tranship_box_id = b.id " +
        " where b.tranship_id = ?1 and t.status!='0000'" +
        " group by t.sample_type_id,t.sample_classification_id,t.sample_type_name,t.sample_classification_name",nativeQuery = true)
    List<Object[]> countFrozenTubeGroupBySampleTypeAndClass(Long id);
}
