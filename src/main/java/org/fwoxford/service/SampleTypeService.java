package org.fwoxford.service;

import org.fwoxford.service.dto.SampleTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing SampleType.
 */
public interface SampleTypeService {

    /**
     * Save a sampleType.
     *
     * @param sampleTypeDTO the entity to save
     * @return the persisted entity
     */
    SampleTypeDTO save(SampleTypeDTO sampleTypeDTO);

    /**
     *  Get all the sampleTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SampleTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" sampleType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SampleTypeDTO findOne(Long id);

    /**
     *  Delete the "id" sampleType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
