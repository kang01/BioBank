package org.fwoxford.service;

import org.fwoxford.service.dto.FrozenBoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing FrozenBox.
 */
public interface FrozenBoxService {

    /**
     * Save a frozenBox.
     *
     * @param frozenBoxDTO the entity to save
     * @return the persisted entity
     */
    FrozenBoxDTO save(FrozenBoxDTO frozenBoxDTO);

    /**
     *  Get all the frozenBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenBoxDTO findOne(Long id);

    /**
     *  Delete the "id" frozenBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
