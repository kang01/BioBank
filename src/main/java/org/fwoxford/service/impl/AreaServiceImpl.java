package org.fwoxford.service.impl;

import org.fwoxford.service.AreaService;
import org.fwoxford.domain.Area;
import org.fwoxford.repository.AreaRepository;
import org.fwoxford.service.dto.AreaDTO;
import org.fwoxford.service.mapper.AreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Area.
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService{

    private final Logger log = LoggerFactory.getLogger(AreaServiceImpl.class);

    private final AreaRepository areaRepository;

    private final AreaMapper areaMapper;

    public AreaServiceImpl(AreaRepository areaRepository, AreaMapper areaMapper) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
    }

    /**
     * Save a area.
     *
     * @param areaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AreaDTO save(AreaDTO areaDTO) {
        log.debug("Request to save Area : {}", areaDTO);
        Area area = areaMapper.areaDTOToArea(areaDTO);
        area = areaRepository.save(area);
        AreaDTO result = areaMapper.areaToAreaDTO(area);
        return result;
    }

    /**
     *  Get all the areas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AreaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Areas");
        Page<Area> result = areaRepository.findAll(pageable);
        return result.map(area -> areaMapper.areaToAreaDTO(area));
    }

    /**
     *  Get one area by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AreaDTO findOne(Long id) {
        log.debug("Request to get Area : {}", id);
        Area area = areaRepository.findOne(id);
        AreaDTO areaDTO = areaMapper.areaToAreaDTO(area);
        return areaDTO;
    }

    /**
     *  Delete the  area by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Area : {}", id);
        areaRepository.delete(id);
    }

    /**
     * 根據設備ID查詢區域信息
     * @param equipmentId 設備ID
     * @return
     */
    @Override
    public List<AreaDTO> getAreaByEquipmentId(Long equipmentId) {
        log.debug("Request to get Area By EquipmentId : {}", equipmentId);
        List<Area> areas = areaRepository.getAreaByEquipmentId(equipmentId);
        List<AreaDTO> areaDTOS = areaMapper.areasToAreaDTOs(areas);
        return areaDTOS;
    }
}
