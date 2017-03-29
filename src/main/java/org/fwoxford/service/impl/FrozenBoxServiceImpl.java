package org.fwoxford.service.impl;

import org.fwoxford.domain.Equipment;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.service.FrozenBoxService;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.dto.FrozenBoxDTO;
import org.fwoxford.service.dto.response.FrozenBoxAndFrozenTubeResponse;
import org.fwoxford.service.dto.response.FrozenTubeResponse;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.core.support.ExampleMatcherAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing FrozenBox.
 */
@Service
@Transactional
public class FrozenBoxServiceImpl implements FrozenBoxService{

    private final Logger log = LoggerFactory.getLogger(FrozenBoxServiceImpl.class);

    private final FrozenBoxRepository frozenBoxRepository;

    private final FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private FrozenTubeService frozenTubeService;

    @Autowired
    private FrozenTubeMapper frozenTubeMapping;

    public FrozenBoxServiceImpl(FrozenBoxRepository frozenBoxRepository, FrozenBoxMapper frozenBoxMapper) {
        this.frozenBoxRepository = frozenBoxRepository;
        this.frozenBoxMapper = frozenBoxMapper;
    }

    /**
     * Save a frozenBox.
     *
     * @param frozenBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FrozenBoxDTO save(FrozenBoxDTO frozenBoxDTO) {
        log.debug("Request to save FrozenBox : {}", frozenBoxDTO);
        FrozenBox frozenBox = frozenBoxMapper.frozenBoxDTOToFrozenBox(frozenBoxDTO);
        frozenBox = frozenBoxRepository.save(frozenBox);
        FrozenBoxDTO result = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);
        return result;
    }

    /**
     *  Get all the frozenBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FrozenBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FrozenBoxes");
        Page<FrozenBox> result = frozenBoxRepository.findAll(pageable);
        return result.map(frozenBox -> frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox));
    }

    /**
     *  Get one frozenBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FrozenBoxDTO findOne(Long id) {
        log.debug("Request to get FrozenBox : {}", id);
        FrozenBox frozenBox = frozenBoxRepository.findOne(id);
        FrozenBoxDTO frozenBoxDTO = frozenBoxMapper.frozenBoxToFrozenBoxDTO(frozenBox);
        return frozenBoxDTO;
    }

    /**
     *  Delete the  frozenBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FrozenBox : {}", id);
        frozenBoxRepository.delete(id);
    }

    /**
     * 根据转运记录ID 查询冻存盒列表
     * @param transhipId 转运记录ID
     * @return
     */
    @Override
    public List<FrozenBoxDTO> findAllFrozenBoxByTranshipId(Long transhipId) {
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAllFrozenBoxByTranshipId(transhipId);
        return frozenBoxMapper.frozenBoxesToFrozenBoxDTOs(frozenBoxList);
    }
    /**
     * 根据冻存盒id查询冻存管信息
     * @param frozenBoxId 冻存盒id
     * @return
     */
    @Override
    public FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxId(Long frozenBoxId) {
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();
        //查询冻存盒信息
        FrozenBox frozenBox = frozenBoxRepository.findOne(frozenBoxId);

        //查询冻存管列表信息
        List<FrozenTube> frozenTube = frozenTubeService.findFrozenTubeListByBoxId(frozenBoxId);
        List<FrozenTubeResponse> frozenTubeResponses = frozenTubeMapping.frozenTubeToFrozenTubeResponse(frozenTube);

        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox,frozenTubeResponses);

        return res;
    }
    /**
     * 根据冻存盒CODE查询冻存管信息
     * @param frozenBoxCode 冻存盒CODE
     * @return
     */
    @Override
    public FrozenBoxAndFrozenTubeResponse findFrozenBoxAndTubeByBoxCode(String frozenBoxCode) {
        FrozenBoxAndFrozenTubeResponse res = new FrozenBoxAndFrozenTubeResponse();

        //查询冻存盒信息
        FrozenBox frozenBox = this.findFrozenBoxDetailsByBoxCode(frozenBoxCode);

        //查询冻存管列表信息
        List<FrozenTube> frozenTube = frozenTubeService.findFrozenTubeListByBoxCode(frozenBoxCode);

        List<FrozenTubeResponse> frozenTubeResponses = frozenTubeMapping.frozenTubeToFrozenTubeResponse(frozenTube);

        res = frozenBoxMapper.forzenBoxAndTubeToResponse(frozenBox,frozenTubeResponses);

        return res;
    }

    /**
     * 根据冻存盒code查询冻存盒基本信息
     * @param frozenBoxCode 冻存盒code
     * @return
     */
    public FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode) {
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
        return frozenBox;
    }

    /**
     * 批量保存冻存盒
     * @param frozenBoxDTOList
     */
    @Override
    public List<FrozenBox> saveBatch(List<FrozenBoxDTO> frozenBoxDTOList) {
        Assert.notNull(frozenBoxDTOList,"frozen Box must not be null");
        List<FrozenBox> frozenBoxes = frozenBoxMapper.frozenBoxDTOsToFrozenBoxes(frozenBoxDTOList);
        List<FrozenBox> frozenBoxList = frozenBoxRepository.save(frozenBoxes);
        return frozenBoxList;
    }

    /**
     * 根据冻存盒code串查询冻存盒以及冻存管的信息
     * @param frozenBoxCodeStr 冻存盒code串
     * @return
     */
    @Override
    public List<FrozenBoxAndFrozenTubeResponse> findFrozenBoxAndTubeListByBoxCodeStr(String frozenBoxCodeStr) {
        List<FrozenBoxAndFrozenTubeResponse> frozenBoxAndFrozenTubeResponses = new ArrayList<FrozenBoxAndFrozenTubeResponse>();

         if(StringUtils.isEmpty(frozenBoxCodeStr)){
            throw new BankServiceException("请传入有效的冻存盒编码！",frozenBoxCodeStr);
         }

        String frozenBoxCode[] = frozenBoxCodeStr.split(",");
        for(String i :frozenBoxCode){
            FrozenBoxAndFrozenTubeResponse response = findFrozenBoxAndTubeByBoxCode(i);
            frozenBoxAndFrozenTubeResponses.add(response);
        }

        return frozenBoxAndFrozenTubeResponses;
    }

    /**
     * 判断某设备某区域某架子某行某列是否有盒子存在
     * @param equipmentId
     * @param areaId
     * @param supportRackId
     * @param column
     * @param row
     * @return
     */
    @Override
    public Long countByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row) {
        Long counts = frozenBoxRepository.countByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(equipmentId,areaId,supportRackId,column,row);
        return counts;
    }
}
