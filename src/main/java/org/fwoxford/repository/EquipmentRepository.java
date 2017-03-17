package org.fwoxford.repository;

import org.fwoxford.domain.Equipment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Equipment entity.
 */
@SuppressWarnings("unused")
public interface EquipmentRepository extends JpaRepository<Equipment,Long> {

}
