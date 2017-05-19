package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.service.StockOutRequirementService;
import org.fwoxford.service.dto.StockOutRequirementDTO;
import org.fwoxford.service.dto.response.StockOutRequirementForApply;
import org.fwoxford.service.dto.response.StockOutRequirementForSave;
import org.fwoxford.service.mapper.StockOutRequirementMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Service Implementation for managing StockOutRequirement.
 */
@Service
@Transactional
public class StockOutRequirementServiceImpl implements StockOutRequirementService{

    private final Logger log = LoggerFactory.getLogger(StockOutRequirementServiceImpl.class);

    private final StockOutRequirementRepository stockOutRequirementRepository;

    private final StockOutRequirementMapper stockOutRequirementMapper;

    @Autowired
    private StockOutApplyRepository stockOutApplyRepository;

    @Autowired
    private FrozenTubeTypeRepository frozenTubeTypeRepository;

    @Autowired
    private SampleTypeRepository sampleTypeRepository;

    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;

    @Autowired
    private ReportExportingService reportExportingService;

    @Autowired
    private StockOutRequiredSampleRepository stockOutRequiredSampleRepository;

    @Autowired
    private StockOutReqFrozenTubeService stockOutReqFrozenTubeService;

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
    public StockOutRequirementForSave saveStockOutRequirement(StockOutRequirementForSave stockOutRequirement, Long stockOutApplyId) {
        if(stockOutApplyId == null){
            throw new BankServiceException("申请ID不能为空！");
        }
        if(StringUtils.isEmpty(stockOutRequirement.getRequirementName())){
            throw new BankServiceException("需求名称不能为空！",stockOutRequirement.toString());
        }
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(stockOutApplyId);
        if(stockOutApply == null){
            throw new BankServiceException("未查询到申请单的记录！",stockOutApplyId.toString());
        }
        if(!stockOutApply.getStatus().equals(Constants.STOCK_OUT_PENDING)){
            throw new BankServiceException("申请单的状态不能新增需求！",stockOutApply.getStatus());
        }

        StockOutRequirement requirement = new StockOutRequirement();
        if(stockOutRequirement.getId()!=null){
            requirement.setId(stockOutRequirement.getId());
        }
        requirement.setStatus(Constants.STOCK_OUT_REQUIREMENT_CKECKING);
        requirement.setStockOutApply(stockOutApply);
        requirement.setRequirementName(stockOutRequirement.getRequirementName());
        requirement.setRequirementCode(BankUtil.getUniqueID());
        requirement.setApplyCode(stockOutApply.getApplyCode());
        requirement.setCountOfSample(stockOutRequirement.getCountOfSample());
        requirement.setDiseaseType(stockOutRequirement.getDiseaseTypeId());
        requirement.setSex(stockOutRequirement.getSex());
        requirement.setMemo(stockOutRequirement.getMemo());
        requirement.setIsBloodLipid(stockOutRequirement.getIsBloodLipid());
        requirement.setIsHemolysis(stockOutRequirement.getIsHemolysis());

        String age = stockOutRequirement.getAge();
        if(!StringUtils.isEmpty(age)){
            String[] ageStr = age.split(";");
            Integer ageMax = ageStr[1]!=null?Integer.valueOf(ageStr[1]):null;
            Integer ageMin = ageStr[0]!=null?Integer.valueOf(ageStr[0]):null;
            requirement.setAgeMax(ageMax);
            requirement.setAgeMin(ageMin);
        }
        if(stockOutRequirement.getFrozenTubeTypeId() != null){
            FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findOne(stockOutRequirement.getFrozenTubeTypeId());
            if(frozenTubeType ==null){
                throw new BankServiceException("未查到冻存管类型！");
            }
            requirement.setFrozenTubeType(frozenTubeType);
        }
        if(stockOutRequirement.getSampleTypeId()!=null){
            SampleType sampleType = sampleTypeRepository.findOne(stockOutRequirement.getSampleTypeId());
            if(sampleType ==null){
                throw new BankServiceException("未查到样本类型！");
            }
            requirement.setSampleType(sampleType);
        }
        if(stockOutRequirement.getSampleClassificationId()!=null){
            SampleClassification sampleClassification = sampleClassificationRepository.findOne(stockOutRequirement.getSampleClassificationId());
            if(sampleClassification ==null){
                throw new BankServiceException("未查到样本分类！");
            }
            requirement.setSampleClassification(sampleClassification);
        }
        stockOutRequirementRepository.save(requirement);
        stockOutRequirement.setId(requirement.getId());
        return stockOutRequirement;
    }
    @Override
    public StockOutRequirementForApply saveAndUploadStockOutRequirement(StockOutRequirementForSave stockOutRequirement, Long stockOutApplyId, MultipartFile file) {
        StockOutRequirementForApply stockOutRequirementForApply = new StockOutRequirementForApply();
        if(stockOutApplyId == null){
            throw new BankServiceException("申请ID不能为空！");
        }
        if(StringUtils.isEmpty(stockOutRequirement.getRequirementName())){
            throw new BankServiceException("需求名称不能为空！",stockOutRequirement.toString());
        }
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(stockOutApplyId);
        if(stockOutApply == null){
            throw new BankServiceException("未查询到申请单的记录！",stockOutApplyId.toString());
        }
        if(!stockOutApply.getStatus().equals(Constants.STOCK_OUT_PENDING)){
            throw new BankServiceException("申请单的状态不能新增需求！",stockOutApply.getStatus());
        }

        StockOutRequirement requirement = new StockOutRequirement();
        if(stockOutRequirement.getId()!=null){
            requirement.setId(stockOutRequirement.getId());
            stockOutRequiredSampleRepository.deleteByStockOutRequirementId(stockOutRequirement.getId());
        }
        requirement.setStatus(Constants.STOCK_OUT_REQUIREMENT_CKECKING);
        requirement.setStockOutApply(stockOutApply);
        requirement.setRequirementName(stockOutRequirement.getRequirementName());
        requirement.setRequirementCode(BankUtil.getUniqueID());
        requirement.setApplyCode(stockOutApply.getApplyCode());
        requirement.setMemo(stockOutRequirement.getMemo());
        stockOutRequirementRepository.save(requirement);

        List<StockOutRequiredSample> stockOutRequiredSamples = new ArrayList<StockOutRequiredSample>();
        Map<String,String> map = new HashMap<>();
        StringBuffer samples = new StringBuffer();
        try {
            InputStream stream = file.getInputStream();
            HashSet<String[]> hashSet =  reportExportingService.readRequiredSamplesFromExcelFile(stream);
            for(String[] s : hashSet){
                StockOutRequiredSample stockOutRequiredSample = new StockOutRequiredSample();
                String code = s[0];
                String type = s[1];
                stockOutRequiredSample.setStatus(Constants.VALID);
                stockOutRequiredSample.setSampleCode(code);
                stockOutRequiredSample.setSampleType(type);
                stockOutRequiredSample.setStockOutRequirement(requirement);
                if(map.get(code)!=null){
                    continue;
                }
                map.put(code,type);
                stockOutRequiredSamples.add(stockOutRequiredSample);
                if(stockOutRequiredSamples.size()>=1000){
                    stockOutRequiredSampleRepository.save(stockOutRequiredSamples);
                    stockOutRequiredSamples.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stockOutRequiredSampleRepository.save(stockOutRequiredSamples);
        requirement.setCountOfSample(stockOutRequiredSamples.size());
        stockOutRequirement.setCountOfSample(stockOutRequiredSamples.size());
        stockOutRequirementRepository.save(requirement);
        stockOutRequirementForApply.setId(requirement.getId());
        stockOutRequirementForApply.setCountOfSample(requirement.getCountOfSample());
        stockOutRequirementForApply.setRequirementName(requirement.getRequirementName());
        stockOutRequirementForApply.setMemo(requirement.getMemo());
        stockOutRequirementForApply.setStatus(requirement.getStatus());
        for(String s : map.keySet()){
            samples.append(s);
            samples.append("-");
            samples.append(map.get(s));
            samples.append(",");
        }
        if(!StringUtils.isEmpty(samples)){
            String samplesStr = samples.substring(0,samples.length()-1);
            stockOutRequirementForApply.setSamples(samplesStr);
        }
        return stockOutRequirementForApply;
    }
    @Override
    public StockOutRequirementForApply getRequirementById(Long id) {
        StockOutRequirementForApply result = new StockOutRequirementForApply();
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        if(stockOutRequirement == null){
            throw new BankServiceException("查询需求失败！");
        }
        result.setId(id);
        result.setRequirementName(stockOutRequirement.getRequirementName());
        result.setStatus(stockOutRequirement.getStatus());
        result.setCountOfSample(stockOutRequirement.getCountOfSample());
        result.setMemo(stockOutRequirement.getMemo());
        //获取指定样本
        List<StockOutRequiredSample> stockOutRequiredSamples = stockOutRequiredSampleRepository.findByStockOutRequirementId(id);
        StringBuffer samples = new StringBuffer();
        for(StockOutRequiredSample s :stockOutRequiredSamples){
            samples.append(s.getSampleCode());
            samples.append("-");
            samples.append(s.getSampleType());
            samples.append(",");
        }
        if(!StringUtils.isEmpty(samples)){
            String samplesStr = samples.substring(0,samples.length()-1);
            result.setSamples(samplesStr);
        }
        if(StringUtils.isEmpty(samples)){
            result.setSex(stockOutRequirement.getSex());
            result.setAge(stockOutRequirement.getAgeMin()+";"+stockOutRequirement.getAgeMax());
            result.setIsBloodLipid(stockOutRequirement.isIsBloodLipid());
            result.setFrozenTubeTypeId(stockOutRequirement.getFrozenTubeType()!=null?stockOutRequirement.getFrozenTubeType().getId():null);
            result.setDiseaseTypeId(stockOutRequirement.getDiseaseType());
            result.setIsHemolysis(stockOutRequirement.isIsHemolysis());
            result.setSampleTypeId(stockOutRequirement.getSampleType()!=null?stockOutRequirement.getSampleType().getId():null);
            result.setSampleClassificationId(stockOutRequirement.getSampleClassification()!=null?stockOutRequirement.getSampleClassification().getId():null);
        }
        return result;
    }

    /**
     * 核对样本需求
     * @param id
     * @return
     */
    @Override
    public StockOutRequirementForApply checkStockOutRequirement(Long id) {
        StockOutRequirementForApply stockOutRequirementForApply = new StockOutRequirementForApply();
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        if(stockOutRequirement == null){
            throw new BankServiceException("未查询到需求！");
        }
        String status = Constants.STOCK_OUT_REQUIREMENT_CKECKING;
        //获取指定样本
        List<StockOutRequiredSample> stockOutRequiredSamples = stockOutRequiredSampleRepository.findByStockOutRequirementId(id);
        if(stockOutRequiredSamples.size()>0){
            //核对导入指定样本
            status = stockOutReqFrozenTubeService.checkStockOutSampleByAppointedSample(stockOutRequiredSamples);
        }else{
            //核对录入部分需求
            status = stockOutReqFrozenTubeService.checkStockOutSampleByRequirement(id);
        }
        stockOutRequirement.setStatus(status);
        stockOutRequirementRepository.save(stockOutRequirement);
        stockOutRequirementForApply.setId(id);
        stockOutRequirementForApply.setStatus(status);
        return stockOutRequirementForApply;
    }
}
