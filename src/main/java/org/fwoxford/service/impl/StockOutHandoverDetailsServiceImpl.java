package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutHandoverDetailsService;
import org.fwoxford.domain.StockOutHandoverDetails;
import org.fwoxford.repository.StockOutHandoverDetailsRepository;
import org.fwoxford.service.dto.StockOutHandoverDetailsDTO;
import org.fwoxford.service.mapper.StockOutHandoverDetailsMapper;
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
 * Service Implementation for managing StockOutHandoverDetails.
 */
@Service
@Transactional
public class StockOutHandoverDetailsServiceImpl implements StockOutHandoverDetailsService{

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverDetailsServiceImpl.class);
    
    private final StockOutHandoverDetailsRepository stockOutHandoverDetailsRepository;

    private final StockOutHandoverDetailsMapper stockOutHandoverDetailsMapper;

    public StockOutHandoverDetailsServiceImpl(StockOutHandoverDetailsRepository stockOutHandoverDetailsRepository, StockOutHandoverDetailsMapper stockOutHandoverDetailsMapper) {
        this.stockOutHandoverDetailsRepository = stockOutHandoverDetailsRepository;
        this.stockOutHandoverDetailsMapper = stockOutHandoverDetailsMapper;
    }

    /**
     * Save a stockOutHandoverDetails.
     *
     * @param stockOutHandoverDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutHandoverDetailsDTO save(StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO) {
        log.debug("Request to save StockOutHandoverDetails : {}", stockOutHandoverDetailsDTO);
        StockOutHandoverDetails stockOutHandoverDetails = stockOutHandoverDetailsMapper.stockOutHandoverDetailsDTOToStockOutHandoverDetails(stockOutHandoverDetailsDTO);
        stockOutHandoverDetails = stockOutHandoverDetailsRepository.save(stockOutHandoverDetails);
        StockOutHandoverDetailsDTO result = stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(stockOutHandoverDetails);
        return result;
    }

    /**
     *  Get all the stockOutHandoverDetails.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutHandoverDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutHandoverDetails");
        Page<StockOutHandoverDetails> result = stockOutHandoverDetailsRepository.findAll(pageable);
        return result.map(stockOutHandoverDetails -> stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(stockOutHandoverDetails));
    }

    /**
     *  Get one stockOutHandoverDetails by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutHandoverDetailsDTO findOne(Long id) {
        log.debug("Request to get StockOutHandoverDetails : {}", id);
        StockOutHandoverDetails stockOutHandoverDetails = stockOutHandoverDetailsRepository.findOne(id);
        StockOutHandoverDetailsDTO stockOutHandoverDetailsDTO = stockOutHandoverDetailsMapper.stockOutHandoverDetailsToStockOutHandoverDetailsDTO(stockOutHandoverDetails);
        return stockOutHandoverDetailsDTO;
    }

    /**
     *  Delete the  stockOutHandoverDetails by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutHandoverDetails : {}", id);
        stockOutHandoverDetailsRepository.delete(id);
    }
}
