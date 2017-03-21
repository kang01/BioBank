package org.fwoxford.service;

import org.fwoxford.service.dto.FrozenTubeRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing FrozenTubeRecord.
 */
public interface FrozenTubeRecordService {

    /**
     * Save a frozenTubeRecord.
     *
     * @param frozenTubeRecordDTO the entity to save
     * @return the persisted entity
     */
    FrozenTubeRecordDTO save(FrozenTubeRecordDTO frozenTubeRecordDTO);

    /**
     *  Get all the frozenTubeRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenTubeRecordDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenTubeRecord.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenTubeRecordDTO findOne(Long id);

    /**
     *  Delete the "id" frozenTubeRecord.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
