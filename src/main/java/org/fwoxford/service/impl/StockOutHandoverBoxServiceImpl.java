package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutHandoverBoxService;
import org.fwoxford.domain.StockOutHandoverBox;
import org.fwoxford.repository.StockOutHandoverBoxRepository;
import org.fwoxford.service.dto.StockOutHandoverBoxDTO;
import org.fwoxford.service.mapper.StockOutHandoverBoxMapper;
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
 * Service Implementation for managing StockOutHandoverBox.
 */
@Service
@Transactional
public class StockOutHandoverBoxServiceImpl implements StockOutHandoverBoxService{

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverBoxServiceImpl.class);
    
    private final StockOutHandoverBoxRepository stockOutHandoverBoxRepository;

    private final StockOutHandoverBoxMapper stockOutHandoverBoxMapper;

    public StockOutHandoverBoxServiceImpl(StockOutHandoverBoxRepository stockOutHandoverBoxRepository, StockOutHandoverBoxMapper stockOutHandoverBoxMapper) {
        this.stockOutHandoverBoxRepository = stockOutHandoverBoxRepository;
        this.stockOutHandoverBoxMapper = stockOutHandoverBoxMapper;
    }

    /**
     * Save a stockOutHandoverBox.
     *
     * @param stockOutHandoverBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutHandoverBoxDTO save(StockOutHandoverBoxDTO stockOutHandoverBoxDTO) {
        log.debug("Request to save StockOutHandoverBox : {}", stockOutHandoverBoxDTO);
        StockOutHandoverBox stockOutHandoverBox = stockOutHandoverBoxMapper.stockOutHandoverBoxDTOToStockOutHandoverBox(stockOutHandoverBoxDTO);
        stockOutHandoverBox = stockOutHandoverBoxRepository.save(stockOutHandoverBox);
        StockOutHandoverBoxDTO result = stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(stockOutHandoverBox);
        return result;
    }

    /**
     *  Get all the stockOutHandoverBoxes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutHandoverBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutHandoverBoxes");
        Page<StockOutHandoverBox> result = stockOutHandoverBoxRepository.findAll(pageable);
        return result.map(stockOutHandoverBox -> stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(stockOutHandoverBox));
    }

    /**
     *  Get one stockOutHandoverBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutHandoverBoxDTO findOne(Long id) {
        log.debug("Request to get StockOutHandoverBox : {}", id);
        StockOutHandoverBox stockOutHandoverBox = stockOutHandoverBoxRepository.findOne(id);
        StockOutHandoverBoxDTO stockOutHandoverBoxDTO = stockOutHandoverBoxMapper.stockOutHandoverBoxToStockOutHandoverBoxDTO(stockOutHandoverBox);
        return stockOutHandoverBoxDTO;
    }

    /**
     *  Delete the  stockOutHandoverBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutHandoverBox : {}", id);
        stockOutHandoverBoxRepository.delete(id);
    }
}
