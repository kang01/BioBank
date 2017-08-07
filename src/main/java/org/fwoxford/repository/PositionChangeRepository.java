package org.fwoxford.repository;

import org.fwoxford.domain.PositionChange;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PositionChange entity.
 */
@SuppressWarnings("unused")
public interface PositionChangeRepository extends JpaRepository<PositionChange,Long> {

}
