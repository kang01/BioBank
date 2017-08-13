package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.domain.StockOutFiles;
import org.fwoxford.service.AttachmentService;
import org.fwoxford.service.StockOutFilesService;
import org.fwoxford.service.dto.StockOutFilesDTO;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.AttachmentDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Attachment.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private static final String ENTITY_NAME = "attachment";

    private final AttachmentService attachmentService;

    @Autowired
    private StockOutFilesService stockOutFilesService;
    public AttachmentResource(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * POST  /attachments : Create a new attachment.
     *
     * @param attachmentDTO the attachmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attachmentDTO, or with status 400 (Bad Request) if the attachment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attachments")
    @Timed
    public ResponseEntity<AttachmentDTO> createAttachment(@Valid @RequestBody AttachmentDTO attachmentDTO) throws URISyntaxException {
        log.debug("REST request to save Attachment : {}", attachmentDTO);
        if (attachmentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new attachment cannot already have an ID")).body(null);
        }
        AttachmentDTO result = attachmentService.save(attachmentDTO);
        return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * 修改已经上传的文件
     * 只能修改图片的title和description
     * @param attachmentDTO the attachmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attachmentDTO,
     * or with status 400 (Bad Request) if the attachmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the attachmentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attachments")
    @Timed
    public ResponseEntity<AttachmentDTO> updateAttachment(@Valid @RequestBody AttachmentDTO attachmentDTO) throws URISyntaxException {
        log.debug("REST request to update Attachment : {}", attachmentDTO);
        if (attachmentDTO.getId() == null) {
            throw new BankServiceException("文件ID不能为空");
        }
        AttachmentDTO result = attachmentService.updateAttachment(attachmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attachments : get all the attachments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attachments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/attachments")
    @Timed
    public ResponseEntity<List<AttachmentDTO>> getAllAttachments(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Attachments");
        Page<AttachmentDTO> page = attachmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attachments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attachments/:id : get the "id" attachment.
     *
     * @param id the id of the attachmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attachmentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable Long id) {
        log.debug("REST request to get Attachment : {}", id);
        AttachmentDTO attachmentDTO = attachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attachmentDTO));
    }

    /**
     * 删除上传的文件
     * @param id the id of the attachmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        log.debug("REST request to delete Attachment : {}", id);
        attachmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 根据转运编码查询上传的文件
     * @param code
     * @param request
     * @return
     */
    @GetMapping("/attachments/transhipCode/{code}")
    @Timed
    public ResponseEntity<List<AttachmentDTO>> getTranship(@PathVariable String code, HttpServletRequest request) {
        log.debug("REST request to get AttachmentDTOs : {}", code);
        List<AttachmentDTO> attachmentDTOS = attachmentService.findFilesByTranshipCode(code,request);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attachmentDTOS));
    }
    /**
     * 读取文件
     * @param headFileId
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/attachments/{headFileId}/{fileName}",method = RequestMethod.GET,
        produces = "image/*")
    @Timed
    public ResponseEntity findDataById(@PathVariable Long headFileId, @PathVariable String fileName) {
        try{
            StockOutFilesDTO data = stockOutFilesService.findOne(headFileId);
            if(data != null) {
                byte[] fileData = data.getFiles();
                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(new MediaType("application", data.getFileType()));
                headers.set("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
                return new ResponseEntity(fileData, headers, HttpStatus.OK);

            } else {
                return ResponseEntity.badRequest().build();
            }
        }catch(Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
}
