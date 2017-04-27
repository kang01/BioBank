package org.fwoxford.service;

import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.Tranship;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing Tranship.
 */
public interface TranshipService {

    /**
     * Save a tranship.
     *
     * @param transhipDTO the entity to save
     * @return the persisted entity
     */
    TranshipDTO save(TranshipDTO transhipDTO);

    /**
     *  Get all the tranships.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tranship.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipDTO findOne(Long id);

    /**
     *  Delete the "id" tranship.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 查询转运记录列表
     * @param input
     * @return
     */
    DataTablesOutput<Tranship> findAllTranship(DataTablesInput input);

    /**
     * 根据转运记录ID查询转运记录以及冻存盒信息
     * @param id
     * @return
     */
    TranshipByIdResponse findTranshipAndFrozenBox(Long id);

    /**
     * 新增保存转运记录
     * @param transhipDTO
     * @return
     */
    TranshipDTO insertTranship(TranshipDTO transhipDTO);

    /**
     * 初始化转运记录
     * @return
     */
    TranshipDTO initTranship();

    List<FrozenTubeDTO> getFrozenTubeDTOList(List<FrozenBoxDTO> frozenBoxDTOList, List<FrozenBox> frozenBoxes);

    List<FrozenBoxDTO> getFrozenBoxDtoList(List<FrozenBoxDTO> frozenBoxDTOListLast, List<FrozenTubeDTO> frozenTubeDTOS);

    TranshipDTO invalidTranship(String transhipCode);
}
