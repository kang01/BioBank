package org.fwoxford.service;

import org.fwoxford.service.dto.DelegateDTO;
import org.fwoxford.service.dto.response.DelegateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Delegate.
 */
public interface DelegateService {

    /**
     * Save a delegate.
     *
     * @param delegateDTO the entity to save
     * @return the persisted entity
     */
    DelegateDTO save(DelegateDTO delegateDTO);

    /**
     *  Get all the delegates.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<DelegateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" delegate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DelegateDTO findOne(Long id);

    /**
     *  Delete the "id" delegate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<DelegateResponse> getAllDelegateList();
}
