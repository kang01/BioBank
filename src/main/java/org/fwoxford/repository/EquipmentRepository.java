package org.fwoxford.repository;

import org.fwoxford.domain.Equipment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Equipment entity.
 */
@SuppressWarnings("unused")
public interface EquipmentRepository extends JpaRepository<Equipment,Long> {

    Equipment findOneByEquipmentCode(String code);
    @Query(value = "select * from equipment t where t.status != '0000' ",nativeQuery = true)
    List<Equipment> findAllEquipments();

    @Query(value = "select e.id as equipment_id,e.equipment_code,a.id as area_id,a.area_code,s.id as support_rack_id,s.support_rack_code," +
        "            (shelfType.support_rack_rows*shelfType.support_rack_columns-(select count(b.id) from frozen_box b where b.equipment_id = e.id and  b.area_id = a.id " +
        "            and  b.support_rack_id = s.id and b.status = '2004'))as count_of_rest " +

        "            from equipment e left join area a on e.id = a.equipment_id " +
        "            left join support_rack s on s.area_id = a.id " +
        "            LEFT JOIN support_rack_type shelfType ON s.support_rack_type_id = shelfType.id",nativeQuery = true)
    List<Object[]> findAllUnFullEquipment();
}
