package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutApplyProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutApplyProject.
 */
public interface StockOutApplyProjectService {

    /**
     * Save a stockOutApplyProject.
     *
     * @param stockOutApplyProjectDTO the entity to save
     * @return the persisted entity
     */
    StockOutApplyProjectDTO save(StockOutApplyProjectDTO stockOutApplyProjectDTO);

    /**
     *  Get all the stockOutApplyProjects.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutApplyProjectDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutApplyProject.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutApplyProjectDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutApplyProject.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
