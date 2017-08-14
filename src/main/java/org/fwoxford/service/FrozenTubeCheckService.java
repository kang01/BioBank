package org.fwoxford.service;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.FrozenTubeRepository;
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

/**
 * Service class for checking tubes.
 */
@Service
@Transactional
public class FrozenTubeCheckService {

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    private final Logger log = LoggerFactory.getLogger(FrozenTubeCheckService.class);


    public void checkSampleCodeRepeat(List<String> sampleCodeStr, Map<String, FrozenTube> frozenTubeMap,FrozenBox frozenBox) {
        if(sampleCodeStr.size()==0){
            return;
        }
        ArrayList<String> repeatCode = new ArrayList<>();
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(sampleCodeStr,frozenBox.getProjectCode(),frozenBox.getSampleType().getId(), Constants.INVALID);
        for(FrozenTube tube :frozenTubeList){
            String sampleCode = tube.getSampleCode()!=null?tube.getSampleCode():tube.getSampleTempCode();
            FrozenTube frozenTube = frozenTubeMap.get(sampleCode);
            if(frozenTube.getSampleClassification()!=null&&tube.getSampleClassification()!=null){
                if((frozenTube.getId()==null && frozenTube.getSampleClassification().getId()==tube.getSampleClassification().getId())
                    ||(frozenTube.getId()!=null
                    && frozenTube.getId()!=tube.getId()
                    &&!frozenTube.getId().equals(tube.getId())
                    &&frozenTube.getSampleClassification().getId()!=tube.getSampleClassification().getId()
                    &&!frozenTube.getSampleClassification().getId().equals(tube.getSampleClassification().getId()))){
                    repeatCode.add(sampleCode);
                }
            }else{
                if(frozenTube.getId()==null
                    || (frozenTube.getId() != null && frozenTube.getId()!=tube.getId()
                    && !frozenTube.getId().equals(tube.getId()))){
                    repeatCode.add(sampleCode);
                }
            }
        }
        if(repeatCode.size()>0){
            throw new BankServiceException("请勿提交重复的样本编码！",String.join(",",repeatCode));
        }
    }
}
