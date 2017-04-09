package org.fwoxford.repository;

import org.fwoxford.domain.SupportRack;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SupportRack entity.
 */
@SuppressWarnings("unused")
public interface SupportRackRepository extends JpaRepository<SupportRack,Long> {
    @Query("select r from SupportRack r where r.area.equipmentCode =?1")
    List<SupportRack> findSupportRackByEquipmentCode(String equipmentCode);

    @Query("select r from SupportRack r where r.area.equipmentCode =?1 and r.area.areaCode =?2")
    List<SupportRack> findSupportRackByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);
}
