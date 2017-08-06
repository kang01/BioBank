package org.fwoxford.service;

import org.fwoxford.service.dto.PositionDestroyRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing PositionDestroyRecord.
 */
public interface PositionDestroyRecordService {

    /**
     * Save a positionDestroyRecord.
     *
     * @param positionDestroyRecordDTO the entity to save
     * @return the persisted entity
     */
    PositionDestroyRecordDTO save(PositionDestroyRecordDTO positionDestroyRecordDTO);

    /**
     *  Get all the positionDestroyRecords.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PositionDestroyRecordDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" positionDestroyRecord.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionDestroyRecordDTO findOne(Long id);

    /**
     *  Delete the "id" positionDestroyRecord.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
