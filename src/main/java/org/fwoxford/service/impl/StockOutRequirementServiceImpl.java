package org.fwoxford.service.impl;

import org.fwoxford.service.StockOutRequirementService;
import org.fwoxford.domain.StockOutRequirement;
import org.fwoxford.repository.StockOutRequirementRepository;
import org.fwoxford.service.dto.StockOutRequirementDTO;
import org.fwoxford.service.dto.response.StockOutRequirementForSave;
import org.fwoxford.service.mapper.StockOutRequirementMapper;
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
 * Service Implementation for managing StockOutRequirement.
 */
@Service
@Transactional
public class StockOutRequirementServiceImpl implements StockOutRequirementService{

    private final Logger log = LoggerFactory.getLogger(StockOutRequirementServiceImpl.class);

    private final StockOutRequirementRepository stockOutRequirementRepository;

    private final StockOutRequirementMapper stockOutRequirementMapper;

    public StockOutRequirementServiceImpl(StockOutRequirementRepository stockOutRequirementRepository, StockOutRequirementMapper stockOutRequirementMapper) {
        this.stockOutRequirementRepository = stockOutRequirementRepository;
        this.stockOutRequirementMapper = stockOutRequirementMapper;
    }

    /**
     * Save a stockOutRequirement.
     *
     * @param stockOutRequirementDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutRequirementDTO save(StockOutRequirementDTO stockOutRequirementDTO) {
        log.debug("Request to save StockOutRequirement : {}", stockOutRequirementDTO);
        StockOutRequirement stockOutRequirement = stockOutRequirementMapper.stockOutRequirementDTOToStockOutRequirement(stockOutRequirementDTO);
        stockOutRequirement = stockOutRequirementRepository.save(stockOutRequirement);
        StockOutRequirementDTO result = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);
        return result;
    }

    /**
     *  Get all the stockOutRequirements.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutRequirementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutRequirements");
        Page<StockOutRequirement> result = stockOutRequirementRepository.findAll(pageable);
        return result.map(stockOutRequirement -> stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement));
    }

    /**
     *  Get one stockOutRequirement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutRequirementDTO findOne(Long id) {
        log.debug("Request to get StockOutRequirement : {}", id);
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        StockOutRequirementDTO stockOutRequirementDTO = stockOutRequirementMapper.stockOutRequirementToStockOutRequirementDTO(stockOutRequirement);
        return stockOutRequirementDTO;
    }

    /**
     *  Delete the  stockOutRequirement by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutRequirement : {}", id);
        stockOutRequirementRepository.delete(id);
    }

    @Override
    public StockOutRequirementForSave saveStockOutRequirement(StockOutRequirementForSave stockOutRequirement) {
        return null;
    }
}
