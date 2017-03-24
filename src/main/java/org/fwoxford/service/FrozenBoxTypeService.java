package org.fwoxford.service;

import org.fwoxford.service.dto.FrozenBoxTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing FrozenBoxType.
 */
public interface FrozenBoxTypeService {

    /**
     * Save a frozenBoxType.
     *
     * @param frozenBoxTypeDTO the entity to save
     * @return the persisted entity
     */
    FrozenBoxTypeDTO save(FrozenBoxTypeDTO frozenBoxTypeDTO);

    /**
     *  Get all the frozenBoxTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenBoxTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenBoxType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenBoxTypeDTO findOne(Long id);

    /**
     *  Delete the "id" frozenBoxType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 查詢所有的有效凍存盒類型
     * @return
     */
    List<FrozenBoxTypeDTO> findAllFrozenBoxTypes();
}
