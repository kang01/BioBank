package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/7/28.
 */
public class FrozenTubeBase {
    private Long id;
    private String sampleCode;
    private String tubeRows;
    private String tubeColumns;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    @Override
    public String toString() {
        return "FrozenTubeBase{" +
            "id=" + id +
            ", sampleCode='" + sampleCode  +
            ", tubeRows='" + tubeRows +
            ", tubeColumns='" + tubeColumns +
            '}';
    }
}
