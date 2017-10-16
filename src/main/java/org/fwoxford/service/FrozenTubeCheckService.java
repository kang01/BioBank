package org.fwoxford.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
}
