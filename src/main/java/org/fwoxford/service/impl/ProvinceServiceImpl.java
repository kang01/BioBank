package org.fwoxford.service.impl;

import org.fwoxford.service.ProvinceService;
import org.fwoxford.domain.Province;
import org.fwoxford.repository.ProvinceRepository;
import org.fwoxford.service.dto.ProvinceDTO;
import org.fwoxford.service.mapper.ProvinceMapper;
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
 * Service Implementation for managing Province.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService{

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);
    
    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        Province province = provinceMapper.provinceDTOToProvince(provinceDTO);
        province = provinceRepository.save(province);
        ProvinceDTO result = provinceMapper.provinceToProvinceDTO(province);
        return result;
    }

    /**
     *  Get all the provinces.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        Page<Province> result = provinceRepository.findAll(pageable);
        return result.map(province -> provinceMapper.provinceToProvinceDTO(province));
    }

    /**
     *  Get one province by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProvinceDTO findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        Province province = provinceRepository.findOne(id);
        ProvinceDTO provinceDTO = provinceMapper.provinceToProvinceDTO(province);
        return provinceDTO;
    }

    /**
     *  Delete the  province by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.delete(id);
    }
}
