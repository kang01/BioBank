package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.StorageInBox;
import org.fwoxford.domain.User;
import org.fwoxford.repository.StorageInRepositries;
import org.fwoxford.security.SecurityUtils;
import org.fwoxford.service.StorageInBoxService;
import org.fwoxford.service.StorageInService;
import org.fwoxford.domain.StorageIn;
import org.fwoxford.repository.StorageInRepository;
import org.fwoxford.service.TranshipService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.StorageInBoxDTO;
import org.fwoxford.service.dto.StorageInDTO;
import org.fwoxford.service.dto.TranshipDTO;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.mapper.StorageInMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StorageIn.
 */
@Service
@Transactional
public class StorageInServiceImpl implements StorageInService{

    private final Logger log = LoggerFactory.getLogger(StorageInServiceImpl.class);

    private final StorageInRepository storageInRepository;

    private final StorageInMapper storageInMapper;

    private final StorageInRepositries storageInRepositries;

    @Autowired
    private TranshipService transhipService;

    @Autowired
    private StorageInBoxService storageInBoxService;
    public StorageInServiceImpl(StorageInRepository storageInRepository, StorageInMapper storageInMapper,StorageInRepositries storageInRepositries) {
        this.storageInRepository = storageInRepository;
        this.storageInMapper = storageInMapper;
        this.storageInRepositries = storageInRepositries;
    }

    /**
     * Save a storageIn.
     *
     * @param storageInDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StorageInDTO save(StorageInDTO storageInDTO) {
        log.debug("Request to save StorageIn : {}", storageInDTO);
        StorageIn storageIn = storageInMapper.storageInDTOToStorageIn(storageInDTO);
        storageIn = storageInRepository.save(storageIn);
        StorageInDTO result = storageInMapper.storageInToStorageInDTO(storageIn);
        return result;
    }

    /**
     *  Get all the storageIns.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StorageInDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StorageIns");
        Page<StorageIn> result = storageInRepository.findAll(pageable);
        return result.map(storageIn -> storageInMapper.storageInToStorageInDTO(storageIn));
    }

    /**
     *  Get one storageIn by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StorageInDTO findOne(Long id) {
        log.debug("Request to get StorageIn : {}", id);
        StorageIn storageIn = storageInRepository.findOne(id);
        StorageInDTO storageInDTO = storageInMapper.storageInToStorageInDTO(storageIn);
        return storageInDTO;
    }

    /**
     *  Delete the  storageIn by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StorageIn : {}", id);
        storageInRepository.delete(id);
    }

    /**
     * 入库保存
     * @param storageInDTO
     * @return
     */
    @Override
    public StorageInDTO saveStorageIns(StorageInDTO storageInDTO) {
        Long transhipId = storageInDTO.getTranshipId();//转运ID
        //如果转运ID不为空，则查询这次转运的记录，保存至入库，如果为空，
        TranshipByIdResponse transhipRes = new TranshipByIdResponse();
        TranshipDTO tranship = new TranshipDTO();
        if(transhipId != null){
            tranship = transhipService.findOne(transhipId);
            transhipRes =  transhipService.findTranshipAndFrozenBox(transhipId);
        }
        storageInDTO = createStorageInDTO(storageInDTO,tranship);
        StorageInDTO storageIn = this.save(storageInDTO);
        List<FrozenBoxDTO> frozenBoxDTOList =  transhipRes.getFrozenBoxDTOList();
        List<StorageInBoxDTO> storageInBoxDTOS = createStorageInBoxDTO(frozenBoxDTOList,storageIn);
        List<StorageInBoxDTO>  storageInBoxDTOSList = storageInBoxService.saveBatch(storageInBoxDTOS);
        return storageInDTO;
    }

    @Override
    public DataTablesOutput<StorageInDTO> findStorageIn(DataTablesInput input) {

        //获取转运列表
        DataTablesOutput<StorageIn> storageInDataTablesOutput =  storageInRepositries.findAll(input);
        List<StorageIn> storageIns =  storageInDataTablesOutput.getData();

        //构造返回列表
        List<StorageInDTO> storageInDTOS = storageInMapper.storageInsToStorageInDTOs(storageIns);

        //构造返回分页数据
        DataTablesOutput<StorageInDTO> responseDataTablesOutput = new DataTablesOutput<>();
        responseDataTablesOutput.setDraw(storageInDataTablesOutput.getDraw());
        responseDataTablesOutput.setError(storageInDataTablesOutput.getError());
        responseDataTablesOutput.setData(storageInDTOS);
        responseDataTablesOutput.setRecordsFiltered(storageInDataTablesOutput.getRecordsFiltered());
        responseDataTablesOutput.setRecordsTotal(storageInDataTablesOutput.getRecordsTotal());
        return responseDataTablesOutput;
    }

    private List<StorageInBoxDTO> createStorageInBoxDTO(List<FrozenBoxDTO> frozenBoxDTOList, StorageInDTO storageIn) {
        List<StorageInBoxDTO> storageInBoxDTOS = new ArrayList<StorageInBoxDTO>();
        for(FrozenBoxDTO box : frozenBoxDTOList){
            StorageInBoxDTO inBoxDTO = new StorageInBoxDTO();
            inBoxDTO.setEquipmentCode(box.getEquipmentCode());
            inBoxDTO.setEquipmentId(box.getEquipmentId());
            inBoxDTO.setAreaId(box.getAreaId());
            inBoxDTO.setAreaCode(box.getAreaCode());
            inBoxDTO.setSupportRackId(box.getSupportRackId());
            inBoxDTO.setSupportRackCode(box.getSupportRackCode());
            inBoxDTO.setRowsInShelf(box.getRowsInShelf());
            inBoxDTO.setColumnsInShelf(box.getColumnsInShelf());
            inBoxDTO.setFrozenBoxCode(box.getFrozenBoxCode());
            inBoxDTO.setStorageInId(storageIn.getId());
            inBoxDTO.setMemo("");
            inBoxDTO.setStatus(Constants.STORANGE_IN_PENDING);
            inBoxDTO.setCreatedBy(SecurityUtils.getCurrentUserLogin());
            inBoxDTO.setCreatedDate(ZonedDateTime.now());
            inBoxDTO.setLastModifiedDate(ZonedDateTime.now());
            inBoxDTO.setLastModifiedBy(SecurityUtils.getCurrentUserLogin());
            storageInBoxDTOS.add(inBoxDTO);
        }
        return storageInBoxDTOS;
    }

    private StorageInDTO createStorageInDTO(StorageInDTO storageInDTO, TranshipDTO tranship) {
        storageInDTO.setProjectId(tranship.getProjectId());
        storageInDTO.setStatus(Constants.STORANGE_IN_PENDING);
        storageInDTO.setProjectSiteCode(tranship.getProjectSiteCode());
        storageInDTO.setProjectCode(tranship.getProjectCode());
        storageInDTO.setProjectSiteId(tranship.getProjectSiteId());
        storageInDTO.setReceiveId(null);
        storageInDTO.setReceiveDate(tranship.getReceiveDate());
        storageInDTO.setReceiveName(tranship.getReceiver());
        storageInDTO.setSampleNumber(tranship.getSampleNumber());
        storageInDTO.setSignId(null);
        storageInDTO.setSignDate(null);
        storageInDTO.setSignName("");
        storageInDTO.setStorageInDate(null);
        storageInDTO.setStorageInPersonId1(null);
        storageInDTO.setStorageInPersonId2(null);
        storageInDTO.setStorageInPersonName1("");
        storageInDTO.setStorangeInPersonName2("");
        storageInDTO.setStorageInType(Constants.STORANGE_IN_TYPE_1ST);
        storageInDTO.setMemo("");
        return storageInDTO;
    }
}
