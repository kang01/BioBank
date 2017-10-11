package org.fwoxford.service;

import net.sf.json.JSONArray;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing StockOutRequiredSample.
 */
public interface StockOutRequiredSampleService {

    /**
     * Save a stockOutRequiredSample.
     *
     * @param stockOutRequiredSampleDTO the entity to save
     * @return the persisted entity
     */
    StockOutRequiredSampleDTO save(StockOutRequiredSampleDTO stockOutRequiredSampleDTO);

    /**
     *  Get all the stockOutRequiredSamples.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockOutRequiredSampleDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockOutRequiredSample.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockOutRequiredSampleDTO findOne(Long id);

    /**
     *  Delete the "id" stockOutRequiredSample.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    DataTablesOutput<StockOutRequiredSampleDTO> getPageStockOutRequiredSampleByRequired(DataTablesInput input, Long id);

    Page<StockOutRequiredSampleDTO> getAllStockOutRequiredSamplesByRequirementId(Pageable pageable, Long id);

    JSONArray getRequiredSamples(Long id);
}
