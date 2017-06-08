package org.fwoxford.service;

import org.fwoxford.service.dto.StockInTubeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockInTube.
 */
public interface StockInTubeService {

    /**
     * Save a stockInTube.
     *
     * @param stockInTubeDTO the entity to save
     * @return the persisted entity
     */
    StockInTubeDTO save(StockInTubeDTO stockInTubeDTO);

    /**
     *  Get all the stockInTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockInTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockInTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockInTubeDTO findOne(Long id);

    /**
     *  Delete the "id" stockInTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
