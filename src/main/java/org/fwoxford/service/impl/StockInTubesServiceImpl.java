package org.fwoxford.service.impl;

import org.fwoxford.service.StockInTubesService;
import org.fwoxford.domain.StockInTubes;
import org.fwoxford.repository.StockInTubesRepository;
import org.fwoxford.service.dto.StockInTubesDTO;
import org.fwoxford.service.mapper.StockInTubesMapper;
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
 * Service Implementation for managing StockInTubes.
 */
@Service
@Transactional
public class StockInTubesServiceImpl implements StockInTubesService{

    private final Logger log = LoggerFactory.getLogger(StockInTubesServiceImpl.class);
    
    private final StockInTubesRepository stockInTubesRepository;

    private final StockInTubesMapper stockInTubesMapper;

    public StockInTubesServiceImpl(StockInTubesRepository stockInTubesRepository, StockInTubesMapper stockInTubesMapper) {
        this.stockInTubesRepository = stockInTubesRepository;
        this.stockInTubesMapper = stockInTubesMapper;
    }

    /**
     * Save a stockInTubes.
     *
     * @param stockInTubesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockInTubesDTO save(StockInTubesDTO stockInTubesDTO) {
        log.debug("Request to save StockInTubes : {}", stockInTubesDTO);
        StockInTubes stockInTubes = stockInTubesMapper.stockInTubesDTOToStockInTubes(stockInTubesDTO);
        stockInTubes = stockInTubesRepository.save(stockInTubes);
        StockInTubesDTO result = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);
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
    public Page<StockInTubesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockInTubes");
        Page<StockInTubes> result = stockInTubesRepository.findAll(pageable);
        return result.map(stockInTubes -> stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes));
    }

    /**
     *  Get one stockInTubes by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockInTubesDTO findOne(Long id) {
        log.debug("Request to get StockInTubes : {}", id);
        StockInTubes stockInTubes = stockInTubesRepository.findOne(id);
        StockInTubesDTO stockInTubesDTO = stockInTubesMapper.stockInTubesToStockInTubesDTO(stockInTubes);
        return stockInTubesDTO;
    }

    /**
     *  Delete the  stockInTubes by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockInTubes : {}", id);
        stockInTubesRepository.delete(id);
    }
}
