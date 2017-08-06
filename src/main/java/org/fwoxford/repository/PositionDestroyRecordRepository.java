package org.fwoxford.repository;

import org.fwoxford.domain.PositionDestroyRecord;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PositionDestroyRecord entity.
 */
@SuppressWarnings("unused")
public interface PositionDestroyRecordRepository extends JpaRepository<PositionDestroyRecord,Long> {

}
