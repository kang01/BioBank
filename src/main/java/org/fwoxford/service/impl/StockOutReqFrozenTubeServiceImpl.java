package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.repository.StockOutApplyProjectRepository;
import org.fwoxford.repository.StockOutRequirementRepository;
import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.repository.StockOutReqFrozenTubeRepository;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import org.fwoxford.service.mapper.StockOutReqFrozenTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    private StockOutApplyProjectRepository stockOutApplyProjectRepository;

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

    /**
     * 核对指定样本
     * @param stockOutRequiredSamples
     * @param stockOutRequirement
     * @return
     */
    @Override
    public String checkStockOutSampleByAppointedSample(List<StockOutRequiredSample> stockOutRequiredSamples, StockOutRequirement stockOutRequirement) {
        if(stockOutRequirement == null){
            throw new BankServiceException("需求不能为空！");
        }
        List<Long> projectIds = stockOutApplyProjectRepository.findProjectByStockRequirementId(stockOutRequirement.getId());
        String status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS;
        int i = 0;
        List<Object[]> outTubeList = frozenTubeRepository.findAllStockOutFrozenTube();
        Map<String,List<StockOutRequiredSample>> requirementGroupBySampleType =
            stockOutRequiredSamples.stream().collect(Collectors.groupingBy(w -> w.getSampleType()));
        List<FrozenTube> frozenTubeListLast = new ArrayList<FrozenTube>();
        System.out.print("----beginTime:"+new Date());
        for(String sampleType:requirementGroupBySampleType.keySet()){

            List<StockOutRequiredSample> stockOutRequiredSampleList = requirementGroupBySampleType.get(sampleType);
            //每次取1000支
            List<String> sampleCodeList = new ArrayList<>();
            List<String> sampleTypeCodeList = new ArrayList<>();
            for(StockOutRequiredSample s :stockOutRequiredSamples){
                String appointedSampleCode = s.getSampleCode();
                String appointedSampleType = s.getSampleType();
                if(sampleCodeList.size()==1000){
                    List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeInAndSampleTypeCodeAndProjectIn(sampleCodeList,sampleType,projectIds);

                    frozenTubeList.removeIf(t->{
                        return outTubeList.contains(t.getId());
                    });
                    sampleCodeList = new ArrayList<>();
                    sampleTypeCodeList = new ArrayList<>();
                    frozenTubeList = frozenTubeList.size()>1000?frozenTubeList.subList(0,1000):frozenTubeList;
                    frozenTubeListLast.addAll(frozenTubeList);
                }

                sampleCodeList.add(appointedSampleCode);
                sampleTypeCodeList.add(appointedSampleType);
            }
            if(sampleCodeList.size()>0){
                List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeInAndSampleTypeCodeAndProjectIn(sampleCodeList,sampleType,projectIds);
                frozenTubeList = frozenTubeList.size()>sampleCodeList.size()?frozenTubeList.subList(0,sampleCodeList.size()):frozenTubeList;
                frozenTubeListLast.addAll(frozenTubeList);
            }
        }
        if(frozenTubeListLast.size()<stockOutRequiredSamples.size()){
            status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;
        }
        System.out.print("----entTime:"+new Date());
        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = new ArrayList<>();
        for(StockOutRequiredSample s :stockOutRequiredSamples){
//            String appointedSampleCode = s.getSampleCode();
//            String appointedSampleType = s.getSampleType();
//            List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeAndSampleTypeCodeAndProject(appointedSampleCode,appointedSampleType,projectIds);
//
//            frozenTubeList.removeIf(t->{
//                    return outTubeList.contains(t.getId());
//                });
//            if(frozenTubeList == null || frozenTubeList.size() ==0){
//                status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;
//                continue;
//            }
//            System.out.print("核对第"+i+"个样本，编码为："+appointedSampleCode+"核对结果："+status);
            for(FrozenTube frozenTube : frozenTubeListLast){
                StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
                stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE);
                stockOutReqFrozenTube.setStockOutRequirement(stockOutRequirement);
                stockOutReqFrozenTube.setMemo(frozenTube!=null?frozenTube.getMemo():null);
                stockOutReqFrozenTube.setFrozenBox(frozenTube!=null?frozenTube.getFrozenBox():null);
                stockOutReqFrozenTube.setFrozenTube(frozenTube!=null?frozenTube:null);
                stockOutReqFrozenTube.setImportingSampleId(s.getId());
                stockOutReqFrozenTube.setTubeColumns(frozenTube!=null?frozenTube.getTubeColumns():null);
                stockOutReqFrozenTube.setTubeRows(frozenTube!=null?frozenTube.getTubeRows():null);
                stockOutReqFrozenTubeList.add(stockOutReqFrozenTube);
                if(stockOutReqFrozenTubeList.size()==1000){
                    stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubeList);
                    stockOutReqFrozenTubeList = new ArrayList<>();
                }
//                i++;
            }
        }
        if(stockOutReqFrozenTubeList.size()>0){
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubeList);
        }
        stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubeList);
        stockOutRequirement.setCountOfSampleReal(frozenTubeListLast.size());
        return status;
    }

    /**
     * 核对需求样本
     * @param stockOutRequirement
     * @return
     */
    @Override
    public String checkStockOutSampleByRequirement(StockOutRequirement stockOutRequirement) {
        String status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS;
        if(stockOutRequirement == null){
            throw new BankServiceException("未查询到需求！");
        }
        List<Long> projectIds = stockOutApplyProjectRepository.findProjectByStockRequirementId(stockOutRequirement.getId());

        Integer countOfSample = stockOutRequirement.getCountOfSample();
        Integer sampleTypeId = stockOutRequirement.getSampleType()!=null?stockOutRequirement.getSampleType().getId().intValue():0;
        Integer samplyClassificationId = stockOutRequirement.getSampleClassification()!=null?stockOutRequirement.getSampleClassification().getId().intValue():0;
        Integer ageMin = stockOutRequirement.getAgeMin()!=null?stockOutRequirement.getAgeMin().intValue():0;
        Integer ageMax = stockOutRequirement.getAgeMax()!=null?stockOutRequirement.getAgeMax().intValue():0;
        String diseaseType =stockOutRequirement.getDiseaseType();
        Integer frozenTubeTypeId = stockOutRequirement.getFrozenTubeType()!=null?stockOutRequirement.getFrozenTubeType().getId().intValue():0;
        String sex = stockOutRequirement.getSex();
        Integer isBloodLipid = stockOutRequirement.isIsBloodLipid()!=null&&stockOutRequirement.isIsBloodLipid().equals(true)?1:0;
        Integer isHemolysis = stockOutRequirement.isIsHemolysis()!=null&&stockOutRequirement.isIsHemolysis().equals(true)?1:0;
        //查询已经出库的样本
        List<Object[]> outTubeList = frozenTubeRepository.findAllStockOutFrozenTube();
        List<Object[]> checkedFrozenTubeList = new ArrayList<Object[]>();
        for(int i=0;;i+=1000){
            //查询全部的样本--先取1000条
            List<Object[]> frozenTubeList = frozenTubeRepository.findByRequirements(sampleTypeId,samplyClassificationId,
                frozenTubeTypeId,diseaseType,sex,isBloodLipid,isHemolysis,ageMin,ageMax,i);
            //排除不符合条件的---不在项目里,不是已经出库的
            frozenTubeList.removeIf(t->{
                return outTubeList.contains(t[0])||!projectIds.contains(Long.valueOf(t[1].toString()));
            });

           if(frozenTubeList.size()<countOfSample){
               checkedFrozenTubeList.addAll(frozenTubeList);
               countOfSample=countOfSample-frozenTubeList.size();
               continue;
           }
           checkedFrozenTubeList.addAll(frozenTubeList.subList(0,countOfSample));
           break;
        }
        if(checkedFrozenTubeList.size()<countOfSample){
            status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;
        }

        List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubes = new ArrayList<StockOutReqFrozenTube>();
        int i=0;
        for(Object[] t :checkedFrozenTubeList){
            StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
            stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE);
            stockOutReqFrozenTube.setStockOutRequirement(stockOutRequirement);
            stockOutReqFrozenTube.setMemo(t[5]!=null?t[5].toString():null);
            stockOutReqFrozenTube.setFrozenBox(stockOutReqFrozenTubeMapper.frozenBoxFromId(Long.valueOf(t[2].toString())));
            stockOutReqFrozenTube.setFrozenTube(stockOutReqFrozenTubeMapper.frozenTubeFromId(Long.valueOf(t[0].toString())));
            stockOutReqFrozenTube.setTubeColumns(t[4].toString());
            stockOutReqFrozenTube.setTubeRows(t[3].toString());
            stockOutReqFrozenTubes.add(stockOutReqFrozenTube);
            if(stockOutReqFrozenTubes.size()==2000){
                stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
                stockOutReqFrozenTubes = new ArrayList<StockOutReqFrozenTube>();
            }
            i++;
        }
        if(stockOutReqFrozenTubes.size()>0){
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
        }
        stockOutRequirement.setCountOfSampleReal(i);
        return status;
    }
}
