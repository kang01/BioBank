package org.fwoxford.service;

import org.fwoxford.domain.TranshipBox;
import org.fwoxford.service.dto.FrozenBoxCodeForTranshipDTO;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.TranshipBoxListDTO;
import org.fwoxford.service.dto.TranshipBoxListForSaveBatchDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing TranshipBox.
 */
public interface TranshipBoxService {

    /**
     * Save a transhipBox.
     *
     * @param transhipBoxDTO the entity to save
     * @return the persisted entity
     */
    TranshipBoxDTO save(TranshipBoxDTO transhipBoxDTO);

    /**
     *  Get all the transhipBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transhipBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipBoxDTO findOne(Long id);

    /**
     *  Delete the "id" transhipBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 批量保存转运与冻存盒的关系
     * @param transhipBoxDTOList
     * @return
     */
    List<TranshipBoxDTO> saveBatch(List<TranshipBoxDTO> transhipBoxDTOList);

    /**
     * 根据转运ID和冻存盒ID查询转运与冻存盒的关系
     * @param transhipId
     * @param id
     * @return
     */
    TranshipBoxDTO findByTranshipIdAndFrozenBoxId(Long transhipId, Long id);

    /**
     * 批量保存转运的冻存盒
     * @param transhipBoxListDTO
     * @return
     */
    TranshipBoxListForSaveBatchDTO saveBatchTranshipBox(TranshipBoxListDTO transhipBoxListDTO);

    /**
     * 根据冻存盒CODE查询冻存盒和冻存管信息（取转运的冻存盒以及冻存管数据）
     * @param frozenBoxCode
     * @return
     */
    FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxCode(String frozenBoxCode);

    /**
     * 根据冻存盒编码删除转运中的冻存盒以及冻存管
     * @param frozenBoxCode
     */
    void deleteTranshipBoxByFrozenBox(String frozenBoxCode);

    /**
     * 根据转运编码查询冻存盒编码List
     * @param transhipCode
     * @return
     */
    List<FrozenBoxCodeForTranshipDTO> getFrozenBoxCodeByTranshipCode(String transhipCode);

    /**
     * 分页查询转运冻存盒
     * @param transhipCode
     * @param input
     * @return
     */
    DataTablesOutput<FrozenBoxCodeForTranshipDTO> getPageFrozenBoxCodeByTranshipCode(String transhipCode, DataTablesInput input);

    /**
     * 归还冻存盒的保存
     * @param transhipBoxListDTO
     * @return
     */
    TranshipBoxListForSaveBatchDTO saveBatchTranshipBoxForReturn(TranshipBoxListDTO transhipBoxListDTO);

    /**
     * 根据出库申请编码和冻存盒编码串获取出库冻存盒和样本信息
     * @param applyCode
     * @param frozenBoxCodeStr
     * @return
     */
    List<FrozenBoxAndFrozenTubeResponse> getStockOutFrozenBoxAndSample(String applyCode, String frozenBoxCodeStr);
}
