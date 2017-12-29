package org.fwoxford.service;

import org.fwoxford.service.dto.EquipmentModleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing EquipmentModle.
 */
public interface EquipmentModleService {

    /**
     * Save a equipmentModle.
     *
     * @param equipmentModleDTO the entity to save
     * @return the persisted entity
     */
    EquipmentModleDTO save(EquipmentModleDTO equipmentModleDTO);

    /**
     *  Get all the equipmentModles.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EquipmentModleDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" equipmentModle.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EquipmentModleDTO findOne(Long id);

    /**
     *  Delete the "id" equipmentModle.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 获取所有的冻存架类型
     * @return
     */
    List<EquipmentModleDTO> findAllEquipmentType();
}
