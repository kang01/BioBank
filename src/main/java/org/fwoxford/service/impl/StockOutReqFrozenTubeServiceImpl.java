package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.domain.StockOutReqFrozenTube;
import org.fwoxford.repository.StockOutReqFrozenTubeRepository;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutReqFrozenTubeMapper;
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
 * Service Implementation for managing StockOutReqFrozenTube.
 */
@Service
@Transactional
public class StockOutReqFrozenTubeServiceImpl implements StockOutReqFrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutReqFrozenTubeServiceImpl.class);
    
    private final StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    private final StockOutReqFrozenTubeMapper stockOutReqFrozenTubeMapper;

    public StockOutReqFrozenTubeServiceImpl(StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository, StockOutReqFrozenTubeMapper stockOutReqFrozenTubeMapper) {
        this.stockOutReqFrozenTubeRepository = stockOutReqFrozenTubeRepository;
        this.stockOutReqFrozenTubeMapper = stockOutReqFrozenTubeMapper;
    }

    /**
     * Save a stockOutReqFrozenTube.
     *
     * @param stockOutReqFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutReqFrozenTubeDTO save(StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO) {
        log.debug("Request to save StockOutReqFrozenTube : {}", stockOutReqFrozenTubeDTO);
        StockOutReqFrozenTube stockOutReqFrozenTube = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeDTOToStockOutReqFrozenTube(stockOutReqFrozenTubeDTO);
        stockOutReqFrozenTube = stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTube);
        StockOutReqFrozenTubeDTO result = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);
        return result;
    }

    /**
     *  Get all the stockOutReqFrozenTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutReqFrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutReqFrozenTubes");
        Page<StockOutReqFrozenTube> result = stockOutReqFrozenTubeRepository.findAll(pageable);
        return result.map(stockOutReqFrozenTube -> stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube));
    }

    /**
     *  Get one stockOutReqFrozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutReqFrozenTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutReqFrozenTube : {}", id);
        StockOutReqFrozenTube stockOutReqFrozenTube = stockOutReqFrozenTubeRepository.findOne(id);
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);
        return stockOutReqFrozenTubeDTO;
    }

    /**
     *  Delete the  stockOutReqFrozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutReqFrozenTube : {}", id);
        stockOutReqFrozenTubeRepository.delete(id);
    }
}
