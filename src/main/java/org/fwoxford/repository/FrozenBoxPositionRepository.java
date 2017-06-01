package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBoxPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBoxPosition entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxPositionRepository extends JpaRepository<FrozenBoxPosition,Long> {

    FrozenBoxPosition findOneByFrozenBoxIdAndStatus(Long id, String status);

    List<FrozenBoxPosition> findByFrozenBoxIdAndStatus(Long id, String status);
}
