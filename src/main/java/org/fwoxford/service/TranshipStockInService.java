package org.fwoxford.service;

import org.fwoxford.service.dto.TranshipStockInDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing TranshipStockIn.
 */
public interface TranshipStockInService {

    /**
     * Save a transhipStockIn.
     *
     * @param transhipStockInDTO the entity to save
     * @return the persisted entity
     */
    TranshipStockInDTO save(TranshipStockInDTO transhipStockInDTO);

    /**
     *  Get all the transhipStockIns.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipStockInDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transhipStockIn.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipStockInDTO findOne(Long id);

    /**
     *  Delete the "id" transhipStockIn.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
