package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBoxType;

import org.fwoxford.service.dto.FrozenBoxTypeDTO;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBoxType entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxTypeRepository extends JpaRepository<FrozenBoxType,Long> {
    @Query("select t from FrozenBoxType t where t.status != '0000'")
    List<FrozenBoxType> findAllFrozenBoxTypes();

    FrozenBoxType findByFrozenBoxTypeCode(String frozenBoxTypeCode);
}
