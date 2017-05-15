package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutTaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutTask.
 */
public interface StockOutTaskService {

    /**
     * Save a stockOutTask.
     *
     * @param stockOutTaskDTO the entity to save
     * @return the persisted entity
     */
    StockOutTaskDTO save(StockOutTaskDTO stockOutTaskDTO);

    /**
     *  Get all the stockOutTasks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutTaskDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutTask.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutTaskDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutTask.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
