package org.fwoxford.service;

import org.fwoxford.service.dto.StockOutBoxTubeDTO;
import org.fwoxford.service.dto.response.StockOutFrozenTubeDataTableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing StockOutBoxTube.
 */
public interface StockOutBoxTubeService {

    /**
     * Save a stockOutBoxTube.
     *
     * @param stockOutBoxTubeDTO the entity to save
     * @return the persisted entity
     */
    StockOutBoxTubeDTO save(StockOutBoxTubeDTO stockOutBoxTubeDTO);

    /**
     *  Get all the stockOutBoxTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutBoxTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutBoxTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutBoxTubeDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutBoxTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Page<StockOutFrozenTubeDataTableEntity> getStockOutTubeByStockOutBoxIds(List<Long> ids, Pageable pageRequest);
}
