package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutApplyService;
import org.fwoxford.service.dto.StockOutApplyDTO;
import org.fwoxford.service.dto.StockOutApplyProjectDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    public StockOutApplyDTO initStockOutApply() {
        StockOutApply stockOutApply = new StockOutApply();
        stockOutApply.setStatus(Constants.STOCK_OUT_PENDING);
        stockOutApply.setApplyCode(BankUtil.getUniqueID());
        stockOutApplyRepository.save(stockOutApply);
        return stockOutApplyMapper.stockOutApplyToStockOutApplyDTO(stockOutApply);
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
       for(StockOutApplyProject s :stockOutApplyProjects){
           projectIds.add(s.getProject().getId());
       }
       res.setProjectIds(projectIds);
       //获取申请的需求
        List<StockOutRequirementForApplyTable> stockOutRequirementForApplyTables = new ArrayList<StockOutRequirementForApplyTable>();
        List<StockOutRequirement> stockOutRequirementList = stockOutRequirementRepository.findByStockOutApplyId(id);
        for(StockOutRequirement requirement : stockOutRequirementList){
            StockOutRequirementForApplyTable stockOutRequirementForApplyTable = new StockOutRequirementForApplyTable();
            stockOutRequirementForApplyTable.setId(requirement.getId());
            stockOutRequirementForApplyTable.setStatus(requirement.getStatus());
            stockOutRequirementForApplyTable.setCountOfSample(requirement.getCountOfSample());

            //获取指定样本
            List<StockOutRequiredSample> stockOutRequiredSamples = stockOutRequiredSampleRepository.findByStockOutRequirementId(requirement.getId());
            StringBuffer samples = new StringBuffer();
            for(StockOutRequiredSample s :stockOutRequiredSamples){
                samples.append(s.getSampleCode());
                samples.append("-");
                samples.append(s.getSampleType());
                samples.append(",");
            }
            if(samples.length()>0){
                String samplesStr = samples.substring(0,samples.length()-1);
                stockOutRequirementForApplyTable.setSamples(samplesStr);
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
        }
        res.setStockOutRequirement(stockOutRequirementForApplyTables);
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
}
