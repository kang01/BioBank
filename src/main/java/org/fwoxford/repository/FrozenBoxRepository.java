package org.fwoxford.repository;

import org.fwoxford.domain.FrozenBox;

import org.fwoxford.service.dto.FrozenBoxDTO;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {

    List<FrozenBoxDTO> findAllFrozenBoxByTranshipId(Long transhipId);
}
