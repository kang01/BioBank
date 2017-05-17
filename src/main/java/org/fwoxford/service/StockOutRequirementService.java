package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutRequirementDTO;
import org.fwoxford.service.dto.response.StockOutRequirementForSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing StockOutRequirement.
 */
public interface StockOutRequirementService {

    /**
     * Save a stockOutRequirement.
     *
     * @param stockOutRequirementDTO the entity to save
     * @return the persisted entity
     */
    StockOutRequirementDTO save(StockOutRequirementDTO stockOutRequirementDTO);

    /**
     *  Get all the stockOutRequirements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutRequirementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutRequirement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutRequirementDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutRequirement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    StockOutRequirementForSave saveStockOutRequirement(StockOutRequirementForSave stockOutRequirement, Long stockOutApplyId);
}
