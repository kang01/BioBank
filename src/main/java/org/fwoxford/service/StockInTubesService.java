package org.fwoxford.service;

import org.fwoxford.service.dto.StockInTubesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockInTubes.
 */
public interface StockInTubesService {

    /**
     * Save a stockInTubes.
     *
     * @param stockInTubesDTO the entity to save
     * @return the persisted entity
     */
    StockInTubesDTO save(StockInTubesDTO stockInTubesDTO);

    /**
     *  Get all the stockInTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockInTubesDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockInTubes.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockInTubesDTO findOne(Long id);

    /**
     *  Delete the "id" stockInTubes.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
