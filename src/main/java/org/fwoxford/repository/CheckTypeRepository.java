package org.fwoxford.repository;

import org.fwoxford.domain.CheckType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CheckType entity.
 */
@SuppressWarnings("unused")
public interface CheckTypeRepository extends JpaRepository<CheckType,Long> {

    CheckType findByIdAndStatus(Long id,String status);

    List<CheckType> findByStatus(String status);
}
