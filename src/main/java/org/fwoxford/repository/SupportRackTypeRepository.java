package org.fwoxford.repository;

import org.fwoxford.domain.SupportRack;
import org.fwoxford.domain.SupportRackType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SupportRackType entity.
 */
@SuppressWarnings("unused")
public interface SupportRackTypeRepository extends JpaRepository<SupportRackType,Long> {

}
