package org.fwoxford.service;

import org.fwoxford.service.dto.TranshipBoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing TranshipBox.
 */
public interface TranshipBoxService {

    /**
     * Save a transhipBox.
     *
     * @param transhipBoxDTO the entity to save
     * @return the persisted entity
     */
    TranshipBoxDTO save(TranshipBoxDTO transhipBoxDTO);

    /**
     *  Get all the transhipBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transhipBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipBoxDTO findOne(Long id);

    /**
     *  Delete the "id" transhipBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
