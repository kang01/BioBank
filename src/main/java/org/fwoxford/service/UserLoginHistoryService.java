package org.fwoxford.service;

import org.fwoxford.service.dto.UserLoginHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing UserLoginHistory.
 */
public interface UserLoginHistoryService {

    /**
     * Save a userLoginHistory.
     *
     * @param userLoginHistoryDTO the entity to save
     * @return the persisted entity
     */
    UserLoginHistoryDTO save(UserLoginHistoryDTO userLoginHistoryDTO);

    /**
     *  Get all the userLoginHistories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<UserLoginHistoryDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" userLoginHistory.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    UserLoginHistoryDTO findOne(Long id);

    /**
     *  Delete the "id" userLoginHistory.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
