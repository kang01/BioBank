package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.StockOutRequiredSample;
import org.fwoxford.domain.StockOutRequirement;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.repository.StockOutRequirementRepository;
import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.domain.StockOutReqFrozenTube;
import org.fwoxford.repository.StockOutReqFrozenTubeRepository;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutReqFrozenTubeMapper;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutReqFrozenTube.
 */
@Service
@Transactional
public class StockOutReqFrozenTubeServiceImpl implements StockOutReqFrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(StockOutReqFrozenTubeServiceImpl.class);

    private final StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    private final StockOutReqFrozenTubeMapper stockOutReqFrozenTubeMapper;

    @Autowired
    private StockOutRequirementRepository stockOutRequirementRepository;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    public StockOutReqFrozenTubeServiceImpl(StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository, StockOutReqFrozenTubeMapper stockOutReqFrozenTubeMapper) {
        this.stockOutReqFrozenTubeRepository = stockOutReqFrozenTubeRepository;
        this.stockOutReqFrozenTubeMapper = stockOutReqFrozenTubeMapper;
    }

    /**
     * Save a stockOutReqFrozenTube.
     *
     * @param stockOutReqFrozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutReqFrozenTubeDTO save(StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO) {
        log.debug("Request to save StockOutReqFrozenTube : {}", stockOutReqFrozenTubeDTO);
        StockOutReqFrozenTube stockOutReqFrozenTube = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeDTOToStockOutReqFrozenTube(stockOutReqFrozenTubeDTO);
        stockOutReqFrozenTube = stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTube);
        StockOutReqFrozenTubeDTO result = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);
        return result;
    }

    /**
     *  Get all the stockOutReqFrozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutReqFrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutReqFrozenTubes");
        Page<StockOutReqFrozenTube> result = stockOutReqFrozenTubeRepository.findAll(pageable);
        return result.map(stockOutReqFrozenTube -> stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube));
    }

    /**
     *  Get one stockOutReqFrozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutReqFrozenTubeDTO findOne(Long id) {
        log.debug("Request to get StockOutReqFrozenTube : {}", id);
        StockOutReqFrozenTube stockOutReqFrozenTube = stockOutReqFrozenTubeRepository.findOne(id);
        StockOutReqFrozenTubeDTO stockOutReqFrozenTubeDTO = stockOutReqFrozenTubeMapper.stockOutReqFrozenTubeToStockOutReqFrozenTubeDTO(stockOutReqFrozenTube);
        return stockOutReqFrozenTubeDTO;
    }

    /**
     *  Delete the  stockOutReqFrozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutReqFrozenTube : {}", id);
        stockOutReqFrozenTubeRepository.delete(id);
    }

    @Override
    public String checkStockOutSampleByAppointedSample(List<StockOutRequiredSample> stockOutRequiredSamples) {

        String status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS;
        for(StockOutRequiredSample s :stockOutRequiredSamples){
            StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
            String appointedSampleCode = s.getSampleCode();
            String appointedSampleType = s.getSampleType();
            FrozenTube frozenTube = frozenTubeRepository.findBySampleCodeAndSampleTypeCode(appointedSampleCode,appointedSampleType,s.getStockOutRequirement().getId());
            if(frozenTube == null){
                status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;
            }
            stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE);
            stockOutReqFrozenTube.setStockOutRequirement(s.getStockOutRequirement());
            stockOutReqFrozenTube.setMemo(frozenTube!=null?frozenTube.getMemo():null);
            stockOutReqFrozenTube.setFrozenBox(frozenTube!=null?frozenTube.getFrozenBox():null);
            stockOutReqFrozenTube.setFrozenTube(frozenTube!=null?frozenTube:null);
            stockOutReqFrozenTube.setImportingSampleId(s.getId());
            stockOutReqFrozenTube.setTubeColumns(frozenTube!=null?frozenTube.getTubeColumns():null);
            stockOutReqFrozenTube.setTubeRows(frozenTube!=null?frozenTube.getTubeRows():null);
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTube);
        }
        return status;
    }

    @Override
    public String checkStockOutSampleByRequirement(Long id) {
        String status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS;
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        Integer countOfSample = stockOutRequirement.getCountOfSample();
        Long sampleTypeId = stockOutRequirement.getSampleType()!=null?stockOutRequirement.getSampleType().getId():null;
        Long samplyClassificationId = stockOutRequirement.getSampleClassification()!=null?stockOutRequirement.getSampleClassification().getId():null;
        Integer ageMin = stockOutRequirement.getAgeMin();
        Integer ageMax = stockOutRequirement.getAgeMax();
        String diseaseType =stockOutRequirement.getDiseaseType();
        Long frozenTubeTypeId = stockOutRequirement.getFrozenTubeType()!=null?stockOutRequirement.getFrozenTubeType().getId():null;
        String sex = stockOutRequirement.getSex();
        Boolean isBloodLipid = stockOutRequirement.isIsBloodLipid();
        Boolean isHemolysis = stockOutRequirement.isIsHemolysis();

//        List<FrozenTube> frozenTubes = frozenTubeRepository.findByRequiremented(sampleTypeId,samplyClassificationId,
//            frozenTubeTypeId,diseaseType,sex,isBloodLipid,isHemolysis);

        List<FrozenTube> frozenTubes = frozenTubeRepository.findByRequirement(sampleTypeId,samplyClassificationId,
            frozenTubeTypeId,diseaseType,sex,isBloodLipid,isHemolysis,ageMin,ageMax);
        if(frozenTubes.size()<countOfSample){
            status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;
        }
        int i = 0;
        for(FrozenTube frozenTube :frozenTubes){
            StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
            stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE);
            stockOutReqFrozenTube.setStockOutRequirement(stockOutRequirement);
            stockOutReqFrozenTube.setMemo(frozenTube!=null?frozenTube.getMemo():null);
            stockOutReqFrozenTube.setFrozenBox(frozenTube!=null?frozenTube.getFrozenBox():null);
            stockOutReqFrozenTube.setFrozenTube(frozenTube!=null?frozenTube:null);
            stockOutReqFrozenTube.setTubeColumns(frozenTube!=null?frozenTube.getTubeColumns():null);
            stockOutReqFrozenTube.setTubeRows(frozenTube!=null?frozenTube.getTubeRows():null);
            if(i<countOfSample){
                stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTube);
            }
            i++;
        }
        return status;
    }
}
