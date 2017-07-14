package org.fwoxford.repository;

import org.fwoxford.domain.Area;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Area entity.
 */
@SuppressWarnings("unused")
public interface AreaRepository extends JpaRepository<Area,Long> {

    Area findOneByAreaCodeAndEquipmentId(String areaCode, Long equipmentId);

    List<Area> getAreaByEquipmentId(Long equipmentId);
}
