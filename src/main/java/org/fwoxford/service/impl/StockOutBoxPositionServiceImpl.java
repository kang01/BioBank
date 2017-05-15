package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutBoxPositionService;
import org.fwoxford.domain.StockOutBoxPosition;
import org.fwoxford.repository.StockOutBoxPositionRepository;
import org.fwoxford.service.dto.StockOutBoxPositionDTO;
import org.fwoxford.service.mapper.StockOutBoxPositionMapper;
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
 * Service Implementation for managing StockOutBoxPosition.
 */
@Service
@Transactional
public class StockOutBoxPositionServiceImpl implements StockOutBoxPositionService{

    private final Logger log = LoggerFactory.getLogger(StockOutBoxPositionServiceImpl.class);
    
    private final StockOutBoxPositionRepository stockOutBoxPositionRepository;

    private final StockOutBoxPositionMapper stockOutBoxPositionMapper;

    public StockOutBoxPositionServiceImpl(StockOutBoxPositionRepository stockOutBoxPositionRepository, StockOutBoxPositionMapper stockOutBoxPositionMapper) {
        this.stockOutBoxPositionRepository = stockOutBoxPositionRepository;
        this.stockOutBoxPositionMapper = stockOutBoxPositionMapper;
    }

    /**
     * Save a stockOutBoxPosition.
     *
     * @param stockOutBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutBoxPositionDTO save(StockOutBoxPositionDTO stockOutBoxPositionDTO) {
        log.debug("Request to save StockOutBoxPosition : {}", stockOutBoxPositionDTO);
        StockOutBoxPosition stockOutBoxPosition = stockOutBoxPositionMapper.stockOutBoxPositionDTOToStockOutBoxPosition(stockOutBoxPositionDTO);
        stockOutBoxPosition = stockOutBoxPositionRepository.save(stockOutBoxPosition);
        StockOutBoxPositionDTO result = stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(stockOutBoxPosition);
        return result;
    }

    /**
     *  Get all the stockOutBoxPositions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutBoxPositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutBoxPositions");
        Page<StockOutBoxPosition> result = stockOutBoxPositionRepository.findAll(pageable);
        return result.map(stockOutBoxPosition -> stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(stockOutBoxPosition));
    }

    /**
     *  Get one stockOutBoxPosition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutBoxPositionDTO findOne(Long id) {
        log.debug("Request to get StockOutBoxPosition : {}", id);
        StockOutBoxPosition stockOutBoxPosition = stockOutBoxPositionRepository.findOne(id);
        StockOutBoxPositionDTO stockOutBoxPositionDTO = stockOutBoxPositionMapper.stockOutBoxPositionToStockOutBoxPositionDTO(stockOutBoxPosition);
        return stockOutBoxPositionDTO;
    }

    /**
     *  Delete the  stockOutBoxPosition by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutBoxPosition : {}", id);
        stockOutBoxPositionRepository.delete(id);
    }
}
