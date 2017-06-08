package org.fwoxford.service.impl;

import org.fwoxford.service.StockInTubeService;
import org.fwoxford.domain.StockInTube;
import org.fwoxford.repository.StockInTubeRepository;
import org.fwoxford.service.dto.StockInTubeDTO;
import org.fwoxford.service.mapper.StockInTubeMapper;
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
 * Service Implementation for managing StockInTube.
 */
@Service
@Transactional
public class StockInTubeServiceImpl implements StockInTubeService{

    private final Logger log = LoggerFactory.getLogger(StockInTubeServiceImpl.class);
    
    private final StockInTubeRepository stockInTubeRepository;

    private final StockInTubeMapper stockInTubeMapper;

    public StockInTubeServiceImpl(StockInTubeRepository stockInTubeRepository, StockInTubeMapper stockInTubeMapper) {
        this.stockInTubeRepository = stockInTubeRepository;
        this.stockInTubeMapper = stockInTubeMapper;
    }

    /**
     * Save a stockInTube.
     *
     * @param stockInTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockInTubeDTO save(StockInTubeDTO stockInTubeDTO) {
        log.debug("Request to save StockInTube : {}", stockInTubeDTO);
        StockInTube stockInTube = stockInTubeMapper.stockInTubeDTOToStockInTube(stockInTubeDTO);
        stockInTube = stockInTubeRepository.save(stockInTube);
        StockInTubeDTO result = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);
        return result;
    }

    /**
     *  Get all the stockInTubes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockInTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockInTubes");
        Page<StockInTube> result = stockInTubeRepository.findAll(pageable);
        return result.map(stockInTube -> stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube));
    }

    /**
     *  Get one stockInTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockInTubeDTO findOne(Long id) {
        log.debug("Request to get StockInTube : {}", id);
        StockInTube stockInTube = stockInTubeRepository.findOne(id);
        StockInTubeDTO stockInTubeDTO = stockInTubeMapper.stockInTubeToStockInTubeDTO(stockInTube);
        return stockInTubeDTO;
    }

    /**
     *  Delete the  stockInTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockInTube : {}", id);
        stockInTubeRepository.delete(id);
    }
}
