package org.fwoxford.service;

import org.fwoxford.domain.FrozenTube;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing FrozenTube.
 */
public interface FrozenTubeService {

    /**
     * Save a frozenTube.
     *
     * @param frozenTubeDTO the entity to save
     * @return the persisted entity
     */
    FrozenTubeDTO save(FrozenTubeDTO frozenTubeDTO);

    /**
     *  Get all the frozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenTubeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenTube.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenTubeDTO findOne(Long id);

    /**
     *  Delete the "id" frozenTube.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 根据冻存盒id查询冻存管信息
     * @param frozenBoxId 冻存盒id
     * @return
     */
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);
    /**
     * 根据冻存盒Code查询冻存管信息
     * @param frozenBoxCode 冻存盒Code
     * @return
     */
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);
}
