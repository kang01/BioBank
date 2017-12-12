package org.fwoxford.service;

import org.fwoxford.service.dto.*;
import org.fwoxford.service.dto.response.StockInForDataDetail;
import org.fwoxford.service.dto.response.TranshipByIdResponse;
import org.fwoxford.service.dto.response.TranshipResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Service Interface for managing Tranship.
 */
public interface TranshipService {

    /**
     * Save a tranship.
     *
     * @param transhipDTO the entity to save
     * @return the persisted entity
     */
    TranshipDTO save(TranshipDTO transhipDTO);

    /**
     *  Get all the tranships.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tranship.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipDTO findOne(Long id);

    /**
     *  Delete the "id" tranship.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 查询转运记录列表
     * @param input
     * @return
     */
    DataTablesOutput<TranshipResponse> findAllTranship(DataTablesInput input,String receiveType);

    /**
     * 根据转运记录ID查询转运记录以及冻存盒信息
     * @param id
     * @return
     */
    TranshipByIdResponse findTranshipAndFrozenBox(Long id);

    /**
     * 初始化转运记录
     * @return
     */
    TranshipDTO initTranship();
    TranshipDTO initTranship(Long projectId, Long projectSiteId,Long stockOutApplyId);

    /**
     * 作废转运记录
     * @param transhipCode
     * @return
     */
    TranshipDTO invalidTranship(String transhipCode);

    Boolean isRepeatTrackNumber(String transhipCode, String trackNumber);

    /**
     * 转运完成
     * @param transhipCode
     * @param transhipToStockInDTO
     * @return
     */
    StockInForDataDetail completedTranship(String transhipCode, TranshipToStockInDTO transhipToStockInDTO);

    /**
     * 上传图片
     *
     * @param attachmentDTO
     * @param transhipId
     * @param file
     * @param request
     * @return
     */
    AttachmentDTO saveAndUploadTranship(AttachmentDTO attachmentDTO, Long transhipId, MultipartFile file, HttpServletRequest request);

    /**
     * 初始化归还记录
     * @param stockOutApplyId
     * @return
     */
    TranshipDTO initReturnBack(Long stockOutApplyId);

    /**
     * 修改保存归还记录
     * @param transhipDTO
     * @return
     */
    TranshipDTO saveReturnBack(TranshipDTO transhipDTO);
}
