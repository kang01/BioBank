package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutPlanFrozenTubeService;
import org.fwoxford.domain.StockOutPlanFrozenTube;
import org.fwoxford.repository.StockOutPlanFrozenTubeRepository;
import org.fwoxford.service.dto.StockOutPlanFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutPlanFrozenTubeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutPlanFrozenTube.
 */
@Service
@Transactional
public class StockOutPlanFrozenTubeServiceImpl implements StockOutPlanFrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutPlanFrozenTubeServiceImpl.class);
    
    private final StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    private final StockOutPlanFrozenTubeMapper stockOutPlanFrozenTubeMapper;

    public StockOutPlanFrozenTubeServiceImpl(StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository, StockOutPlanFrozenTubeMapper stockOutPlanFrozenTubeMapper) {
        this.stockOutPlanFrozenTubeRepository = stockOutPlanFrozenTubeRepository;
        this.stockOutPlanFrozenTubeMapper = stockOutPlanFrozenTubeMapper;
    }

    /**
     * Save a stockOutPlanFrozenTube.
     *
     * @param stockOutPlanFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutPlanFrozenTubeDTO save(StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO) {
        log.debug("Request to save StockOutPlanFrozenTube : {}", stockOutPlanFrozenTubeDTO);
        StockOutPlanFrozenTube stockOutPlanFrozenTube = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeDTOToStockOutPlanFrozenTube(stockOutPlanFrozenTubeDTO);
        stockOutPlanFrozenTube = stockOutPlanFrozenTubeRepository.save(stockOutPlanFrozenTube);
        StockOutPlanFrozenTubeDTO result = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube);
        return result;
    }

    /**
     *  Get all the stockOutPlanFrozenTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutPlanFrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutPlanFrozenTubes");
        Page<StockOutPlanFrozenTube> result = stockOutPlanFrozenTubeRepository.findAll(pageable);
        return result.map(stockOutPlanFrozenTube -> stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube));
    }

    /**
     *  Get one stockOutPlanFrozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutPlanFrozenTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutPlanFrozenTube : {}", id);
        StockOutPlanFrozenTube stockOutPlanFrozenTube = stockOutPlanFrozenTubeRepository.findOne(id);
        StockOutPlanFrozenTubeDTO stockOutPlanFrozenTubeDTO = stockOutPlanFrozenTubeMapper.stockOutPlanFrozenTubeToStockOutPlanFrozenTubeDTO(stockOutPlanFrozenTube);
        return stockOutPlanFrozenTubeDTO;
    }

    /**
     *  Delete the  stockOutPlanFrozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutPlanFrozenTube : {}", id);
        stockOutPlanFrozenTubeRepository.delete(id);
    }
}
