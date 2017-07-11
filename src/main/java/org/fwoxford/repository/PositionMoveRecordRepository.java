package org.fwoxford.repository;

import org.fwoxford.domain.PositionMoveRecord;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PositionMoveRecord entity.
 */
@SuppressWarnings("unused")
public interface PositionMoveRecordRepository extends JpaRepository<PositionMoveRecord,Long> {

}
