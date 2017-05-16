package org.fwoxford.repository;

import org.fwoxford.domain.FrozenTubeType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTubeType entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeTypeRepository extends JpaRepository<FrozenTubeType,Long> {
    @Query(value = "select p.* from frozen_tube_type p  where rownum <= 1 " , nativeQuery = true)
    FrozenTubeType findTopOne();

    List<FrozenTubeType> findByStatusNot(String status);
}
