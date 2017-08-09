package org.fwoxford.service.impl;

import org.fwoxford.domain.StockOutFiles;
import org.fwoxford.domain.Tranship;
import org.fwoxford.repository.StockOutFilesRepository;
import org.fwoxford.repository.TranshipRepository;
import org.fwoxford.service.AttachmentService;
import org.fwoxford.domain.Attachment;
import org.fwoxford.repository.AttachmentRepository;
import org.fwoxford.service.dto.AttachmentDTO;
import org.fwoxford.service.mapper.AttachmentMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Attachment.
 */
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService{

    private final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;

    @Autowired
    private TranshipRepository transhipRepository;

    @Autowired
    private StockOutFilesRepository stockOutFilesRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AttachmentDTO save(AttachmentDTO attachmentDTO) {
        log.debug("Request to save Attachment : {}", attachmentDTO);
        Attachment attachment = attachmentMapper.attachmentDTOToAttachment(attachmentDTO);
        attachment = attachmentRepository.save(attachment);
        AttachmentDTO result = attachmentMapper.attachmentToAttachmentDTO(attachment);
        return result;
    }

    /**
     *  Get all the attachments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        Page<Attachment> result = attachmentRepository.findAll(pageable);
        return result.map(attachment -> attachmentMapper.attachmentToAttachmentDTO(attachment));
    }

    /**
     *  Get one attachment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AttachmentDTO findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        Attachment attachment = attachmentRepository.findOne(id);
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);
        return attachmentDTO;
    }

    /**
     *  Delete the  attachment by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.delete(id);
    }

    @Override
    public List<AttachmentDTO> findFilesByTranshipCode(String code, HttpServletRequest request) {
        List<AttachmentDTO> attachmentDTOS = new ArrayList<>();
        Tranship tranship = transhipRepository.findByTranshipCode(code);
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+
            request.getServerPort()+"/api/attachments/";

        if(tranship == null){
            throw new BankServiceException("转运记录不存在！");
        }
        List<Attachment> attachmentList = attachmentRepository.findByBusinessId(tranship.getId());
        if(attachmentList == null || attachmentList.size()==0){
            return null;
        }
        for(Attachment attachment : attachmentList){
            AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);
            Long file = attachment.getFileId1();
            if(file!=null){
                StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(file);
                if(stockOutFiles == null){
                    throw new BankServiceException("文件不存在！");
                }
                String big = basePath+stockOutFiles.getId()+"/"+stockOutFiles.getFilePath();
                attachmentDTO.setBigImage(big);
            }
            Long file2 = attachment.getFileId2();
            if(file2!=null){
                StockOutFiles stockOutFiles = stockOutFilesRepository.findOne(file2);
                if(stockOutFiles == null){
                    throw new BankServiceException("文件不存在！");
                }
                String small = basePath+stockOutFiles.getId()+"/"+stockOutFiles.getFilePath();
                attachmentDTO.setSmallImage(small);
            }
            attachmentDTOS.add(attachmentDTO);
        }
        return attachmentDTOS;
    }
}
