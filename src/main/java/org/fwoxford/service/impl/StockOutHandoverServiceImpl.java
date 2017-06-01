package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.StockOutHandoverService;
import org.fwoxford.service.dto.StockOutHandoverDTO;
import org.fwoxford.service.dto.response.StockOutHandoverForDataTableEntity;
import org.fwoxford.service.mapper.StockOutHandoverMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private StockOutHandoverDetailsRepository stockOutHandoverDetailsRepository;

    @Autowired
    private UserRepository userRepository;

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
            stockOutHandover.setHandoverCode(BankUtil.getUniqueID());
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
        Long count = stockOutHandoverRepository.countByStockOutTaskId(taskId);
        if(count.intValue()>0){
            throw new BankServiceException("该任务已有交接单，不能重复创建！");
        }
        stockOutHandover.handoverCode(BankUtil.getUniqueID())
            .stockOutTask(stockOutTask)
            .stockOutApply(stockOutTask.getStockOutPlan().getStockOutApply())
            .stockOutPlan(stockOutTask.getStockOutPlan())
            .status(Constants.STOCK_OUT_HANDOVER_PENDING);
        stockOutHandoverRepository.save(stockOutHandover);
        //保存交接详情
        List<StockOutBoxTube> stockOutBoxTubeList = stockOutBoxTubeRepository.findByStockOutTaskId(taskId);
        for(StockOutBoxTube b:stockOutBoxTubeList){
            StockOutHandoverDetails stockOutHandoverDetails = new StockOutHandoverDetails();
            stockOutHandoverDetails.status(Constants.STOCK_OUT_HANDOVER_PENDING).stockOutBoxTube(b).stockOutHandover(stockOutHandover);
            stockOutHandoverDetailsRepository.save(stockOutHandoverDetails);
        }
        return stockOutHandoverMapper.stockOutHandOverToStockOutHandOverDTO(stockOutHandover);
    }

    @Override
    public Page<StockOutHandoverForDataTableEntity> getPageStockOutHandOver(Pageable pageable) {
        Page<StockOutHandover> result = stockOutHandoverRepository.findAll(pageable);

        return result.map(handover -> {
            StockOutHandoverForDataTableEntity dto = new StockOutHandoverForDataTableEntity();
            dto.setId(handover.getId());
            dto.setStatus(handover.getStatus());
            dto.setHandoverCode(handover.getHandoverCode());
            dto.setUsage(handover.getStockOutApply()!=null?handover.getStockOutApply().getPurposeOfSample():null);
            dto.setApplyCode(handover.getStockOutApply()!=null?handover.getStockOutApply().getApplyCode():null);
            Long personId = handover.getHandoverPersonId();
            if(personId!=null){
                User user = userRepository.findOne(personId);
                dto.setDeliverName(user!=null?user.getLastName()+user.getFirstName():null);
            }
            dto.setReceiver(handover.getReceiverName());
            dto.setReceiveDate(handover.getHandoverTime());
            Long count= stockOutHandoverDetailsRepository.countByStockOutHandoverId(handover.getId());
            dto.setCountOfSample(count);
            return dto;
        });
    }
}
