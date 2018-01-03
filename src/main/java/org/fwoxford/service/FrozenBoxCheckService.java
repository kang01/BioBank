package org.fwoxford.service;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.TranshipBoxRepository;
import org.fwoxford.service.dto.TranshipBoxDTO;
import org.fwoxford.service.dto.TranshipBoxListDTO;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for checking boxes.
 */
@Service
@Transactional
public class FrozenBoxCheckService {

    @Autowired
    private FrozenBoxRepository frozenBoxRepository;
    @Autowired
    private TranshipBoxService transhipBoxService;
    @Autowired
    private TranshipBoxRepository transhipBoxRepository;

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
        if(!StringUtils.isEmpty(columnsInShelf) && !StringUtils.isEmpty(rowsInShelf)){
            FrozenBox frozenBox = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(equipment.getEquipmentCode()
                ,area.getAreaCode(),supportRack.getSupportRackCode(),columnsInShelf,rowsInShelf);
            if((box.getId()==null&&frozenBox!=null)|| (box.getId()!=null &&frozenBox!=null &&  box.getId()!=frozenBox.getId()&&!box.getId().equals(frozenBox.getId()))){
                throw new BankServiceException("此位置已存放冻存盒，请更换其他位置！",box.toString());
            }
        }
    }

    /**
     * 验证冻存盒编码是否重复
     * @param frozenBoxCodeMap
     */
    public void checkFrozenBoxCodeRepead(Map<String,Long> frozenBoxCodeMap) {
        ArrayList<String> repeatCode = new ArrayList<>();
        List<String> frozenBoxCodeStr = new ArrayList<String>();
        for(String key :frozenBoxCodeMap.keySet()){
            frozenBoxCodeStr.add(key);
        }
        if(frozenBoxCodeStr.size()==0){
            return;
        }
        //原生SQL查询
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findByFrozenBoxCodeIn(frozenBoxCodeStr);
        for (FrozenBox frozenBox : frozenBoxList){
            Long frozenBoxId = frozenBoxCodeMap.get(frozenBox.getFrozenBoxCode());
           if((frozenBoxCodeMap.get(frozenBox.getFrozenBoxCode()) == -1)
               || (frozenBoxCodeMap.get(frozenBox.getFrozenBoxCode())!= -1 && frozenBox.getId()!=frozenBoxId && !frozenBox.getId().equals(frozenBoxId))){
               repeatCode.add(frozenBox.getFrozenBoxCode());
           }
        }
        if(repeatCode.size()>0){
            throw new BankServiceException("请勿提交重复的冻存盒编码！",String.join(",",repeatCode));
        }
    }
    public List<TranshipBoxDTO> checkFrozenBoxCodeForStockOutReturn(List<TranshipBoxDTO> transhipBoxDTOS, Tranship tranship) {
        if(tranship == null){
            return null;
        }
        List<String> frozenBoxCodeStr = transhipBoxDTOS.stream().map(s->{
            String boxCode = s.getFrozenBoxCode();
            if(StringUtils.isEmpty(boxCode)){
                throw new BankServiceException("冻存盒编码不能为空");
            }
            return boxCode;
        }).collect(Collectors.toList());
        String boxCodeStr = String.join(",",frozenBoxCodeStr);
        //验证归还冻存盒是否重复（1） 在转运盒中看归还中的是否重复
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByFrozenBoxCodeInAndStatus(frozenBoxCodeStr,Constants.FROZEN_BOX_RETURN_BACK);
        ArrayList<String> repeatCode = new ArrayList<>();
        for(TranshipBox transhipBox :transhipBoxes){
            for(TranshipBoxDTO transhipBoxDTO : transhipBoxDTOS){
                if(transhipBox.getFrozenBoxCode()!=null && transhipBoxDTO.getFrozenBoxCode()!=null
                        && transhipBox.getFrozenBoxCode().equals( transhipBoxDTO.getFrozenBoxCode())
                        && (transhipBoxDTO.getId() == null|| (transhipBoxDTO.getId()!=null && !transhipBoxDTO.getId().equals(transhipBox.getId())) )){
                    repeatCode.add(transhipBox.getFrozenBoxCode());
                }
            }
        }
        //（2） 从冻存盒的接收完成，待入库，已入库状态中判断冻存盒是否重复
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findByFrozenBoxCodeInAndStatusIn(frozenBoxCodeStr,
                new ArrayList<String>(){{add(Constants.FROZEN_BOX_NEW);
                    add(Constants.FROZEN_BOX_STOCKED);
                    add(Constants.FROZEN_BOX_STOCKING);
                    add(Constants.FROZEN_BOX_STOCK_OUT_PENDING);
                    add(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED);
                    add(Constants.FROZEN_BOX_TRANSHIP_COMPLETE);
                    add(Constants.FROZEN_BOX_PUT_SHELVES);}});
        for(FrozenBox frozenBox :frozenBoxList){
            for(TranshipBoxDTO transhipBoxDTO : transhipBoxDTOS){
                if(frozenBox.getFrozenBoxCode()!=null && transhipBoxDTO.getFrozenBoxCode()!=null
                        && frozenBox.getFrozenBoxCode().equals( transhipBoxDTO.getFrozenBoxCode())
                        && (transhipBoxDTO.getFrozenBoxId() == null||
                        (transhipBoxDTO.getFrozenBoxId()!=null && !transhipBoxDTO.getFrozenBoxId().equals(frozenBox.getId())) )){
                    repeatCode.add(transhipBoxDTO.getFrozenBoxCode());
                }
            }
        }
        if(repeatCode.size()>0){
            throw new BankServiceException("请勿提交重复的冻存盒编码！",String.join(",",repeatCode));
        }
        List<TranshipBoxDTO> stockOutFrozenBoxAndSampleList = transhipBoxService.getStockOutFrozenBoxAndSample(tranship.getProjectCode(),boxCodeStr);
        for(TranshipBoxDTO stockOutFrozenBox :stockOutFrozenBoxAndSampleList){
            if(stockOutFrozenBox.getIsRealData().equals(Constants.NO)){
                String boxCode = StringUtils.isEmpty(stockOutFrozenBox.getFrozenBoxCode())?stockOutFrozenBox.getFrozenBoxCode1D():stockOutFrozenBox.getFrozenBoxCode();
                throw new BankServiceException("冻存盒"+boxCode+"导入失败！");
            }
        }
        return stockOutFrozenBoxAndSampleList;
    }
}
