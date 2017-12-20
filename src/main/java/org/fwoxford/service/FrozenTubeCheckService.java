package org.fwoxford.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.repository.TranshipTubeRepository;
import org.fwoxford.service.dto.TranshipTubeDTO;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for checking tubes.
 */
@Service
@Transactional
public class FrozenTubeCheckService {

    @Autowired
    FrozenTubeRepository frozenTubeRepository;
    @Autowired
    TranshipTubeRepository transhipTubeRepository;

    private final Logger log = LoggerFactory.getLogger(FrozenTubeCheckService.class);


    public void checkSampleCodeRepeat(List<String> sampleCodeStr, Map<List<String>, FrozenTube> frozenTubeMap,FrozenBox frozenBox) {
        if(sampleCodeStr.size()==0){
            return;
        }
        List<FrozenTube> frozenTubeListForCheckRepeat = new ArrayList<FrozenTube>();
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(sampleCodeStr,frozenBox.getProjectCode(),frozenBox.getSampleType().getId(), Constants.INVALID);

        for(FrozenTube tube :frozenTubeList){
            String sampleCode = tube.getSampleCode()!=null?tube.getSampleCode():tube.getSampleTempCode();
            List<String> stringList = new ArrayList<>();
            String sampleClassificationCode = "-1";
            if(tube.getSampleClassification()!=null){
                sampleClassificationCode = tube.getSampleClassification().getSampleClassificationCode();
            }
            stringList.add(0,sampleCode);stringList.add(1,sampleClassificationCode);
            FrozenTube frozenTube = frozenTubeMap.get(stringList);
            if(frozenTube != null){
                if(frozenTube.getSampleClassification()!=null&&tube.getSampleClassification()!=null){//有分类
                    //新增时，分类相同不能保存
                    if(frozenTube.getId()==null&&frozenTube.getSampleClassification().getId()==tube.getSampleClassification().getId()){
                        frozenTubeListForCheckRepeat.add(frozenTube);
                    }
                    //修改时，分类相同，ID不同，不能保存
                    if(frozenTube.getId()!=null&&!frozenTube.getId().equals(tube.getId())
                        &&frozenTube.getSampleClassification().getId().equals(tube.getSampleClassification().getId())){
                        frozenTubeListForCheckRepeat.add(frozenTube);
                    }
                }else{//无分类  在给项目必须配置样本分类以后不会出现这样情况
                    if(frozenTube.getId()==null || (!frozenTube.getId().equals(null )&& !frozenTube.getId().equals(tube.getId()))){
                        frozenTubeListForCheckRepeat.add(frozenTube);
                    }
                }
            }

        }
        JSONArray jsonArray = new JSONArray();
        for(FrozenTube f:frozenTubeListForCheckRepeat){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",f.getId());
                jsonObject.put("sampleCode",f.getSampleCode()!=null?f.getSampleCode():f.getSampleTempCode());
                jsonObject.put("tubeColumns",f.getTubeColumns());
                jsonObject.put("tubeRows",f.getTubeRows());
                jsonArray.add(jsonObject);
        }
        if(frozenTubeListForCheckRepeat.size()>0){
            throw new BankServiceException("请勿提交重复的样本编码！",jsonArray.toString());
        }
    }

    public void checkSampleCodeRepeatForReturnBack(List<TranshipTubeDTO> transhipTubeDTOS, List<TranshipTubeDTO> transhipTubeDTOSForCheckAndSave, Tranship tranship) {
        List<TranshipTubeDTO> repeatSampleList = new ArrayList<>();
        for(TranshipTubeDTO tubeDTO : transhipTubeDTOS){
            TranshipTubeDTO transhipTubeDTOFormStockOut = transhipTubeDTOSForCheckAndSave.stream().filter(s->s.getSampleCode().equals(tubeDTO.getSampleCode())).findFirst().orElse(null);
            if(transhipTubeDTOFormStockOut ==  null){
                repeatSampleList.add(tubeDTO);
            }
        }
        throwExceptionByTranshipTubeDTO(repeatSampleList);

        //在库存中验证是否重复
        List<String> sampleCodeStr = transhipTubeDTOS.stream().map(s->s.getSampleCode()).collect(Collectors.toList());
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeInAndProjectCode(sampleCodeStr,tranship.getProjectCode());
        for(TranshipTubeDTO tubeDTO :transhipTubeDTOS){
            for(FrozenTube tube : frozenTubeList){
                if(tubeDTO.getSampleCode().equals(tube.getSampleCode())&& tubeDTO.getSampleTypeCode().equals(tube.getSampleTypeCode())){
                    if((tubeDTO.getFrozenTubeId() == null || (tubeDTO.getFrozenTubeId() != null && tubeDTO.getFrozenTubeId() == tube.getId()))){
                        if(!tube.getFrozenTubeState().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)){
                            if(!repeatSampleList.contains(tubeDTO)){
                                repeatSampleList.add(tubeDTO);
                            }
                        }
                    }
                }
            }
        }
        throwExceptionByTranshipTubeDTO(repeatSampleList);
        //在转运中验证是否有归还中的
        List<TranshipTube> transhipTubes = transhipTubeRepository.findBySampleCodeInAndFrozenTubeStateInAndStatusNot(sampleCodeStr
                ,new ArrayList<String>(){{add(Constants.FROZEN_BOX_RETURN_BACK);}},Constants.INVALID);
        for(TranshipTubeDTO tubeDTO :transhipTubeDTOS){
            for(TranshipTube tube : transhipTubes){
                if(tubeDTO.getSampleCode().equals(tube.getSampleCode())&& tubeDTO.getSampleTypeCode().equals(tube.getSampleTypeCode())){
                    if (tubeDTO.getFrozenTubeId() == null || (tubeDTO.getFrozenTubeId()!=null && tubeDTO.getFrozenTubeId()== tube.getFrozenTube().getId())){
                        if(!repeatSampleList.contains(tubeDTO)){
                            repeatSampleList.add(tubeDTO);
                        }
                    }
                }
            }
        }
        throwExceptionByTranshipTubeDTO(repeatSampleList);
    }

    public void throwExceptionByTranshipTubeDTO(List<TranshipTubeDTO> repeatSampleList) {
        JSONArray jsonArray = new JSONArray();
        for(TranshipTubeDTO transhipTubeDTO :repeatSampleList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",transhipTubeDTO.getId());
            jsonObject.put("sampleCode",transhipTubeDTO.getSampleCode());
            jsonObject.put("tubeColumns",transhipTubeDTO.getTubeColumns());
            jsonObject.put("tubeRows",transhipTubeDTO.getTubeRows());
            jsonArray.add(jsonObject);
        }
        if(jsonArray.size()>0){
            throw new BankServiceException("盒内有不是此次申请出库的冻存管，不能保存！",jsonArray.toString());
        }
    }
}
