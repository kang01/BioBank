package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.SampleType;
import org.fwoxford.repository.SampleTypeRepository;
import org.fwoxford.service.ProjectSampleClassService;
import org.fwoxford.domain.ProjectSampleClass;
import org.fwoxford.repository.ProjectSampleClassRepository;
import org.fwoxford.service.dto.ProjectSampleClassDTO;
import org.fwoxford.service.dto.ProjectSampleClassificationDTO;
import org.fwoxford.service.dto.ProjectSampleTypeDTO;
import org.fwoxford.service.dto.SampleTypeDTO;
import org.fwoxford.service.mapper.ProjectSampleClassMapper;
import org.fwoxford.service.mapper.SampleTypeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProjectSampleClass.
 */
@Service
@Transactional
public class ProjectSampleClassServiceImpl implements ProjectSampleClassService{

    private final Logger log = LoggerFactory.getLogger(ProjectSampleClassServiceImpl.class);

    private final ProjectSampleClassRepository projectSampleClassRepository;

    private final ProjectSampleClassMapper projectSampleClassMapper;

    @Autowired
    private SampleTypeRepository sampleTypeRepository;

    @Autowired
    private SampleTypeMapper sampleTypeMapper;

    public ProjectSampleClassServiceImpl(ProjectSampleClassRepository projectSampleClassRepository, ProjectSampleClassMapper projectSampleClassMapper) {
        this.projectSampleClassRepository = projectSampleClassRepository;
        this.projectSampleClassMapper = projectSampleClassMapper;
    }

    /**
     * Save a projectSampleClass.
     *
     * @param projectSampleClassDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectSampleClassDTO save(ProjectSampleClassDTO projectSampleClassDTO) {
        log.debug("Request to save ProjectSampleClass : {}", projectSampleClassDTO);
        ProjectSampleClass projectSampleClass = projectSampleClassMapper.projectSampleClassDTOToProjectSampleClass(projectSampleClassDTO);
        projectSampleClass = projectSampleClassRepository.save(projectSampleClass);
        ProjectSampleClassDTO result = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);
        return result;
    }

    /**
     *  Get all the projectSampleClasses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectSampleClassDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectSampleClasses");
        Page<ProjectSampleClass> result = projectSampleClassRepository.findAll(pageable);
        return result.map(projectSampleClass -> projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass));
    }

    /**
     *  Get one projectSampleClass by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectSampleClassDTO findOne(Long id) {
        log.debug("Request to get ProjectSampleClass : {}", id);
        ProjectSampleClass projectSampleClass = projectSampleClassRepository.findOne(id);
        ProjectSampleClassDTO projectSampleClassDTO = projectSampleClassMapper.projectSampleClassToProjectSampleClassDTO(projectSampleClass);
        return projectSampleClassDTO;
    }

    /**
     *  Delete the  projectSampleClass by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectSampleClass : {}", id);
        projectSampleClassRepository.delete(id);
    }

    @Override
    public List<SampleTypeDTO> getSampleTypeByProjectId(Long projectId) {
        if(projectId == null){
            throw new BankServiceException("项目ID不能为空！");
        }
        List<SampleTypeDTO> sampleTypeDTOS = new ArrayList<SampleTypeDTO>();
        List<SampleType> sampleTypeList = sampleTypeRepository.findAll();
        for(SampleType s :sampleTypeList){
            SampleTypeDTO sampleTypeDTO = sampleTypeMapper.sampleTypeToSampleTypeDTO(s);
            int count = projectSampleClassRepository.countByProjectIdAndSampleTypeId(projectId,s.getId());
            if(count==0){
                sampleTypeDTO.setFlag(Constants.NO);
            }else{
                sampleTypeDTO.setFlag(Constants.YES);
            }
            sampleTypeDTOS.add(sampleTypeDTO);
        }
        return sampleTypeDTOS;
    }

    @Override
    public List<ProjectSampleClassificationDTO> getSampleClassificationByProjectIdAndsampleTypeId(Long projectId, Long sampleTypeId) {
        if(projectId ==null){
            throw new BankServiceException("项目ID不能为空！");
        }
        if(sampleTypeId ==null){
            throw new BankServiceException("样本类型Id不能为空！");
        }
        List<ProjectSampleClassificationDTO> projectSampleClassificationDTOS = new ArrayList<ProjectSampleClassificationDTO>();
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findByProjectIdAndSampleTypeId(projectId,sampleTypeId);
        for(ProjectSampleClass p :projectSampleClasses){
            ProjectSampleClassificationDTO projectSampleClassificationDTO = projectSampleClassMapper.projectSampleClassToProjectClassificationDTO(p);
            projectSampleClassificationDTO.setSampleClassificationCode(p.getSampleClassification()!=null?p.getSampleClassification().getSampleClassificationCode():null);
            projectSampleClassificationDTOS.add(projectSampleClassificationDTO);
        }
        return projectSampleClassificationDTOS;
    }

    @Override
    public List<ProjectSampleClass> findByProjectIdAndSampleTypeIdAndSampleClassificationId(Long projectId, Long sampleTypeId, Long sampleClassificationId) {
        if(projectId ==null){
            throw new BankServiceException("项目ID不能为空！");
        }
        if(sampleTypeId==null){
            throw new BankServiceException("样本类型Id不能为空！");
        }
        List<ProjectSampleClass> projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeId(projectId,sampleTypeId);
        return projectSampleClass;
    }

    @Override
    public List<ProjectSampleClassificationDTO> getSampleClassificationByProjectIdsAndsampleTypeId(String projectIds, Long sampleTypeId) {
        List<ProjectSampleClassificationDTO> projectSampleClassificationDTOS = new ArrayList<ProjectSampleClassificationDTO>();
        if(StringUtils.isEmpty(projectIds)){
            throw new BankServiceException("项目ID不能为空！",projectIds);
        }
        if(sampleTypeId ==null){
            throw new BankServiceException("样本类型Id不能为空！");
        }
        List<ProjectSampleClass> projectSampleClasses = new ArrayList<ProjectSampleClass>();
        String[] projectIdStr = projectIds.split(",");
        List<Long> projectIdList = new ArrayList<Long>();
        for(String s :projectIdStr){
            Long projectId = Long.valueOf(s);
            projectIdList.add(projectId);
        }
        projectSampleClasses = projectSampleClassRepository.findByProjectIdInAndSampleTypeId(projectIdList,sampleTypeId);
        projectSampleClassificationDTOS = projectSampleClassMapper.projectSampleClassesToProjectClassificationDTOs(projectSampleClasses);
        return projectSampleClassificationDTOS;
    }
}
