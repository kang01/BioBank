package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import liquibase.util.StringUtils;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.ReportExportingService;
import org.fwoxford.service.StockOutHandoverService;
import org.fwoxford.service.dto.StockOutHandoverDTO;
import org.fwoxford.service.dto.response.StockOutHandoverForDataTableEntity;
import org.fwoxford.service.dto.response.StockOutHandoverReportDTO;
import org.fwoxford.service.dto.response.StockOutHandoverSampleReportDTO;
import org.fwoxford.service.mapper.StockOutHandoverMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOutHandover.
 */
@Service
@Transactional
public class StockOutHandoverServiceImpl implements StockOutHandoverService{

    private final Logger log = LoggerFactory.getLogger(StockOutHandoverServiceImpl.class);

    private final StockOutHandoverRepository stockOutHandoverRepository;

    private final StockOutHandoverMapper stockOutHandoverMapper;

    @Autowired
    private StockOutTaskRepository stockOutTaskRepository;

    @Autowired
    private StockOutBoxTubeRepository stockOutBoxTubeRepository;

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;

    @Autowired
    private StockOutHandoverDetailsRepository stockOutHandoverDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportExportingService reportExportingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StockOutFrozenBoxRepository stockOutFrozenBoxRepository;

    @Autowired
    private StockOutHandoverRepositries stockOutHandoverRepositries;

    @Autowired
    private StockOutHandoverSampleRepositries stockOutHandoverSampleRepositries;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;
    @Autowired
    private BankUtil bankUtil;

    @Autowired
    private StockOutHandoverBoxRepository stockOutHandoverBoxRepository;

    @Autowired
    private StockOutApplyRepository stockOutApplyRepository;

    public StockOutHandoverServiceImpl(StockOutHandoverRepository stockOutHandoverRepository, StockOutHandoverMapper stockOutHandoverMapper) {
        this.stockOutHandoverRepository = stockOutHandoverRepository;
        this.stockOutHandoverMapper = stockOutHandoverMapper;
    }

    /**
     * Save a stockOutHandover.
     *
     * @param stockOutHandoverDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockOutHandoverDTO save(StockOutHandoverDTO stockOutHandoverDTO) {
        log.debug("Request to save StockOutHandover : {}", stockOutHandoverDTO);
        StockOutHandover stockOutHandover = stockOutHandoverMapper.stockOutHandOverDTOToStockOutHandOver(stockOutHandoverDTO);
        if(stockOutHandoverDTO.getId()==null){
            stockOutHandover.setHandoverCode(bankUtil.getUniqueID("G"));
            stockOutHandover.setStatus(Constants.STOCK_OUT_HANDOVER_PENDING);
        }
        stockOutHandover = stockOutHandoverRepository.save(stockOutHandover);
        StockOutHandoverDTO result = stockOutHandoverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandover);
        return result;
    }

    /**
     *  Get all the stockOutHandovers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutHandoverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockOutHandovers");
        Page<StockOutHandover> result = stockOutHandoverRepository.findAll(pageable);
        return result.map(stockOutHandover -> stockOutHandoverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandover));
    }

    /**
     *  Get one stockOutHandover by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockOutHandoverDTO findOne(Long id) {
        log.debug("Request to get StockOutHandover : {}", id);
        StockOutHandover stockOutHandover = stockOutHandoverRepository.findOne(id);
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandover);
        if(stockOutHandover.getHandoverPersonId()!=null){
            User user = userRepository.findOne(stockOutHandover.getHandoverPersonId());
            stockOutHandoverDTO.setHandoverPersonName(user!=null?user.getLastName()+user.getFirstName():null);
        }
        Long countOfSample = stockOutHandoverDetailsRepository.countByStockOutHandoverId(id);
        stockOutHandoverDTO.setCountOfSample(countOfSample.intValue());
        stockOutHandoverDTO.setStockOutApplyCode(stockOutHandover.getStockOutApply()!=null?stockOutHandover.getStockOutApply().getApplyCode():null);
        stockOutHandoverDTO.setStockOutTaskCode(stockOutHandover.getStockOutTask()!=null?stockOutHandover.getStockOutTask().getStockOutTaskCode():null);
        stockOutHandoverDTO.setStockOutPlanCode(stockOutHandover.getStockOutPlan()!=null?stockOutHandover.getStockOutPlan().getStockOutPlanCode():null);
        return stockOutHandoverDTO;
    }

    /**
     *  Delete the  stockOutHandover by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockOutHandover : {}", id);
        stockOutHandoverRepository.delete(id);
    }

    /**
     * 根据任务创建交接单
     * @param taskId
     * @return
     */
    @Override
    public StockOutHandoverDTO saveByTask(Long taskId) {
        StockOutHandover stockOutHandover = new StockOutHandover();
        StockOutTask stockOutTask =  stockOutTaskRepository.findOne(taskId);
        if(stockOutTask ==null){
            throw new BankServiceException("任务不存在！");
        }
        //查询任务下是否还有待交接的冻存盒？？？

        //查询任务是否有未完成的交接单？？？？

        stockOutHandover.handoverCode(bankUtil.getUniqueID("G"))
            .stockOutTask(stockOutTask)
            .stockOutApply(stockOutTask.getStockOutPlan().getStockOutApply())
            .stockOutPlan(stockOutTask.getStockOutPlan())
            .status(Constants.STOCK_OUT_HANDOVER_PENDING);
        stockOutHandoverRepository.save(stockOutHandover);

        return stockOutHandoverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandover);
    }

    /**
     * 打印交接单
     * @param id
     * @return
     */
    @Override
    public ByteArrayOutputStream printStockOutHandover(Long id) {
        StockOutHandoverReportDTO handoverDTO = createStockOutHandOverReportDTO(id);
        ByteArrayOutputStream outputStream = reportExportingService.makeStockOutHandoverReport(handoverDTO);
        return outputStream;
    }

    private StockOutHandoverReportDTO createStockOutHandOverReportDTO(Long id) {
        StockOutHandoverReportDTO handoverDTO = new StockOutHandoverReportDTO();
        StockOutHandover stockOutHandover = stockOutHandoverRepository.findOne(id);
        handoverDTO.setId(stockOutHandover.getId());

        handoverDTO.setHandoverDate(stockOutHandover.getHandoverTime()!=null?stockOutHandover.getHandoverTime().toString():null);
        handoverDTO.setHandOverNumber(stockOutHandover.getHandoverCode());
        handoverDTO.setMemo(stockOutHandover.getMemo());
        handoverDTO.setApplicationNumber(stockOutHandover.getStockOutApply()!=null?stockOutHandover.getStockOutApply().getApplyCode():null);
        handoverDTO.setPlanNumber(stockOutHandover.getStockOutPlan()!=null?stockOutHandover.getStockOutPlan().getStockOutPlanCode():null);
        handoverDTO.setReceiverCompany(stockOutHandover.getReceiverOrganization());
        handoverDTO.setReceiverContact(stockOutHandover.getReceiverPhone());
        handoverDTO.setTaskNumber(stockOutHandover.getStockOutTask()!=null?stockOutHandover.getStockOutTask().getStockOutTaskCode():null);

        if(stockOutHandover.getHandoverPersonId() != null){
            User user = userRepository.findOne(stockOutHandover.getHandoverPersonId());
            handoverDTO.setDeliver(user!=null?user.getLastName()+user.getFirstName():null);
        }
        handoverDTO.setDeliverDate(stockOutHandover.getHandoverTime()!=null?stockOutHandover.getHandoverTime().toString():null);
        handoverDTO.setReceiveDate(stockOutHandover.getHandoverTime()!=null?stockOutHandover.getHandoverTime().toString():null);
        handoverDTO.setReceiver(stockOutHandover.getReceiverName());

        Integer countOfBox = stockOutHandoverDetailsRepository.countFrozenBoxByStockOutHandoverId(id);
        List<StockOutHandoverSampleReportDTO> stockOutHandoverSampleReportDTOS = new ArrayList<StockOutHandoverSampleReportDTO>();
        List<StockOutHandoverDetails> stockOutHandoverDetails = stockOutHandoverDetailsRepository.findByStockOutHandoverId(id);
        ArrayList<String> projectCodes = new ArrayList<>();
        for(StockOutHandoverDetails s : stockOutHandoverDetails){
            StockOutHandoverSampleReportDTO sample = new StockOutHandoverSampleReportDTO();
            sample = createStockOutHandOverSampleReportDTO(s);
            if(sample.getProjectCode()!=null && !projectCodes.contains(sample.getProjectCode())){
                projectCodes.add(sample.getProjectCode());
            }
            stockOutHandoverSampleReportDTOS.add(sample);
        }
        handoverDTO.setProjectCode(String.join(",", projectCodes));
        handoverDTO.setCountOfBox(countOfBox);
        handoverDTO.setSamples(stockOutHandoverSampleReportDTOS);
        handoverDTO.setCountOfSample(stockOutHandoverSampleReportDTOS.size());
        return handoverDTO;
    }

    private StockOutHandoverSampleReportDTO createStockOutHandOverSampleReportDTO(StockOutHandoverDetails s) {
        if(s==null || s.getStockOutReqFrozenTube() == null || s.getStockOutReqFrozenTube().getFrozenTube()==null){
            return null;
        }
        StockOutHandoverSampleReportDTO sample = new StockOutHandoverSampleReportDTO();
        FrozenTube frozenTube = s.getStockOutReqFrozenTube().getFrozenTube();

        sample.setId(s.getId());
        sample.setProjectCode(frozenTube.getProjectCode());
        sample.setAge(frozenTube.getAge()!=null?frozenTube.getAge().toString():null);
        sample.setBoxCode(s.getStockOutHandoverBox().getFrozenBoxCode());
        sample.setFrozenBoxCode1D(s.getStockOutHandoverBox().getFrozenBoxCode1D());
        sample.setDiseaseType(frozenTube.getDiseaseType());
        sample.setLocation(frozenTube.getTubeRows()+frozenTube.getTubeColumns());
        String sampleCode = StringUtils.isEmpty(frozenTube.getSampleCode())?frozenTube.getSampleTempCode():frozenTube.getSampleCode();
        sample.setSampleCode(sampleCode);
        sample.setSampleType(frozenTube.getSampleTypeName());
        sample.setSex(Constants.SEX_MAP.get(frozenTube.getGender())!=null?Constants.SEX_MAP.get(frozenTube.getGender()).toString():null);
        return sample;
    }
    @Override
    public StockOutHandoverDTO completeStockOutHandover(List<Long> ids, StockOutHandoverDTO stockOutHandoverDTO) {
        Long handOverId = stockOutHandoverDTO.getId();
        if(handOverId == null){
            throw new BankServiceException("交接ID不能为空！");
        }
        //验证交付人用户密码
        Long handoverPersonId = stockOutHandoverDTO.getHandoverPersonId();
        String password = stockOutHandoverDTO.getPassword();
        User user = userRepository.findOne(handoverPersonId);
        if(user == null){
            throw new BankServiceException("交付人不存在！");
        }
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new BankServiceException("用户名与密码不一致！");
        }

        StockOutHandover stockOutHandover = stockOutHandoverRepository.findOne(handOverId);
        if(stockOutHandover == null){
            throw new BankServiceException("未查询到交接单！");
        }

        if (!Constants.STOCK_OUT_HANDOVER_PENDING.equals(stockOutHandover.getStatus())){
            throw new BankServiceException("该交接单未处于进行中，不能完成交接。");
        }
        stockOutHandover.setHandoverPersonId(handoverPersonId);
        stockOutHandover.setHandoverTime(stockOutHandoverDTO.getHandoverTime());
        stockOutHandover.setStatus(Constants.STOCK_OUT_HANDOVER_COMPLETED);


        List<StockOutTask> stockOutTasks = new ArrayList<>();

        // 将盒ID按照长度10进行分组
        List<List<Long>> arrIds = Lists.partition(ids, 10);
        for(List<Long> boxIds : arrIds){

            // 找到指定出库盒中的管子
            List<StockOutReqFrozenTube> tubes = stockOutReqFrozenTubeRepository.findByStockOutFrozenBoxId(
                boxIds.stream().filter(i -> i != null).collect(Collectors.toList()),
                null);

            // 根据出库盒对管子进行分组
            Map<Long, List<StockOutReqFrozenTube>> tubesGroupByBox = tubes.stream().collect(
                Collectors.groupingBy(t -> t.getStockOutFrozenBox().getId()));

            for(List<StockOutReqFrozenTube> boxTubes: tubesGroupByBox.values()){
                StockOutHandoverBox stockOutHandoverBox = new StockOutHandoverBox();
                StockOutFrozenBox stockOutFrozenBox = boxTubes.get(0).getStockOutFrozenBox();

                // 修改出库盒状态
                stockOutFrozenBox.handoverTime(stockOutHandoverDTO.getHandoverTime()).status(Constants.STOCK_OUT_FROZEN_BOX_HANDOVER);
                if(stockOutHandover.getStockOutTask()!=null){
                    if(stockOutFrozenBox.getStockOutTask().getId()!=stockOutHandover.getStockOutTask().getId()
                        ||!stockOutFrozenBox.getStockOutTask().getId().equals(stockOutHandover.getStockOutTask().getId())){
                        throw new BankServiceException("交接冻存盒不在任务之内！");
                    }
                }
                // 修改冻存盒状态
                FrozenBox frozenBox = stockOutFrozenBox.getFrozenBox();
                frozenBox.setStatus(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);

                stockOutHandoverBox.stockOutHandover(stockOutHandover)
                    .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                    .equipmentCode(stockOutFrozenBox.getEquipmentCode())
                    .areaCode(stockOutFrozenBox.getAreaCode())
                    .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                    .rowsInShelf(stockOutFrozenBox.getRowsInShelf())
                    .columnsInShelf(stockOutFrozenBox.getColumnsInShelf())
                    .frozenBoxCode(stockOutFrozenBox.getFrozenBoxCode())
                    .frozenBoxCode1D(stockOutFrozenBox.getFrozenBoxCode1D())
                    .area(stockOutFrozenBox.getArea())
                    .equipment(stockOutFrozenBox.getEquipment())
                    .supportRack(stockOutFrozenBox.getSupportRack())
                    .sampleTypeCode(stockOutFrozenBox.getSampleTypeCode())
                    .sampleType(stockOutFrozenBox.getSampleType())
                    .sampleTypeName(stockOutFrozenBox.getSampleTypeName())
                    .sampleClassification(stockOutFrozenBox.getSampleClassification())
                    .sampleClassificationCode(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(stockOutFrozenBox.getDislocationNumber())
                    .emptyHoleNumber(stockOutFrozenBox.getEmptyHoleNumber())
                    .emptyTubeNumber(stockOutFrozenBox.getEmptyTubeNumber())
                    .frozenBoxType(stockOutFrozenBox.getFrozenBoxType())
                    .frozenBoxTypeCode(stockOutFrozenBox.getFrozenBoxTypeCode())
                    .frozenBoxTypeColumns(stockOutFrozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(stockOutFrozenBox.getFrozenBoxTypeRows())
                    .isRealData(stockOutFrozenBox.getIsRealData())
                    .isSplit(stockOutFrozenBox.getIsSplit())
                    .project(stockOutFrozenBox.getProject())
                    .projectCode(stockOutFrozenBox.getProjectCode())
                    .projectName(stockOutFrozenBox.getProjectName())
                    .projectSite(stockOutFrozenBox.getProjectSite())
                    .projectSiteCode(stockOutFrozenBox.getProjectSiteCode())
                    .projectSiteName(stockOutFrozenBox.getProjectSiteName())

                    .stockOutFrozenBox(stockOutFrozenBox)
                    .memo(stockOutFrozenBox.getMemo())
                    .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);

                // 保存交接盒
                stockOutHandoverBox = stockOutHandoverBoxRepository.save(stockOutHandoverBox);
                // 保存出库盒
                stockOutFrozenBoxRepository.save(stockOutFrozenBox);
                // 保存库存盒
                frozenBoxRepository.save(frozenBox);

                List<StockOutHandoverDetails> handoverTubes = new ArrayList<>();
                List<FrozenTube> frozenTubes = new ArrayList<>();
                StockOutHandoverBox finalStockOutHandoverBox = stockOutHandoverBox;
                boxTubes.forEach(t->{
                    // 生成交接管子
                    handoverTubes.add(new StockOutHandoverDetails()
                        .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)
                        .memo(t.getMemo())
                        .stockOutReqFrozenTube(t)
                        .stockOutHandoverBox(finalStockOutHandoverBox));
                    // 修改冻存管状态和使用次数
                    int usedTimes = t.getFrozenTube().getSampleUsedTimes()!=null?t.getFrozenTube().getSampleUsedTimes():0;
                    frozenTubes.add(t.getFrozenTube().frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER).sampleUsedTimes(usedTimes+1));
                });
                // 保存交接管
                stockOutHandoverDetailsRepository.save(handoverTubes);

                // 保存库存管
                frozenTubeRepository.save(frozenTubes);
            }

            Map<StockOutTask, List<StockOutReqFrozenTube>> tubeGroupByTask = tubes.stream().collect(Collectors.groupingBy(s->s.getStockOutTask()));
            for(StockOutTask stockOutTask : tubeGroupByTask.keySet()){
                Long countOfHandOverSample = stockOutHandoverDetailsRepository.countByStockOutTaskId(stockOutTask.getId());
                stockOutTask.countOfHandOverSample(countOfHandOverSample.intValue());
                stockOutTasks.add(stockOutTask);
            }
        }
        StockOutApply stockOutApply =stockOutHandover.getStockOutApply();
        Long countOfHandOverSampleForApply = stockOutHandoverDetailsRepository.countByStockOutApply(stockOutApply.getId());
        stockOutApply.countOfHandOverSample(countOfHandOverSampleForApply.intValue());
        stockOutApplyRepository.save(stockOutApply);

        stockOutTaskRepository.save(stockOutTasks);
        Long countOfHandoverTube = stockOutHandoverDetailsRepository.countByStockOutHandoverId(stockOutHandover.getId());
        stockOutHandover.countOfHandoverSample(countOfHandoverTube.intValue());
        stockOutHandoverRepository.save(stockOutHandover);

        return stockOutHandoverDTO;
    }

    @Override
    public StockOutHandoverDTO getStockOutHandoverDetail(Long id) {
        log.debug("Request to get StockOutHandover and handoverSample : {}", id);
        StockOutHandover stockOutHandover = stockOutHandoverRepository.findOne(id);
        StockOutHandoverDTO stockOutHandoverDTO = stockOutHandoverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandover);
        if(stockOutHandover.getHandoverPersonId()!=null){
            User user = userRepository.findOne(stockOutHandover.getHandoverPersonId());
            stockOutHandoverDTO.setHandoverPersonName(user!=null?user.getLastName()+user.getFirstName():null);
        }
        stockOutHandoverDTO.setStockOutApplyCode(stockOutHandover.getStockOutApply()!=null?stockOutHandover.getStockOutApply().getApplyCode():null);
        stockOutHandoverDTO.setStockOutTaskCode(stockOutHandover.getStockOutTask()!=null?stockOutHandover.getStockOutTask().getStockOutTaskCode():null);
        stockOutHandoverDTO.setStockOutPlanCode(stockOutHandover.getStockOutPlan()!=null?stockOutHandover.getStockOutPlan().getStockOutPlanCode():null);
        List<StockOutHandoverSampleReportDTO> stockOutHandoverSampleReportDTOS = new ArrayList<StockOutHandoverSampleReportDTO>();
        List<StockOutHandoverDetails> stockOutHandoverDetails = stockOutHandoverDetailsRepository.findByStockOutHandoverId(id);
        for(StockOutHandoverDetails s : stockOutHandoverDetails){
            StockOutHandoverSampleReportDTO sample = new StockOutHandoverSampleReportDTO();
            sample = createStockOutHandOverSampleReportDTO(s);
            stockOutHandoverSampleReportDTOS.add(sample);
        }
        stockOutHandoverDTO.setHandoverFrozenTubes(stockOutHandoverSampleReportDTOS);
        stockOutHandoverDTO.setCountOfSample(stockOutHandoverSampleReportDTOS.size());
        return stockOutHandoverDTO;
    }

    /**
     * 作废交接
     * @param id
     * @param stockOutHandoverDTO
     * @return
     */
    @Override
    public StockOutHandoverDTO invalidStockOutHandover(Long id, StockOutHandoverDTO stockOutHandoverDTO) {
        StockOutHandover stockOutHandover = stockOutHandoverRepository.findOne(id);
        if(stockOutHandover == null){
            throw new BankServiceException("未查询到需求作废的交接数据！");
        }
        stockOutHandover.setStatus(Constants.STOCK_OUT_HANDOVER_INVALID);
        stockOutHandover.setInvalidReason(stockOutHandoverDTO.getInvalidReason());
        stockOutHandoverRepository.save(stockOutHandover);
        return stockOutHandoverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandover);
    }

    @Override
    public DataTablesOutput<StockOutHandoverForDataTableEntity> getPageDataStockOutHandOver(DataTablesInput input) {
        return stockOutHandoverRepositries.findAll(input);
    }

    @Override
    public DataTablesOutput<StockOutHandoverSampleReportDTO> getPageStockOutHandoverSample(Long id, DataTablesInput input) {
        input.addColumn("stockOutHandoverId",true,true,id+"+");
        Converter<StockOutHandoverSampleReportDTO, StockOutHandoverSampleReportDTO> converter = new Converter<StockOutHandoverSampleReportDTO, StockOutHandoverSampleReportDTO>() {
            @Override
            public StockOutHandoverSampleReportDTO convert(StockOutHandoverSampleReportDTO e) {
                String sampleCode = e.getSampleCode();
                if(StringUtils.isEmpty(sampleCode) || sampleCode.equals(null)){
                    sampleCode = e.getSampleTempCode();
                }
                return new StockOutHandoverSampleReportDTO(e.getId(),e.getNo(),e.getBoxCode(),e.getLocation(),sampleCode,e.getSampleType(),e.getSex()
                    ,e.getAge(),e.getDiseaseType(),e.getProjectCode(),e.getStockOutHandoverId(),e.getFrozenBoxCode1D(),e.getSampleTempCode(),e.getMemo());
            }
        };
        return stockOutHandoverSampleRepositries.findAll(input,converter);
    }
}
