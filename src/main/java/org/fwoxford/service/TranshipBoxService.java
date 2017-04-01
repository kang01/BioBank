package org.fwoxford.service;

import org.fwoxford.domain.TranshipBox;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.TranshipBoxListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing TranshipBox.
 */
public interface TranshipBoxService {

    /**
     * Save a transhipBox.
     *
     * @param transhipBoxDTO the entity to save
     * @return the persisted entity
     */
    TranshipBoxDTO save(TranshipBoxDTO transhipBoxDTO);

    /**
     *  Get all the transhipBoxes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TranshipBoxDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transhipBox.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TranshipBoxDTO findOne(Long id);

    /**
     *  Delete the "id" transhipBox.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 批量保存转运与冻存盒的关系
     * @param transhipBoxDTOList
     * @return
     */
    List<TranshipBoxDTO> saveBatch(List<TranshipBoxDTO> transhipBoxDTOList);

    /**
     * 根据转运ID和冻存盒ID查询转运与冻存盒的关系
     * @param transhipId
     * @param id
     * @return
     */
    TranshipBoxDTO findByTranshipIdAndFrozenBoxId(Long transhipId, Long id);

    /**
     * 批量保存转运的冻存盒
     * @param transhipBoxListDTO
     * @return
     */
    TranshipBoxListDTO saveBatchTranshipBox(TranshipBoxListDTO transhipBoxListDTO);
}
