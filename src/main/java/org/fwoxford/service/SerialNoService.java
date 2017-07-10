package org.fwoxford.service;

import org.fwoxford.service.dto.SerialNoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing SerialNo.
 */
public interface SerialNoService {

    /**
     * Save a serialNo.
     *
     * @param serialNoDTO the entity to save
     * @return the persisted entity
     */
    SerialNoDTO save(SerialNoDTO serialNoDTO);

    /**
     *  Get all the serialNos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SerialNoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" serialNo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SerialNoDTO findOne(Long id);

    /**
     *  Delete the "id" serialNo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
