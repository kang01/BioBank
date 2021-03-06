package org.fwoxford.service;

import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.domain.TranshipTube;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.TranshipTubeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing TranshipTube.
 */
public interface TranshipTubeService {

    /**
     * Save a transhipTube.
     *
     * @param transhipTubeDTO the entity to save
     * @return the persisted entity
     */
    TranshipTubeDTO save(TranshipTubeDTO transhipTubeDTO);

    /**
     *  Get all the transhipTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transhipTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipTubeDTO findOne(Long id);

    /**
     *  Delete the "id" transhipTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<TranshipTube> saveTranshipTube(TranshipBox transhipBox, List<FrozenTube> frozenTubeList);

    /**
     * 转运冻存管/归还冻存管销毁
     * @param transhipTubeDTO
     * @param boxId
     */
    TranshipBoxDTO destroyTranshipTube(TranshipTubeDTO transhipTubeDTO, Long boxId);
}
