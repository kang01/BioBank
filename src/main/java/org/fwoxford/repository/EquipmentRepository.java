package org.fwoxford.repository;

import org.fwoxford.domain.Equipment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Equipment entity.
 */
@SuppressWarnings("unused")
public interface EquipmentRepository extends JpaRepository<Equipment,Long> {

    Equipment findOneByEquipmentCode(String code);

    @Query("select t from Equipment t where t.status != '0000'")
    List<Equipment> findAllEquipments();
}
