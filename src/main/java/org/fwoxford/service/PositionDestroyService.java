package org.fwoxford.service;

import org.fwoxford.service.dto.PositionDestroyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing PositionDestroy.
 */
public interface PositionDestroyService {

    /**
     * Save a positionDestroy.
     *
     * @param positionDestroyDTO the entity to save
     * @return the persisted entity
     */
    PositionDestroyDTO save(PositionDestroyDTO positionDestroyDTO);

    /**
     *  Get all the positionDestroys.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PositionDestroyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" positionDestroy.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionDestroyDTO findOne(Long id);

    /**
     *  Delete the "id" positionDestroy.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 销毁
     * @param positionDestroyDTO
     * @param type
     * @return
     */
    PositionDestroyDTO createDestroyPosition(PositionDestroyDTO positionDestroyDTO, String type);
}
