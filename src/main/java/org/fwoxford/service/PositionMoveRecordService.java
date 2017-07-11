package org.fwoxford.service;

import org.fwoxford.service.dto.PositionMoveRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing PositionMoveRecord.
 */
public interface PositionMoveRecordService {

    /**
     * Save a positionMoveRecord.
     *
     * @param positionMoveRecordDTO the entity to save
     * @return the persisted entity
     */
    PositionMoveRecordDTO save(PositionMoveRecordDTO positionMoveRecordDTO);

    /**
     *  Get all the positionMoveRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PositionMoveRecordDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" positionMoveRecord.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionMoveRecordDTO findOne(Long id);

    /**
     *  Delete the "id" positionMoveRecord.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
