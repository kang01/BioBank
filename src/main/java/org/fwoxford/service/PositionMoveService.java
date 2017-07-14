package org.fwoxford.service;

import org.fwoxford.service.dto.PositionMoveDTO;
import org.fwoxford.service.dto.response.PositionMoveBoxDTO;
import org.fwoxford.service.dto.response.PositionMoveSampleDTO;
import org.fwoxford.service.dto.response.PositionMoveShelvesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing PositionMove.
 */
public interface PositionMoveService {

    /**
     * Save a positionMove.
     *
     * @param positionMoveDTO the entity to save
     * @return the persisted entity
     */
    PositionMoveDTO save(PositionMoveDTO positionMoveDTO);

    /**
     *  Get all the positionMoves.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PositionMoveDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" positionMove.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionMoveDTO findOne(Long id);

    /**
     *  Delete the "id" positionMove.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 样本移位
     * @param positionMoveDTO
     * @return
     */
    PositionMoveSampleDTO moveSamplePosition(PositionMoveSampleDTO positionMoveDTO);

    PositionMoveBoxDTO savePositionMoveForBox(PositionMoveBoxDTO positionMoveDTO);

    PositionMoveShelvesDTO savePositionMoveForShelf(PositionMoveShelvesDTO positionMoveDTO);

    PositionMoveDTO creataSamplePosition(PositionMoveDTO positionMoveDTO);
}
