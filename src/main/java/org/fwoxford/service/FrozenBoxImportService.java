package org.fwoxford.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeDataForm;
import org.fwoxford.service.dto.response.FrozenTubeImportingForm;
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
import java.util.*;
import java.util.stream.Collectors;

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
        Map<String,List<FrozenTubeDTO>> map = new HashMap<String,List<FrozenTubeDTO>>();
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

                //截取冻存盒编码获取类型
                String sampleClassificationTypeCode = frozenBoxCode.substring(4,6);
                String pCode = frozenBoxCode.substring(0,4);
                if(!pCode.equals(projectCode)){
                    throw new BankServiceException("转运单的项目与冻存盒所属项目不一致，导入失败！");
                }
                ProjectSampleClass projectSampleClass = new ProjectSampleClass();
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

                frozenBoxAndFrozenTubeResponse.setIsRealData(Constants.YES);
                frozenBoxAndFrozenTubeResponse.setFrozenBoxCode(frozenBoxCode);
                frozenBoxAndFrozenTubeResponse.setFrozenBoxCode1D(frozenBoxCode);
                frozenBoxAndFrozenTubeResponse.setFrozenBoxType(frozenBoxType);
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeId(frozenBoxType.getId());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeName(frozenBoxType.getFrozenBoxTypeName());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows());
                frozenBoxAndFrozenTubeResponse.setFrozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns());


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
                List<FrozenTubeDTO> frozenTubeResponses = new ArrayList<FrozenTubeDTO>();
                switch (sampleTypeCode){
                    case "99":frozenTubeResponses =  create99Sample(map,frozenBoxCode,sampleType,projectSampleClasses,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "A":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "R":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "E":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    case "W":frozenTubeResponses =  createSample(map,frozenBoxCode,sampleType,projectSampleClass,frozenBoxAndFrozenTubeResponse,frozenBoxAndFrozenTubeResponse,frozenTubeType,sampleCode);break;
                    default:break;
                }

                List<FrozenTubeDTO> frozenTubeResponseList = map.get(frozenBoxCode);
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

    public List<FrozenTubeDTO> createSample(Map<String, List<FrozenTubeDTO>> map, String frozenBoxCode, SampleType sampleType, ProjectSampleClass projectSampleClass, FrozenBoxAndFrozenTubeResponse boxAndFrozenTubeResponse, FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse, FrozenTubeType frozenTubeType, String sampleCode) {
        frozenBoxAndFrozenTubeResponse.setIsSplit(Constants.NO);
        frozenBoxAndFrozenTubeResponse.setSampleClassification(projectSampleClass.getSampleClassification());
        frozenBoxAndFrozenTubeResponse.setSampleClassificationId(projectSampleClass.getSampleClassification().getId());
        frozenBoxAndFrozenTubeResponse.setSampleClassificationCode(projectSampleClass.getSampleClassificationCode());
        frozenBoxAndFrozenTubeResponse.setSampleClassificationName(projectSampleClass.getSampleClassificationName());
        frozenBoxAndFrozenTubeResponse.setBackColorForClass(projectSampleClass.getSampleClassification().getBackColor());
        frozenBoxAndFrozenTubeResponse.setFrontColorForClass(projectSampleClass.getSampleClassification().getFrontColor());

        List<FrozenTubeDTO> frozenTubeResponses = new ArrayList<FrozenTubeDTO>();
        List<FrozenTubeDTO> frozenTubeResponseList = map.get(frozenBoxCode)!=null?map.get(frozenBoxCode):new ArrayList<FrozenTubeDTO>();
        int i = 0;
        String rowsInTube = "";
        String columnsInTube = "";
        Map<String,FrozenTubeDTO> posMap = new HashMap<>();
        for(FrozenTubeDTO tube :frozenTubeResponseList){
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

        FrozenTubeDTO frozenTubeResponse = new FrozenTubeDTO();
        frozenTubeResponse.setStatus(Constants.FROZEN_TUBE_NORMAL);
        frozenTubeResponse.setSampleCode(sampleCode);
        frozenTubeResponse.setFrozenBoxCode(frozenBoxCode);
        frozenTubeResponse.setTubeRows(rowsInTube);
        frozenTubeResponse.setTubeColumns(columnsInTube);
        frozenTubeResponse.setSampleTypeId(sampleType.getId());
        frozenTubeResponse.setSampleTypeName(sampleType.getSampleTypeName());
        frozenTubeResponse.setSampleTypeCode(sampleType.getSampleTypeCode());
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

    public List<FrozenTubeDTO> create99Sample(Map<String, List<FrozenTubeDTO>> map, String frozenBoxCode, SampleType sampleType, List<ProjectSampleClass> projectSampleClasses, FrozenBoxAndFrozenTubeResponse frozenBoxAndFrozenTubeResponse, FrozenTubeType frozenTubeType, String sampleCode) {
        frozenBoxAndFrozenTubeResponse.setIsSplit(Constants.YES);
        //从map中获取冻存盒，取盒内的位置
        List<FrozenTubeDTO> frozenTubeResponses = new ArrayList<FrozenTubeDTO>();
        List<FrozenTubeDTO> frozenTubeResponseList = map.get(frozenBoxCode);
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
            FrozenTubeDTO frozenTubeResponse = new FrozenTubeDTO();
            frozenTubeResponse.setStatus(Constants.FROZEN_TUBE_NORMAL);
            frozenTubeResponse.setSampleCode(sampleCode);
            frozenTubeResponse.setTubeRows(String.valueOf(c1));
            frozenTubeResponse.setTubeColumns(String.valueOf(j));
            frozenTubeResponse.setFrozenBoxCode(frozenBoxCode);
            frozenTubeResponse.setSampleTypeId(sampleType.getId());
            frozenTubeResponse.setSampleTypeName(sampleType.getSampleTypeName());
            frozenTubeResponse.setSampleTypeCode(sampleType.getSampleTypeCode());
            String sampleClass = Constants.SAMPLECLASS_MAP.get(String.valueOf(j));
            if(StringUtils.isEmpty(sampleClass)){
                throw new BankServiceException("样本分类获取失败！");
            }
            ProjectSampleClass projectSampleClass =projectSampleClasses.stream().filter(s->s.getSampleClassificationCode().equals(sampleClass)).findFirst().orElse(null);
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
    public List<FrozenBoxAndFrozenTubeResponse> importFrozenBoxAndFrozenTube(String frozenBoxCodeStr, Long sampleTypeId, Long boxTypeId) {
        List<FrozenBoxAndFrozenTubeResponse> frozenBoxDTOList = new ArrayList<FrozenBoxAndFrozenTubeResponse>();
        String[] frozenBoxCode = frozenBoxCodeStr.split(",");

        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findOne(boxTypeId);
        if(frozenBoxType == null || frozenBoxType.getStatus() == null || !frozenBoxType.getStatus().equals(Constants.VALID)){
            throw new BankServiceException("指定的冻存盒类型无效！");
        }
        // 根据冻存盒类型选择冻存管类型
        List<FrozenTubeType> frozenTubeTypes = frozenTubeTypeRepository.findAll();
        if (frozenTubeTypes == null){
            frozenTubeTypes = new ArrayList<>();
        }
        frozenTubeTypes = frozenTubeTypes.stream().filter(d-> d.getStatus() != null && Constants.VALID.equals(d.getStatus())).collect(Collectors.toList());
        Map<String, FrozenTubeType> frozenTubeTypeMap = new HashMap<>();
        frozenTubeTypeMap.put("DCH", frozenTubeTypes.stream().filter(d->d.getFrozenTubeTypeCode() != null && "DCG".equals(d.getFrozenTubeTypeCode())).findFirst().get());
        frozenTubeTypeMap.put("DJH", frozenTubeTypes.stream().filter(d->d.getFrozenTubeTypeCode() != null && "RNA".equals(d.getFrozenTubeTypeCode())).findFirst().get());
        frozenTubeTypeMap.put("96KB", frozenTubeTypes.stream().filter(d->d.getFrozenTubeTypeCode() != null && "2DDCG".equals(d.getFrozenTubeTypeCode())).findFirst().get());
        FrozenTubeType frozenTubeType = frozenTubeTypeMap.get(frozenBoxType.getFrozenBoxTypeCode());
        if(frozenTubeType == null){
            throw new BankServiceException("查询冻存管类型失败！");
        }

        SampleType sampleType = sampleTypeRepository.findOne(sampleTypeId);
        if(sampleType == null || sampleType.getStatus() == null || !sampleType.getStatus().equals(Constants.VALID)){
            throw new BankServiceException("指定的样本类型无效！");
        }

        for(String code :frozenBoxCode){
            FrozenBoxAndFrozenTubeResponse frozenBoxDTO = new FrozenBoxAndFrozenTubeResponse();
            frozenBoxDTO.setIsSplit(Constants.YES);
            frozenBoxDTO.setIsRealData(Constants.NO);
            frozenBoxDTO.setStatus(Constants.FROZEN_BOX_NEW);

            frozenBoxDTO.setFrozenBoxCode(code);
            frozenBoxDTO.setFrozenBoxCode1D(code);

            frozenBoxDTO.setFrozenBoxType(frozenBoxType);

            frozenBoxDTO.setFrozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode());
            frozenBoxDTO.setFrozenBoxTypeName(frozenBoxType.getFrozenBoxTypeName());
            frozenBoxDTO.setFrozenBoxTypeId(frozenBoxType.getId());
            frozenBoxDTO.setFrozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows());
            frozenBoxDTO.setFrozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns());

            frozenBoxDTO.setSampleType(sampleType);
            frozenBoxDTO.setSampleTypeId(sampleType.getId());
            frozenBoxDTO.setSampleTypeCode(sampleType.getSampleTypeCode());
            frozenBoxDTO.setSampleTypeName(sampleType.getSampleTypeName());
            frozenBoxDTO.setIsMixed(sampleType.getIsMixed());
            List<FrozenTubeDTO> frozenTubeResponses = getSampleInfo(frozenBoxDTO, frozenTubeType);
            frozenBoxDTO.setFrozenTubeDTOS(frozenTubeResponses);
            frozenBoxDTO.setIsRealData(Constants.YES);
            frozenBoxDTO.setCountOfSample(frozenTubeResponses.size());
            frozenBoxDTOList.add(frozenBoxDTO);
        }
        return frozenBoxDTOList;
    }
    public List<FrozenTubeDTO> getSampleInfo(FrozenBoxAndFrozenTubeResponse frozenBoxDTO, FrozenTubeType frozenTubeType) {

        String code = frozenBoxDTO.getFrozenBoxCode1D();
        SampleType sampleType = frozenBoxDTO.getSampleType();

        // 配置导入API
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://10.24.10.43:8080/biobank/specimens?boxcode="+code);
        HttpMethodParams params = new HttpMethodParams();
        getMethod.setParams(params);
        getMethod.setRequestHeader("X-Auth-Token", "d93925bc7axKfXc2A5WFJEzuPXYX4q");
        // 接口的返回结果
        List<FrozenTubeImportingForm> frozenTubeImportingForms = new ArrayList<>();
        try{
            // 调用导入API
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != 200) {
                log.error("Method failed: "
                    + getMethod.getStatusLine());
                // ????导入失败后还要继续？
            } else {
                byte[] responseBody = getMethod.getResponseBody();
                String str = new String(responseBody);
                JSONArray jsonArray = new JSONArray();
                if(!StringUtils.isEmpty(str)){
                    jsonArray = JSONArray.fromObject(str);
                }
                frozenTubeImportingForms = (List<FrozenTubeImportingForm>) JSONArray.toCollection(jsonArray,FrozenTubeImportingForm.class);
            }
        } catch (HttpException e) {
            log.error("冻存盒导入失败", e);
        } catch (IOException e) {
            log.error("冻存盒导入失败", e);
        } finally {
            getMethod.releaseConnection();
        }

        if(frozenTubeImportingForms.size() == 0){
            throw new BankServiceException("冻存盒导入失败！" + frozenBoxDTO.getFrozenBoxCode1D() + "没有冻存管数据。");
        }

        // 获取冻存盒的样本类型
        String sampleTypeCode = sampleType.getSampleTypeCode();
        List<FrozenTubeDTO> frozenTubeResponses = frozenTubeImportingForms.stream().map(t -> {
            if (!sampleTypeCode.equals(t.getSpecimenType())){
                return null;
            }
            String status = t.getIsEmpty();
            if(status.equals("2")){
                status = Constants.FROZEN_TUBE_NORMAL;
            }else{
                status = Constants.FROZEN_TUBE_EMPTY;
            }
            Long patientId = Long.valueOf(t.getBloodCode().trim());
            Boolean ishemolysis = true; Boolean isbloodLipid = true;
            String hemolysis = t.getIsHemolysis();
            String bloodLipid  = t.getIsLipid();
            if(hemolysis.equals("2")){
                ishemolysis = false;
            }
            if(bloodLipid.equals("2")){
                isbloodLipid = false;
            }

            FrozenTubeDTO frozenTubeResponse = new FrozenTubeDTO();
            frozenTubeResponse.setStatus(status);
            frozenTubeResponse.setPatientId(patientId);
            frozenTubeResponse.setFrozenBoxCode(frozenBoxDTO.getFrozenBoxCode());
            frozenTubeResponse.setSampleCode(t.getSpecimenCode());

            frozenTubeResponse.setHemolysis(ishemolysis);
            frozenTubeResponse.setBloodLipid(isbloodLipid);
            frozenTubeResponse.setFrozenTubeTypeId(frozenTubeType.getId());
            frozenTubeResponse.setFrozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode());
            frozenTubeResponse.setFrozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName());
            frozenTubeResponse.setFrozenTubeVolumns(frozenTubeType.getFrozenTubeVolumn());
            frozenTubeResponse.setFrozenTubeVolumnsUnit(frozenTubeType.getFrozenTubeVolumnUnit());
            frozenTubeResponse.setSampleUsedTimesMost(frozenTubeType.getSampleUsedTimesMost());

            frozenTubeResponse.setTubeColumns(t.getColOfSpecimenPos());
            char rows=(char) (Integer.parseInt(t.getRowOfSpecimenPos())+64);
            //去掉行号I，向后累加
            if (Integer.parseInt(t.getRowOfSpecimenPos())>= 9) {
                rows = (char) (Integer.parseInt(t.getRowOfSpecimenPos()) + 65);
            }
            frozenTubeResponse.setTubeRows(String.valueOf(rows));

            frozenTubeResponse.setSampleTypeId(sampleType.getId());
            frozenTubeResponse.setSampleTypeCode(sampleType.getSampleTypeCode());
            frozenTubeResponse.setSampleTypeName(sampleType.getSampleTypeName());
            frozenTubeResponse.setIsMixed(sampleType.getIsMixed());

            return frozenTubeResponse;
        }).filter(t -> t != null).collect(Collectors.toList());

        if(frozenTubeResponses.size() == 0){
            throw new BankServiceException("冻存盒导入失败！没有指定样本类型(" + frozenBoxDTO.getSampleTypeCode() + ")的冻存管数据。");
        }

        return frozenTubeResponses;
    }

    public List<JSONObject> importFrozenBoxByBoxCode(String boxCode) {
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        // 配置导入API
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(Constants.HTTPURL+"?projectId=006&boxCode="+boxCode);
        HttpMethodParams params = new HttpMethodParams();
        getMethod.setParams(params);
        // 接口的返回结果
        List<FrozenTubeDataForm> frozenTubeImportingForms = new ArrayList<>();
        try{
            // 调用导入API
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != 200) {
                log.error("Method failed: "
                    + getMethod.getStatusLine());
                // ????导入失败后还要继续？
            } else {
                byte[] responseBody = getMethod.getResponseBody();
                String str = new String(responseBody);

                if(!StringUtils.isEmpty(str)){
                    jsonObjects = JSONArray.fromObject(str);
                }
//                frozenTubeImportingForms = (List<FrozenTubeDataForm>) JSONArray.toCollection(jsonArray,FrozenTubeDataForm.class);
            }
        } catch (HttpException e) {
            log.error("冻存盒导入失败", e);
        } catch (IOException e) {
            log.error("冻存盒导入失败", e);
        } finally {
            getMethod.releaseConnection();
        }

//        if(jsonObjects.size() == 0){
//            throw new BankServiceException("冻存盒导入失败！" + boxCode + "没有冻存管数据。");
//        }
        return jsonObjects;
    }

    public List<JSONObject> importFrozenBoxAndSampleAllDataFromLIMS(String frozenBoxCode) {
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        // 配置导入API
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(Constants.HTTPURL_LIMS+frozenBoxCode);
        // 接口的返回结果
        try{
            // 调用导入API
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != 200) {
                log.error("Method failed: "
                    + getMethod.getStatusLine());
            } else {
                byte[] responseBody = getMethod.getResponseBody();
                String str = new String(responseBody);

                if(!StringUtils.isEmpty(str)){
                    jsonObjects = JSONArray.fromObject(str);
                }
            }
        } catch (HttpException e) {
            log.error("冻存盒导入失败", e);
        } catch (IOException e) {
            log.error("冻存盒导入失败", e);
        } finally {
            getMethod.releaseConnection();
        }
        return jsonObjects;
    }
}
