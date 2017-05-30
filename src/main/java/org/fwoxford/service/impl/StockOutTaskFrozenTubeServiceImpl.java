package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutTaskFrozenTubeService;
import org.fwoxford.domain.StockOutTaskFrozenTube;
import org.fwoxford.repository.StockOutTaskFrozenTubeRepository;
import org.fwoxford.service.dto.StockOutTaskFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutTaskFrozenTubeMapper;
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
 * Service Implementation for managing StockOutTaskFrozenTube.
 */
@Service
@Transactional
public class StockOutTaskFrozenTubeServiceImpl implements StockOutTaskFrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutTaskFrozenTubeServiceImpl.class);
    
    private final StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    private final StockOutTaskFrozenTubeMapper stockOutTaskFrozenTubeMapper;

    public StockOutTaskFrozenTubeServiceImpl(StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository, StockOutTaskFrozenTubeMapper stockOutTaskFrozenTubeMapper) {
        this.stockOutTaskFrozenTubeRepository = stockOutTaskFrozenTubeRepository;
        this.stockOutTaskFrozenTubeMapper = stockOutTaskFrozenTubeMapper;
    }

    /**
     * Save a stockOutTaskFrozenTube.
     *
     * @param stockOutTaskFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutTaskFrozenTubeDTO save(StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO) {
        log.debug("Request to save StockOutTaskFrozenTube : {}", stockOutTaskFrozenTubeDTO);
        StockOutTaskFrozenTube stockOutTaskFrozenTube = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeDTOToStockOutTaskFrozenTube(stockOutTaskFrozenTubeDTO);
        stockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.save(stockOutTaskFrozenTube);
        StockOutTaskFrozenTubeDTO result = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube);
        return result;
    }

    /**
     *  Get all the stockOutTaskFrozenTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutTaskFrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutTaskFrozenTubes");
        Page<StockOutTaskFrozenTube> result = stockOutTaskFrozenTubeRepository.findAll(pageable);
        return result.map(stockOutTaskFrozenTube -> stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube));
    }

    /**
     *  Get one stockOutTaskFrozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutTaskFrozenTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutTaskFrozenTube : {}", id);
        StockOutTaskFrozenTube stockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.findOne(id);
        StockOutTaskFrozenTubeDTO stockOutTaskFrozenTubeDTO = stockOutTaskFrozenTubeMapper.stockOutTaskFrozenTubeToStockOutTaskFrozenTubeDTO(stockOutTaskFrozenTube);
        return stockOutTaskFrozenTubeDTO;
    }

    /**
     *  Delete the  stockOutTaskFrozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutTaskFrozenTube : {}", id);
        stockOutTaskFrozenTubeRepository.delete(id);
    }
}
