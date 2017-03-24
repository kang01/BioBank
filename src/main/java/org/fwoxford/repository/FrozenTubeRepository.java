package org.fwoxford.repository;

import org.fwoxford.domain.FrozenTube;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTube entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeRepository extends JpaRepository<FrozenTube,Long> {

    @Query("select t from FrozenTube t where t.frozenBox.id = ?1")
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBoxCode = ?1 and status!='00'")
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);
}
