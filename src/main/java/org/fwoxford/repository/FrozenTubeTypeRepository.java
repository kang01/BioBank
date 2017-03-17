package org.fwoxford.repository;

import org.fwoxford.domain.FrozenTubeType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTubeType entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeTypeRepository extends JpaRepository<FrozenTubeType,Long> {

}
