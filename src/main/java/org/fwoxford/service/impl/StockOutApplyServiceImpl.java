package org.fwoxford.service.impl;

import io.swagger.models.auth.In;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutApplyService;
import org.fwoxford.service.StockOutRequirementService;
import org.fwoxford.service.dto.StockOutApplyDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.StockOutApplyMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing StockOutApply.
 */
@Service
@Transactional
public class StockOutApplyServiceImpl implements StockOutApplyService{

    private final Logger log = LoggerFactory.getLogger(StockOutApplyServiceImpl.class);

    private final StockOutApplyRepository stockOutApplyRepository;

    private final StockOutApplyMapper stockOutApplyMapper;

    @Autowired
    private DelegateRepository delegateRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StockOutApplyProjectRepository stockOutApplyProjectRepository;

    @Autowired
    private StockOutApplyRepositries stockOutApplyRepositries;

    @Autowired
    private StockOutRequiredSampleRepository stockOutRequiredSampleRepository;

    @Autowired
    private StockOutRequirementRepository stockOutRequirementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private ReportExportingService reportExportingService;

    @Autowired
    private StockOutRequirementService stockOutRequirementService;

    @Autowired
    private StockOutFilesRepository stockOutFilesRepository;

    @Autowired
    private StockOutPlanRepository stockOutPlanRepository;

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    public StockOutApplyServiceImpl(StockOutApplyRepository stockOutApplyRepository, StockOutApplyMapper stockOutApplyMapper) {
        this.stockOutApplyRepository = stockOutApplyRepository;
        this.stockOutApplyMapper = stockOutApplyMapper;
    }

    /**
     * Save a stockOutApply.
     *
     * @param stockOutApplyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutApplyDTO save(StockOutApplyDTO stockOutApplyDTO) {
        log.debug("Request to save StockOutApply : {}", stockOutApplyDTO);
        StockOutApply stockOutApply = stockOutApplyMapper.stockOutApplyDTOToStockOutApply(stockOutApplyDTO);
        stockOutApply = stockOutApplyRepository.save(stockOutApply);
        StockOutApplyDTO result = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);
        return result;
    }

    /**
     *  Get all the stockOutApplies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutApplyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutApplies");
        Page<StockOutApply> result = stockOutApplyRepository.findAll(pageable);
        return result.map(stockOutApply -> stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply));
    }

    /**
     *  Get one stockOutApply by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutApplyDTO findOne(Long id) {
        log.debug("Request to get StockOutApply : {}", id);
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);
        return stockOutApplyDTO;
    }

    /**
     *  Delete the  stockOutApply by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutApply : {}", id);
        stockOutApplyRepository.delete(id);
    }

    @Override
    public DataTablesOutput<StockOutApplyForDataTableEntity> findStockOutApply(DataTablesInput input) {
        DataTablesOutput<StockOutApplyForDataTableEntity> output = stockOutApplyRepositries.findAll(input);
        return output;
    }

    @Override
    public StockOutApplyForSave initStockOutApply() {
        StockOutApply stockOutApply = new StockOutApply();
        stockOutApply.setStatus(Constants.STOCK_OUT_PENDING);
        stockOutApply.setApplyCode(BankUtil.getUniqueID());
        stockOutApplyRepository.save(stockOutApply);
        StockOutApplyForSave stockOutApplyForSave = new StockOutApplyDetail();
        stockOutApplyForSave.setId(stockOutApply.getId());
        stockOutApplyForSave.setStatus(stockOutApply.getStatus());
        stockOutApplyForSave.setApplyCode(stockOutApply.getApplyCode());
        return stockOutApplyForSave;
    }

    @Override
    public StockOutApplyForSave saveStockOutApply(StockOutApplyForSave stockOutApplyForSave) {
        Long id = stockOutApplyForSave.getId();
        if(id == null){
            throw new BankServiceException("出库申请ID不能为空！",stockOutApplyForSave.toString());
        }
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        if(stockOutApply == null){
            throw new BankServiceException("未查询到需要修改的出库申请！");
        }
        if(stockOutApply.getStatus().equals(Constants.STOCK_OUT_APPROVED) || stockOutApply.getStatus().equals(Constants.STOCK_OUT_INVALID)){
            throw new BankServiceException("出库申请状态不满足修改条件！");
        }
        stockOutApply.setApplyPersonName(stockOutApplyForSave.getApplyPersonName());
        Delegate delegate = delegateRepository.findOne(stockOutApplyForSave.getDelegateId());
        stockOutApply.setDelegate(delegate);
        stockOutApply.setStartTime(stockOutApplyForSave.getStartTime());
        stockOutApply.setEndTime(stockOutApplyForSave.getEndTime());
        stockOutApply.setRecordId(stockOutApplyForSave.getRecordId());
        stockOutApply.setRecordTime(stockOutApplyForSave.getRecordTime());
        stockOutApply.setPurposeOfSample(stockOutApplyForSave.getPurposeOfSample());
        stockOutApplyRepository.save(stockOutApply);
        List<Long> projectIds = stockOutApplyForSave.getProjectIds();
        stockOutApplyProjectRepository.deleteByStockOutApplyId(stockOutApply.getId());
        List<StockOutApplyProject> stockOutApplyProjects = new ArrayList<StockOutApplyProject>();
        if(projectIds!=null)
        for(Long projectId :projectIds){
            if( projectId !=null){
                StockOutApplyProject stockOutApplyProject = new StockOutApplyProject();
                stockOutApplyProject.setStatus(Constants.VALID);
                Project project = projectRepository.findOne(projectId);
                if(project == null){
                    throw new BankServiceException("项目不存在！",projectId.toString());
                }
                stockOutApplyProject.setProject(project);
                stockOutApplyProject.setStockOutApply(stockOutApply);
                stockOutApplyProjects.add(stockOutApplyProject);
            }
        }
        stockOutApplyProjectRepository.save(stockOutApplyProjects);
        return stockOutApplyForSave;
    }

    /**
     * 根据上一级申请ID，取下一级出库申请列表
     * @param id
     * @return
     */
    @Override
    public List<StockOutApplyForDataTableEntity> getNextStockOutApplyList(Long id) {
        List<StockOutApplyForDataTableEntity> stockOutApplyForDataTableEntities= stockOutApplyRepositries.findByParentApplyId(id);
        return stockOutApplyForDataTableEntities;
    }

    @Override
    public StockOutApplyDetail getStockOutDetailAndRequirement(Long id) {
        StockOutApplyDetail res = new StockOutApplyDetail();
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        res.setId(id);
        res.setRecordId(stockOutApply.getRecordId());
        res.setRecordTime(stockOutApply.getRecordTime());
        res.setDelegateId(stockOutApply.getDelegate()!=null?stockOutApply.getDelegate().getId():null);
        res.setDelegateName(stockOutApply.getDelegate()!=null?stockOutApply.getDelegate().getDelegateName():null);
        res.setApplyCode(stockOutApply.getApplyCode());
        res.setApplyPersonName(stockOutApply.getApplyPersonName());
        res.setEndTime(stockOutApply.getEndTime());
        res.setStartTime(stockOutApply.getStartTime());
        res.setPurposeOfSample(stockOutApply.getPurposeOfSample());
        res.setStatus(stockOutApply.getStatus());
        List<ProjectResponse> projectResponses = new ArrayList<ProjectResponse>();
        //获取授权的项目
        List<StockOutApplyProject> stockOutApplyProjects = stockOutApplyProjectRepository.findByStockOutApplyId(id);
        List<Long> projectIds = new ArrayList<Long>();
        StringBuffer nameBuffer = new StringBuffer();
        StringBuffer codeBuffer = new StringBuffer();
       for(StockOutApplyProject s :stockOutApplyProjects){
           projectIds.add(s.getProject().getId());
           nameBuffer.append(s.getProject().getProjectName());
           nameBuffer.append(",");
           codeBuffer.append(s.getProject().getProjectCode());
           codeBuffer.append(",");
       }
       res.setProjectIds(projectIds);
       if(nameBuffer.length()>0){
           String names = nameBuffer.substring(0,nameBuffer.length()-1);
           res.setProjcetNames(names);
       }
        if(codeBuffer.length()>0){
            String codes = codeBuffer.substring(0,codeBuffer.length()-1);
            res.setProjcetCodes(codes);
        }
        if(stockOutApply.getRecordId()!=null){
            User user = userRepository.findOne(stockOutApply.getRecordId());
            res.setRecorder(user!=null?user.getLastName()+user.getFirstName():null);
        }
       //获取申请的需求
        List<StockOutRequirementForApplyTable> stockOutRequirementForApplyTables = new ArrayList<StockOutRequirementForApplyTable>();
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findByStockOutApplyId(id);
        Long countOfStockOutSample = stockOutReqFrozenTubeRepository.countByApply(id);
        int countOfSampleAll=0;
        for(StockOutRequirement requirement : stockOutRequirementList){
            StockOutRequirementForApplyTable stockOutRequirementForApplyTable = new StockOutRequirementForApplyTable();
            stockOutRequirementForApplyTable.setId(requirement.getId());
            stockOutRequirementForApplyTable.setStatus(requirement.getStatus());
            stockOutRequirementForApplyTable.setCountOfSample(requirement.getCountOfSample());
            stockOutRequirementForApplyTable.setRequirementName(requirement.getRequirementName());
            if(requirement.getImportingFileId()!=null){
                StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(requirement.getImportingFileId());
                stockOutRequirementForApplyTable.setSamples(stockOutFiles!=null?stockOutFiles.getFileName():null);
            }else {
                stockOutRequirementForApplyTable.setDiseaseTypeId(requirement.getDiseaseType());
                stockOutRequirementForApplyTable.setIsBloodLipid(requirement.isIsBloodLipid());
                stockOutRequirementForApplyTable.setIsHemolysis(requirement.isIsHemolysis());
                stockOutRequirementForApplyTable.setFrozenTubeTypeName(requirement.getFrozenTubeType()!=null?requirement.getFrozenTubeType().getFrozenTubeTypeName():null);
                stockOutRequirementForApplyTable.setSampleTypeName(requirement.getSampleType()!=null?requirement.getSampleType().getSampleTypeName():null);
                stockOutRequirementForApplyTable.setSex(Constants.SEX_MAP.get(requirement.getSex())!=null?Constants.SEX_MAP.get(requirement.getSex()).toString():null);
                stockOutRequirementForApplyTable.setAge(requirement.getAgeMin()+"-"+requirement.getAgeMax()+"岁");
            }
            stockOutRequirementForApplyTables.add(stockOutRequirementForApplyTable);
            countOfSampleAll +=requirement.getCountOfSample();
        }
        res.setStockOutRequirement(stockOutRequirementForApplyTables);
        res.setCountOfStockOutSample(countOfStockOutSample);
        res.setCountOfSample(Long.valueOf(countOfSampleAll));
        return res;
    }

    /**
     * 新增附加申请
     * @param parentApplyId
     * @return
     */
    @Override
    public StockOutApplyDTO additionalApply(Long parentApplyId) {
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(parentApplyId);
        if(stockOutApply == null){
            throw new BankServiceException("未查询到可以附加的申请！");
        }
        StockOutApplyDTO stockOutApplyDTO = stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);
        stockOutApplyDTO.setId(null);
        stockOutApplyDTO.setApplyCode(BankUtil.getUniqueID());
        stockOutApplyDTO.setParentApplyId(parentApplyId);
        stockOutApplyDTO.setStatus(Constants.STOCK_OUT_PENDING);
        StockOutApply stockOutApplyChild = stockOutApplyMapper.stockOutApplyDTOToStockOutApply(stockOutApplyDTO);
        stockOutApplyRepository.save(stockOutApplyChild);
        stockOutApplyDTO.setId(stockOutApplyChild.getId());

        List<StockOutApplyProject> stockOutApplyProjects = stockOutApplyProjectRepository.findByStockOutApplyId(parentApplyId);
        List<StockOutApplyProject> stockOutApplyProjectList = new ArrayList<StockOutApplyProject>();
        if(stockOutApplyProjects!=null && stockOutApplyProjects.size()>0){
            for(StockOutApplyProject s:stockOutApplyProjects){
                StockOutApplyProject stockOutApplyProject = new StockOutApplyProject();
                stockOutApplyProject.setStatus(Constants.VALID);
                stockOutApplyProject.setProject(s.getProject());
                stockOutApplyProject.setStockOutApply(stockOutApplyChild);
                stockOutApplyProjectList.add(stockOutApplyProject);
            }
        }
        stockOutApplyProjectRepository.save(stockOutApplyProjects);
        return stockOutApplyDTO;
    }

    /**
     * 出库申请批准
     * @param id
     * @param stockOutApplyForApprove
     * @return
     */
    @Override
    public StockOutApplyDTO approveStockOutApply(Long id, StockOutApplyForApprove stockOutApplyForApprove) {
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        if(stockOutApply == null){
            throw new BankServiceException("未查询到出库申请！");
        }
        Long approveId = stockOutApplyForApprove.getApproverId();
        String password = stockOutApplyForApprove.getPassword();
        User user = userRepository.findOne(approveId);
        if(user == null){
            throw new BankServiceException("申请人不存在！");
        }
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new BankServiceException("用户名与密码不一致！");
        }
        LocalDate date = stockOutApplyForApprove.getApproveTime();
        stockOutApply.setApproverId(approveId);
        stockOutApply.setApproveTime(date);
        stockOutApply.setStatus(Constants.STOCK_OUT_APPROVED);
        stockOutApplyRepository.save(stockOutApply);
        return stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);
    }

    /**
     * 打印出库申请
     * @param id
     * @return
     */
    @Override
    public ByteArrayOutputStream printStockOutApply(Long id) {
        StockOutApplyReportDTO applyDTO = new StockOutApplyReportDTO();
        applyDTO = createApplyReportDTO(id);
        ByteArrayOutputStream outputStream = reportExportingService.makeStockOutRequirementReport(applyDTO);
        return outputStream;
    }

    private StockOutApplyReportDTO createApplyReportDTO(Long id) {
        StockOutApplyReportDTO applyDTO = new StockOutApplyReportDTO();
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        if(stockOutApply.getStatus()!=Constants.STOCK_OUT_APPROVED){
            stockOutApply.setStatus(Constants.STOCK_OUT_PENDING_APPROVAL);
            stockOutApply.setApplyDate(LocalDate.now());
            stockOutApplyRepository.save(stockOutApply);
        }
        List<String> projects = new ArrayList<String>();
        Integer countOfSample = 0;
        Integer countOfStockOutSample = 0;
        //获取授权的项目
        List<StockOutApplyProject> stockOutApplyProjects = stockOutApplyProjectRepository.findByStockOutApplyId(id);
        for(StockOutApplyProject s :stockOutApplyProjects){
            projects.add(s.getProject().getProjectCode());
        }
        //获取申请的需求
        List<StockOutRequirementReportDTO> requirements = new ArrayList<StockOutRequirementReportDTO>();
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findByStockOutApplyId(id);
        for(StockOutRequirement requirement : stockOutRequirementList){
            StockOutRequirementReportDTO stockOutRequirementReportDTO = new StockOutRequirementReportDTO();
            //获取指定样本
            List<StockOutRequiredSample> stockOutRequiredSamples = stockOutRequiredSampleRepository.findByStockOutRequirementId(requirement.getId());
            stockOutRequirementReportDTO.setId(requirement.getId());
            stockOutRequirementReportDTO.setCountOfSample(requirement.getCountOfSample());
            stockOutRequirementReportDTO.setRequirementName(requirement.getRequirementName());
            stockOutRequirementReportDTO.setMemo(requirement.getMemo());
            stockOutRequirementReportDTO.setCountOfStockOutSample(stockOutRequiredSamples.size());
            requirements.add(stockOutRequirementReportDTO);
            countOfSample += requirement.getCountOfSample();
            countOfStockOutSample += stockOutRequiredSamples.size();
        }
        applyDTO.setId(id);
        applyDTO.setCountOfSample(countOfSample);
        applyDTO.setApplicantName(stockOutApply.getApplyPersonName());
        applyDTO.setApplicationDate(stockOutApply.getApplyDate()!=null?stockOutApply.getApplyDate():LocalDate.now());
        applyDTO.setApplyCompany(stockOutApply.getDelegate()!=null?stockOutApply.getDelegate().getDelegateName():null);
        applyDTO.setApplyNumber(stockOutApply.getApplyCode());
        applyDTO.setCountOfStockOutSample(countOfStockOutSample);
        applyDTO.setEndDate(stockOutApply.getEndTime());
        applyDTO.setMemo(stockOutApply.getMemo());
        applyDTO.setProjects(projects);
        applyDTO.setPurposeOfSample(stockOutApply.getPurposeOfSample());
        applyDTO.setRecordDate(stockOutApply.getRecordTime());
        if(stockOutApply.getRecordId()!=null){
            User user = userRepository.findOne(stockOutApply.getRecordId());
            applyDTO.setRecorderName(user!=null?user.getLastName()+user.getFirstName():null);
        }
        applyDTO.setStartDate(stockOutApply.getStartTime());
        applyDTO.setRequirements(requirements);
        return applyDTO;
    }

    /**
     * 复原核对信息
     * @param id
     * @return
     */
    @Override
    public void revertStockOutRequirementCheck(Long id) {
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findByStockOutApplyId(id);
        for(StockOutRequirement s : stockOutRequirementList){
            stockOutRequirementService.revertStockOutRequirement(s.getId());
        }
    }

    @Override
    public StockOutApplyDetail getStockOutDetailAndRequirementByPlanId(Long id) {
        StockOutPlan stockOutPlan = stockOutPlanRepository.findOne(id);
        if(stockOutPlan == null){
            throw new BankServiceException("计划不存在！");
        }

        return this.getStockOutDetailAndRequirement(stockOutPlan.getStockOutApply().getId());
    }

    @Override
    public List<StockOutApplyDTO> getAllStockOutApplies() {
        List<StockOutApply> stockOutApplies = stockOutApplyRepository.findAllNotHandOverApply();
        return stockOutApplyMapper.stockOutAppliesToStockOutApplyDTOs(stockOutApplies);
    }
}
