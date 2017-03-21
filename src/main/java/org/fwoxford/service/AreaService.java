package org.fwoxford.service;

import org.fwoxford.service.dto.AreaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Area.
 */
public interface AreaService {

    /**
     * Save a area.
     *
     * @param areaDTO the entity to save
     * @return the persisted entity
     */
    AreaDTO save(AreaDTO areaDTO);

    /**
     *  Get all the areas.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AreaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" area.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AreaDTO findOne(Long id);

    /**
     *  Delete the "id" area.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
