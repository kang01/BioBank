package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutFilesService;
import org.fwoxford.service.StockOutReqFrozenTubeService;
import org.fwoxford.service.StockOutRequirementService;
import org.fwoxford.service.dto.StockOutRequiredSampleDTO;
import org.fwoxford.service.dto.StockOutRequirementDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.StockOutRequirementMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.fwoxford.web.rest.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    private StockOutApplyProjectRepository stockOutApplyProjectRepository;

    @Autowired
    private StockOutFilesService stockOutFilesService;

    @Autowired
    private StockOutFilesRepository stockOutFilesRepository;

    @Autowired
    private StockOutRequirementSampleRepositories stockOutRequirementSampleRepositories;

    @Autowired
    private BankUtil bankUtil;

    @Autowired
    EntityManager entityManager;

    @Autowired
    FrozenTubeRepository frozenTubeRepository;

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
        //验证申请状态是否可以删除
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        if(stockOutRequirement ==null){
            throw new BankServiceException("未查询到需要删除的需求！");
        }
        if(!stockOutRequirement.getStockOutApply().getStatus().equals(Constants.STOCK_OUT_PENDING)){
            throw new BankServiceException("由于申请的状态已不在进行中，需求不能删除！");
        }
        //删除需求的样本
        stockOutRequiredSampleRepository.deleteByStockOutRequirementId(id);
        //删除核对通过的样本
        stockOutReqFrozenTubeRepository.deleteByStockOutRequirementId(id);

        stockOutRequirementRepository.delete(id);

    }

    @Override
    public StockOutRequirementForApply saveStockOutRequirement(StockOutRequirementForApply stockOutRequirement, Long stockOutApplyId) {
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
        if(!stockOutApply.getStatus().equals(Constants.STOCK_OUT_PENDING)&&!stockOutApply.getStatus().equals(Constants.STOCK_OUT_APPROVE_REFUSED)){
            throw new BankServiceException("申请单的状态不能新增需求！",stockOutApply.getStatus());
        }
        StockOutRequirement requirement = new StockOutRequirement();
        if(stockOutRequirement.getId()!=null){
            requirement  = stockOutRequirementRepository.findOne(stockOutRequirement.getId());
            //删除核对过的样本
            stockOutReqFrozenTubeRepository.deleteByStockOutRequirementId(requirement.getId());
        }
        requirement.setStatus(Constants.STOCK_OUT_REQUIREMENT_CKECKING);
        requirement.setStockOutApply(stockOutApply);
        requirement.setRequirementName(stockOutRequirement.getRequirementName());
        requirement.setRequirementCode(bankUtil.getUniqueID("D"));
        requirement.setApplyCode(stockOutApply.getApplyCode());
        requirement.setCountOfSample(stockOutRequirement.getCountOfSample());
        requirement.setDiseaseType(!stockOutRequirement.getDiseaseTypeId().equals("null")?stockOutRequirement.getDiseaseTypeId():null);
        requirement.setSex(!stockOutRequirement.getSex().equals("null")?stockOutRequirement.getSex():null);
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
        }else{
            requirement.setAgeMax(null);
            requirement.setAgeMin(null);
        }
        if(stockOutRequirement.getFrozenTubeTypeId() != null){
            FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findOne(stockOutRequirement.getFrozenTubeTypeId());
            if(frozenTubeType ==null){
                throw new BankServiceException("未查到冻存管类型！");
            }
            requirement.setFrozenTubeType(frozenTubeType);
        }else{
            requirement.setFrozenTubeType(null);
        }
        if(stockOutRequirement.getSampleTypeId()!=null){
            SampleType sampleType = sampleTypeRepository.findOne(stockOutRequirement.getSampleTypeId());
            if(sampleType ==null){
                throw new BankServiceException("未查到样本类型！");
            }
            requirement.setSampleType(sampleType);
        }else{
            requirement.setSampleType(null);
        }
        if(stockOutRequirement.getSampleClassificationId()!=null){
            SampleClassification sampleClassification = sampleClassificationRepository.findOne(stockOutRequirement.getSampleClassificationId());
            if(sampleClassification ==null){
                throw new BankServiceException("未查到样本分类！");
            }
            requirement.setSampleClassification(sampleClassification);
        }else{
            requirement.setSampleClassification(null);
        }
        stockOutRequirementRepository.save(requirement);
        stockOutRequirement.setId(requirement.getId());

        return stockOutRequirement;
    }
    @Override
    public StockOutRequirementForApply saveAndUploadStockOutRequirement(StockOutRequirementForApply stockOutRequirement, Long stockOutApplyId, MultipartFile file, HttpServletRequest request) {
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
        //保存附件
//        StockOutFiles stockOutFiles = stockOutFilesService.saveFiles(file,request);
        StockOutRequirement requirement = new StockOutRequirement();

        if(stockOutRequirement.getId()!=null){
            requirement.setId(stockOutRequirement.getId());
            stockOutRequiredSampleRepository.deleteByStockOutRequirementId(stockOutRequirement.getId());
        }
        List<StockOutRequiredSample> stockOutRequiredSamples = new ArrayList<StockOutRequiredSample>();
        List<StockOutRequiredSample> stockOutRequiredSamplesList = new ArrayList<StockOutRequiredSample>();
        Map<String,String> map = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        try {
            String filetype=file.getOriginalFilename().split("\\.")[1];//后缀
            ArrayList<ArrayList<Object>> arrayLists = ExcelUtils.readExcel(filetype,file.getInputStream());
            List<ArrayList<Object>>  arrayListArrayList = arrayLists.subList(1,arrayLists.size());
            for(List arrayList :arrayListArrayList){
                Object boxCode =  arrayList.get(0);
                Object sampleCode =  arrayList.get(1);
                Object sampleType =  arrayList.get(2);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("frozenBoxCode1D",boxCode);
                jsonObject.put("code",sampleCode);
                jsonObject.put("type",sampleType);
                jsonArray.add(jsonObject);
            }

//            InputStream stream = file.getInputStream(); HashSet<String[]> hashSet =  reportExportingService.readRequiredSamplesFromExcelFile(stream);
//            for(String[] s : hashSet){
//                String code = s[0];
//                String type = s[1];
//                if(map.get(code)!=null){
//                    continue;
//                }
//                map.put(code,type);
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("code",code);
//                jsonObject.put("type",type);
//                jsonArray.add(jsonObject);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        StockOutFiles stockOutFiles = new StockOutFiles();
        if (!file.isEmpty()) {
            try {
                stockOutFiles.setFileName(file.getOriginalFilename());
                // 文件保存路径
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"
                    + file.getOriginalFilename();
                stockOutFiles.setFilePath(filePath);
                stockOutFiles.setFiles(file.getBytes());
                stockOutFiles.setFilesContentType(file.getContentType());
                stockOutFiles.setFileSize((int)file.getSize());
                stockOutFiles.setFileType(file.getContentType());
                stockOutFiles.setStatus(Constants.VALID);
                stockOutFiles.setFileContent(jsonArray.toString());
                stockOutFilesRepository.save(stockOutFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        requirement.setStatus(Constants.STOCK_OUT_REQUIREMENT_CKECKING);
        requirement.setStockOutApply(stockOutApply);
        requirement.setRequirementName(stockOutRequirement.getRequirementName());
        requirement.setRequirementCode(bankUtil.getUniqueID("D"));
        requirement.setApplyCode(stockOutApply.getApplyCode());
        requirement.setMemo(stockOutRequirement.getMemo());
        requirement.setCountOfSample(jsonArray.size());
        requirement.setImportingFileId(stockOutFiles!=null?stockOutFiles.getId():null);
        stockOutRequirementRepository.save(requirement);
//        if (stockOutRequiredSamples.size() > 0){
//            stockOutRequiredSampleRepository.save(stockOutRequiredSamples);
//            stockOutRequiredSamples.clear();
//        }
//
//        requirement.setCountOfSample(stockOutRequiredSamplesList.size());
//        stockOutRequirement.setCountOfSample(stockOutRequiredSamplesList.size());
//        stockOutRequirementRepository.save(requirement);
        stockOutRequirementForApply.setId(requirement.getId());
        stockOutRequirementForApply.setCountOfSample(requirement.getCountOfSample());
        stockOutRequirementForApply.setRequirementName(requirement.getRequirementName());
        stockOutRequirementForApply.setMemo(requirement.getMemo());
        stockOutRequirementForApply.setStatus(requirement.getStatus());
        stockOutRequirementForApply.setSamples(file.getOriginalFilename());
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

        if(stockOutRequirement.getImportingFileId()!=null){
            StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(stockOutRequirement.getImportingFileId());
            result.setSamples(stockOutFiles!=null?stockOutFiles.getFileName():null);
        }else{
            result.setSex(stockOutRequirement.getSex());
            if(stockOutRequirement.getAgeMin()!=null){
                result.setAge(stockOutRequirement.getAgeMin()+";"+stockOutRequirement.getAgeMax());
            }
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
        if(stockOutRequirement.getImportingFileId()!=null){
            List<StockOutRequiredSampleDTO> stockOutRequiredSamples = new ArrayList<StockOutRequiredSampleDTO>();
//            List<StockOutRequiredSample> stockOutRequiredSamples = stockOutRequiredSampleRepository.findByStockOutRequirementId(id);
            StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(stockOutRequirement.getImportingFileId());
            String fileContent = stockOutFiles.getFileContent();
            List<JSONObject> jsonArray = JSONArray.fromObject(fileContent);
            List<StockOutRequiredSampleDTO> stockOutRequiredSampleList = new ArrayList<>();
            for(int i=0;i<jsonArray.size();i++){
                StockOutRequiredSampleDTO stockOutRequiredSample = new StockOutRequiredSampleDTO();
                String boxCode1D = jsonArray.get(i).getString("frozenBoxCode1D");
                stockOutRequiredSample.setFrozenBoxCode1D(boxCode1D);
                String type = jsonArray.get(i).getString("type");
                stockOutRequiredSample.setSampleCode(jsonArray.get(i).getString("code"));
                stockOutRequiredSample.setSampleType(type);
                if(!StringUtils.isEmpty(boxCode1D)){
                    stockOutRequiredSampleList.add(stockOutRequiredSample);
                }else {

                    stockOutRequiredSamples.add(stockOutRequiredSample);
                }
            }
            Map<String, List<StockOutRequiredSampleDTO>> boxListGroupByType =
                stockOutRequiredSampleList.stream().collect(Collectors.groupingBy(w -> w.getSampleType()));
            for(String type :boxListGroupByType.keySet()){
                List<StockOutRequiredSampleDTO> boxCodeListGroupByType = boxListGroupByType.get(type);
                List<String> codeList = new ArrayList<>();
                boxCodeListGroupByType.forEach(s->codeList.add(s.getFrozenBoxCode1D()));
                List<List<String>> boxCodeListEach1000 = Lists.partition(codeList, 1000);
                for( List<String> code : boxCodeListEach1000){
                    List<Object[]> sampleList = frozenTubeRepository.findByFrozenBoxCode1DIn(code,type);
                    for(Object[] obj :sampleList){
                        StockOutRequiredSampleDTO stockOutRequiredSample = new StockOutRequiredSampleDTO();
                        if(obj[0] != null){
                            stockOutRequiredSample.setSampleCode(obj[0].toString());
                            stockOutRequiredSample.setSampleType(type);
                            stockOutRequiredSamples.add(stockOutRequiredSample);
                        }
                    }
                }
            }
            //核对导入指定样本
            status = stockOutReqFrozenTubeService.checkStockOutSampleByAppointedSample(stockOutRequiredSamples,stockOutRequirement);
        }else{
            //核对录入部分需求
            status = stockOutReqFrozenTubeService.checkStockOutSampleByRequirement(stockOutRequirement);
        }
        stockOutRequirement.setStatus(status);
        stockOutRequirementRepository.save(stockOutRequirement);
        stockOutRequirementForApply.setId(id);
        stockOutRequirementForApply.setStatus(status);
        return stockOutRequirementForApply;
    }

    @Override
    public StockOutRequirementSampleDetail getCheckDetail(Long id) {
        StockOutRequirementSampleDetail details = new StockOutRequirementSampleDetail();
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        if(stockOutRequirement == null){
            throw new BankServiceException("未查询到样本需求！");
        }
//        List<StockOutReqFrozenTube> stockOutRequiredSamples = stockOutReqFrozenTubeRepository.findByStockOutRequirementId(id);
        int countOfStockOutSample = stockOutReqFrozenTubeRepository.countByStockOutRequirementId(id);

        details.setId(id);
        details.setSex(stockOutRequirement.getSex());
        details.setCountOfStockOutSample(countOfStockOutSample);
        details.setCountOfSample(stockOutRequirement.getCountOfSample());
        details.setMemo(stockOutRequirement.getMemo());
        if(stockOutRequirement.getAgeMin() != null)
        details.setAges(stockOutRequirement.getAgeMin()+"至"+stockOutRequirement.getAgeMax());

        List<StockOutApplyProject> stockOutApplyProjects = stockOutApplyProjectRepository.findByStockRequirementId(id);
        StringBuffer stringBuffer = new StringBuffer();
        for(StockOutApplyProject s :stockOutApplyProjects){
            stringBuffer.append(s.getProject().getProjectName());
            stringBuffer.append(",");
        }
        if(stringBuffer.length()>0){
            String projects = stringBuffer.toString().substring(0,stringBuffer.length()-1);
            details.setProjects(projects);
        }

        details.setIsHemolysis(stockOutRequirement.isIsHemolysis());
        details.setIsBloodLipid(stockOutRequirement.isIsBloodLipid());
        details.setTubeType(stockOutRequirement.getFrozenTubeType()!=null?stockOutRequirement.getFrozenTubeType().getFrozenTubeTypeName():null);
        details.setSampleType(stockOutRequirement.getSampleType()!=null?stockOutRequirement.getSampleType().getSampleTypeName():null);
        //获取样本详情
//        List<StockOutRequirementFrozenTubeDetail> frozenTubes = new ArrayList<StockOutRequirementFrozenTubeDetail>();
//        for(StockOutReqFrozenTube sample:stockOutRequiredSamples){
//            StockOutRequirementFrozenTubeDetail frozenTubeDetail = new StockOutRequirementFrozenTubeDetail();
//            FrozenTube tube = sample.getFrozenTube();
//            if(tube!=null){
//                frozenTubeDetail.setStatus(tube.getStatus());
//                frozenTubeDetail.setProjectCode(tube.getProjectCode());
//                frozenTubeDetail.setIsBloodLipid(tube.isIsBloodLipid());
//                frozenTubeDetail.setIsHemolysis(tube.isIsHemolysis());
//                frozenTubeDetail.setDiseaseTypeId(tube.getDiseaseType());
//                frozenTubeDetail.setMemo(tube.getMemo());
//                frozenTubeDetail.setSampleCode(tube.getSampleCode());
//                frozenTubeDetail.setSampleTypeName(tube.getSampleTypeName());
//                frozenTubeDetail.setSampleUsedTimes(tube.getSampleUsedTimes());
//                frozenTubeDetail.setSex(tube.getGender());
//                frozenTubeDetail.setAge(tube.getAge());
//            }
//            frozenTubes.add(frozenTubeDetail);
//        }
//        details.setFrozenTubeList(frozenTubes);
        return details;
    }

    /**
     * 复原核对
     * @param id
     * @return
     */
    @Override
    public StockOutRequirementForApply revertStockOutRequirement(Long id) {
        StockOutRequirementForApply stockOutRequirementForApply = new StockOutRequirementForApply();
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        if(stockOutRequirement == null){
            throw new BankServiceException("未查询到样本需求！");
        }
        stockOutRequirement.setStatus(Constants.STOCK_OUT_REQUIREMENT_CKECKING);
        stockOutRequirement.setCountOfSampleReal(0);
        stockOutRequirementRepository.save(stockOutRequirement);
        //删除核对通过的样本
//        stockOutReqFrozenTubeRepository.deleteByStockOutRequirementId(id);
        StringBuffer sql = new StringBuffer();
        sql.append("delete from stock_out_req_frozen_tube where stock_out_requirement_id = ?1");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("1", id).executeUpdate();
//        entityManager.flush();

//        int a = 1/0;
        stockOutRequirementForApply.setId(id);
        stockOutRequirementForApply.setStatus(stockOutRequirement.getStatus());
        return stockOutRequirementForApply;
    }

    /**
     * 批量核对
     * @param ids
     * @return
     */
    @Override
    public void batchCheckStockOutRequirement(List<Long> ids) {
        //查询全部样本需求
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findByIdInAndStatusNot(ids,Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS);

        if(stockOutRequirementList.size()>0){
            Map<Double,StockOutRequirement> map = getOrderStockOutRequirement(stockOutRequirementList);
            for(Double d :map.keySet()){
                StockOutRequirement stockOutRequirements = map.get(d);
                checkStockOutRequirement(stockOutRequirements.getId());
            }
        }
    }
    /**
     * 权重说明：SUM（权重系数）/ COUNT(权重系数) = 权重
     * @param stockOutRequirementList
     * @return
     */
    private Map<Double,StockOutRequirement> getOrderStockOutRequirement(List<StockOutRequirement> stockOutRequirementList) {
        Map<Double, StockOutRequirement> map = new TreeMap<Double, StockOutRequirement>(
            new Comparator<Double>() {
                public int compare(Double obj1, Double obj2) {
                    // 降序排序
                    return obj2.compareTo(obj1);
                }
            });
        Double i= 0.0001;
        for(StockOutRequirement s : stockOutRequirementList){
            i+=0.0001;
            Double key = 0.0;Double keySum = 0.0;Double keyCount = 0.0;
            if(s.getCountOfSample()!=null){//样本数量，权重系数0（因为所有的需求都有这个基本条件）
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("countOfSample").toString());
                keyCount++;
            }
            Long countProject = stockOutApplyProjectRepository.countByStockRequirementId(s.getId());
            if(countProject.intValue()>0){//项目编码，权重系数0（因为所有的需求都有这个基本条件）
                keySum += Integer.parseInt(Constants.KEY_NUMBER_MAP.get("projectCode").toString());
                keyCount++;
            }
            //获取指定样本
            Long countSample = stockOutRequiredSampleRepository.countByStockOutRequirementId(s.getId());
            if(countSample.intValue()>0){//样本编码，权重系数101（因为已经具体到管子，没有比它更具体的需求了）
                keySum += Integer.parseInt(Constants.KEY_NUMBER_MAP.get("sampleCode").toString());
                keyCount++;
            }
            if(s.getSampleType()!=null){// 样本类型，权重系数1
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("sampleType").toString());
                keyCount++;
            }
            if(s.getSampleClassification()!=null){// 样本分类，权重系数2
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("sampleClassificationType").toString());
                keyCount++;
            }
            if(s.getFrozenTubeType()!=null){//  冻存管类型，权重系数1
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("frozenTubeType").toString());
                keyCount++;
            }
            if(s.getSex()!=null){//  性别，权重系数1
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("sex").toString());
                keyCount++;
            }
            if(s.getAgeMin()!=null){//  年龄段，权重系数3
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("ages").toString());
                keyCount++;
            }
            if(s.getDiseaseType()!=null){//  疾病类型，权重系数5
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("diseaseType").toString());
                keyCount++;
            }
            if(s.isIsHemolysis() != null && s.isIsHemolysis()!=false){//  溶血，权重系数5
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("isHemolysis").toString());
                keyCount++;
            }
            if(s.isIsBloodLipid() != null && s.isIsBloodLipid()!=false){//  脂质血，权重系数5
                keySum +=Integer.parseInt(Constants.KEY_NUMBER_MAP.get("isBloodLipid").toString());
                keyCount++;
            }
            key = keySum/keyCount;
            if(map.get(key)!=null){
                map.put(key+i,s);
            }else{
                map.put(key,s);
            }
        }
        return map;
    }

    /**
     * 打印出库申请详情
     * @param id
     * @return
     */
    @Override
    public ByteArrayOutputStream printStockOutRequirementDetailReport(Long id) {
        StockOutRequirementDetailReportDTO requirementDTO = checkStockOutRequirementDetailReportDTO(id);
        ByteArrayOutputStream outputStream = reportExportingService.makeStockOutRequirementCheckReport(requirementDTO);
        return outputStream;
    }

    private StockOutRequirementDetailReportDTO checkStockOutRequirementDetailReportDTO(Long id) {
        StockOutRequirementDetailReportDTO report = new StockOutRequirementDetailReportDTO();
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findOne(id);
        if(stockOutRequirement == null){
            throw new BankServiceException("未查询到样本需求！");
        }
        List<StockOutReqFrozenTube> stockOutRequiredSamples = stockOutReqFrozenTubeRepository.findByStockOutRequirementId(id);

        report.setId(id);
        report.setSex(stockOutRequirement.getSex());
        report.setCountOfStockOutSample(stockOutRequiredSamples.size());
        report.setCountOfSample(stockOutRequirement.getCountOfSample());
        report.setMemo(stockOutRequirement.getMemo());
        report.setRequirementNO(stockOutRequirement.getRequirementCode());
        report.setRequirementName(stockOutRequirement.getRequirementName());
        //todo
        report.setDiseaseType(stockOutRequirement.getDiseaseType());
        report.setTubeType(stockOutRequirement.getFrozenTubeType()!=null?stockOutRequirement.getFrozenTubeType().getFrozenTubeTypeName():null);
        report.setSampleType(stockOutRequirement.getSampleType()!=null?stockOutRequirement.getSampleType().getSampleTypeName():null);
        if(stockOutRequirement.getAgeMin() != null)
            report.setAges(stockOutRequirement.getAgeMin()+"至"+stockOutRequirement.getAgeMax());
        List<StockOutApplyProject> stockOutApplyProjects = stockOutApplyProjectRepository.findByStockRequirementId(id);
        StringBuffer stringBuffer = new StringBuffer();
        for(StockOutApplyProject s :stockOutApplyProjects){
            stringBuffer.append(s.getProject().getProjectName());
            stringBuffer.append(",");
        }
        if(stringBuffer.length()>0){
            String projects = stringBuffer.toString().substring(0,stringBuffer.length()-1);
            report.setProjects(projects);
        }

        //获取样本详情
        List<StockOutSampleCheckResultDTO> frozenTubes = new ArrayList<StockOutSampleCheckResultDTO>();
        for(StockOutReqFrozenTube sample:stockOutRequiredSamples){
            StockOutSampleCheckResultDTO frozenTubeDetail = new StockOutSampleCheckResultDTO();
            FrozenTube tube = sample.getFrozenTube();
            frozenTubeDetail.setId(tube.getId());
            frozenTubeDetail.setDiseaseType(tube.getDiseaseType());
            frozenTubeDetail.setStatus(tube.getStatus());
            frozenTubeDetail.setProjectCode(tube.getProjectCode());
            frozenTubeDetail.setMemo(tube.getMemo());
            frozenTubeDetail.setSampleCode(tube.getSampleCode());
            frozenTubeDetail.setSampleType(tube.getSampleTypeName());
            frozenTubeDetail.setTimes(Long.valueOf(tube.getSampleUsedTimes()!=null?tube.getSampleUsedTimes():0));
            frozenTubeDetail.setSex(tube.getGender());
            Object statusObj = Constants.FROZEN_TUBE_MAP.get(tube.getStatus());
            String status = statusObj!=null?statusObj.toString():null;
            frozenTubeDetail.setStatus(status);
            frozenTubeDetail.setAge(tube.getAge()!=null?tube.getAge().toString():null);
            frozenTubes.add(frozenTubeDetail);
        }
        report.setCheckResults(frozenTubes);
        return report;
    }

    /**
     * 核对需求样本详情
     * @param id
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<StockOutRequirementFrozenTubeDetail> getCheckDetailByRequirement(Long id, DataTablesInput input) {
        input.addColumn("stockOutRequirementId",true,true,id+"+");
        return stockOutRequirementSampleRepositories.findAll(input);
    }
}
