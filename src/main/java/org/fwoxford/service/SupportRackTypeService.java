package org.fwoxford.service;

import org.fwoxford.service.dto.SupportRackTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing SupportRackType.
 */
public interface SupportRackTypeService {

    /**
     * Save a supportRackType.
     *
     * @param supportRackTypeDTO the entity to save
     * @return the persisted entity
     */
    SupportRackTypeDTO save(SupportRackTypeDTO supportRackTypeDTO);

    /**
     *  Get all the supportRackTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SupportRackTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" supportRackType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SupportRackTypeDTO findOne(Long id);

    /**
     *  Delete the "id" supportRackType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
