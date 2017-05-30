package org.fwoxford.service;

import org.fwoxford.service.dto.BoxAndTubeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing BoxAndTube.
 */
public interface BoxAndTubeService {

    /**
     * Save a boxAndTube.
     *
     * @param boxAndTubeDTO the entity to save
     * @return the persisted entity
     */
    BoxAndTubeDTO save(BoxAndTubeDTO boxAndTubeDTO);

    /**
     *  Get all the boxAndTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BoxAndTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" boxAndTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BoxAndTubeDTO findOne(Long id);

    /**
     *  Delete the "id" boxAndTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
