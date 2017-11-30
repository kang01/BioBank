package org.fwoxford.service;

import org.fwoxford.service.dto.CheckTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing CheckType.
 */
public interface CheckTypeService {

    /**
     * Save a checkType.
     *
     * @param checkTypeDTO the entity to save
     * @return the persisted entity
     */
    CheckTypeDTO save(CheckTypeDTO checkTypeDTO);

    /**
     *  Get all the checkTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CheckTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" checkType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CheckTypeDTO findOne(Long id);

    /**
     *  Delete the "id" checkType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<CheckTypeDTO> findAllCheckTypeList();
}
