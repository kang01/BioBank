package org.fwoxford.service;

import org.fwoxford.service.dto.StorageInDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StorageIn.
 */
public interface StorageInService {

    /**
     * Save a storageIn.
     *
     * @param storageInDTO the entity to save
     * @return the persisted entity
     */
    StorageInDTO save(StorageInDTO storageInDTO);

    /**
     *  Get all the storageIns.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StorageInDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" storageIn.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StorageInDTO findOne(Long id);

    /**
     *  Delete the "id" storageIn.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}