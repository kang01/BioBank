package org.fwoxford.service;

import org.fwoxford.service.dto.EquipmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Equipment.
 */
public interface EquipmentService {

    /**
     * Save a equipment.
     *
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    EquipmentDTO save(EquipmentDTO equipmentDTO);

    /**
     *  Get all the equipment.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EquipmentDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" equipment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EquipmentDTO findOne(Long id);

    /**
     *  Delete the "id" equipment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
