package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutPlanFrozenTubeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutPlanFrozenTube.
 */
public interface StockOutPlanFrozenTubeService {

    /**
     * Save a stockOutPlanFrozenTube.
     *
     * @param stockOutPlanFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    StockOutPlanFrozenTubeDTO save(StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO);

    /**
     *  Get all the stockOutPlanFrozenTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutPlanFrozenTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutPlanFrozenTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutPlanFrozenTubeDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutPlanFrozenTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
