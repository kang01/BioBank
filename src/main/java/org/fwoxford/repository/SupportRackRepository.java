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

    @Query("select r from SupportRack r where r.area.equipmentCode =?1")
    List<SupportRack> findSupportRackByEquipmentCode(String equipmentCode);

    @Query("select r from SupportRack r where r.area.equipmentCode =?1 and r.area.areaCode =?2")
    List<SupportRack> findSupportRackByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select r from SupportRack r where r.area.equipment.id =?1")
    List<SupportRack> findByEquipmentId(Long id);

    @Query(value = "select * from support_rack where area_id = ?1 and support_rack_code = ?2 and rownum=1",nativeQuery = true)
    SupportRack findByAreaIdAndSupportRackCode(Long id, String supportRackCode);

    Long countSupportRackByAreaId(Long id);

    @Query("select r from SupportRack r where r.area.equipmentCode =?1 and r.area.areaCode =?2 and r.supportRackCode =?3")
    SupportRack findSupportRackByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    @Query(value = "select s.* from support_rack s " +
        "    left join support_rack_type st on st.id = s.support_rack_type_id " +
        "    left join area a on a.id = s.area_id " +
        "    left join equipment e on e.id = a.equipment_id " +
        "    left join (select support_rack_id,count(*)as noo from frozen_box b  where b.status in ('2004','2006') and equipment_id = ?1 group by b.support_rack_id) box\n" +
        "    on s.id = box.support_rack_id and box.noo <(cast(st.support_rack_rows as number(20))*cast(support_rack_columns as number(20)))" +
        "    where e.id = ?1",nativeQuery = true)
    List<SupportRack> findUnFullSupportRackByEquipmentId(Long id);
}
