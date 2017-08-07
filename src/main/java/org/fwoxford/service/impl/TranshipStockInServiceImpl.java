package org.fwoxford.service.impl;

import org.fwoxford.service.TranshipStockInService;
import org.fwoxford.domain.TranshipStockIn;
import org.fwoxford.repository.TranshipStockInRepository;
import org.fwoxford.service.dto.TranshipStockInDTO;
import org.fwoxford.service.mapper.TranshipStockInMapper;
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
 * Service Implementation for managing TranshipStockIn.
 */
@Service
@Transactional
public class TranshipStockInServiceImpl implements TranshipStockInService{

    private final Logger log = LoggerFactory.getLogger(TranshipStockInServiceImpl.class);
    
    private final TranshipStockInRepository transhipStockInRepository;

    private final TranshipStockInMapper transhipStockInMapper;

    public TranshipStockInServiceImpl(TranshipStockInRepository transhipStockInRepository, TranshipStockInMapper transhipStockInMapper) {
        this.transhipStockInRepository = transhipStockInRepository;
        this.transhipStockInMapper = transhipStockInMapper;
    }

    /**
     * Save a transhipStockIn.
     *
     * @param transhipStockInDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranshipStockInDTO save(TranshipStockInDTO transhipStockInDTO) {
        log.debug("Request to save TranshipStockIn : {}", transhipStockInDTO);
        TranshipStockIn transhipStockIn = transhipStockInMapper.transhipStockInDTOToTranshipStockIn(transhipStockInDTO);
        transhipStockIn = transhipStockInRepository.save(transhipStockIn);
        TranshipStockInDTO result = transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn);
        return result;
    }

    /**
     *  Get all the transhipStockIns.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranshipStockInDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TranshipStockIns");
        Page<TranshipStockIn> result = transhipStockInRepository.findAll(pageable);
        return result.map(transhipStockIn -> transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn));
    }

    /**
     *  Get one transhipStockIn by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranshipStockInDTO findOne(Long id) {
        log.debug("Request to get TranshipStockIn : {}", id);
        TranshipStockIn transhipStockIn = transhipStockInRepository.findOne(id);
        TranshipStockInDTO transhipStockInDTO = transhipStockInMapper.transhipStockInToTranshipStockInDTO(transhipStockIn);
        return transhipStockInDTO;
    }

    /**
     *  Delete the  transhipStockIn by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TranshipStockIn : {}", id);
        transhipStockInRepository.delete(id);
    }
}
