package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutFrozenTubeDTO;
import org.fwoxford.service.dto.response.StockOutFrozenTubeForPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutFrozenTube.
 */
public interface StockOutFrozenTubeService {

    /**
     * Save a stockOutFrozenTube.
     *
     * @param stockOutFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    StockOutFrozenTubeDTO save(StockOutFrozenTubeDTO stockOutFrozenTubeDTO);

    /**
     *  Get all the stockOutFrozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutFrozenTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutFrozenTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutFrozenTubeDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutFrozenTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<StockOutFrozenTubeForPlan> getStockOutFrozenTubeForPlanByApplyAndBox(Long applyId, Long frozenBoxId);

    List<StockOutFrozenTubeForPlan> getStockOutFrozenTubeForPlanByRequirementAndBox( List<Long> requirementIds, Long frozenBoxId);
}
