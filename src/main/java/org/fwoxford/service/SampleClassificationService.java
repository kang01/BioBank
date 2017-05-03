package org.fwoxford.service;

import org.fwoxford.service.dto.SampleClassificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing SampleClassification.
 */
public interface SampleClassificationService {

    /**
     * Save a sampleClassification.
     *
     * @param sampleClassificationDTO the entity to save
     * @return the persisted entity
     */
    SampleClassificationDTO save(SampleClassificationDTO sampleClassificationDTO);

    /**
     *  Get all the sampleClassifications.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SampleClassificationDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" sampleClassification.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SampleClassificationDTO findOne(Long id);

    /**
     *  Delete the "id" sampleClassification.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
