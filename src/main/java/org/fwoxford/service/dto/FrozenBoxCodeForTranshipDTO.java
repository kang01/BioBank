package org.fwoxford.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2017/5/4.
 */
public class FrozenBoxCodeForTranshipDTO {
    @JsonView(DataTablesOutput.View.class)
    private Long frozenBoxId;
    @JsonView(DataTablesOutput.View.class)
    private String frozenBoxCode;

    public FrozenBoxCodeForTranshipDTO() {
    }

    public FrozenBoxCodeForTranshipDTO(Long frozenBoxId, String frozenBoxCode) {
        this.frozenBoxId = frozenBoxId;
        this.frozenBoxCode = frozenBoxCode;
    }

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
