package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.service.TranshipTubeService;
import org.fwoxford.domain.TranshipTube;
import org.fwoxford.repository.TranshipTubeRepository;
import org.fwoxford.service.dto.TranshipTubeDTO;
import org.fwoxford.service.mapper.TranshipTubeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TranshipTube.
 */
@Service
@Transactional
public class TranshipTubeServiceImpl implements TranshipTubeService{

    private final Logger log = LoggerFactory.getLogger(TranshipTubeServiceImpl.class);

    private final TranshipTubeRepository transhipTubeRepository;

    private final TranshipTubeMapper transhipTubeMapper;

    public TranshipTubeServiceImpl(TranshipTubeRepository transhipTubeRepository, TranshipTubeMapper transhipTubeMapper) {
        this.transhipTubeRepository = transhipTubeRepository;
        this.transhipTubeMapper = transhipTubeMapper;
    }

    /**
     * Save a transhipTube.
     *
     * @param transhipTubeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranshipTubeDTO save(TranshipTubeDTO transhipTubeDTO) {
        log.debug("Request to save TranshipTube : {}", transhipTubeDTO);
        TranshipTube transhipTube = transhipTubeMapper.transhipTubeDTOToTranshipTube(transhipTubeDTO);
        transhipTube = transhipTubeRepository.save(transhipTube);
        TranshipTubeDTO result = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);
        return result;
    }

    /**
     *  Get all the transhipTubes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranshipTubeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TranshipTubes");
        Page<TranshipTube> result = transhipTubeRepository.findAll(pageable);
        return result.map(transhipTube -> transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube));
    }

    /**
     *  Get one transhipTube by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranshipTubeDTO findOne(Long id) {
        log.debug("Request to get TranshipTube : {}", id);
        TranshipTube transhipTube = transhipTubeRepository.findOne(id);
        TranshipTubeDTO transhipTubeDTO = transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube);
        return transhipTubeDTO;
    }

    /**
     *  Delete the  transhipTube by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TranshipTube : {}", id);
        transhipTubeRepository.delete(id);
    }

    @Override
    public List<TranshipTubeDTO> saveTranshipTube(TranshipBox transhipBox, List<FrozenTube> frozenTubeList) {
        if(transhipBox == null){
            return null;
        }
        List<TranshipTubeDTO> transhipTubeDTOS = new ArrayList<TranshipTubeDTO>();
        List<TranshipTube> transhipTubes = transhipTubeRepository.findByTranshipBoxIdLast(transhipBox.getId());

        for (FrozenTube frozenTube : frozenTubeList) {
            for(TranshipTube f:transhipTubes) {
                if (f.getFrozenTube().getId().equals(frozenTube.getId())) {
                    continue;
                } else {
                    f.setStatus(Constants.INVALID);
                }
            }
        }
        for (FrozenTube frozenTube : frozenTubeList) {
            if (frozenTube.getId() == null) {
                return null;
            }
            Long count = transhipTubeRepository.countByColumnsInTubeAndRowsInTubeAndStatusAndTranshipBoxIdAndFrozenTubeId(
                frozenTube.getTubeColumns(), frozenTube.getTubeRows(), frozenTube.getStatus(), transhipBox.getId(), frozenTube.getId()
            );
            if (count.intValue() == 0) {
                TranshipTube transhipTube = new TranshipTube();
                transhipTube.status(frozenTube.getStatus()).memo(frozenTube.getMemo())
                    .rowsInTube(frozenTube.getTubeRows()).frozenTube(frozenTube).columnsInTube(frozenTube.getTubeColumns())
                    .transhipBox(transhipBox).errorType(frozenTube.getErrorType()).frozenBoxCode(frozenTube.getFrozenBoxCode())
                    .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(frozenTube.getFrozenTubeState())
                    .frozenTubeType(frozenTube.getFrozenTubeType()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                    .project(frozenTube.getProject()).projectCode(frozenTube.getProjectCode()).projectSite(frozenTube.getProjectSite())
                    .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassification(frozenTube.getSampleClassification())
                    .sampleClassificationCode(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getSampleClassificationCode():null)
                    .sampleClassificationName(frozenTube.getSampleClassification()!=null?frozenTube.getSampleClassification().getSampleClassificationName():null)
                    .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode()).sampleType(frozenTube.getSampleType())
                    .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                    .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
                transhipTubeDTOS.add(transhipTubeMapper.transhipTubeToTranshipTubeDTO(transhipTube));
                transhipTubeRepository.save(transhipTube);
            }
        }
        return transhipTubeDTOS;
    }
}
