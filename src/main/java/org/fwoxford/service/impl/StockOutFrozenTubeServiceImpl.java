package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutFrozenTubeService;
import org.fwoxford.domain.StockOutFrozenTube;
import org.fwoxford.repository.StockOutFrozenTubeRepository;
import org.fwoxford.service.dto.StockOutFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutFrozenTubeMapper;
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
 * Service Implementation for managing StockOutFrozenTube.
 */
@Service
@Transactional
public class StockOutFrozenTubeServiceImpl implements StockOutFrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenTubeServiceImpl.class);
    
    private final StockOutFrozenTubeRepository stockOutFrozenTubeRepository;

    private final StockOutFrozenTubeMapper stockOutFrozenTubeMapper;

    public StockOutFrozenTubeServiceImpl(StockOutFrozenTubeRepository stockOutFrozenTubeRepository, StockOutFrozenTubeMapper stockOutFrozenTubeMapper) {
        this.stockOutFrozenTubeRepository = stockOutFrozenTubeRepository;
        this.stockOutFrozenTubeMapper = stockOutFrozenTubeMapper;
    }

    /**
     * Save a stockOutFrozenTube.
     *
     * @param stockOutFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutFrozenTubeDTO save(StockOutFrozenTubeDTO stockOutFrozenTubeDTO) {
        log.debug("Request to save StockOutFrozenTube : {}", stockOutFrozenTubeDTO);
        StockOutFrozenTube stockOutFrozenTube = stockOutFrozenTubeMapper.stockOutFrozenTubeDTOToStockOutFrozenTube(stockOutFrozenTubeDTO);
        stockOutFrozenTube = stockOutFrozenTubeRepository.save(stockOutFrozenTube);
        StockOutFrozenTubeDTO result = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);
        return result;
    }

    /**
     *  Get all the stockOutFrozenTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutFrozenTubes");
        Page<StockOutFrozenTube> result = stockOutFrozenTubeRepository.findAll(pageable);
        return result.map(stockOutFrozenTube -> stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube));
    }

    /**
     *  Get one stockOutFrozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutFrozenTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutFrozenTube : {}", id);
        StockOutFrozenTube stockOutFrozenTube = stockOutFrozenTubeRepository.findOne(id);
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);
        return stockOutFrozenTubeDTO;
    }

    /**
     *  Delete the  stockOutFrozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutFrozenTube : {}", id);
        stockOutFrozenTubeRepository.delete(id);
    }
}
