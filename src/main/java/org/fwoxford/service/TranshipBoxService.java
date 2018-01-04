package org.fwoxford.service;

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
     * 根据冻存盒编码串获取出库冻存盒和样本信息
     * @param projectCode
     * @param frozenBoxCodeStr
     * @return
     */
    List<TranshipBoxDTO> getStockOutFrozenBoxAndSample(String projectCode, String frozenBoxCodeStr,String boxType);

    /**
     * 归还冻存盒的保存
     * @param id
     * @param transhipBoxDTOS
     * @return
     */
    List<TranshipBoxDTO> saveBatchTranshipBoxForReturn(Long id, List<TranshipBoxDTO> transhipBoxDTOS);
    /**
     * 根据冻存盒获取归还冻存盒和归还样本的信息
     * @param id
     * @return
     */
    TranshipBoxDTO findTranshipBoxAndSampleByTranshipBoxId(Long id);

    /**
     * 删除归还冻存盒
     * @param id
     */
    void deleteReturnBackBox(Long id);

    /**
     * 根据转运单编码和冻存盒编码查询转运单的详情
     * @param transhipCode
     * @param frozenBoxCode
     * @return
     */
    FrozenBoxAndFrozenTubeResponse findTranshipBoxAndSampleByTranshipCodeAndFrozenBoxCode(String transhipCode, String frozenBoxCode);

    /**
     * 根据冻存盒编码查询冻存盒信息（只能是新增）---新增归还冻存盒的查询
     * @param frozenBoxCode
     * @return
     */
    TranshipBoxDTO findForzenBoxForReturnBack( String frozenBoxCode);

    /**
     * 保存归还冻存盒---保存原始交接过和新增的冻存盒
     * @param id
     * @param transhipBoxDTOS
     * @return
     */

    List<TranshipBoxDTO> saveBatchOriginalBoxesForReturnBack(Long id, List<TranshipBoxDTO> transhipBoxDTOS);

    /**
     * 仅取DNA类型的冻存盒和样本数据
     * @param projectCode
     * @param boxCodeStr
     * @return
     */
    List<TranshipBoxDTO> findStockOutFrozenBoxAndSampleForDNA(String projectCode, String boxCodeStr);
}
