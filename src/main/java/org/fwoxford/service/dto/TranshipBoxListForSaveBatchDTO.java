package org.fwoxford.service.dto;

import java.util.List;

/**
 * Created by gengluying on 2017/5/4.
 */
public class TranshipBoxListForSaveBatchDTO {
    /**
     * 转运id
     */
    private Long transhipId;

    private List<FrozenBoxDTO> frozenBoxDTOList;

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
    }

    public List<FrozenBoxDTO> getFrozenBoxDTOList() {
        return frozenBoxDTOList;
    }

    public void setFrozenBoxDTOList(List<FrozenBoxDTO> frozenBoxDTOList) {
        this.frozenBoxDTOList = frozenBoxDTOList;
    }
}
