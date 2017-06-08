package org.fwoxford.service.impl;

import org.fwoxford.service.StockInBoxPositionService;
import org.fwoxford.domain.StockInBoxPosition;
import org.fwoxford.repository.StockInBoxPositionRepository;
import org.fwoxford.service.dto.StockInBoxPositionDTO;
import org.fwoxford.service.mapper.StockInBoxPositionMapper;
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
 * Service Implementation for managing StockInBoxPosition.
 */
@Service
@Transactional
public class StockInBoxPositionServiceImpl implements StockInBoxPositionService{

    private final Logger log = LoggerFactory.getLogger(StockInBoxPositionServiceImpl.class);
    
    private final StockInBoxPositionRepository stockInBoxPositionRepository;

    private final StockInBoxPositionMapper stockInBoxPositionMapper;

    public StockInBoxPositionServiceImpl(StockInBoxPositionRepository stockInBoxPositionRepository, StockInBoxPositionMapper stockInBoxPositionMapper) {
        this.stockInBoxPositionRepository = stockInBoxPositionRepository;
        this.stockInBoxPositionMapper = stockInBoxPositionMapper;
    }

    /**
     * Save a stockInBoxPosition.
     *
     * @param stockInBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockInBoxPositionDTO save(StockInBoxPositionDTO stockInBoxPositionDTO) {
        log.debug("Request to save StockInBoxPosition : {}", stockInBoxPositionDTO);
        StockInBoxPosition stockInBoxPosition = stockInBoxPositionMapper.stockInBoxPositionDTOToStockInBoxPosition(stockInBoxPositionDTO);
        stockInBoxPosition = stockInBoxPositionRepository.save(stockInBoxPosition);
        StockInBoxPositionDTO result = stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(stockInBoxPosition);
        return result;
    }

    /**
     *  Get all the stockInBoxPositions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockInBoxPositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockInBoxPositions");
        Page<StockInBoxPosition> result = stockInBoxPositionRepository.findAll(pageable);
        return result.map(stockInBoxPosition -> stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(stockInBoxPosition));
    }

    /**
     *  Get one stockInBoxPosition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockInBoxPositionDTO findOne(Long id) {
        log.debug("Request to get StockInBoxPosition : {}", id);
        StockInBoxPosition stockInBoxPosition = stockInBoxPositionRepository.findOne(id);
        StockInBoxPositionDTO stockInBoxPositionDTO = stockInBoxPositionMapper.stockInBoxPositionToStockInBoxPositionDTO(stockInBoxPosition);
        return stockInBoxPositionDTO;
    }

    /**
     *  Delete the  stockInBoxPosition by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockInBoxPosition : {}", id);
        stockInBoxPositionRepository.delete(id);
    }
}
