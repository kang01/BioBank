package org.fwoxford.service.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the TranshipBox entity.
 */
public class TranshipBoxListDTO implements Serializable {


    /**
     * 转运id
     */
    private Long transhipId;

    private List<FrozenBoxForSaveBatchDTO> frozenBoxDTOList;

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
    }

    public List<FrozenBoxForSaveBatchDTO> getFrozenBoxDTOList() {
        return frozenBoxDTOList;
    }

    public void setFrozenBoxDTOList(List<FrozenBoxForSaveBatchDTO> frozenBoxDTOList) {
        this.frozenBoxDTOList = frozenBoxDTOList;
    }
}
