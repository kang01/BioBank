package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.FrozenBoxCheckService;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.service.UserService;
import org.fwoxford.service.dto.FrozenTubeDTO;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.StockOutFrozenBoxMapper;
import org.fwoxford.service.mapper.StockOutTaskMapper;
import org.fwoxford.web.rest.StockOutFrozenBoxPoisition;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutFrozenBox.
 */
@Service
@Transactional
public class StockOutFrozenBoxServiceImpl implements StockOutFrozenBoxService{

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenBoxServiceImpl.class);

    private final StockOutFrozenBoxRepository stockOutFrozenBoxRepository;

    private final StockOutFrozenBoxMapper stockOutFrozenBoxMapper;
    @Autowired
    StockOutTaskRepository stockOutTaskRepository;

    @Autowired
    FrozenBoxRepository frozenBoxRepository;

    @Autowired
    StockOutBoxTubeRepository stockOutBoxTubeRepository;

    @Autowired
    FrozenTubeRepository frozenTubeRepository;

    @Autowired
    FrozenBoxTypeRepository frozenBoxTypeRepository;

    @Autowired
    FrozenTubeMapper frozenTubeMapper;

    @Autowired
    FrozenBoxMapper frozenBoxMapper;

    @Autowired
    StockOutHandoverRepository stockOutHandoverRepository;

    @Autowired
    StockOutBoxPositionRepository stockOutBoxPositionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    StockOutTaskMapper stockOutTaskMapper;

    @Autowired
    ReportExportingService reportExportingService;

    @Autowired
    StockOutApplyRepository stockOutApplyRepository;

    @Autowired
    StockOutHandoverFrozenBoxRepositries stockOutHandoverFrozenBoxRepositries;

    @Autowired
    StockOutPlanRepository stockOutPlanRepository;

    @Autowired
    StockOutFrozenBoxForWaitingRepositries stockOutFrozenBoxForWaitingRepositries;

    @Autowired
    StockOutFrozenBoxInTaskRepositries stockOutFrozenBoxInTaskRepositries;

    @Autowired
    StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    FrozenBoxCheckService frozenBoxCheckService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    SampleTypeRepository sampleTypeRepository;

    @Autowired
    ProjectRepository projectRepository ;
    @Autowired
    ProjectSampleClassRepository projectSampleClassRepository;

    public StockOutFrozenBoxServiceImpl(StockOutFrozenBoxRepository stockOutFrozenBoxRepository
            , StockOutFrozenBoxMapper stockOutFrozenBoxMapper) {
        this.stockOutFrozenBoxRepository = stockOutFrozenBoxRepository;
        this.stockOutFrozenBoxMapper = stockOutFrozenBoxMapper;
    }

    /**
     * Save a stockOutFrozenBox.
     *
     * @param stockOutFrozenBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutFrozenBoxDTO save(StockOutFrozenBoxDTO stockOutFrozenBoxDTO) {
        log.debug("Request to save StockOutFrozenBox : {}", stockOutFrozenBoxDTO);
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxMapper.stockOutFrozenBoxDTOToStockOutFrozenBox(stockOutFrozenBoxDTO);
        stockOutFrozenBox = stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        StockOutFrozenBoxDTO result = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);
        return result;
    }

    /**
     *  Get all the stockOutFrozenBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutFrozenBoxes");
        Page<StockOutFrozenBox> result = stockOutFrozenBoxRepository.findAll(pageable);
        return result.map(stockOutFrozenBox -> stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox));
    }

    /**
     *  Get one stockOutFrozenBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutFrozenBoxDTO findOne(Long id) {
        log.debug("Request to get StockOutFrozenBox : {}", id);
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(id);
        StockOutFrozenBoxDTO stockOutFrozenBoxDTO = stockOutFrozenBoxMapper.stockOutFrozenBoxToStockOutFrozenBoxDTO(stockOutFrozenBox);
        return stockOutFrozenBoxDTO;
    }

    /**
     *  Delete the  stockOutFrozenBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutFrozenBox : {}", id);
        stockOutFrozenBoxRepository.delete(id);
    }
    @Override
    public List<StockOutFrozenBoxForTaskDataTableEntity> getAllStockOutFrozenBoxesByTask(Long taskId) {
        List<StockOutFrozenBoxForTaskDataTableEntity> alist = new ArrayList<StockOutFrozenBoxForTaskDataTableEntity>();
        //根据待出库的样本查询出待出库的冻存盒
        List<FrozenBox> boxes = stockOutReqFrozenTubeRepository.findByStockOutTaskIdAndStatus(taskId,Constants.STOCK_OUT_SAMPLE_IN_USE);

        //此任务内待出库的冻存盒和样本量
        List<Object[]> countByTaskGroupByBox = stockOutReqFrozenTubeRepository.countByTaskGroupByBox(taskId);

        Map<Long, List<FrozenBox>> boxesGroupByBoxId = boxes.stream().collect(Collectors.groupingBy(s->s.getId()));
        for(Long boxId : boxesGroupByBoxId.keySet()){
            StockOutFrozenBoxForTaskDataTableEntity box = new StockOutFrozenBoxForTaskDataTableEntity();
            FrozenBox frozenBox = boxesGroupByBoxId.get(boxId).get(0);

            Object[] obje = countByTaskGroupByBox.stream().filter(s->Long.valueOf(s[0].toString()).equals(boxId)).findFirst().orElse(null);
            Long count = obje!=null?Long.valueOf(obje[1].toString()):0;
            box.setId(frozenBox.getId());
            box.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            box.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
            box.setSampleTypeName(frozenBox.getSampleTypeName());
            box.setMemo(frozenBox.getMemo());
            box.setSampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null);
            box.setStatus(frozenBox.getStatus());
            box.setProjectName(frozenBox.getProjectName());
            box.setProjectCode(frozenBox.getProjectCode());
            String position = BankUtil.getPositionString(frozenBox);
            box.setPosition(position);
            box.setCountOfSample(count);
            alist.add(box);
        }
        return alist;
    }

    /**
     * 临时盒的保存
     * @param frozenBoxDTO
     * @return
     */
    @Override
    public List<StockOutFrozenBoxDTO> createFrozenBoxForStockOut(List<FrozenBoxAndFrozenTubeResponse> frozenBoxDTO, Long taskId) {
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(taskId);
        if(stockOutTask == null){
            throw new BankServiceException("任务ID无效！");
        }
        List<FrozenBoxType> boxTypes = frozenBoxTypeRepository.findAllFrozenBoxTypes();
        List<StockOutFrozenBox> stockOutFrozenBoxs = new ArrayList<>();
        for(FrozenBoxAndFrozenTubeResponse box:frozenBoxDTO){
            if(box.getFrozenBoxCode() == null){
                throw new BankServiceException("冻存盒编码不能为空！");
            }
            if(box.getSampleTypeId()==null){
                throw new BankServiceException("样本类型不能为空！");
            }
            if(StringUtils.isEmpty(box.getProjectCode())){
                throw new BankServiceException("项目编码不能为空！");
            }
            //验证盒子编码是否存在
            Map<String,Long> map = new HashMap<String,Long>();
            Long id = box.getId()==null?-1:box.getId();
            map.put(box.getFrozenBoxCode(),id);
            frozenBoxCheckService.checkFrozenBoxCodeRepead(map);
            FrozenBox frozenBox = new FrozenBox();
            if(box.getId()!=null){
                frozenBox = frozenBoxRepository.findOne(box.getId());
                if(frozenBox == null){
                    frozenBox =  new FrozenBox();
                }
            }
            //验证冻存盒类型是否一致
            FrozenBoxType boxType = new FrozenBoxType();
            if (box.getFrozenBoxTypeId() != null) {
                FrozenBoxType frozenBoxType = new FrozenBoxType();
                frozenBoxType.setId(box.getFrozenBoxTypeId());
                int boxTypeIndex = boxTypes.indexOf(frozenBoxType);
                if (boxTypeIndex >= 0) {
                    boxType = boxTypes.get(boxTypeIndex);
                    frozenBox.setFrozenBoxType(boxType);
                    frozenBox.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
                    frozenBox.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
                    frozenBox.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
                }else {
                    throw new BankServiceException("冻存盒类型不能为空！",box.toString());
                }
            }
            Project project = projectRepository.findByProjectCode(box.getProjectCode());
            if(project == null){
                throw new BankServiceException("项目"+box.getProjectCode()+"不存在！");
            }
            frozenBox.project(project).projectCode(project.getProjectCode()).projectName(project.getProjectName());
            SampleType sampleType = sampleTypeRepository.findOne(box.getSampleTypeId());
            if(sampleType == null){
                throw new BankServiceException("样本类型不能为空！",box.getSampleTypeId().toString());
            }
            frozenBox.sampleType(sampleType).sampleTypeName(sampleType.getSampleTypeName()).sampleTypeCode(sampleType.getSampleTypeCode());
            List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findByProjectIdAndSampleTypeId(project.getId(),sampleType.getId());
            if(projectSampleClasses!=null && projectSampleClasses.size()>0){
                if(box.getSampleClassificationId() == null){
                    throw new BankServiceException(project.getProjectCode()+"项目下，"+sampleType.getSampleTypeName()+"类型，已经配置了样本分类，样本分类不能为空！");
                }
                ProjectSampleClass projectSampleClass = projectSampleClasses.stream().filter(s->
                        s.getProject().getId().equals(project.getId())&& s.getSampleClassification().equals(box.getSampleClassificationId())).findFirst().orElse(null);
                if(projectSampleClass == null){
                    throw new BankServiceException("样本分类不存在！");
                }
                frozenBox.setSampleClassification(projectSampleClass.getSampleClassification());
            }
            //验证冻存盒数量是否错误（不能超过冻存盒的最大规格）
            String columns = boxType.getFrozenBoxTypeColumns()!=null?boxType.getFrozenBoxTypeColumns():new String("0");
            String rows = boxType.getFrozenBoxTypeRows()!=null?boxType.getFrozenBoxTypeRows():new String("0");
            int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
            if(box.getFrozenTubeDTOS().size()==0){
                continue;
            }
            if(box.getFrozenTubeDTOS().size()>allCounts){
                throw new BankServiceException("临时盒中冻存管数量错误！",frozenBoxDTO.toString());
            }

            frozenBox.frozenBoxCode(box.getFrozenBoxCode()).frozenBoxCode1D(box.getFrozenBoxCode1D()).status(Constants.FROZEN_BOX_STOCK_OUT_PENDING);
            frozenBoxRepository.save(frozenBox);
            box.setId(frozenBox.getId());
            //保存出库盒
            StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findByFrozenBoxIdAndStockOutTaskId(frozenBox.getId(),taskId);
            if(stockOutFrozenBox == null){
                stockOutFrozenBox = new StockOutFrozenBox();
            }
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_NEW);
            stockOutFrozenBox.setFrozenBox(frozenBox);
            stockOutFrozenBox.setStockOutTask(stockOutTask);
            stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode()).sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName());
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
            stockOutFrozenBoxs.add(stockOutFrozenBox);
            List<Long> frozenTubeIds = new ArrayList<>();
            for(FrozenTubeDTO f: box.getFrozenTubeDTOS()){
                frozenTubeIds.add(f.getId());
            }
            //查询要出库的样本
            List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutTaskIdAndFrozenTubeIdInAndStatusNot(taskId,frozenTubeIds,Constants.STOCK_OUT_SAMPLE_WAITING_OUT);
            List<FrozenTube> frozenTubeList = new ArrayList<>();
            //出库的样本原来所在的冻存盒的ID，为了更改原来冻存盒内样本的数量
            List<Long> stockOutFromBoxId = new ArrayList<>();
            List<FrozenBox>  stockOutFromBoxList = new ArrayList<>();
            for(FrozenTubeDTO f: box.getFrozenTubeDTOS()){
                for(StockOutReqFrozenTube s:stockOutReqFrozenTubes){
                    if(f.getId().equals(s.getFrozenTube().getId())){
                        FrozenBox frozenBoxOld = s.getFrozenBox();
                        if(!stockOutFromBoxId.contains(frozenBoxOld.getId())){
                            stockOutFromBoxId.add(frozenBoxOld.getId());
                            stockOutFromBoxList.add(frozenBoxOld);
                        }
                        //更改出库样本所属的冻存盒，位置，状态
                        s.stockOutFrozenBox(stockOutFrozenBox).status(Constants.STOCK_OUT_SAMPLE_WAITING_OUT)
                            .frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_PENDING).tubeColumns(f.getTubeColumns()).tubeRows(f.getTubeRows())
                            .frozenBox(frozenBox).frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
                        //更改样本所属的冻存盒以及盒内的位置
                        FrozenTube frozenTube =s.getFrozenTube()
                            .frozenBox(frozenBox).frozenBoxCode(frozenBox.getFrozenBoxCode()).tubeColumns(f.getTubeColumns()).tubeRows(f.getTubeRows()).frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_PENDING);
                        if(StringUtils.isEmpty(frozenTube.getProjectCode())
                                ||(!StringUtils.isEmpty(frozenTube.getProjectCode()) && !frozenTube.getProjectCode().equals(project.getProjectCode())) ){
                            throw new BankServiceException("样本"+frozenTube.getSampleCode()+"与冻存盒所属项目不一致！");
                        }
                        frozenTubeList.add(frozenTube);
                    }
                }
            }
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
            frozenTubeRepository.save(frozenTubeList);

            //更改出库样本原来所属的冻存盒的当前样本量
            List<Object[]> countAllSampleByfrozenBoxIds =  frozenTubeRepository.countGroupByFrozenBoxId(stockOutFromBoxId);
            for(FrozenBox boxFrom: stockOutFromBoxList){
                Object[] obje = countAllSampleByfrozenBoxIds.stream().filter(s->Long.valueOf(s[0].toString()).equals(boxFrom.getId())).findFirst().orElse(null);
                Integer count = obje!=null?Integer.valueOf(obje[1].toString()):0;
                frozenBox.countOfSample(count);
            }
            frozenBoxRepository.save(stockOutFromBoxList);
            //创建的新的出库冻存盒的样本数量
            Long countOfSample = frozenTubeRepository.countByFrozenBoxIdAndStatusNot(frozenBox.getId(),Constants.INVALID);
            frozenBox.countOfSample(countOfSample.intValue());
            frozenBoxRepository.save(frozenBox);
            stockOutFrozenBox.countOfSample(countOfSample.intValue());
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        }
        return stockOutFrozenBoxMapper.stockOutFrozenBoxesToStockOutFrozenBoxDTOs(stockOutFrozenBoxs);
    }

    @Override
    public List<FrozenBoxAndFrozenTubeResponse> getAllTempStockOutFrozenBoxesByTask(Long taskId) {
        List<FrozenBoxAndFrozenTubeResponse> alist = new ArrayList<FrozenBoxAndFrozenTubeResponse>();
        List<StockOutFrozenBox> boxes =  stockOutFrozenBoxRepository.findByStockOutTaskIdAndStatus(taskId,Constants.STOCK_OUT_FROZEN_BOX_NEW);
        for(StockOutFrozenBox s :boxes){
            FrozenBox frozenBox = s.getFrozenBox();
            if(frozenBox ==null){continue;}
            //根据冻存盒编码查询冻存管
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
            FrozenBoxType boxType = frozenBox.getFrozenBoxType();
            String columns = boxType.getFrozenBoxTypeColumns()!=null?boxType.getFrozenBoxTypeColumns():new String("0");
            String rows = boxType.getFrozenBoxTypeRows()!=null?boxType.getFrozenBoxTypeRows():new String("0");
            int allCounts = Integer.parseInt(columns) * Integer.parseInt(rows);
            if(frozenTubes.size()==allCounts){
                continue;
            }
            List<FrozenTubeDTO> frozenTubeResponse = frozenTubeMapper.frozenTubesToFrozenTubeDTOs(frozenTubes);
            FrozenBoxAndFrozenTubeResponse box = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox);
            box.setFrozenTubeDTOS(frozenTubeResponse);
            alist.add(box);
        }
        return alist;
    }

    @Override
    public List<StockOutFrozenBoxDataTableEntity> getStockOutFrozenBoxesByTask(Long taskId) {
        List<StockOutFrozenBoxDataTableEntity> alist = new ArrayList<StockOutFrozenBoxDataTableEntity>();
        List<StockOutFrozenBox> boxes =  stockOutFrozenBoxRepository.findByStockOutTaskId(taskId);
        for(StockOutFrozenBox f :boxes){
            FrozenBox frozenBox = f.getFrozenBox();
            StockOutFrozenBoxDataTableEntity box = new StockOutFrozenBoxDataTableEntity();
            if(frozenBox ==null){continue;}
            box.setId(f.getId());
            box.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            box.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
            box.setSampleTypeName(frozenBox.getSampleTypeName());
            String position = BankUtil.getPositionString(f.getEquipmentCode(),f.getAreaCode(),f.getSupportRackCode(),f.getColumnsInShelf(),f.getRowsInShelf(),null,null);
            box.setPosition(position);
            box.setCountOfSample(f.getCountOfSample()!=null?Long.valueOf(f.getCountOfSample()):0L);
            box.setMemo(f.getMemo());
            box.setStatus(f.getStatus());
            box.setProjectCode(f.getProjectCode());
            box.setProjectName(f.getProjectName());
            box.setStockOutHandoverTime(f.getHandoverTime());
            alist.add(box);
        }
        return alist;
    }

    /**
     * 出库
     * @param stockOutFrozenBoxPoisition
     * @param taskId
     * @param frozenBoxIds
     * @return
     */
    @Override
    public StockOutTaskDTO stockOut(StockOutFrozenBoxPoisition stockOutFrozenBoxPoisition, Long taskId, List<Long> frozenBoxIds) {
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(taskId);
        if(stockOutTask == null){
            throw new BankServiceException("出库任务不存在！");
        }
        //验证两个出库负责人密码
        Long loginId1 = stockOutTask.getStockOutHeadId1();
        Long loginId2 = stockOutTask.getStockOutHeadId2();
        if(loginId1 == null || loginId2 == null){
            throw new BankServiceException("出库负责人不能为空！");
        }
        User user1 = userRepository.findOne(loginId1);
        User user2 = userRepository.findOne(loginId2);
        String loginName1 = user1.getLogin();
        String loginName2 =  user2.getLogin();
        String password1 = stockOutFrozenBoxPoisition.getPassword1();
        String password2 = stockOutFrozenBoxPoisition.getPassword2();
        Long equipmentId = stockOutFrozenBoxPoisition.getEquipmentId();
        Long areaId = stockOutFrozenBoxPoisition.getAreaId();

        Equipment equipment = new Equipment();
        if(equipmentId != null){
            equipment = equipmentRepository.findOne(equipmentId);
        }

        Area area = new Area();
        if(areaId != null){
            area = areaRepository.findOne(areaId);
        }
        if(password1 == null || password2 == null){
            throw new BankServiceException("出库负责人密码不能为空！");
        }
        if(loginName1!=null&&password1!=null){
            userService.isCorrectUser(loginName1,password1);
        }
        if(loginName2!=null&&password2!=null){
            userService.isCorrectUser(loginName2,password2);
        }
        List<FrozenBox> frozenBoxList = new ArrayList<>();
        List<StockOutBoxPosition> stockOutBoxPositionList = new ArrayList<StockOutBoxPosition>();
        List<StockOutFrozenBox> stockOutFrozenBoxes = stockOutFrozenBoxRepository.findByIdIn(frozenBoxIds);
        for(StockOutFrozenBox stockOutFrozenBox : stockOutFrozenBoxes){
            FrozenBox frozenBox = stockOutFrozenBox.getFrozenBox();
            Long countOfStockOutSample = stockOutReqFrozenTubeRepository.countByStockOutFrozenBoxId(stockOutFrozenBox.getId());
            //保存出库冻存盒
            stockOutFrozenBox.status(Constants.STOCK_OUT_FROZEN_BOX_COMPLETED).frozenBox(frozenBox).equipment(equipment)
                .equipmentCode(equipment!=null?equipment.getEquipmentCode():null).area(area).areaCode(area!=null?area.getAreaCode():null);
            stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName()).memo(frozenBox.getMemo()).countOfSample(countOfStockOutSample.intValue());
            //保存冻存盒位置
            StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition()
                .stockOutFrozenBox(stockOutFrozenBox).equipment(equipment).equipmentCode(equipment!=null?equipment.getEquipmentCode():null)
                .area(area).areaCode(area!=null?area.getAreaCode():null).status(Constants.VALID);
            stockOutBoxPositionList.add(stockOutBoxPosition);
            //保存冻存盒的位置和状态
            frozenBox.equipment(equipment).equipmentCode(equipment!=null?equipment.getEquipmentCode():null)
                .area(area).areaCode(area!=null?area.getAreaCode():null).status(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED);
            frozenBoxList.add(frozenBox);
        }
        stockOutFrozenBoxRepository.save(stockOutFrozenBoxes);
        stockOutBoxPositionRepository.save(stockOutBoxPositionList);
        frozenBoxRepository.save(frozenBoxList);
        //查询出已经出库的样本
        List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutFrozenBoxIdIn(frozenBoxIds);

        //更改出库样本状态为已出库
        List<List<Long>> frozenBoxs = Lists.partition(frozenBoxIds, 1000);
        for(List<Long> ids: frozenBoxs){
            StringBuffer sql = new StringBuffer();
            sql.append("update stock_out_req_frozen_tube t set t.status = ?1 ,t.frozen_tube_state = ?2 where  t.stock_out_frozen_box_id in ?3");
            Query query = entityManager.createNativeQuery(sql.toString());
            query.setParameter("1", Constants.STOCK_OUT_SAMPLE_COMPLETED);
            query.setParameter("2", Constants.FROZEN_BOX_STOCK_OUT_COMPLETED);
            query.setParameter("3", ids);
            query.executeUpdate();

        }
        //样本的ID集合
        List<Long> frozenTubeIds = new ArrayList<Long>();
        stockOutReqFrozenTubes.forEach(s->{
            frozenTubeIds.add(s.getFrozenTube().getId());
        });
        for(StockOutReqFrozenTube s: stockOutReqFrozenTubes){
            FrozenTube frozenTube = s.getFrozenTube();
            frozenTubeIds.add(frozenTube.getId());
        }
        //更改样本状态为已出库
        List<List<Long>> frozenTubes = Lists.partition(frozenTubeIds, 1000);
        for(List<Long> ids: frozenTubes){
            StringBuffer sqlForTube = new StringBuffer();
            sqlForTube.append("update frozen_tube t set t.frozen_tube_state = ?1 where t.id in ?2");
            Query queryForTube = entityManager.createNativeQuery(sqlForTube.toString());
            queryForTube.setParameter("1", Constants.FROZEN_BOX_STOCK_OUT_COMPLETED);
            queryForTube.setParameter("2", ids);
            queryForTube.executeUpdate();
        }

        //任务未出库样本量
        List<String> statusList = new ArrayList<>();
        statusList.add(Constants.STOCK_OUT_SAMPLE_IN_USE_NOT);
        statusList.add(Constants.STOCK_OUT_SAMPLE_COMPLETED);
        Long countOfTaskTube = stockOutReqFrozenTubeRepository.countByStockOutTaskIdAndStatusNotIn(taskId,statusList);
        //如果任务内的样本量都出库了，任务状态为已完成，如果出库样本中有异常出库的样本为异常出库
        if(countOfTaskTube.intValue()==0) {
            //异常出库样本量
            Long abNormalTube = stockOutReqFrozenTubeRepository.countAbnormalTubeByStockOutTaskId(taskId);
            if (abNormalTube.intValue() != 0) {
                stockOutTask.setStatus(Constants.STOCK_OUT_TASK_ABNORMAL);
            }
            stockOutTask.setStatus(Constants.STOCK_OUT_TASK_COMPLETED);
        }
        stockOutTaskRepository.save(stockOutTask);

        //未完成出库申请的冻存管数量
        List<String> statusList_ = new ArrayList<>();
        statusList_.add(Constants.STOCK_OUT_SAMPLE_IN_USE);
        statusList_.add(Constants.STOCK_OUT_SAMPLE_WAITING_OUT);
        Long countOfUnCompleteTask = stockOutReqFrozenTubeRepository.countUnCompleteSampleByStockOutApplyAndStatusIn(stockOutTask.getStockOutPlan().getStockOutApply().getId(),statusList_);
       //如果此次申请的出库样本都出库了，计划更改为已完成的状态，申请和出库是一对一的关系。
        if(countOfUnCompleteTask.intValue()==0){
            StockOutPlan stockOutPlan = stockOutTask.getStockOutPlan();
            stockOutPlan.setStatus(Constants.STOCK_OUT_PLAN_COMPLETED);
            stockOutPlanRepository.save(stockOutPlan);
        }
        return stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
    }

    @Override
    public StockOutFrozenBoxDTO stockOutNote(StockOutFrozenBoxDTO stockOutFrozenBoxDTO) {
        if(stockOutFrozenBoxDTO == null || stockOutFrozenBoxDTO.getId() == null ){
            throw new BankServiceException("出库盒ID不能为空！");
        }
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(stockOutFrozenBoxDTO.getId());
        if(stockOutFrozenBox == null){
            throw new BankServiceException("出库盒不存在！");
        }
        stockOutFrozenBox.setMemo(stockOutFrozenBoxDTO.getMemo());
        stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        return stockOutFrozenBoxDTO;
    }

    /**
     * 打印取盒单
     * @param taskId
     * @return
     */
    @Override
    public ByteArrayOutputStream printStockOutFrozenBox(Long taskId) {
        List<StockOutTakeBoxReportDTO> takeBoxDTOs =   createStockOutTakeBoxReportDTO(taskId);
        ByteArrayOutputStream outputStream = reportExportingService.makeStockOutTakeBoxReport(takeBoxDTOs);
        return outputStream;
    }

    private List<StockOutTakeBoxReportDTO> createStockOutTakeBoxReportDTO(Long taskId) {
        List<StockOutTakeBoxReportDTO> stockOutTakeBoxReportDTOS = new ArrayList<StockOutTakeBoxReportDTO>();
        //从任务出库样本中找到要出库的冻存盒
        List<FrozenBox> boxes =  frozenBoxRepository.findByStockOutTaskId(taskId);
        for(FrozenBox frozenBox :boxes){
            StockOutTakeBoxReportDTO stockOutTakeBoxReportDTO = new StockOutTakeBoxReportDTO();
            stockOutTakeBoxReportDTO.setId(frozenBox.getId());
            stockOutTakeBoxReportDTO.setAreaCode(frozenBox.getAreaCode());
            stockOutTakeBoxReportDTO.setEquipmentCode(frozenBox.getEquipmentCode());
            stockOutTakeBoxReportDTO.setShelfCode(frozenBox.getSupportRackCode());
            stockOutTakeBoxReportDTO.setBoxCode(frozenBox.getFrozenBoxCode());
            stockOutTakeBoxReportDTO.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
            stockOutTakeBoxReportDTO.setShelfLocation(frozenBox.getColumnsInShelf()+frozenBox.getRowsInShelf());
            stockOutTakeBoxReportDTOS.add(stockOutTakeBoxReportDTO);
        }

        return stockOutTakeBoxReportDTOS;
    }

    /**
     * 查询待交接冻存盒
     * @param id 申请ID
     * @param input
     * @return
     */

    @Override
    public DataTablesOutput<StockOutFrozenBoxForDataTableEntity> getPageWaitingHandOverStockOutFrozenBoxes(Long id, DataTablesInput input) {
        StockOutApply stockOutApply = stockOutApplyRepository.findOne(id);
        if(stockOutApply == null){
            throw new BankServiceException("未查询到申请单");
        }
        input.setLength(-1);
        input.getColumns().forEach(column -> {
            if(column.getData().equals("applyId") || column.getData().equals("planId") || column.getData().equals("taskId")){
                if(!column.getSearch().getValue().equals(null)&&!column.getSearch().getValue().equals("")){
                    column.setSearchValue(column.getSearch().getValue()+"+");
                }
            }
        });

        DataTablesOutput<StockOutFrozenBoxForDataTableEntity> output = stockOutHandoverFrozenBoxRepositries.findAll(input);
        List<StockOutFrozenBoxForDataTableEntity> stockOutFrozenBoxForDataTableEntities = new ArrayList<StockOutFrozenBoxForDataTableEntity>();
        output.getData().forEach(entity -> {
            StockOutFrozenBoxForDataTableEntity dataTableEntity = new StockOutFrozenBoxForDataTableEntity();
            String position = BankUtil.getPositionString(entity.getEquipmentCode(),entity.getAreaCode(),entity.getSupportRackCode(),entity.getColumnsInShelf(),entity.getRowsInShelf(),null,null);
            dataTableEntity.setPosition(position);
            dataTableEntity.setId(entity.getId());
            dataTableEntity.setPlanCode(entity.getPlanCode());
            dataTableEntity.setFrozenBoxCode(entity.getFrozenBoxCode());
            dataTableEntity.setFrozenBoxCode1D(entity.getFrozenBoxCode1D());
            dataTableEntity.setPlanId(entity.getId());
            dataTableEntity.setApplyCode(entity.getApplyCode());
            dataTableEntity.setApplyId(entity.getApplyId());
            dataTableEntity.setTaskId(entity.getTaskId());
            dataTableEntity.setTaskCode(entity.getTaskCode());
            dataTableEntity.setCountOfSample(entity.getCountOfSample());
            dataTableEntity.setDelegateId(entity.getDelegateId());
            dataTableEntity.setDelegate(entity.getDelegate());
            dataTableEntity.setMemo(entity.getMemo());
            dataTableEntity.setSampleTypeName(entity.getSampleTypeName());
            dataTableEntity.setStatus(entity.getStatus());
            stockOutFrozenBoxForDataTableEntities.add(dataTableEntity);
        });
        output.setData(stockOutFrozenBoxForDataTableEntities);
        return output;
    }

    /**
     * 查询已交接冻存盒
     * @param id
     * @param pageRequest
     * @return
     */
    @Override
    public Page<StockOutFrozenBoxForDataTableEntity> getPageHandoverStockOutFrozenBoxes(Long id, Pageable pageRequest) {
      return null;
    }

    /**
     * 分页查询根据需求查询需要出库的冻存盒
     * @param ids 需求ID串
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<FrozenBoxForStockOutDataTableEntity> getPageByRequirementIds(List<Long> ids, DataTablesInput input) {
        Specification<FrozenBoxForStockOutDataTableEntity> specification = new Specification<FrozenBoxForStockOutDataTableEntity>() {
            @Override
            public Predicate toPredicate(Root<FrozenBoxForStockOutDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                CriteriaBuilder.In<Long> in  = cb.in(root.get("stockOutRequirementId"));
                for(Long id :ids){
                    in.value(id);
                }
                predicate.add(in);
                Predicate[] pre = new Predicate[predicate.size()];
                query.where(predicate.toArray(pre));
                return query.getRestriction();
            }
        };
        DataTablesOutput<FrozenBoxForStockOutDataTableEntity> output = stockOutFrozenBoxForWaitingRepositries.findAll(input,specification);
        List<FrozenBoxForStockOutDataTableEntity> alist = new ArrayList<FrozenBoxForStockOutDataTableEntity>();
        List<String> boxCodes = new ArrayList<>();
        output.getData().forEach(s->{
            if(!boxCodes.contains(s.getFrozenBoxCode())){
                boxCodes.add(s.getFrozenBoxCode());
                FrozenBoxForStockOutDataTableEntity dto = new FrozenBoxForStockOutDataTableEntity();
                dto.setId(s.getId());
                dto.setFrozenBoxCode(s.getFrozenBoxCode());
                dto.setFrozenBoxCode1D(s.getFrozenBoxCode1D());
                dto.setSampleTypeName(s.getSampleTypeName());
                String position =  BankUtil.getPositionString(s.getEquipmentCode(),s.getAreaCode(),s.getSupportRackCode(),s.getColumnsInShelf(),s.getRowsInShelf(),null,null);
                dto.setPosition(position);
                dto.setCountOfSample(s.getCountOfSample());
                dto.setStockOutRequirementId(s.getStockOutRequirementId());
                alist.add(dto);
            }else{
                long filterRecord = output.getRecordsFiltered()-1;
                output.setRecordsFiltered(filterRecord);
            }
        });
        output.setData(alist);
        return output;
    }

    /**
     * 分页查询某个计划的出库任务
     * @param id
     * @param input
     * @return
     */
    @Override
    public DataTablesOutput<StockOutFrozenBoxForTaskDetailDataTableEntity> getPageByTask(Long id, DataTablesInput input) {
        input.addColumn("stockOutTaskId", true, true, id+"+");
        DataTablesOutput<StockOutFrozenBoxForTaskDetailDataTableEntity> output = stockOutFrozenBoxInTaskRepositries.findAll(input);
        List<StockOutFrozenBoxForTaskDetailDataTableEntity> alist = new ArrayList<StockOutFrozenBoxForTaskDetailDataTableEntity>();
        output.getData().forEach(s->{
            StockOutFrozenBoxForTaskDetailDataTableEntity dto = new StockOutFrozenBoxForTaskDetailDataTableEntity();
            dto.setId(s.getId());
            dto.setFrozenBoxCode(s.getFrozenBoxCode());
            dto.setFrozenBoxCode1D(s.getFrozenBoxCode1D());
            dto.setSampleTypeName(s.getSampleTypeName());
            String position =  BankUtil.getPositionString(s.getEquipmentCode(),s.getAreaCode(),s.getSupportRackCode(),s.getColumnsInShelf(),s.getRowsInShelf(),null,null);
            dto.setPosition(position);
            dto.setCountOfSample(s.getCountOfSample());
            alist.add(dto);
        });
        output.setData(alist);
        return output;
    }
}
