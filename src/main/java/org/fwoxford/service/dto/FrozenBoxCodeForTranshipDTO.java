package org.fwoxford.service.dto;

/**
 * Created by gengluying on 2017/5/4.
 */
public class FrozenBoxCodeForTranshipDTO {
    private Long frozenBoxId;
    private String frozenBoxCode;

    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }
}
