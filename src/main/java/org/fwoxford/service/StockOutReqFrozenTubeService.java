package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutReqFrozenTube.
 */
public interface StockOutReqFrozenTubeService {

    /**
     * Save a stockOutReqFrozenTube.
     *
     * @param stockOutReqFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    StockOutReqFrozenTubeDTO save(StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO);

    /**
     *  Get all the stockOutReqFrozenTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutReqFrozenTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutReqFrozenTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutReqFrozenTubeDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutReqFrozenTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
