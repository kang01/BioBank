package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutTaskFrozenTubeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutTaskFrozenTube.
 */
public interface StockOutTaskFrozenTubeService {

    /**
     * Save a stockOutTaskFrozenTube.
     *
     * @param stockOutTaskFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    StockOutTaskFrozenTubeDTO save(StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO);

    /**
     *  Get all the stockOutTaskFrozenTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutTaskFrozenTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutTaskFrozenTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutTaskFrozenTubeDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutTaskFrozenTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
