package org.fwoxford.service.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2017/5/4.
 */
public class FrozenBoxCodeForTranshipDTO {
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @JsonView(DataTablesOutput.View.class)
    private Long frozenBoxId;
    @JsonView(DataTablesOutput.View.class)
    private String frozenBoxCode;
    @JsonView(DataTablesOutput.View.class)
    private String frozenBoxCode1D;

    public FrozenBoxCodeForTranshipDTO() {
    }

    public FrozenBoxCodeForTranshipDTO(Long id ,Long frozenBoxId, String frozenBoxCode, String frozenBoxCode1D) {
        this.id = id;
        this.frozenBoxId = frozenBoxId;
        this.frozenBoxCode = frozenBoxCode;
        this.frozenBoxCode1D = frozenBoxCode1D;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
    }
}
