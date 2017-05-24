package org.fwoxford.service.impl;

import org.fwoxford.domain.StockOutBoxPosition;
import org.fwoxford.repository.StockOutFrozenTubeRepository;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.repository.StockOutFrozenBoxRepository;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import org.fwoxford.service.dto.response.StockOutFrozenBoxForTaskDataTableEntity;
import org.fwoxford.service.mapper.StockOutFrozenBoxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutFrozenBox.
 */
@Service
@Transactional
public class StockOutFrozenBoxServiceImpl implements StockOutFrozenBoxService{

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenBoxServiceImpl.class);

    private final StockOutFrozenBoxRepository stockOutFrozenBoxRepository;
    private final StockOutFrozenTubeRepository stockOutFrozenTubeRepository;

    private final StockOutFrozenBoxMapper stockOutFrozenBoxMapper;

    public StockOutFrozenBoxServiceImpl(StockOutFrozenBoxRepository stockOutFrozenBoxRepository
            , StockOutFrozenBoxMapper stockOutFrozenBoxMapper
            , StockOutFrozenTubeRepository stockOutFrozenTubeRepository) {
        this.stockOutFrozenBoxRepository = stockOutFrozenBoxRepository;
        this.stockOutFrozenBoxMapper = stockOutFrozenBoxMapper;
        this.stockOutFrozenTubeRepository = stockOutFrozenTubeRepository;
    }

    /**
     * Save a stockOutFrozenBox.
     *
     * @param stockOutFrozenBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutFrozenBoxDTO save(StockOutFrozenBoxDTO stockOutFrozenBoxDTO) {
        log.debug("Request to save StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxMapper.stockOutFrozenBoxDTOToStockOutFrozenBox(stockOutFrozenBoxDTO);
        stockOutFrozenBox = stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        StockOutFrozenBoxDTO result = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);
        return result;
    }

    /**
     *  Get all the stockOutFrozenBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutFrozenBoxes");
        Page<StockOutFrozenBox> result = stockOutFrozenBoxRepository.findAll(pageable);
        return result.map(stockOutFrozenBox -> stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox));
    }

    /**
     *  Get one stockOutFrozenBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutFrozenBoxDTO findOne(Long id) {
        log.debug("Request to get StockOutFrozenBox : {}", id);
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(id);
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);
        return stockOutFrozenBoxDTO;
    }

    /**
     *  Delete the  stockOutFrozenBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutFrozenBox : {}", id);
        stockOutFrozenBoxRepository.delete(id);
    }


    private String toPositionString(StockOutBoxPosition pos){

        ArrayList<String> positions = new ArrayList<>();
        if (pos.getEquipmentCode() != null || pos.getEquipmentCode().length() > 0){
            positions.add(pos.getEquipmentCode());
        }

        if (pos.getAreaCode() != null && pos.getAreaCode().length() > 0) {
            positions.add(pos.getAreaCode());
        }

        if (pos.getSupportRackCode() != null && pos.getSupportRackCode().length() > 0){
            positions.add(pos.getSupportRackCode());
        }

        if (pos.getRowsInShelf() != null && pos.getRowsInShelf().length() > 0 && pos.getColumnsInShelf() != null && pos.getColumnsInShelf().length() > 0){
            positions.add(pos.getColumnsInShelf()+pos.getRowsInShelf());
        }

        return String.join(".", positions);
    }

    /**
     *  获取指定任务的指定分页的出库盒子.
     *  @param taskId The task id
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenBoxForTaskDataTableEntity> findAllByTask(Long taskId, Pageable pageable) {
        log.debug("Request to get all StockOutFrozenBoxes");
        Page<StockOutFrozenBox> result = stockOutFrozenBoxRepository.findAllByTask(taskId, pageable);
        return result.map(stockOutFrozenBox -> {
            StockOutFrozenBoxForTaskDataTableEntity dto = new StockOutFrozenBoxForTaskDataTableEntity();
            dto.setId(stockOutFrozenBox.getId());
            dto.setFrozenBoxCode(stockOutFrozenBox.getFrozenBox().getFrozenBoxCode());
            dto.setSampleTypeName(stockOutFrozenBox.getFrozenBox().getSampleTypeName());

            String position = toPositionString(stockOutFrozenBox.getStockOutBoxPosition());
            dto.setPosition(position);

            Long count = stockOutFrozenTubeRepository.countByFrozenBox(stockOutFrozenBox.getId());

            dto.setCountOfSample(count);

            return dto;
        });
    }
}
