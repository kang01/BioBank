package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBoxType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBoxType entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxTypeRepository extends JpaRepository<FrozenBoxType,Long> {

}
