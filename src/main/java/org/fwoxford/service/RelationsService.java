package org.fwoxford.service;

import org.fwoxford.service.dto.RelationsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Relations.
 */
public interface RelationsService {

    /**
     * Save a relations.
     *
     * @param relationsDTO the entity to save
     * @return the persisted entity
     */
    RelationsDTO save(RelationsDTO relationsDTO);

    /**
     *  Get all the relations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<RelationsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" relations.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RelationsDTO findOne(Long id);

    /**
     *  Delete the "id" relations.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
