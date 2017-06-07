package org.fwoxford.repository;

import org.fwoxford.domain.UserLoginHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserLoginHistory entity.
 */
@SuppressWarnings("unused")
public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory,Long> {

    UserLoginHistory findByBusinessId(Long id);
}
