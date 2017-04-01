package org.fwoxford.service.impl;

import com.fasterxml.jackson.annotation.JsonView;
import org.fwoxford.domain.*;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.repository.TranshipRepository;
import org.fwoxford.service.*;
import org.fwoxford.repository.TranshipBoxRepository;
import org.fwoxford.service.dto.*;
import org.fwoxford.service.mapper.FrozenBoxMapper;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.mapper.TranshipBoxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TranshipBox.
 */
@Service
@Transactional
public class TranshipBoxServiceImpl implements TranshipBoxService{

    private final Logger log = LoggerFactory.getLogger(TranshipBoxServiceImpl.class);

    private final TranshipBoxRepository transhipBoxRepository;
    private final FrozenBoxRepository frozenBoxRepository;
    private final FrozenTubeRepository frozenTubeRepository;
    private final TranshipRepository transhipRepository;


    private final TranshipBoxMapper transhipBoxMapper;

    @Autowired
    private  FrozenBoxService frozenBoxService;

    @Autowired
    private FrozenBoxMapper frozenBoxMapper;

    @Autowired
    private TranshipService transhipService;

    @Autowired
    private FrozenTubeService frozenTubeService;

    @Autowired
    private FrozenTubeMapper frozenTubeMapper;

    @Autowired
    private FrozenBoxTypeService frozenBoxTypeService;

    @Autowired
    private SampleTypeService sampleTypeService;

    public TranshipBoxServiceImpl(TranshipBoxRepository transhipBoxRepository,
                                  FrozenBoxRepository frozenBoxRepository,
                                  FrozenTubeRepository frozenTubeRepository,
                                  TranshipRepository transhipRepository,
                                  TranshipBoxMapper transhipBoxMapper) {
        this.transhipBoxRepository = transhipBoxRepository;
        this.transhipBoxMapper = transhipBoxMapper;
        this.frozenBoxRepository = frozenBoxRepository;
        this.frozenTubeRepository = frozenTubeRepository;
        this.transhipRepository = transhipRepository;
    }

    /**
     * Save a transhipBox.
     *
     * @param transhipBoxDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranshipBoxDTO save(TranshipBoxDTO transhipBoxDTO) {
        log.debug("Request to save TranshipBox : {}", transhipBoxDTO);

        TranshipBox transhipBox = transhipBoxMapper.transhipBoxDTOToTranshipBox(transhipBoxDTO);
        TranshipBox newTranshipBox = transhipBoxMapper.initTranshipToTranship(transhipBox);
        transhipBox = transhipBoxRepository.save(newTranshipBox);
        TranshipBoxDTO result = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);
        return result;
    }

    /**
     *  Get all the transhipBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranshipBoxDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TranshipBoxes");
        Page<TranshipBox> result = transhipBoxRepository.findAll(pageable);
        return result.map(transhipBox -> transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox));
    }

    /**
     *  Get one transhipBox by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranshipBoxDTO findOne(Long id) {
        log.debug("Request to get TranshipBox : {}", id);
        TranshipBox transhipBox = transhipBoxRepository.findOne(id);
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);
        return transhipBoxDTO;
    }

    /**
     *  Delete the  transhipBox by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TranshipBox : {}", id);
        transhipBoxRepository.delete(id);
    }

    /**
     * 批量保存转运与冻存盒的关系
     * @param transhipBoxDTOList
     * @return
     */
    @Override
    public List<TranshipBoxDTO> saveBatch(List<TranshipBoxDTO> transhipBoxDTOList) {
        List<TranshipBox> transhipBoxes = transhipBoxMapper.transhipBoxDTOsToTranshipBoxes(transhipBoxDTOList);
        List<TranshipBox> transhipBoxList =  transhipBoxRepository.save(transhipBoxes);
        return transhipBoxMapper.transhipBoxesToTranshipBoxDTOs(transhipBoxList);
    }

    /**
     * 根据转运ID和冻存盒ID查询转运与冻存盒的关系
     * @param transhipId 转运ID
     * @param frozenBoxId 冻存盒ID
     * @return
     */
    @Override
    public TranshipBoxDTO findByTranshipIdAndFrozenBoxId(Long transhipId, Long frozenBoxId) {
        TranshipBox transhipBox = transhipBoxRepository.findByTranshipIdAndFrozenBoxId(transhipId,frozenBoxId);
        TranshipBoxDTO transhipBoxDTO = transhipBoxMapper.transhipBoxToTranshipBoxDTO(transhipBox);
        return transhipBoxDTO;
    }

    /**
     * 批量保存转运的冻存盒
     *
     * @param transhipBoxListDTO
     * @return
     */
    @Override
    public TranshipBoxListDTO saveBatchTranshipBox(TranshipBoxListDTO transhipBoxListDTO) {
        Long transhipId = transhipBoxListDTO.getTranshipId();
        List<FrozenBoxDTO> frozenBoxDTOList = transhipBoxListDTO.getFrozenBoxDTOList();
        frozenBoxDTOList = createFrozenBoxAndTubeDetail(frozenBoxDTOList);
        //保存冻存盒
        Tranship tranship = new Tranship();tranship.setId(transhipId);
        List<FrozenBoxDTO> frozenBoxDTOLists =  frozenBoxMapper.frozenTranshipAndBoxToFrozenBoxDTOList(frozenBoxDTOList,tranship);
        List<FrozenBox> frozenBoxes =  frozenBoxService.saveBatch(frozenBoxDTOLists);
        List<FrozenBoxDTO> frozenBoxDTOListLast = frozenBoxMapper.frozenBoxesToFrozenBoxDTOs(frozenBoxes);

        //保存冻存盒和转运的关系
        List<TranshipBoxDTO> transhipBoxDTOList = new ArrayList<TranshipBoxDTO>();
        for(FrozenBoxDTO boxDTO : frozenBoxDTOListLast){
            TranshipBoxDTO transhipBoxDTO = frozenBoxMapper.frozenBoxToTranshipBoxDTO(boxDTO);
            transhipBoxDTOList.add(transhipBoxDTO);
        }
        List<TranshipBoxDTO> transhipBoxes =  this.saveBatch(transhipBoxDTOList);

        //保存冻存管
//        List<FrozenTubeDTO> frozenTubeDTOList = frozenTubeMapper.frozenBoxAndTubeToFrozenTubeDTOList(frozenBoxDTOList,frozenBoxes);
        List<FrozenTubeDTO> frozenTubeDTOList = transhipService.getFrozenTubeDTOList(frozenBoxDTOList,frozenBoxes);
        List<FrozenTube> frozenTubes =  frozenTubeService.saveBatch(frozenTubeDTOList);
        List<FrozenTubeDTO> frozenTubeDTOS = frozenTubeMapper.frozenTubesToFrozenTubeDTOs(frozenTubes);
        List<FrozenBoxDTO> alist = transhipService.getFrozenBoxDtoList(frozenBoxDTOListLast,frozenTubeDTOS);
        transhipBoxListDTO.setFrozenBoxDTOList(alist);
        return transhipBoxListDTO;
    }

    /**
     * 删除冻存管，冻存盒，转运盒子
     * @param transhipBoxListDTO
     */
    public void deleteTranshipBoxAndTube(TranshipBoxListDTO transhipBoxListDTO) {
        List<FrozenBoxDTO> frozenBoxDTOList = transhipBoxListDTO.getFrozenBoxDTOList();
        for(FrozenBoxDTO box:frozenBoxDTOList){
            transhipBoxRepository.deleteByFrozenBoxId(box.getId());

//            FrozenBox frozenBox = frozenBoxService.findFrozenBoxDetailsByBoxCode(box.getFrozenBoxCode());
//            if(frozenBox != null){
//                List<FrozenTube> frozenTubeS = frozenTubeService.findFrozenTubeListByBoxId(frozenBox.getId());
//                for(FrozenTube tube: frozenTubeS){
//                    //删除管子
//                    frozenTubeService.delete(tube.getId());
//                }
//                //删除转运盒子
//                transhipBoxRepository.deleteByFrozenBoxId(box.getId());
//                //删除盒子
//                frozenBoxService.delete(frozenBox.getId());
//            }
        }
    }

    public List<FrozenBoxDTO> createFrozenBoxAndTubeDetail(List<FrozenBoxDTO> frozenBoxDTOList) {
        List<FrozenBoxDTO> dtos = new ArrayList<FrozenBoxDTO>();

        List<FrozenBoxTypeDTO> frozenBoxTypeDTOList = frozenBoxTypeService.findAllFrozenBoxTypes();
        List<SampleTypeDTO> sampleTypeDTOS = sampleTypeService.findAllSampleTypes();
        for(FrozenBoxDTO box :frozenBoxDTOList){
            for(FrozenBoxTypeDTO boxType: frozenBoxTypeDTOList){
                if(box.getFrozenBoxTypeId().equals(boxType.getId())){
                    box.setFrozenBoxColumns(boxType.getFrozenBoxTypeColumns());
                    box.setFrozenBoxTypeCode(boxType.getFrozenBoxTypeCode());
                    box.setFrozenBoxRows(boxType.getFrozenBoxTypeRows());
                }
            }
            for(SampleTypeDTO sampleType : sampleTypeDTOS){
                if(box.getSampleTypeId().equals(sampleType.getId())){
                    box.setSampleTypeName(sampleType.getSampleTypeName());
                    box.setSampleTypeCode(sampleType.getSampleTypeCode());
                }
            }
            dtos.add(box);
        }
        return dtos;
    }
}
