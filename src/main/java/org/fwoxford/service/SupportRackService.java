package org.fwoxford.service;

import org.fwoxford.service.dto.SupportRackDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing SupportRack.
 */
public interface SupportRackService {

    /**
     * Save a supportRack.
     *
     * @param supportRackDTO the entity to save
     * @return the persisted entity
     */
    SupportRackDTO save(SupportRackDTO supportRackDTO);

    /**
     *  Get all the supportRacks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SupportRackDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" supportRack.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SupportRackDTO findOne(Long id);

    /**
     *  Delete the "id" supportRack.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
