package org.fwoxford.service;
import org.fwoxford.domain.Area;
import org.fwoxford.domain.Equipment;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.SupportRack;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Service class for checking boxes.
 */
@Service
@Transactional
public class FrozenBoxCheckService {

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;

    private final Logger log = LoggerFactory.getLogger(FrozenBoxCheckService.class);

    public void checkFrozenBoxPosition(FrozenBox box) {
        Equipment equipment = box.getEquipment();
        Area area = box.getArea();
        SupportRack supportRack = box.getSupportRack();
        String columnsInShelf = box.getColumnsInShelf();
        String rowsInShelf = box.getRowsInShelf();
        if(area != null && (equipment==null || !area.getEquipment().equals(equipment))){
            throw new BankServiceException("区域不在设备下！");
        }
        if(supportRack!=null && (area==null ||!supportRack.getArea().equals(area))){
            throw new BankServiceException("冻存架不在该区域下！");
        }
        if((!StringUtils.isEmpty(columnsInShelf) || !StringUtils.isEmpty(rowsInShelf))
            &&(equipment==null||area==null||supportRack==null)){
            throw new BankServiceException("位置无效！");
        }
        if(columnsInShelf!=null && rowsInShelf!=null){
            Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(equipment.getEquipmentCode()
                ,area.getAreaCode(),supportRack.getSupportRackCode(),columnsInShelf,rowsInShelf);
            if(count.intValue()>0){
                throw new BankServiceException("此位置已存放冻存盒，请更换其他位置！",box.toString());
            }
        }
    }
}
