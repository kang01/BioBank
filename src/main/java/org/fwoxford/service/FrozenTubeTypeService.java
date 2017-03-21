package org.fwoxford.service;

import org.fwoxford.service.dto.FrozenTubeTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing FrozenTubeType.
 */
public interface FrozenTubeTypeService {

    /**
     * Save a frozenTubeType.
     *
     * @param frozenTubeTypeDTO the entity to save
     * @return the persisted entity
     */
    FrozenTubeTypeDTO save(FrozenTubeTypeDTO frozenTubeTypeDTO);

    /**
     *  Get all the frozenTubeTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenTubeTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenTubeType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenTubeTypeDTO findOne(Long id);

    /**
     *  Delete the "id" frozenTubeType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
