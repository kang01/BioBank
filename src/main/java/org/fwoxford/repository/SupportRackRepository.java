package org.fwoxford.repository;

import org.fwoxford.domain.SupportRack;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SupportRack entity.
 */
@SuppressWarnings("unused")
public interface SupportRackRepository extends JpaRepository<SupportRack,Long> {

    List<SupportRack> findSupportRackByAreaId(Long areaId);
}
