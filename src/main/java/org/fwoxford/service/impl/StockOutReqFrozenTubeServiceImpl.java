package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.service.dto.StockOutReqFrozenTubeDTO;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;
import org.fwoxford.service.mapper.StockOutReqFrozenTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private FrozenBoxRepository frozenBoxRepository;

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
    public String checkStockOutSampleByAppointedSample(List<StockOutRequiredSampleDTO> stockOutRequiredSamples, StockOutRequirement stockOutRequirement) {
        if(stockOutRequirement == null){
            throw new BankServiceException("需求不能为空！");
        }
        List<Long> projectIds = stockOutApplyProjectRepository.findProjectByStockRequirementId(stockOutRequirement.getId());
        String status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;
        int i = 0;
        // All stock out samples
        List<Object> outTubeList = frozenTubeRepository.findAllStockOutFrozenTube();

        // required sample group by sample type and frozenBoxCode
        Map<String,List<StockOutRequiredSampleDTO>> requirementGroupBySampleType =
            stockOutRequiredSamples.stream().collect(Collectors.groupingBy(w -> w.getSampleType()+"&"+w.getFrozenBoxId()));

        List<FrozenTube> frozenTubeListLast = new ArrayList<FrozenTube>();

        for(String key:requirementGroupBySampleType.keySet()){

            String sampleType = key.split("&")[0];
            Long frozenBoxId = !key.split("&")[1].equals(null)?Long.valueOf(key.split("&")[1]):0L;
            List<StockOutRequiredSampleDTO> stockOutRequiredSampleList = requirementGroupBySampleType.get(key);

            //每次取1000支

            List<List<StockOutRequiredSampleDTO>> arrReqSamples = Lists.partition(stockOutRequiredSampleList, 1000);

            for(int index = 0; index < arrReqSamples.size(); index += 10){
                int start = index;
                String[] s1 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s2 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s3 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s4 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s5 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s6 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s7 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s8 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s9 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s10 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                List<FrozenTube> frozenTubeList = frozenTubeRepository.
                    findBySampleTypeCodeAndProjectInAndSampleCodeInAndFrozenBoxId(
                        sampleType,projectIds
                        , Arrays.asList(s1)
                        , Arrays.asList(s2)
                        , Arrays.asList(s3)
                        , Arrays.asList(s4)
                        , Arrays.asList(s5)
                        , Arrays.asList(s6)
                        , Arrays.asList(s7)
                        , Arrays.asList(s8)
                        , Arrays.asList(s9)
                        , Arrays.asList(s10)
                        , frozenBoxId
                    );
                if (frozenTubeList.stream().anyMatch(ft->outTubeList.contains(ft.getId()))) {
                    throw new BankServiceException("请求的样本不在库存。");
                }

                frozenTubeListLast.addAll(frozenTubeList);
            }
        }
        stockOutRequirement.setCountOfSampleReal(frozenTubeListLast.size());
        if(frozenTubeListLast.size()!=0&&(frozenTubeListLast.size()==stockOutRequiredSamples.size())){
             status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS;
        }else{
            return status;
        }
        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = new ArrayList<>();

        for(FrozenTube frozenTube : frozenTubeListLast){
            StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
            stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE);
            stockOutReqFrozenTube.setStockOutRequirement(stockOutRequirement);
            stockOutReqFrozenTube.setMemo(frozenTube!=null?frozenTube.getMemo():null);
            stockOutReqFrozenTube.setFrozenBox(frozenTube!=null?frozenTube.getFrozenBox():null);
            stockOutReqFrozenTube.setFrozenTube(frozenTube!=null?frozenTube:null);

            stockOutReqFrozenTube.setTubeColumns(frozenTube!=null?frozenTube.getTubeColumns():null);
            stockOutReqFrozenTube.setTubeRows(frozenTube!=null?frozenTube.getTubeRows():null);
            /////////////

            stockOutReqFrozenTube.frozenBoxCode1D(frozenTube.getFrozenBox().getFrozenBoxCode1D())
                .frozenBoxCode(frozenTube.getFrozenBoxCode()).errorType(frozenTube.getErrorType())
                .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(frozenTube.getFrozenTubeState())
                .frozenTubeTypeId(frozenTube.getFrozenTubeType().getId()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                .projectId(frozenTube.getProject()!=null?frozenTube.getProject().getId():null).projectCode(frozenTube.getProjectCode())
                .projectSiteId(frozenTube.getProjectSite()!=null?frozenTube.getProjectSite().getId():null)
                .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassificationId(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getId():null)
                .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode())
                .sampleTypeId(frozenTube.getSampleType()!=null?frozenTube.getSampleType().getId():null)
                .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());

            stockOutReqFrozenTubeList.add(stockOutReqFrozenTube);
            if(stockOutReqFrozenTubeList.size()>=1000){
                stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubeList);
                stockOutReqFrozenTubeList = new ArrayList<>();
            }
        }
        if(stockOutReqFrozenTubeList.size()>0){
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubeList);
        }
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
        Long countOfStockInTube = frozenTubeRepository.countByRequirements(sampleTypeId,samplyClassificationId,
            frozenTubeTypeId,diseaseType,sex,isBloodLipid,isHemolysis,ageMin,ageMax,projectIds);
        stockOutRequirement.setCountOfSampleReal(countOfStockInTube.intValue());
        if(countOfStockInTube.intValue()<countOfSample){
            return Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;
//            throw new BankServiceException("库存不足！满足需求的样本量有"+countOfStockInTube+"支,请修改需求！");
        }
        List<FrozenTube> checkedFrozenTubeList = new ArrayList<FrozenTube>();
        for(int i=0;;i+=1000){
            //查询全部的样本--先取1000条
            int length = 1000;
            if(countOfSample<1000){
                length=countOfSample;
            }
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findByRequirements(sampleTypeId,samplyClassificationId,
                    frozenTubeTypeId,diseaseType,sex,isBloodLipid,isHemolysis,ageMin,ageMax,projectIds,i,length);
           if(frozenTubeList.size()<countOfSample){
               checkedFrozenTubeList.addAll(frozenTubeList);
               countOfSample=countOfSample-frozenTubeList.size();
               continue;
           }
           checkedFrozenTubeList.addAll(frozenTubeList.subList(0,countOfSample));
           break;
        }
        List<StockOutReqFrozenTube> stockOutReqFrozenTubes =  new ArrayList<StockOutReqFrozenTube>();
        int i=0;
        for(FrozenTube frozenTube :checkedFrozenTubeList){
            StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube();
            stockOutReqFrozenTube.setStatus(Constants.STOCK_OUT_SAMPLE_IN_USE);
            stockOutReqFrozenTube.setStockOutRequirement(stockOutRequirement);
            stockOutReqFrozenTube.setMemo(frozenTube.getMemo());
            stockOutReqFrozenTube.setFrozenBox(frozenTube.getFrozenBox());
            stockOutReqFrozenTube.setFrozenTube(frozenTube);
            stockOutReqFrozenTube.setTubeColumns(frozenTube.getTubeColumns());
            stockOutReqFrozenTube.setTubeRows(frozenTube.getTubeRows());
            stockOutReqFrozenTube.frozenBoxCode1D(frozenTube.getFrozenBox().getFrozenBoxCode1D())
                .frozenBoxCode(frozenTube.getFrozenBoxCode()).errorType(frozenTube.getErrorType())
                .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(frozenTube.getFrozenTubeState())
                .frozenTubeTypeId(frozenTube.getFrozenTubeType().getId()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                .projectId(frozenTube.getProject()!=null?frozenTube.getProject().getId():null).projectCode(frozenTube.getProjectCode())
                .projectSiteId(frozenTube.getProjectSite()!=null?frozenTube.getProjectSite().getId():null)
                .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassificationId(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getId():null)
                .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode())
                .sampleTypeId(frozenTube.getSampleType()!=null?frozenTube.getSampleType().getId():null)
                .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
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

    @Override
    public String checkStockOutSampleByAppointedSampleOrAppointedBox(List<JSONObject> jsonArray, StockOutRequirement stockOutRequirement) {

        if(stockOutRequirement == null){
            throw new BankServiceException("需求不能为空！");
        }
        List<Long> projectIds = stockOutApplyProjectRepository.findProjectByStockRequirementId(stockOutRequirement.getId());
        String status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT;

        // All stock out samples
        List<Object> outTubeList = frozenTubeRepository.findAllStockOutFrozenTube();


        //整盒出库的需求样本List
        List<StockOutRequiredSampleDTO> stockOutRequiredSampleListByAppointBox = new ArrayList<>();
        //指定样本编码的需求样本List
        List<StockOutRequiredSampleDTO> stockOutRequiredSampleListByAppointSample = new ArrayList<>();

        for(int i=0;i<jsonArray.size();i++){
            StockOutRequiredSampleDTO stockOutRequiredSample = new StockOutRequiredSampleDTO();
            String boxCode1D = jsonArray.get(i).getString("frozenBoxCode1D");
            String type = jsonArray.get(i).getString("type");
            String sampleCode = jsonArray.get(i).getString("code");

            stockOutRequiredSample.setFrozenBoxCode1D(boxCode1D);
            stockOutRequiredSample.setSampleCode(sampleCode);
            stockOutRequiredSample.setSampleType(type);
            if(!StringUtils.isEmpty(boxCode1D)){
                Object frozenBoxId = frozenBoxRepository.findIdByFrozenBoxCode(boxCode1D);
                if(frozenBoxId==null){
                    throw new BankServiceException("冻存盒"+boxCode1D+"不在库存中！请重新上传需求！");
                }
                stockOutRequiredSample.setFrozenBoxId(Long.valueOf(frozenBoxId.toString()));
            }
            if(!StringUtils.isEmpty(boxCode1D) && StringUtils.isEmpty(sampleCode)){
                stockOutRequiredSampleListByAppointBox.add(stockOutRequiredSample);

            }else{
                stockOutRequiredSampleListByAppointSample.add(stockOutRequiredSample);
            }
        }

        //核对整盒申请出库的需求冻存盒
        Map<String, List<StockOutRequiredSampleDTO>> boxListGroupByType =
            stockOutRequiredSampleListByAppointBox.stream().collect(Collectors.groupingBy(w -> w.getSampleType()));
        List<FrozenTube> frozenTubeListLast = new ArrayList<>();
        for(String type :boxListGroupByType.keySet()){
            List<StockOutRequiredSampleDTO> boxCodeListGroupByType = boxListGroupByType.get(type);
            List<String> codeList = new ArrayList<>();
            boxCodeListGroupByType.forEach(s->codeList.add(s.getFrozenBoxCode1D()));
            List<List<String>> boxCodeListEach1000 = Lists.partition(codeList, 1000);
            for( List<String> code : boxCodeListEach1000){
                //整盒出库可以指定到某一个冻存盒，所以可以获取指定冻存盒的Id
                List<FrozenTube> sampleList = frozenTubeRepository.findByFrozenBoxCode1DInOrFrozenBoxCodeInAndSampleTypeAndProjectIdsIn(code,type,projectIds);
                if(sampleList==null || sampleList.size()==0){
                    throw new BankServiceException("核对样本无效！请重新上传需求！");
                }
                for(FrozenTube f : sampleList){
                    for(Object object: outTubeList){
                        if(Long.valueOf(object.toString()).equals(f.getId())){
                            throw new BankServiceException("请求的样本不在库存。");
                        }
                    }
                }
//                if (sampleList.stream().anyMatch(ft->outTubeList.contains(ft.getId()))) {
//                    throw new BankServiceException("请求的样本不在库存。");
//                }
                frozenTubeListLast.addAll(sampleList);
            }
        }

        //核对指定的样本编码
        // required sample group by sample type and frozenBoxCode
        Map<String,List<StockOutRequiredSampleDTO>> requirementGroupBySampleType =
            stockOutRequiredSampleListByAppointSample.stream().collect(Collectors.groupingBy(w -> w.getSampleType()+"&"+w.getFrozenBoxId()));
        for(String key:requirementGroupBySampleType.keySet()){

            String sampleType = key.split("&")[0];
            Long frozenBoxId = !key.split("&")[1].equals("null")&&!key.split("&")[1].equals(null)?Long.valueOf(key.split("&")[1]):0L;
            List<StockOutRequiredSampleDTO> stockOutRequiredSampleListEachSampleType = requirementGroupBySampleType.get(key);

            //每次取1000支

            List<List<StockOutRequiredSampleDTO>> arrReqSamples = Lists.partition(stockOutRequiredSampleListEachSampleType, 1000);

            for(int index = 0; index < arrReqSamples.size(); index += 10){
                int start = index;
                String[] s1 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s2 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s3 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s4 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s5 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s6 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s7 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s8 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s9 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                String[] s10 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getSampleCode()).toArray(s->new String[s]);
                List<FrozenTube> frozenTubeList = frozenTubeRepository.
                    findBySampleTypeCodeAndProjectInAndSampleCodeInAndFrozenBoxId(
                        sampleType,projectIds
                        , Arrays.asList(s1)
                        , Arrays.asList(s2)
                        , Arrays.asList(s3)
                        , Arrays.asList(s4)
                        , Arrays.asList(s5)
                        , Arrays.asList(s6)
                        , Arrays.asList(s7)
                        , Arrays.asList(s8)
                        , Arrays.asList(s9)
                        , Arrays.asList(s10)
                        , frozenBoxId
                    );
//                if (frozenTubeList.stream().anyMatch(ft->outTubeList.contains(ft.getId()))) {
//                    throw new BankServiceException("请求的样本不在库存。");
//                }

                for(FrozenTube f : frozenTubeList){
                    for(Object object: outTubeList){
                        if(Long.valueOf(object.toString()).equals(f.getId())){
                            throw new BankServiceException("请求的样本不在库存。");
                        }
                    }
                }

                int countOfAppointSampleSize = 0;
                if(!Arrays.asList(s1).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s1).size();
                }
                if(!Arrays.asList(s2).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s2).size();
                }
                if(!Arrays.asList(s3).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s3).size();
                }
                if(!Arrays.asList(s4).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s4).size();
                }
                if(!Arrays.asList(s5).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s5).size();
                }
                if(!Arrays.asList(s6).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s6).size();
                }
                if(!Arrays.asList(s7).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s7).size();
                }
                if(!Arrays.asList(s8).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s8).size();
                }
                if(!Arrays.asList(s9).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s9).size();
                }
                if(!Arrays.asList(s10).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s10).size();
                }
                if(frozenTubeList.size()>countOfAppointSampleSize){
                    frozenTubeList = frozenTubeList.subList(0,countOfAppointSampleSize);
                }
                frozenTubeListLast.addAll(frozenTubeList);
            }
        }
        stockOutRequirement.setCountOfSampleReal(frozenTubeListLast.size());
        if(frozenTubeListLast.size()!=0&&(frozenTubeListLast.size()==stockOutRequirement.getCountOfSample())){
            status = Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS;
        }else{
            return status;
        }

        List<StockOutReqFrozenTube> stockOutReqFrozenTubeList = new ArrayList<>();

        for(FrozenTube frozenTube : frozenTubeListLast){
            StockOutReqFrozenTube stockOutReqFrozenTube =new StockOutReqFrozenTube()
                .status(Constants.STOCK_OUT_SAMPLE_IN_USE).stockOutRequirement(stockOutRequirement).memo(frozenTube!=null?frozenTube.getMemo():null)
                .frozenBox(frozenTube!=null?frozenTube.getFrozenBox():null).frozenTube(frozenTube!=null?frozenTube:null)
                .tubeColumns(frozenTube!=null?frozenTube.getTubeColumns():null).tubeRows(frozenTube!=null?frozenTube.getTubeRows():null).frozenBoxCode1D(frozenTube.getFrozenBox().getFrozenBoxCode1D())
                .frozenBoxCode(frozenTube.getFrozenBoxCode()).errorType(frozenTube.getErrorType())
                .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(frozenTube.getFrozenTubeState())
                .frozenTubeTypeId(frozenTube.getFrozenTubeType().getId()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                .projectId(frozenTube.getProject()!=null?frozenTube.getProject().getId():null).projectCode(frozenTube.getProjectCode())
                .projectSiteId(frozenTube.getProjectSite()!=null?frozenTube.getProjectSite().getId():null)
                .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassificationId(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getId():null)
                .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode())
                .sampleTypeId(frozenTube.getSampleType()!=null?frozenTube.getSampleType().getId():null)
                .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());

            stockOutReqFrozenTubeList.add(stockOutReqFrozenTube);
            if(stockOutReqFrozenTubeList.size()>=1000){
                stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubeList);
                stockOutReqFrozenTubeList = new ArrayList<>();
            }
        }
        if(stockOutReqFrozenTubeList.size()>0){
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubeList);
        }
        return status;
    }
}
