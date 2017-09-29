package org.fwoxford.service.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.StockOutReqFrozenTube;
import org.fwoxford.domain.StockOutTaskFrozenTube;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.StockOutReqFrozenTubeRepository;
import org.fwoxford.repository.StockOutTaskFrozenTubeRepository;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing FrozenTube.
 */
@Service
@Transactional
public class FrozenTubeServiceImpl implements FrozenTubeService{

    private final Logger log = LoggerFactory.getLogger(FrozenTubeServiceImpl.class);

    private final FrozenTubeRepository frozenTubeRepository;

    private final FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    public FrozenTubeServiceImpl(FrozenTubeRepository frozenTubeRepository, FrozenTubeMapper frozenTubeMapper) {
        this.frozenTubeRepository = frozenTubeRepository;
        this.frozenTubeMapper = frozenTubeMapper;
    }

    /**
     * Save a frozenTube.
     *
     * @param frozenTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenTubeDTO save(FrozenTubeDTO frozenTubeDTO) {
        log.debug("Request to save FrozenTube : {}", frozenTubeDTO);
        FrozenTube frozenTube = frozenTubeMapper.frozenTubeDTOToFrozenTube(frozenTubeDTO);
        frozenTube = frozenTubeRepository.save(frozenTube);
        FrozenTubeDTO result = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);
        return result;
    }

    /**
     *  Get all the frozenTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenTubes");
        Page<FrozenTube> result = frozenTubeRepository.findAll(pageable);
        return result.map(frozenTube -> frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube));
    }

    /**
     *  Get one frozenTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenTubeDTO findOne(Long id) {
        log.debug("Request to get FrozenTube : {}", id);
        FrozenTube frozenTube = frozenTubeRepository.findOne(id);
        FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(frozenTube);
        frozenTubeDTO.setSampleClassificationName(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getSampleClassificationName():null);
        if(frozenTube.getFrozenBox()!=null){
            FrozenBox frozenBox = frozenBoxRepository.findOne(frozenTube.getFrozenBox().getId());
            String position = BankUtil.getPositionString(frozenBox);
            frozenTubeDTO.setPosition(position);
        }
        return frozenTubeDTO;
    }

    /**
     *  Delete the  frozenTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenTube : {}", id);
        frozenTubeRepository.delete(id);
    }
    /**
     * 根据冻存盒id查询冻存管信息
     * @param frozenBoxId 冻存盒id
     */
    @Override
    public List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId) {
        log.debug("Request to findFrozenTubeListByBoxId : {}", frozenBoxId);
        return frozenTubeRepository.findFrozenTubeListByBoxId(frozenBoxId);
    }
    /**
     * 根据冻存盒Code查询冻存管信息
     * @param frozenBoxCode 冻存盒Code
     */
    @Override
    public List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode) {
        log.debug("Request to findFrozenTubeListByBoxCode : {}", frozenBoxCode);
        return frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBoxCode);
    }

    /**
     * 批量保存冻存管
     * @param frozenTubeDTOList
     */
    @Override
    public List<FrozenTube> saveBatch(List<FrozenTubeDTO> frozenTubeDTOList) {
        Assert.notNull(frozenTubeDTOList,"frozen Tube must not be null");
        List<FrozenTube> frozenTubes = frozenTubeMapper.frozenTubeDTOsToFrozenTubes(frozenTubeDTOList);
//        List<FrozenTube> frozenTubesList =  frozenTubeRepository.save(frozenTubes);

        List<FrozenTube> frozenTubesList = new ArrayList<>();
        for(FrozenTube frozenTube : frozenTubes){
            FrozenTube tube = frozenTubeRepository.save(frozenTube);
            frozenTubesList.add(tube);
        }

        return frozenTubesList;
    }

    /**
     * 根据冻存盒编码和任务ID查询出库样本
     * @param frozenBoxCode
     * @param id
     * @return
     */
    @Override
    public FrozenBoxAndFrozenTubeResponse getFrozenTubeByFrozenBoxCode(String frozenBoxCode,Long id) {
        FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse = new FrozenBoxAndFrozenTubeResponse();
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        if(frozenBox == null){
            throw new BankServiceException("冻存盒不存在！");
        }
        List<FrozenTubeDTO> frozenTubeResponses = new ArrayList<FrozenTubeDTO>();
        //根据冻存盒编码查询冻存管
        List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBoxCode);
        //根据冻存盒查询要出库的样本
        List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutTaskIdAndFrozenBoxId(id,frozenBox.getId());
//        List<StockOutTaskFrozenTube> stockOutFrozenTubes = stockOutTaskFrozenTubeRepository.findByFrozenBoxAndTask(frozenBoxCode,id);
        for(FrozenTube f:frozenTubes){
            FrozenTubeDTO frozenTubeResponse = frozenTubeMapper.frozenTubeToFrozenTubeDTO(f);
            for(StockOutReqFrozenTube s :stockOutReqFrozenTubes){
                if(s.getFrozenTube().getId().equals(f.getId())){
                    if(s.getStatus().equals(Constants.STOCK_OUT_FROZEN_TUBE_CANCEL)){
                        frozenTubeResponse.setStockOutFlag(Constants.TUBE_CANCEL);
                        frozenTubeResponse.setRepealReason(s.getRepealReason());
                    }else{
                        frozenTubeResponse.setStockOutFlag(Constants.YES);
                    }

                }
            }
            frozenTubeResponses.add(frozenTubeResponse);
        }
        frozenBoxAndFrozenTubeResponse = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox);
        frozenBoxAndFrozenTubeResponse.setFrozenTubeDTOS(frozenTubeResponses);
        //构造返回结果
        return frozenBoxAndFrozenTubeResponse;
    }

    @Override
    public List<FrozenTubeDTO> findFrozenTubeBySampleCodeAndProjectAndSampleTypeAndSampleClassifacition(String sampleCode, String projectCode, Long sampleTypeId, Long sampleClassificationId) {
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeIdAndSampleClassitionId(sampleCode,projectCode,sampleTypeId,sampleClassificationId);
        for(FrozenTube f:frozenTubeList){
            if(!StringUtils.isEmpty(f.getFrozenTubeState())&&(!f.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
                &&!f.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER))){
                throw new BankServiceException("冻存管编码"+sampleCode+"已经在库存内，请输入新的冻存管编码！");
            }
        }
        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<FrozenTubeDTO>();
        for(FrozenTube f: frozenTubeList){
            FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(f);
            frozenTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            frozenTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            frozenTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            frozenTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            frozenTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            frozenTubeDTO.setSampleClassificationName(f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationName():null);
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        return frozenTubeDTOS;
    }

    @Override
    public List<FrozenTubeDTO> getFrozenTubeByIds(String ids) {
        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<FrozenTubeDTO>();
        if(StringUtils.isEmpty(ids)){
            throw new BankServiceException("冻存管ID不能为空！");
        }
        String [] frozenTubeIds = ids.split(",");
        for(String id :frozenTubeIds){
            FrozenTube f = frozenTubeRepository.findOne(Long.valueOf(id));
            if(f == null || (f!= null&&f.getStatus().equals(Constants.INVALID))){
                throw new BankServiceException("冻存管不存在！");
            }
            FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(f);
            String sampleCode = frozenTubeDTO.getSampleCode()!=null?frozenTubeDTO.getSampleCode():frozenTubeDTO.getSampleTempCode();
            frozenTubeDTO.setSampleCode(sampleCode);
            frozenTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            frozenTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            frozenTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            frozenTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            frozenTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            frozenTubeDTO.setSampleClassificationName(f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationName():null);
            frozenTubeDTO.setSampleClassificationCode(f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationCode():null);
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        return frozenTubeDTOS;
    }

    @Override
    public List<FrozenTubeDTO> getFrozenTubeBySampleCode(String sampleCode, String projectCode, Long sampleTypeId) {
        List<String> sampleCodeStr = new ArrayList<>();
        sampleCodeStr.add(sampleCode);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(sampleCodeStr,projectCode,sampleTypeId,Constants.INVALID);

        for(FrozenTube f:frozenTubeList){
            if(!StringUtils.isEmpty(f.getFrozenTubeState())&&(!f.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED
            )&&!f.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER))){
                throw new BankServiceException("冻存管编码"+sampleCode+"已经在库存内，请输入新的冻存管编码！");
            }
        }
        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<FrozenTubeDTO>();
        for(FrozenTube f: frozenTubeList){
            FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(f);
            frozenTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            frozenTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            frozenTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            frozenTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            frozenTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            frozenTubeDTO.setSampleClassificationName(f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationName():null);
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        return frozenTubeDTOS;
    }
    @Override
    public List<FrozenTubeDTO> findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(String sampleCode, String projectCode, Long frozenBoxId, Long sampleTypeId, Long sampleClassificationId) {
        List<FrozenTube> frozenTubeList = new ArrayList<FrozenTube>();
        if(frozenBoxId == -1 || frozenBoxId.equals(-1)){
            if(sampleClassificationId == -1 || sampleClassificationId.equals(-1) ){
                List<String> sampleCodeStr = new ArrayList<>();
                sampleCodeStr.add(sampleCode);
                frozenTubeList = frozenTubeRepository.findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(sampleCodeStr,projectCode,sampleTypeId,Constants.INVALID);
            }else{
                frozenTubeList = frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeIdAndSampleClassitionId(sampleCode,projectCode,sampleTypeId,sampleClassificationId);
            }
        }else{
            if(sampleClassificationId == -1 || sampleClassificationId.equals(-1) ){
                frozenTubeList = frozenTubeRepository.findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleType(sampleCode,projectCode,frozenBoxId,sampleTypeId);
            }else{
                frozenTubeList = frozenTubeRepository.findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(sampleCode,projectCode,frozenBoxId,sampleTypeId,sampleClassificationId);
            }}

        for(FrozenTube f:frozenTubeList){
            if(!StringUtils.isEmpty(f.getFrozenTubeState())
                &&(!f.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)&&!f.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER))){
                throw new BankServiceException("冻存管编码"+sampleCode+"已经在库存内，请输入新的冻存管编码！");
            }
        }
        List<FrozenTubeDTO> frozenTubeDTOS = new ArrayList<FrozenTubeDTO>();

        for(FrozenTube f: frozenTubeList){
            FrozenTubeDTO frozenTubeDTO = frozenTubeMapper.frozenTubeToFrozenTubeDTO(f);
            frozenTubeDTO.setSampleCode(StringUtils.isEmpty(frozenTubeDTO.getSampleCode())?frozenTubeDTO.getSampleTempCode():frozenTubeDTO.getSampleCode());
            frozenTubeDTO.setFrontColor(f.getSampleType()!=null?f.getSampleType().getFrontColor():null);
            frozenTubeDTO.setFrontColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getFrontColor():null);
            frozenTubeDTO.setBackColor(f.getSampleType()!=null?f.getSampleType().getBackColor():null);
            frozenTubeDTO.setBackColorForClass(f.getSampleClassification()!=null?f.getSampleClassification().getBackColor():null);
            frozenTubeDTO.setIsMixed(f.getSampleType()!=null?f.getSampleType().getIsMixed():null);
            frozenTubeDTO.setSampleClassificationName(f.getSampleClassification()!=null?f.getSampleClassification().getSampleClassificationName():null);
            frozenTubeDTOS.add(frozenTubeDTO);
        }
        return frozenTubeDTOS;
    }
}
