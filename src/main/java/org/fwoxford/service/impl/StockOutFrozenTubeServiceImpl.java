package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.StockOutReqFrozenTube;
import org.fwoxford.repository.StockOutReqFrozenTubeRepository;
import org.fwoxford.service.StockOutFrozenTubeService;
import org.fwoxford.domain.StockOutFrozenTube;
import org.fwoxford.repository.StockOutFrozenTubeRepository;
import org.fwoxford.service.dto.StockOutFrozenTubeDTO;
import org.fwoxford.service.dto.response.StockOutFrozenTubeForPlan;
import org.fwoxford.service.mapper.StockOutFrozenTubeMapper;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutFrozenTube.
 */
@Service
@Transactional
public class StockOutFrozenTubeServiceImpl implements StockOutFrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenTubeServiceImpl.class);

    private final StockOutFrozenTubeRepository stockOutFrozenTubeRepository;

    private final StockOutFrozenTubeMapper stockOutFrozenTubeMapper;

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    public StockOutFrozenTubeServiceImpl(StockOutFrozenTubeRepository stockOutFrozenTubeRepository, StockOutFrozenTubeMapper stockOutFrozenTubeMapper) {
        this.stockOutFrozenTubeRepository = stockOutFrozenTubeRepository;
        this.stockOutFrozenTubeMapper = stockOutFrozenTubeMapper;
    }

    /**
     * Save a stockOutFrozenTube.
     *
     * @param stockOutFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutFrozenTubeDTO save(StockOutFrozenTubeDTO stockOutFrozenTubeDTO) {
        log.debug("Request to save StockOutFrozenTube : {}", stockOutFrozenTubeDTO);
        StockOutFrozenTube stockOutFrozenTube = stockOutFrozenTubeMapper.stockOutFrozenTubeDTOToStockOutFrozenTube(stockOutFrozenTubeDTO);
        stockOutFrozenTube = stockOutFrozenTubeRepository.save(stockOutFrozenTube);
        StockOutFrozenTubeDTO result = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);
        return result;
    }

    /**
     *  Get all the stockOutFrozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutFrozenTubes");
        Page<StockOutFrozenTube> result = stockOutFrozenTubeRepository.findAll(pageable);
        return result.map(stockOutFrozenTube -> stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube));
    }

    /**
     *  Get one stockOutFrozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutFrozenTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutFrozenTube : {}", id);
        StockOutFrozenTube stockOutFrozenTube = stockOutFrozenTubeRepository.findOne(id);
        StockOutFrozenTubeDTO stockOutFrozenTubeDTO = stockOutFrozenTubeMapper.stockOutFrozenTubeToStockOutFrozenTubeDTO(stockOutFrozenTube);
        return stockOutFrozenTubeDTO;
    }

    /**
     *  Delete the  stockOutFrozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutFrozenTube : {}", id);
        stockOutFrozenTubeRepository.delete(id);
    }

    @Override
    public List<StockOutFrozenTubeForPlan> getStockOutFrozenTubeForPlanByApplyAndBox(Long applyId, Long frozenBoxId) {
        List<StockOutReqFrozenTube> stockOutReqFrozenTube = stockOutReqFrozenTubeRepository.findAllByStockOutApplyIdAndFrozenBoxId(applyId,frozenBoxId);
        List<StockOutFrozenTubeForPlan> result = new ArrayList<StockOutFrozenTubeForPlan>();
        for(StockOutReqFrozenTube s :stockOutReqFrozenTube){
            StockOutFrozenTubeForPlan tube = new StockOutFrozenTubeForPlan();
            FrozenTube tubeS = s.getFrozenTube();
            if(tubeS == null){
                continue;
            }
            tube.setId(tubeS.getId());
            tube.setSampleTypeName(tubeS.getSampleTypeName());
            tube.setStatus(tubeS.getStatus());
            tube.setAge(tubeS.getAge());
            tube.setSex(Constants.SEX_MAP.get(tubeS.getGender())!=null?(String)Constants.SEX_MAP.get(tubeS.getGender()):null);
            tube.setSampleUsedTimes(Long.valueOf(tubeS.getSampleUsedTimes()!=null?tubeS.getSampleUsedTimes():0));
            tube.setSampleCode(tubeS.getSampleCode());
            tube.setMemo(tubeS.getMemo());
            result.add(tube);
        }
        return  result;
    }

    @Override
    public List<StockOutFrozenTubeForPlan> getStockOutFrozenTubeForPlanByRequirementAndBox( List<Long> requirementIds, Long frozenBoxId) {
        List<Long> frozenBoxIds = new ArrayList<Long>(){{add(frozenBoxId);}};
        List<StockOutReqFrozenTube> stockOutReqFrozenTube = stockOutReqFrozenTubeRepository.findAllByStockOutRequirementIdInAndFrozenBoxIdIn(requirementIds,frozenBoxIds);
        List<StockOutFrozenTubeForPlan> result = new ArrayList<StockOutFrozenTubeForPlan>();
        for(StockOutReqFrozenTube s :stockOutReqFrozenTube){
            StockOutFrozenTubeForPlan tube = new StockOutFrozenTubeForPlan();
            FrozenTube tubeS = s.getFrozenTube();
            if(tubeS == null){
                continue;
            }
            tube.setId(tubeS.getId());
            tube.setSampleTypeName(tubeS.getSampleTypeName());
            tube.setStatus(tubeS.getStatus());
            tube.setAge(tubeS.getAge());
            tube.setSex(Constants.SEX_MAP.get(tubeS.getGender())!=null?(String)Constants.SEX_MAP.get(tubeS.getGender()):null);
            tube.setSampleUsedTimes(Long.valueOf(tubeS.getSampleUsedTimes()!=null?tubeS.getSampleUsedTimes():0));
            tube.setSampleClassificationName(tubeS.getSampleClassification()!=null?tubeS.getSampleClassification().getSampleClassificationName():null);
            tube.setSampleCode(tubeS.getSampleCode());
            tube.setMemo(tubeS.getMemo());
            result.add(tube);
        }
        return  result;
    }
}
