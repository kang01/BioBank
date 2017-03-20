package org.fwoxford.repository;

import org.fwoxford.domain.FrozenTubeRecord;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTubeRecord entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeRecordRepository extends JpaRepository<FrozenTubeRecord,Long> {

}
