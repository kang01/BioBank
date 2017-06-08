package org.fwoxford.service.impl;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.TranshipBox;
import org.fwoxford.service.TranshipBoxPositionService;
import org.fwoxford.domain.TranshipBoxPosition;
import org.fwoxford.repository.TranshipBoxPositionRepository;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.TranshipBoxPositionDTO;
import org.fwoxford.service.mapper.TranshipBoxPositionMapper;
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
 * Service Implementation for managing TranshipBoxPosition.
 */
@Service
@Transactional
public class TranshipBoxPositionServiceImpl implements TranshipBoxPositionService{

    private final Logger log = LoggerFactory.getLogger(TranshipBoxPositionServiceImpl.class);

    private final TranshipBoxPositionRepository transhipBoxPositionRepository;

    private final TranshipBoxPositionMapper transhipBoxPositionMapper;

    public TranshipBoxPositionServiceImpl(TranshipBoxPositionRepository transhipBoxPositionRepository, TranshipBoxPositionMapper transhipBoxPositionMapper) {
        this.transhipBoxPositionRepository = transhipBoxPositionRepository;
        this.transhipBoxPositionMapper = transhipBoxPositionMapper;
    }

    /**
     * Save a transhipBoxPosition.
     *
     * @param transhipBoxPositionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranshipBoxPositionDTO save(TranshipBoxPositionDTO transhipBoxPositionDTO) {
        log.debug("Request to save TranshipBoxPosition : {}", transhipBoxPositionDTO);
        TranshipBoxPosition transhipBoxPosition = transhipBoxPositionMapper.transhipBoxPositionDTOToTranshipBoxPosition(transhipBoxPositionDTO);
        transhipBoxPosition = transhipBoxPositionRepository.save(transhipBoxPosition);
        TranshipBoxPositionDTO result = transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(transhipBoxPosition);
        return result;
    }

    /**
     *  Get all the transhipBoxPositions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TranshipBoxPositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TranshipBoxPositions");
        Page<TranshipBoxPosition> result = transhipBoxPositionRepository.findAll(pageable);
        return result.map(transhipBoxPosition -> transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(transhipBoxPosition));
    }

    /**
     *  Get one transhipBoxPosition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TranshipBoxPositionDTO findOne(Long id) {
        log.debug("Request to get TranshipBoxPosition : {}", id);
        TranshipBoxPosition transhipBoxPosition = transhipBoxPositionRepository.findOne(id);
        TranshipBoxPositionDTO transhipBoxPositionDTO = transhipBoxPositionMapper.transhipBoxPositionToTranshipBoxPositionDTO(transhipBoxPosition);
        return transhipBoxPositionDTO;
    }

    /**
     *  Delete the  transhipBoxPosition by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TranshipBoxPosition : {}", id);
        transhipBoxPositionRepository.delete(id);
    }

    @Override
    public TranshipBoxPosition saveTranshipBoxPosition(TranshipBox transhipBox) {
        if(transhipBox == null || transhipBox.getId() ==null){
            return null;
        }

        Long count = transhipBoxPositionRepository
            .countByTranshipBoxIdAndEquipmentCodeAndAreaCodeAndSupportRackCodeAndRowsInShelfAndColumnsInShelf(
                transhipBox.getId(),transhipBox.getEquipmentCode(),transhipBox.getAreaCode(),transhipBox.getSupportRackCode(),
                transhipBox.getRowsInShelf(),transhipBox.getColumnsInShelf()
            );
        TranshipBoxPosition transhipBoxPosition = new TranshipBoxPosition();
        if(count.intValue()==0){
            transhipBoxPosition.transhipBox(transhipBox).memo(transhipBox.getMemo()).status(Constants.VALID)
                .equipment(transhipBox.getFrozenBox().getEquipment()).equipmentCode(transhipBox.getEquipmentCode())
                .area(transhipBox.getFrozenBox().getArea()).areaCode(transhipBox.getAreaCode())
                .supportRack(transhipBox.getFrozenBox().getSupportRack()).supportRackCode(transhipBox.getSupportRackCode())
                .rowsInShelf(transhipBox.getRowsInShelf()).columnsInShelf(transhipBox.getColumnsInShelf());
            transhipBoxPositionRepository.save(transhipBoxPosition);
        }

        return transhipBoxPosition;
    }

    private Boolean comparePosition(TranshipBox transhipBox, TranshipBoxPosition transhipBoxPosition) {
        Boolean flag = false;
        if(transhipBoxPosition == null || !transhipBoxPosition.getEquipmentCode().equals(transhipBox.getEquipmentCode())
            ||!transhipBoxPosition.getAreaCode().equals(transhipBox.getAreaCode())
            ||!transhipBoxPosition.getSupportRackCode().equals(transhipBox.getSupportRackCode())
            ||!transhipBoxPosition.getRowsInShelf().equals(transhipBox.getRowsInShelf())
            ||!transhipBoxPosition.getColumnsInShelf().equals(transhipBox.getColumnsInShelf())){
            flag = true;
        }
        return flag;
    }
}
