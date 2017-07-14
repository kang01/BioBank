package org.fwoxford.service.dto.response;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by gengluying on 2017/5/23.
 */

public class PositionMoveForSample {
    private Long id;
    private String tubeRows;
    private String tubeColumns;
    private String memo;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
