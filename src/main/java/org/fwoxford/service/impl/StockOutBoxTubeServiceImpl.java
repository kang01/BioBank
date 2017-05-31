package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.StockOutFrozenBox;
import org.fwoxford.repository.StockOutFrozenBoxRepository;
import org.fwoxford.service.StockOutBoxTubeService;
import org.fwoxford.domain.StockOutBoxTube;
import org.fwoxford.repository.StockOutBoxTubeRepository;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.StockOutBoxTubeDTO;
import org.fwoxford.service.dto.response.StockOutFrozenTubeDataTableEntity;
import org.fwoxford.service.mapper.StockOutBoxTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing StockOutBoxTube.
 */
@Service
@Transactional
public class StockOutBoxTubeServiceImpl implements StockOutBoxTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutBoxTubeServiceImpl.class);

    private final StockOutBoxTubeRepository stockOutBoxTubeRepository;

    private final StockOutBoxTubeMapper stockOutBoxTubeMapper;

    @Autowired
    private StockOutFrozenBoxRepository stockOutFrozenBoxRepository;

    public StockOutBoxTubeServiceImpl(StockOutBoxTubeRepository stockOutBoxTubeRepository, StockOutBoxTubeMapper stockOutBoxTubeMapper) {
        this.stockOutBoxTubeRepository = stockOutBoxTubeRepository;
        this.stockOutBoxTubeMapper = stockOutBoxTubeMapper;
    }

    /**
     * Save a stockOutBoxTube.
     *
     * @param stockOutBoxTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutBoxTubeDTO save(StockOutBoxTubeDTO stockOutBoxTubeDTO) {
        log.debug("Request to save StockOutBoxTube : {}", stockOutBoxTubeDTO);
        StockOutBoxTube stockOutBoxTube = stockOutBoxTubeMapper.stockOutBoxTubeDTOToStockOutBoxTube(stockOutBoxTubeDTO);
        stockOutBoxTube = stockOutBoxTubeRepository.save(stockOutBoxTube);
        StockOutBoxTubeDTO result = stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(stockOutBoxTube);
        return result;
    }

    /**
     *  Get all the stockOutBoxTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutBoxTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutBoxTubes");
        Page<StockOutBoxTube> result = stockOutBoxTubeRepository.findAll(pageable);
        return result.map(stockOutBoxTube -> stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(stockOutBoxTube));
    }

    /**
     *  Get one stockOutBoxTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutBoxTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutBoxTube : {}", id);
        StockOutBoxTube stockOutBoxTube = stockOutBoxTubeRepository.findOne(id);
        StockOutBoxTubeDTO stockOutBoxTubeDTO = stockOutBoxTubeMapper.stockOutBoxTubeToStockOutBoxTubeDTO(stockOutBoxTube);
        return stockOutBoxTubeDTO;
    }

    /**
     *  Delete the  stockOutBoxTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutBoxTube : {}", id);
        stockOutBoxTubeRepository.delete(id);
    }

    @Override
    public Page<StockOutFrozenTubeDataTableEntity> getStockOutTubeByStockOutBoxIds(List<Long> ids, Pageable pageable) {
        Page<StockOutBoxTube> result = stockOutBoxTubeRepository.findByStockOutFrozenBoxIdIn(ids, pageable);

        return result.map(frozenTube -> {
            StockOutFrozenTubeDataTableEntity dto = new StockOutFrozenTubeDataTableEntity();
            FrozenTube tube = frozenTube.getFrozenTube();
            if(tube == null){
                throw new BankServiceException("");
            }
            dto.setId(tube.getId());
            dto.setSampleCode(tube.getSampleCode());
            dto.setSampleTypeName(tube.getSampleTypeName());
            dto.setSex(Constants.SEX_MAP.get(tube.getGender())!=null?(String)Constants.SEX_MAP.get(tube.getGender()):null);
            dto.setAge(tube.getAge());
            dto.setBloodLipid(tube.isIsBloodLipid());
            dto.setDiseaseTypeId(tube.getDiseaseType());
            dto.setHemolysis(tube.isIsHemolysis());
            dto.setFrozenBoxCode(tube.getFrozenBoxCode());
            return dto;
        });
    }
}