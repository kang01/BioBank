package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutFrozenBoxService;
import org.fwoxford.service.UserService;
import org.fwoxford.service.dto.StockOutFrozenBoxDTO;
import org.fwoxford.service.dto.StockOutTaskDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.dto.response.StockOutFrozenBoxDataTableEntity;
import org.fwoxford.service.dto.response.StockOutFrozenBoxForTaskDataTableEntity;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.StockOutFrozenBoxMapper;
import org.fwoxford.service.mapper.StockOutTaskMapper;
import org.fwoxford.web.rest.StockOutFrozenBoxPoisition;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing StockOutFrozenBox.
 */
@Service
@Transactional
public class StockOutFrozenBoxServiceImpl implements StockOutFrozenBoxService{

    private final Logger log = LoggerFactory.getLogger(StockOutFrozenBoxServiceImpl.class);

    private final StockOutFrozenBoxRepository stockOutFrozenBoxRepository;
    private final StockOutFrozenTubeRepository stockOutFrozenTubeRepository;

    private final StockOutFrozenBoxMapper stockOutFrozenBoxMapper;

    @Autowired
    private StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;


    @Autowired
    private StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;

    @Autowired
    private StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;

    @Autowired
    private StockOutTaskRepository stockOutTaskRepository;

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    @Autowired
    private BoxAndTubeRepository boxAndTubeRepository;

    @Autowired
    private StockOutBoxTubeRepository stockOutBoxTubeRepository;

    @Autowired
    private FrozenTubeRepository frozenTubeRepository;

    @Autowired
    private FrozenBoxTypeRepository frozenBoxTypeRepository;

    @Autowired
    private FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private StockOutHandoverRepository stockOutHandoverRepository;

    @Autowired
    private StockOutBoxPositionRepository stockOutBoxPositionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private StockOutTaskMapper stockOutTaskMapper;

    public StockOutFrozenBoxServiceImpl(StockOutFrozenBoxRepository stockOutFrozenBoxRepository
            , StockOutFrozenBoxMapper stockOutFrozenBoxMapper
            , StockOutFrozenTubeRepository stockOutFrozenTubeRepository) {
        this.stockOutFrozenBoxRepository = stockOutFrozenBoxRepository;
        this.stockOutFrozenBoxMapper = stockOutFrozenBoxMapper;
        this.stockOutFrozenTubeRepository = stockOutFrozenTubeRepository;
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


    private String toPositionString(StockOutBoxPosition pos){
        if(pos ==null){
            return null;
        }
        ArrayList<String> positions = new ArrayList<>();
        if (pos.getEquipmentCode() != null && pos.getEquipmentCode().length() > 0){
            positions.add(pos.getEquipmentCode());
        }

        if (pos.getAreaCode() != null && pos.getAreaCode().length() > 0) {
            positions.add(pos.getAreaCode());
        }

        if (pos.getSupportRackCode() != null && pos.getSupportRackCode().length() > 0){
            positions.add(pos.getSupportRackCode());
        }

        if (pos.getRowsInShelf() != null && pos.getRowsInShelf().length() > 0 && pos.getColumnsInShelf() != null && pos.getColumnsInShelf().length() > 0){
            positions.add(pos.getColumnsInShelf()+pos.getRowsInShelf());
        }

        return String.join(".", positions);
    }

    /**
     *  获取指定任务的指定分页的出库盒子.
     *  @param taskId The task id
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockOutFrozenBoxForTaskDataTableEntity> findAllByTask(Long taskId, Pageable pageable) {
        log.debug("Request to get all StockOutFrozenBoxes");
        Page<StockOutFrozenBox> result = stockOutFrozenBoxRepository.findAllByTask(taskId, pageable);
        return result.map(stockOutFrozenBox -> {
            StockOutFrozenBoxForTaskDataTableEntity dto = new StockOutFrozenBoxForTaskDataTableEntity();
            dto.setId(stockOutFrozenBox.getId());
            dto.setFrozenBoxCode(stockOutFrozenBox.getFrozenBox().getFrozenBoxCode());
            dto.setSampleTypeName(stockOutFrozenBox.getFrozenBox().getSampleTypeName());

//            String position = toPositionString(stockOutFrozenBox.getStockOutBoxPosition());
            String position = getPositionString(stockOutFrozenBox.getFrozenBox());
            dto.setPosition(position);

            Long count = stockOutFrozenTubeRepository.countByFrozenBox(stockOutFrozenBox.getId());

            dto.setCountOfSample(count);

            return dto;
        });
    }

    @Override
    public Page<StockOutFrozenBoxForTaskDataTableEntity> findAllByrequirementIds(List<Long> ids, Pageable pageable) {
        Page<FrozenBox> result = frozenBoxRepository.findAllByrequirementIds(ids, pageable);

        return result.map(frozenBox -> {
            StockOutFrozenBoxForTaskDataTableEntity dto = new StockOutFrozenBoxForTaskDataTableEntity();
            dto.setId(frozenBox.getId());
            dto.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            dto.setSampleTypeName(frozenBox.getSampleTypeName());
            String position = getPositionString(frozenBox);
            dto.setPosition(position);
            Long countOfSample = stockOutPlanFrozenTubeRepository.countByFrozenBoxId(frozenBox.getId());
            dto.setCountOfSample(countOfSample);

            return dto;
        });
    }


    private String getPositionString(FrozenBox frozenBox) {
        String position = "";
        if(frozenBox == null){
            return null;
        }
        ArrayList<String> positions = new ArrayList<>();
        if (frozenBox.getEquipmentCode() != null && frozenBox.getEquipmentCode().length() > 0){
            positions.add(frozenBox.getEquipmentCode());
        }

        if (frozenBox.getAreaCode() != null && frozenBox.getAreaCode().length() > 0) {
            positions.add(frozenBox.getAreaCode());
        }

        if (frozenBox.getSupportRackCode() != null && frozenBox.getSupportRackCode().length() > 0){
            positions.add(frozenBox.getSupportRackCode());
        }

        if (frozenBox.getRowsInShelf() != null && frozenBox.getRowsInShelf().length() > 0 && frozenBox.getColumnsInShelf() != null && frozenBox.getColumnsInShelf().length() > 0){
            positions.add(frozenBox.getColumnsInShelf()+frozenBox.getRowsInShelf());
        }

        return String.join(".", positions);
    }


    @Override
    public List<StockOutFrozenBoxForTaskDataTableEntity> getAllStockOutFrozenBoxesByTask(Long taskId) {
        List<StockOutFrozenBoxForTaskDataTableEntity> alist = new ArrayList<StockOutFrozenBoxForTaskDataTableEntity>();
        List<FrozenBox> boxes =  frozenBoxRepository.findByStockOutTaskId(taskId);
        for(FrozenBox frozenBox :boxes){
            StockOutFrozenBoxForTaskDataTableEntity box = new StockOutFrozenBoxForTaskDataTableEntity();
            if(frozenBox ==null){continue;}
            box.setId(frozenBox.getId());
            box.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
            box.setSampleTypeName(frozenBox.getSampleTypeName());
            String position = getPositionString(frozenBox);
            box.setPosition(position);

            Long count = stockOutTaskFrozenTubeRepository.countByFrozenBox(frozenBox.getId());

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
    public List<FrozenBoxAndFrozenTubeResponse> createFrozenBoxForStockOut(List<FrozenBoxAndFrozenTubeResponse> frozenBoxDTO, Long taskId) {
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(taskId);
        if(stockOutTask == null){
            throw new BankServiceException("任务ID无效！");
        }
        List<FrozenBoxType> boxTypes = frozenBoxTypeRepository.findAllFrozenBoxTypes();
        for(FrozenBoxAndFrozenTubeResponse box:frozenBoxDTO){
            if(box.getFrozenBoxCode() == null){
                throw new BankServiceException("冻存盒编码不能为空！");
            }
            //验证盒子编码是否存在

            List<Object[]> obj = frozenBoxRepository.countByFrozenBoxCode(box.getFrozenBoxCode());
            for(Object[] o:obj){
                String frozenBoxId = o[0].toString();
                if(box.getId()==null){
                    throw new BankServiceException("冻存盒编码已存在！",box.getFrozenBoxCode());
                }else if(box.getId()!=null&&!box.getId().toString().equals(frozenBoxId)){
                    throw new BankServiceException("冻存盒编码已存在！",box.getFrozenBoxCode());
                }
            }
            FrozenBox frozenBox = new FrozenBox();
            if(box.getId()!=null){
                frozenBox = frozenBoxRepository.findOne(box.getId())!=null?frozenBoxRepository.findOne(box.getId()):new FrozenBox();
            }
            if (box.getFrozenBoxType() != null) {
                int boxTypeIndex = boxTypes.indexOf(box.getFrozenBoxType());
                if (boxTypeIndex >= 0) {
                    FrozenBoxType boxType = boxTypes.get(boxTypeIndex);
                    frozenBox.setFrozenBoxType(boxType);
                    frozenBox.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
                    frozenBox.setFrozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns());
                    frozenBox.setFrozenBoxTypeRows(boxType.getFrozenBoxTypeRows());
                }else {
                    throw new BankServiceException("冻存盒类型不能为空！",box.toString());
                }
            }
            frozenBox.setFrozenBoxCode(box.getFrozenBoxCode());
            frozenBox.setSampleNumber(box.getFrozenTubeDTOS().size());
            frozenBox.setStatus(Constants.FROZEN_BOX_STOCK_OUT_PENDING);
            frozenBoxRepository.save(frozenBox);
            //保存出库盒
            StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findByFrozenBoxId(frozenBox.getId());
            if(stockOutFrozenBox == null){
                stockOutFrozenBox = new StockOutFrozenBox();
            }
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_NEW);
            stockOutFrozenBox.setFrozenBox(frozenBox);
            stockOutFrozenBox.setStockOutTask(stockOutTask);
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
            //保存冻存盒与管之间的关系 todo
            BoxAndTube boxAndTube = new BoxAndTube();
            boxAndTube.setFrozenBox(frozenBox);
            boxAndTube.setStatus(Constants.FROZEN_BOX_TUBE_STOCKOUT_PENDING);

            //保存出库盒与管之间的关系
            for(FrozenTubeResponse f: box.getFrozenTubeDTOS()){
                //todo 判断是否已有出库任务
                StockOutBoxTube stockOutBoxTube = new StockOutBoxTube();
                stockOutBoxTube.setStatus(Constants.STOCK_OUT_FROZEN_TUBE_NEW);
                stockOutBoxTube.setStockOutFrozenBox(stockOutFrozenBox);
                StockOutTaskFrozenTube stockOutTaskFrozenTube = stockOutTaskFrozenTubeRepository.findByStockOutTaskAndFrozenTube(taskId,f.getId());
                stockOutBoxTube.setStockOutTaskFrozenTube(stockOutTaskFrozenTube);
                stockOutBoxTube.setFrozenTube(stockOutTaskFrozenTube.getStockOutPlanFrozenTube().getStockOutReqFrozenTube().getFrozenTube());
                boxAndTube.setFrozenTube(stockOutTaskFrozenTube.getStockOutPlanFrozenTube().getStockOutReqFrozenTube().getFrozenTube());
                FrozenTube frozenTube = stockOutTaskFrozenTube.getStockOutPlanFrozenTube().getStockOutReqFrozenTube().getFrozenTube();
                frozenTube.setFrozenBox(frozenBox);
                frozenTube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                frozenTube.setTubeColumns(f.getTubeColumns());
                frozenTube.setTubeRows(f.getTubeRows());
                frozenTubeRepository.save(frozenTube);
                //                boxAndTubeRepository.save(boxAndTube);
                stockOutBoxTubeRepository.save(stockOutBoxTube);
            }

        }
        return frozenBoxDTO;
    }

    @Override
    public List<FrozenBoxAndFrozenTubeResponse> getAllTempStockOutFrozenBoxesByTask(Long taskId) {
        List<FrozenBoxAndFrozenTubeResponse> alist = new ArrayList<FrozenBoxAndFrozenTubeResponse>();
        List<StockOutFrozenBox> boxes =  stockOutFrozenBoxRepository.findByStockOutTaskId(taskId);
        for(StockOutFrozenBox s :boxes){
            FrozenBox frozenBox = s.getFrozenBox();
            if(frozenBox ==null){continue;}
            //根据冻存盒编码查询冻存管
            List<FrozenTube> frozenTubes = frozenTubeRepository.findFrozenTubeListByBoxCode(frozenBox.getFrozenBoxCode());
            List<FrozenTubeResponse> frozenTubeResponse = frozenTubeMapper.frozenTubeToFrozenTubeResponse(frozenTubes);
            FrozenBoxAndFrozenTubeResponse box = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox,frozenTubeResponse);
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
            box.setSampleTypeName(frozenBox.getSampleTypeName());
//            String position = getPositionString(frozenBox);
            String position = toPositionString(f.getStockOutBoxPosition());
            box.setPosition(position);
            Long count = stockOutBoxTubeRepository.countByStockOutFrozenBoxId(f.getId());
            box.setCountOfSample(count);
            box.setMemo(f.getMemo());
            box.setStauts(f.getStatus());
            StockOutHandover stockOutHandover = stockOutHandoverRepository.findByStockOutTaskId(taskId);
            box.setStockOutHandoverTime(stockOutHandover!=null?stockOutHandover.getHandoverTime():null);
            alist.add(box);
        }
        return alist;
    }

    @Override
    public StockOutTaskDTO stockOut(StockOutFrozenBoxPoisition stockOutFrozenBoxPoisition, Long taskId, List<Long> frozenBoxIds) {
        StockOutTask stockOutTask = stockOutTaskRepository.findOne(taskId);
        if(stockOutTask == null){
            throw new BankServiceException("出库任务不存在！");
        }
        //验证负责人密码

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

        for(Long id:frozenBoxIds){
            //保存冻存盒位置
            StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition();
            StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(id);
            if(stockOutFrozenBox == null){
                continue;
            }
            FrozenBox frozenBox = stockOutFrozenBox.getFrozenBox();
            stockOutBoxPosition.setFrozenBox(frozenBox);

            stockOutBoxPosition.setEquipment(equipment);
            stockOutBoxPosition.setEquipmentCode(equipment!=null?equipment.getEquipmentCode():null);
            stockOutBoxPosition.setArea(area);
            stockOutBoxPosition.setAreaCode(area!=null?area.getAreaCode():null);
            stockOutBoxPosition.setStatus(Constants.VALID);
            stockOutBoxPositionRepository.save(stockOutBoxPosition);
            //保存出库冻存盒
            stockOutFrozenBox.setStockOutBoxPosition(stockOutBoxPosition);
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_COMPLETED);
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        }
        return stockOutTaskMapper.stockOutTaskToStockOutTaskDTO(stockOutTask);
    }

    @Override
    public StockOutFrozenBoxDTO stockOutNote(StockOutFrozenBoxDTO stockOutFrozenBoxDTO) {
        if(stockOutFrozenBoxDTO == null || stockOutFrozenBoxDTO.getId() == null ){
            throw new BankServiceException("出库盒ID不能为空！");
        }
        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findOne(stockOutFrozenBoxDTO.getId());
        stockOutFrozenBox.setMemo(stockOutFrozenBoxDTO.getMemo());
        stockOutFrozenBoxRepository.save(stockOutFrozenBox);
        return stockOutFrozenBoxDTO;
    }
}
