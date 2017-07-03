package org.fwoxford.repository;

import org.fwoxford.domain.EquipmentModle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EquipmentModle entity.
 */
@SuppressWarnings("unused")
public interface EquipmentModleRepository extends JpaRepository<EquipmentModle,Long> {

    EquipmentModle findByEquipmentModelCode(String equipmentModelCode);
}
