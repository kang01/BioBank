package org.fwoxford.repository;

import org.fwoxford.domain.SerialNo;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the SerialNo entity.
 */
@SuppressWarnings("unused")
public interface SerialNoRepository extends JpaRepository<SerialNo,Long> {

    SerialNo findTop1ByMachineNo(String flag);

    SerialNo findTop1ByMachineNoAndUsedDate(String flag, LocalDate localDate);

    @Query(value = "select a.* from serial_no a where a.machine_no = ?1 and a.used_date =?2" +
        " and a.status != '0000' " +
        " order by CREATED_DATE desc " +
        " FETCH FIRST 1 ROWS ONLY  " , nativeQuery = true)
    SerialNo findlimitOneByMachineNoAndUsedDate(String flag, LocalDate localDate);
}
