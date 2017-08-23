package org.fwoxford.service;

import org.fwoxford.service.dto.TaskUserHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing UserLoginHistory.
 */
public interface TaskUserHistoryService {

    /**
     * Save a taskUserHistory.
     *
     * @param taskUserHistoryDTO the entity to save
     * @return the persisted entity
     */
    TaskUserHistoryDTO save(TaskUserHistoryDTO taskUserHistoryDTO);

    /**
     *  Get all the taskUserHistories.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TaskUserHistoryDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" taskUserHistory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaskUserHistoryDTO findOne(Long id);

    /**
     *  Delete the "id" taskUserHistory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
