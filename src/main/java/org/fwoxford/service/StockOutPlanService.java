package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutPlanDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutPlan.
 */
public interface StockOutPlanService {

    /**
     * Save a stockOutPlan.
     *
     * @param stockOutPlanDTO the entity to save
     * @return the persisted entity
     */
    StockOutPlanDTO save(StockOutPlanDTO stockOutPlanDTO);

    /**
     *  Get all the stockOutPlans.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutPlanDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutPlan.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutPlanDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutPlan.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
