package org.fwoxford.repository;

import org.fwoxford.domain.TaskUserHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TaskUserHistory entity.
 */
@SuppressWarnings("unused")
public interface TaskUserHistoryRepository extends JpaRepository<TaskUserHistory,Long> {

    TaskUserHistory findByBusinessId(Long id);
}
