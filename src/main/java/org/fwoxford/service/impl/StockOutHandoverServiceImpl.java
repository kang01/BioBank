package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutHandoverService;
import org.fwoxford.domain.StockOutHandover;
import org.fwoxford.repository.StockOutHandoverRepository;
import org.fwoxford.service.dto.StockOutHandoverDTO;
import org.fwoxford.service.mapper.StockOutHandoverMapper;
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
 * Service Implementation for managing StockOutHandover.
 */
@Service
@Transactional
public class StockOutHandoverServiceImpl implements StockOutHandoverService{

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverServiceImpl.class);
    
    private final StockOutHandoverRepository stockOutHandoverRepository;

    private final StockOutHandoverMapper stockOutHandoverMapper;

    public StockOutHandoverServiceImpl(StockOutHandoverRepository stockOutHandoverRepository, StockOutHandoverMapper stockOutHandoverMapper) {
        this.stockOutHandoverRepository = stockOutHandoverRepository;
        this.stockOutHandoverMapper = stockOutHandoverMapper;
    }

    /**
     * Save a stockOutHandover.
     *
     * @param stockOutHandoverDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutHandoverDTO save(StockOutHandoverDTO stockOutHandoverDTO) {
        log.debug("Request to save StockOutHandover : {}", stockOutHandoverDTO);
        StockOutHandover stockOutHandover = stockOutHandoverMapper.stockOutHandoverDTOToStockOutHandover(stockOutHandoverDTO);
        stockOutHandover = stockOutHandoverRepository.save(stockOutHandover);
        StockOutHandoverDTO result = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(stockOutHandover);
        return result;
    }

    /**
     *  Get all the stockOutHandovers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutHandoverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutHandovers");
        Page<StockOutHandover> result = stockOutHandoverRepository.findAll(pageable);
        return result.map(stockOutHandover -> stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(stockOutHandover));
    }

    /**
     *  Get one stockOutHandover by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutHandoverDTO findOne(Long id) {
        log.debug("Request to get StockOutHandover : {}", id);
        StockOutHandover stockOutHandover = stockOutHandoverRepository.findOne(id);
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandoverToStockOutHandoverDTO(stockOutHandover);
        return stockOutHandoverDTO;
    }

    /**
     *  Delete the  stockOutHandover by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutHandover : {}", id);
        stockOutHandoverRepository.delete(id);
    }
}
