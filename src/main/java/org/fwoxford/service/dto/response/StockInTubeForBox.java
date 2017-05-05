package org.fwoxford.service.dto.response;

import org.fwoxford.domain.SampleType;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInTubeForBox {
    private Long id;
    private String frozenBoxCode;
    private String tubeColumns;
    private String tubeRows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }
}
