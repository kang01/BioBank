package org.fwoxford.service;

import org.fwoxford.service.dto.response.StockInBoxForDataTableEntity;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.FrozenBoxPositionDTO;
import org.fwoxford.service.dto.StockInBoxDTO;
import org.fwoxford.service.dto.response.StockInBoxDetail;
import org.fwoxford.service.dto.response.StockInBoxForDataTable;
import org.fwoxford.service.dto.response.StockInBoxForSplit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Service Interface for managing StockInBox.
 */
public interface StockInBoxService {

    /**
     * Save a stockInBox.
     *
     * @param stockInBoxDTO the entity to save
     * @return the persisted entity
     */
    StockInBoxDTO save(StockInBoxDTO stockInBoxDTO);

    /**
     *  Get all the stockInBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StockInBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" stockInBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    StockInBoxDTO findOne(Long id);

    /**
     *  Delete the "id" stockInBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<StockInBoxDTO> saveBatch(List<StockInBoxDTO> stockInBoxDTOS);


    StockInBoxDetail getStockInBoxDetail(String stockInCode, String boxCode);

    List<StockInBoxForSplit> splitedStockIn(String stockInCode, String boxCode, List<StockInBoxForSplit> stockInBoxForDataSplits);

    StockInBoxDetail movedStockIn(String stockInCode, String boxCode, FrozenBoxPositionDTO boxPositionDTO);

    List<StockInBoxForDataTable> findFrozenBoxListByBoxCodeStr(List<String> frozenBoxCodeStr);

    StockInBoxDetail movedDownStockIn(String stockInCode, String boxCode);

    DataTablesOutput<StockInBoxForDataTableEntity> getPageStockInBoxes(String stockInCode, DataTablesInput input);
    /**
     * 创建入库盒
     * @param frozenBoxDTO
     * @param stockInCode
     * @return
     */
    FrozenBoxDTO createBoxByStockIn(FrozenBoxDTO frozenBoxDTO, String stockInCode);

    /**
     * 根据冻存盒编码查询入库冻存盒
     * @param frozenBoxCode
     * @return
     */
    FrozenBoxDTO getFrozenBoxAndTubeByForzenBoxCode(String frozenBoxCode);

    /**
     * 根据入库冻存盒ID查询冻存盒和冻存管的信息
     * @param id
     * @return
     */
    StockInBoxDTO getBoxAndStockTubeByStockInBoxId(Long id);
}
