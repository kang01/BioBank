package org.fwoxford.service;

import org.fwoxford.service.dto.StorageInBoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StorageInBox.
 */
public interface StorageInBoxService {

    /**
     * Save a storageInBox.
     *
     * @param storageInBoxDTO the entity to save
     * @return the persisted entity
     */
    StorageInBoxDTO save(StorageInBoxDTO storageInBoxDTO);

    /**
     *  Get all the storageInBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StorageInBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" storageInBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StorageInBoxDTO findOne(Long id);

    /**
     *  Delete the "id" storageInBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<StorageInBoxDTO> saveBatch(List<StorageInBoxDTO> storageInBoxDTOS);
}
