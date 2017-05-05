package org.fwoxford.service.dto;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInTubeDTO {
    private Long id;
    private String tubeColumns;
    private String tubeRows;
    private String frozenBoxCode;
    @Override
    public String toString() {
        return "StockInTubeDTO{" +
            "id=" + id +
            ", tubeColumns='" + tubeColumns + '\'' +
            ", tubeRows='" + tubeRows + '\'' +
            ", frozenBoxCode='" + frozenBoxCode + '\'' +
            '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }
}
