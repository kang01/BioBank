package org.fwoxford.service;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing FrozenBox.
 */
public interface FrozenBoxService {

    /**
     * Save a frozenBox.
     *
     * @param frozenBoxDTO the entity to save
     * @return the persisted entity
     */
    FrozenBoxDTO save(FrozenBoxDTO frozenBoxDTO);

    /**
     *  Get all the frozenBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<FrozenBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" frozenBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FrozenBoxDTO findOne(Long id);

    /**
     *  Delete the "id" frozenBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 根据转运记录ID 查询冻存盒列表
     * @param transhipId 转运记录ID
     * @return
     */
    List<FrozenBoxDTO> findAllFrozenBoxByTranshipId(Long transhipId);

    /**
     * 根据冻存盒ID查询冻存管信息
     * @param id 冻存盒ID
     * @return
     */
    FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxId(Long id);

    /**
     * 根据冻存盒CODE查询冻存管信息
     * @param frozenBoxCode 冻存盒CODE
     * @return
     */
    FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxCode(String frozenBoxCode);

    /**
     * 根据冻存盒code查询冻存盒基本信息
     * @param frozenBoxCode
     * @return
     */
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);
}
