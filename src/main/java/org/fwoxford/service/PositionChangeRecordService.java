package org.fwoxford.service;

import org.fwoxford.service.dto.PositionChangeRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing PositionChangeRecord.
 */
public interface PositionChangeRecordService {

    /**
     * Save a positionChangeRecord.
     *
     * @param positionChangeRecordDTO the entity to save
     * @return the persisted entity
     */
    PositionChangeRecordDTO save(PositionChangeRecordDTO positionChangeRecordDTO);

    /**
     *  Get all the positionChangeRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PositionChangeRecordDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" positionChangeRecord.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionChangeRecordDTO findOne(Long id);

    /**
     *  Delete the "id" positionChangeRecord.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
