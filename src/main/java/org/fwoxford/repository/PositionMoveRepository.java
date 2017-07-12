package org.fwoxford.repository;

import org.fwoxford.domain.PositionMove;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PositionMove entity.
 */
@SuppressWarnings("unused")
public interface PositionMoveRepository extends JpaRepository<PositionMove,Long> {

}
