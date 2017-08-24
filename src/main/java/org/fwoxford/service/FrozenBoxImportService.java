package org.fwoxford.service;

import net.sf.json.JSONArray;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeImportingForm;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gengluying on 2017/8/17.
 */
@Service
@Transactional
public class FrozenBoxImportService {
    private final Logger log = LoggerFactory.getLogger(FrozenBoxImportService.class);
    @Autowired
    private FrozenBoxTypeRepository frozenBoxTypeRepository;
    @Autowired
    private FrozenTubeTypeRepository frozenTubeTypeRepository;
    @Autowired
    private SampleTypeRepository sampleTypeRepository;
    @Autowired
    private SampleClassificationRepository sampleClassificationRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectSampleClassRepository projectSampleClassRepository;

    /**
     * 从excel上传
     * @param projectCode
     * @param file
     * @param request
     * @return
     */
    public List<FrozenBoxAndFrozenTubeResponse> saveAndUploadFrozenBoxAndTube(String projectCode,MultipartFile file, HttpServletRequest request) {
        if(file.isEmpty()){
            throw new BankServiceException("文件不能为空！");
        }
        FrozenBoxType frozenBoxType_DCH = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        if(frozenBoxType_DCH == null){
            throw new BankServiceException("查询冻存盒类型失败！");
        }
        FrozenBoxType frozenBoxType_DJH = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DJH");
        if(frozenBoxType_DJH == null){
            throw new BankServiceException("查询大橘盒类型失败！");
        }
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findTopOne();
        List<SampleType> sampleTypeList = sampleTypeRepository.findAllSampleTypes();
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        List<FrozenBoxAndFrozenTubeResponse> frozenBoxAndFrozenTubeResponses = new ArrayList<>();
        Map<String,List<FrozenTubeResponse>> map = new HashMap<String,List<FrozenTubeResponse>>();
        Map<String,FrozenBoxAndFrozenTubeResponse> boxMap = new HashMap<String,FrozenBoxAndFrozenTubeResponse>();
        try {
            String filetype=file.getOriginalFilename().split("\\.")[1];//后缀
            ArrayList<ArrayList<Object>> arrayLists = ExcelUtils.readExcel(filetype,file.getInputStream());
            List<ArrayList<Object>>  arrayListArrayList = arrayLists.subList(1,arrayLists.size());
            for(List arrayList :arrayListArrayList){
                FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse = new FrozenBoxAndFrozenTubeResponse();
                //第4列为冻存盒，第6列为大橘盒
                Object DCHCode =  arrayList.get(4);
                Object DJHCode =  arrayList.get(6);
                FrozenBoxType frozenBoxType = new FrozenBoxType();
                String frozenBoxCode = new String();
                String sampleCode = new String();
                if(DCHCode!=null){
                    frozenBoxType = frozenBoxType_DCH;
                    frozenBoxCode = DCHCode.toString();
                    if(arrayList.get(3)==null){
                        throw new BankServiceException("获取冻存盒内样本编码失败！");
                    }
                    sampleCode = arrayList.get(3).toString();
                }else if(DJHCode !=null){
                    frozenBoxType = frozenBoxType_DJH;
                    frozenBoxCode = DJHCode.toString();
                    if(arrayList.get(5)==null){
                        throw new BankServiceException("获取大橘盒内样本编码失败！");
                    }
                    sampleCode = arrayList.get(5).toString();
                }else{
                    throw new BankServiceException("从EXCEL中未获取到冻存盒编码");
                }
                frozenBoxAndFrozenTubeResponse.setIsRealData(Constants.YES);
                frozenBoxAndFrozenTubeResponse.setFrozenBoxCode(frozenBoxCode);
                frozenBoxAndFrozenTubeResponse.setFrozenBoxType(frozenBoxType);
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeId(frozenBoxType.getId());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeName(frozenBoxType.getFrozenBoxTypeName());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns());
                List<FrozenTubeResponse> frozenTubeResponses = new ArrayList<FrozenTubeResponse>();
                //截取冻存盒编码获取类型
                String sampleClassificationTypeCode = frozenBoxCode.substring(4,6);
                ProjectSampleClass projectSampleClass = new ProjectSampleClass();
                String pCode = frozenBoxCode.substring(0,4);
                if(!pCode.equals(projectCode)){
                    throw new BankServiceException("转运单的项目与冻存盒所属项目不一致，导入失败！");
                }
                Project project = projectRepository.findByProjectCode(projectCode);
                SampleType sampleType = new SampleType();
                if(project == null){
                    throw new BankServiceException("项目不存在！");
                }
                List<ProjectSampleClass> projectSampleClasses = new ArrayList<ProjectSampleClass>();
                if(sampleClassificationTypeCode.equals("99")){
                    sampleType = sampleTypeList.stream().filter(s->s.getSampleTypeCode().equals("99")).findFirst().orElse(null);
                    projectSampleClasses =  projectSampleClassRepository.findByProjectIdAndSampleTypeId(project.getId(),sampleType.getId());
                    if(projectSampleClasses == null || projectSampleClasses.size()==0){
                        throw new BankServiceException("该项目未配置混合样本分类！");
                    }
                }else{
                    SampleClassification sampleClassification = sampleClassifications.stream().filter(s-> s.getSampleClassificationCode().equals(sampleClassificationTypeCode)).findFirst().orElse(null);
                    if(sampleClassification == null){
                        throw new BankServiceException("样本分类不存在！");
                    }
                    projectSampleClasses = projectSampleClassRepository.findSampleTypeByProjectAndSampleClassification(projectCode,sampleClassificationTypeCode);
                    if(projectSampleClasses == null || projectSampleClasses.size()==0){
                        throw new BankServiceException("该项目未配置样本分类！");
                    }
                    projectSampleClass = projectSampleClasses.get(0);
                    sampleType = projectSampleClass.getSampleType();
                }
                frozenBoxAndFrozenTubeResponse.setProjectId(project.getId());
                frozenBoxAndFrozenTubeResponse.setProjectCode(project.getProjectCode());
                frozenBoxAndFrozenTubeResponse.setProjectName(project.getProjectName());
                frozenBoxAndFrozenTubeResponse.setStatus(Constants.FROZEN_BOX_NEW);
                frozenBoxAndFrozenTubeResponse.setSampleType(sampleType);
                frozenBoxAndFrozenTubeResponse.setSampleTypeId(sampleType.getId());
                frozenBoxAndFrozenTubeResponse.setSampleTypeName(sampleType.getSampleTypeName());
                frozenBoxAndFrozenTubeResponse.setSampleTypeCode(sampleType.getSampleTypeCode());
                frozenBoxAndFrozenTubeResponse.setIsMixed(sampleType.getIsMixed());
                frozenBoxAndFrozenTubeResponse.setBackColor(sampleType.getBackColor());
                frozenBoxAndFrozenTubeResponse.setFrontColor(sampleType.getFrontColor());
                String sampleTypeCode = sampleType.getSampleTypeCode();
                switch (sampleTypeCode){
                    case "99":frozenTubeResponses =  create99Sample(map,frozenBoxCode,sampleType,projectSampleClasses,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "A":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "R":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "E":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "W":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    default:break;
                }

                List<FrozenTubeResponse> frozenTubeResponseList = map.get(frozenBoxCode);
                if(frozenTubeResponseList!=null && frozenTubeResponseList.size()>0){
                    frozenTubeResponseList.addAll(frozenTubeResponses);
                    map.put(frozenBoxCode,frozenTubeResponseList);
                }else{
                    map.put(frozenBoxCode,frozenTubeResponses);
                }

                FrozenBoxAndFrozenTubeResponse boxAndFrozenTubeResponse = boxMap.get(frozenBoxCode);
                if(boxAndFrozenTubeResponse==null ){
                    boxMap.put(frozenBoxCode,frozenBoxAndFrozenTubeResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String key :boxMap.keySet()){
            FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse = boxMap.get(key);
            frozenBoxAndFrozenTubeResponse.setFrozenTubeDTOS(map.get(key));
            frozenBoxAndFrozenTubeResponse.setCountOfSample(map.get(key).size());
            frozenBoxAndFrozenTubeResponses.add(frozenBoxAndFrozenTubeResponse);
        }
        return frozenBoxAndFrozenTubeResponses;
    }

    public List<FrozenTubeResponse> createSample(Map<String, List<FrozenTubeResponse>> map, String frozenBoxCode, SampleType sampleType, ProjectSampleClass projectSampleClass, FrozenBoxAndFrozenTubeResponse boxAndFrozenTubeResponse, FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse, FrozenTubeType frozenTubeType, String sampleCode) {
        frozenBoxAndFrozenTubeResponse.setIsSplit(Constants.NO);
        frozenBoxAndFrozenTubeResponse.setSampleClassification(projectSampleClass.getSampleClassification());
        frozenBoxAndFrozenTubeResponse.setSampleClassificationId(projectSampleClass.getSampleClassification().getId());
        frozenBoxAndFrozenTubeResponse.setSampleClassificationCode(projectSampleClass.getSampleClassificationCode());
        frozenBoxAndFrozenTubeResponse.setSampleClassificationName(projectSampleClass.getSampleClassificationName());
        frozenBoxAndFrozenTubeResponse.setBackColorForClass(projectSampleClass.getSampleClassification().getBackColor());
        frozenBoxAndFrozenTubeResponse.setFrontColorForClass(projectSampleClass.getSampleClassification().getFrontColor());

        List<FrozenTubeResponse> frozenTubeResponses = new ArrayList<FrozenTubeResponse>();
        List<FrozenTubeResponse> frozenTubeResponseList = map.get(frozenBoxCode)!=null?map.get(frozenBoxCode):new ArrayList<FrozenTubeResponse>();
        int i = 0;
        String rowsInTube = "";
        String columnsInTube = "";
        Map<String,FrozenTubeResponse> posMap = new HashMap<>();
        for(FrozenTubeResponse tube :frozenTubeResponseList){
            String rows = tube.getTubeRows();
            String columns = tube.getTubeColumns();
            posMap.put(rows+columns,tube);
        }
        String[][] posStr = new String[10][10];
        Boolean flag = false;
        for(int m = 0; m <posStr.length;m++){
            String row = String.valueOf((char)(m+65));
            if(m>=8){
                row= String.valueOf((char)(m+66));
            }
            for(int n = 0;n <posStr[m].length;n++){
                String column = String.valueOf(n+1);
                if(posMap.get(row+column)!=null&&posMap.get(row+column).getFrozenBoxCode().equals(frozenBoxCode)){
                    continue;
                }else{
                    flag = true;
                    rowsInTube =row;
                    columnsInTube = column;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        FrozenTubeResponse frozenTubeResponse = new FrozenTubeResponse();
        frozenTubeResponse.setStatus(Constants.FROZEN_TUBE_NORMAL);
        frozenTubeResponse.setSampleCode(sampleCode);
        frozenTubeResponse.setFrozenTubeType(frozenTubeType);
        frozenTubeResponse.setFrozenBoxCode(frozenBoxCode);
        frozenTubeResponse.setTubeRows(rowsInTube);
        frozenTubeResponse.setTubeColumns(columnsInTube);
        frozenTubeResponse.setSampleType(sampleType);
        frozenTubeResponse.setSampleTypeId(sampleType.getId());
        frozenTubeResponse.setSampleTypeName(sampleType.getSampleTypeName());
        frozenTubeResponse.setSampleTypeCode(sampleType.getSampleTypeCode());
        frozenTubeResponse.setSampleClassification(projectSampleClass.getSampleClassification());
        frozenTubeResponse.setSampleClassificationId(projectSampleClass.getSampleClassification().getId());
        frozenTubeResponse.setSampleClassificationCode(projectSampleClass.getSampleClassificationCode());
        frozenTubeResponse.setSampleClassificationName(projectSampleClass.getSampleClassificationName());
        frozenTubeResponse.setIsMixed(sampleType.getIsMixed());
        frozenTubeResponse.setBackColor(sampleType.getBackColor());
        frozenTubeResponse.setFrontColor(sampleType.getFrontColor());
        frozenTubeResponse.setBackColorForClass(projectSampleClass.getSampleClassification().getBackColor());
        frozenTubeResponse.setFrontColorForClass(projectSampleClass.getSampleClassification().getFrontColor());
        frozenTubeResponses.add(frozenTubeResponse);
        return frozenTubeResponses;
    }

    public List<FrozenTubeResponse> create99Sample(Map<String, List<FrozenTubeResponse>> map, String frozenBoxCode, SampleType sampleType, List<ProjectSampleClass> projectSampleClasses, FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse, FrozenTubeType frozenTubeType, String sampleCode) {
        frozenBoxAndFrozenTubeResponse.setIsSplit(Constants.YES);
        //从map中获取冻存盒，取盒内的位置
        List<FrozenTubeResponse> frozenTubeResponses = new ArrayList<FrozenTubeResponse>();
        List<FrozenTubeResponse> frozenTubeResponseList = map.get(frozenBoxCode);
        int i = 0;
        if(frozenTubeResponseList==null || frozenTubeResponseList.size()==0){
            i=1;
        }else{
            i = frozenTubeResponseList.size()/10+1;
            if(i>=9){
                i++;
            }
        }
        char c1=(char) (i+64);

        for(int j = 1;j<=10;j++){
            FrozenTubeResponse frozenTubeResponse = new FrozenTubeResponse();
            frozenTubeResponse.setStatus(Constants.FROZEN_TUBE_NORMAL);
            frozenTubeResponse.setSampleCode(sampleCode);
            frozenTubeResponse.setFrozenTubeType(frozenTubeType);
            frozenTubeResponse.setTubeRows(String.valueOf(c1));
            frozenTubeResponse.setTubeColumns(String.valueOf(j));
            frozenTubeResponse.setFrozenBoxCode(frozenBoxCode);
            frozenTubeResponse.setSampleType(sampleType);
            frozenTubeResponse.setSampleTypeId(sampleType.getId());
            frozenTubeResponse.setSampleTypeName(sampleType.getSampleTypeName());
            frozenTubeResponse.setSampleTypeCode(sampleType.getSampleTypeCode());
            String sampleClass = Constants.SAMPLECLASS_MAP.get(String.valueOf(j));
            if(StringUtils.isEmpty(sampleClass)){
                throw new BankServiceException("样本分类获取失败！");
            }
            ProjectSampleClass projectSampleClass =projectSampleClasses.stream().filter(s->s.getSampleClassificationCode().equals(sampleClass)).findFirst().orElse(null);
            frozenTubeResponse.setSampleClassification(projectSampleClass.getSampleClassification());
            frozenTubeResponse.setSampleClassificationId(projectSampleClass.getSampleClassification().getId());
            frozenTubeResponse.setSampleClassificationCode(projectSampleClass.getSampleClassificationCode());
            frozenTubeResponse.setSampleClassificationName(projectSampleClass.getSampleClassificationName());
            frozenTubeResponse.setIsMixed(sampleType.getIsMixed());
            frozenTubeResponse.setBackColor(sampleType.getBackColor());
            frozenTubeResponse.setFrontColor(sampleType.getFrontColor());
            frozenTubeResponse.setBackColorForClass(projectSampleClass.getSampleClassification().getBackColor());
            frozenTubeResponse.setFrontColorForClass(projectSampleClass.getSampleClassification().getFrontColor());
            frozenTubeResponses.add(frozenTubeResponse);
        }
        return frozenTubeResponses;
    }

    /**
     * 从项目组导入样本
     * @param frozenBoxCodeStr
     * @return
     */
    public List<FrozenBoxAndFrozenTubeResponse> importFrozenBoxAndFrozenTube(String frozenBoxCodeStr) {
        List<FrozenBoxAndFrozenTubeResponse> frozenBoxDTOList = new ArrayList<FrozenBoxAndFrozenTubeResponse>();
        String[] frozenBoxCode = frozenBoxCodeStr.split(",");
        for(String code :frozenBoxCode){
            FrozenBoxAndFrozenTubeResponse frozenBoxDTO = new FrozenBoxAndFrozenTubeResponse();
            frozenBoxDTO = getSampleInfo(code);
            frozenBoxDTOList.add(frozenBoxDTO);
        }
        return frozenBoxDTOList;
    }
    public FrozenBoxAndFrozenTubeResponse getSampleInfo(String code) {
        FrozenBoxAndFrozenTubeResponse frozenBoxDTO = new FrozenBoxAndFrozenTubeResponse();
        frozenBoxDTO.setIsSplit(Constants.YES);
        frozenBoxDTO.setIsRealData(Constants.NO);
        frozenBoxDTO.setStatus(Constants.FROZEN_BOX_NEW);
        frozenBoxDTO.setFrozenBoxCode(code);
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        if(frozenBoxType == null){
            throw new BankServiceException("查询冻存盒类型失败！");
        }
        frozenBoxDTO.setFrozenBoxType(frozenBoxType);

        frozenBoxDTO.setFrozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode());
        frozenBoxDTO.setFrozenBoxTypeName(frozenBoxType.getFrozenBoxTypeName());
        frozenBoxDTO.setFrozenBoxTypeId(frozenBoxType.getId());
        frozenBoxDTO.setFrozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows());
        frozenBoxDTO.setFrozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns());


        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://10.24.10.43:8080/biobank/specimens?boxcode="+code);
        HttpMethodParams params = new HttpMethodParams();
//        params.setParameter("boxcode","650420093");
        getMethod.setParams(params);
        getMethod.setRequestHeader("X-Auth-Token", "d93925bc7axKfXc2A5WFJEzuPXYX4q");
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != 200) {
                System.err.println("Method failed: "
                    + getMethod.getStatusLine());
            }
            byte[] responseBody = getMethod.getResponseBody();
            String str = new String(responseBody);
            JSONArray jsonArray = new JSONArray();
            if(!StringUtils.isEmpty(str)){
                jsonArray = JSONArray.fromObject(str);
            }
            FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findTopOne();
            if(frozenTubeType == null){
                throw new BankServiceException("查询冻存管类型失败！");
            }
            List<FrozenTubeResponse> frozenTubeResponses = new ArrayList<FrozenTubeResponse>();
            List<FrozenTubeImportingForm> frozenTubeImportingForms = (List<FrozenTubeImportingForm>) JSONArray.toCollection(jsonArray,FrozenTubeImportingForm.class);
            Map<String,List<FrozenTubeResponse>> map = new HashMap<String,List<FrozenTubeResponse>>();
            for(FrozenTubeImportingForm frozenTubeImportingForm : frozenTubeImportingForms){
                FrozenTubeResponse frozenTubeResponse = new FrozenTubeResponse();
                String status = frozenTubeImportingForm.getIsEmpty();
                if(status.equals("2")){
                    status = Constants.FROZEN_TUBE_NORMAL;
                }else{
                    status = Constants.FROZEN_TUBE_EMPTY;
                }
                frozenTubeResponse.setStatus(status);
                frozenTubeResponse.setPatientId(Long.valueOf(frozenTubeImportingForm.getBloodCode().trim()));
                frozenTubeResponse.setFrozenTubeType(frozenTubeType);
                frozenTubeResponse.setFrozenBoxCode(code);
                frozenTubeResponse.setSampleCode(frozenTubeImportingForm.getSpecimenCode());
                Boolean ishemolysis = true; Boolean isbloodLipid = true;
                String hemolysis = frozenTubeImportingForm.getIsHemolysis();
                String bloodLipid  = frozenTubeImportingForm.getIsLipid();
                if(hemolysis.equals("2")){
                    ishemolysis = false;
                }
                if(bloodLipid.equals("2")){
                    isbloodLipid = false;
                }
                frozenTubeResponse.setHemolysis(ishemolysis);
                frozenTubeResponse.setBloodLipid(isbloodLipid);
                String sampleTypeCode = frozenTubeImportingForm.getSpecimenType();
                SampleType sampleType = sampleTypeRepository.findBySampleTypeCode(sampleTypeCode);
                if(sampleType == null){
                    throw new BankServiceException("样本类型不存在！");
                }
                frozenTubeResponse.setSampleType(sampleType);
                frozenTubeResponse.setSampleTypeId(sampleType.getId());
                frozenTubeResponse.setSampleTypeCode(sampleType.getSampleTypeCode());
                frozenTubeResponse.setSampleTypeName(sampleType.getSampleTypeName());
                frozenTubeResponse.setIsMixed(sampleType.getIsMixed());

                frozenBoxDTO.setSampleType(sampleType);
                frozenBoxDTO.setSampleTypeId(sampleType.getId());
                frozenBoxDTO.setSampleTypeCode(sampleType.getSampleTypeCode());
                frozenBoxDTO.setSampleTypeName(sampleType.getSampleTypeName());
                frozenBoxDTO.setIsMixed(sampleType.getIsMixed());

                frozenTubeResponse.setTubeColumns(frozenTubeImportingForm.getColOfSpecimenPos());
                char rows=(char) (Integer.parseInt(frozenTubeImportingForm.getRowOfSpecimenPos())+64);
                frozenTubeResponse.setTubeRows(String.valueOf(rows));
                List<FrozenTubeResponse> frozenTubeList = map.get(sampleTypeCode);
                if(frozenTubeList == null ||frozenTubeList.size()==0){
                    List<FrozenTubeResponse> frozenTubeResponseList = new ArrayList<FrozenTubeResponse>();
                    frozenTubeResponseList.add(frozenTubeResponse);
                    map.put(sampleTypeCode,frozenTubeResponseList);
                }else{
                    List<FrozenTubeResponse> frozenTubeResponseList =  map.get(sampleTypeCode);
                    frozenTubeResponseList.add(frozenTubeResponse);
                    map.put(sampleTypeCode,frozenTubeResponseList);
                }
                frozenTubeResponses.add(frozenTubeResponse);
            }
            if(map.keySet().size()==1){
                frozenBoxDTO.setFrozenTubeDTOS(frozenTubeResponses);
                frozenBoxDTO.setIsRealData(Constants.YES);
                frozenBoxDTO.setCountOfSample(frozenTubeResponses.size());
            }else{
                throw new BankServiceException("导入失败！");
            }

        } catch (HttpException e) {
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return frozenBoxDTO;
    }

}
