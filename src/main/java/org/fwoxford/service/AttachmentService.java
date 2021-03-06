package org.fwoxford.service;

import org.fwoxford.service.dto.AttachmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Service Interface for managing Attachment.
 */
public interface AttachmentService {

    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save
     * @return the persisted entity
     */
    AttachmentDTO save(AttachmentDTO attachmentDTO);

    /**
     *  Get all the attachments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AttachmentDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" attachment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AttachmentDTO findOne(Long id);

    /**
     *  Delete the "id" attachment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *根据转运编码获取转运上传的图片
     * @param code
     * @param request
     * @return
     */
    List<AttachmentDTO> findFilesByTranshipCode(String code, HttpServletRequest request);

    /**
     * 修改附件的基本信息
     * @param attachmentDTO
     * @return
     */
    AttachmentDTO updateAttachment(AttachmentDTO attachmentDTO);
}
