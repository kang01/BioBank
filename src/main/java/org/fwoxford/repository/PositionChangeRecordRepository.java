package org.fwoxford.repository;

import org.fwoxford.domain.PositionChangeRecord;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PositionChangeRecord entity.
 */
@SuppressWarnings("unused")
public interface PositionChangeRecordRepository extends JpaRepository<PositionChangeRecord,Long> {

}
