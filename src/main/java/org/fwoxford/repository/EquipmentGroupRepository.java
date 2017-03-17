package org.fwoxford.repository;

import org.fwoxford.domain.EquipmentGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EquipmentGroup entity.
 */
@SuppressWarnings("unused")
public interface EquipmentGroupRepository extends JpaRepository<EquipmentGroup,Long> {

}
