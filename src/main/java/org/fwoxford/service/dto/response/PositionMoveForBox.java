package org.fwoxford.service.dto.response;

import org.fwoxford.service.dto.PositionMoveDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by gengluying on 2017/7/12.
 */
public class PositionMoveForBox extends PositionMoveDTO {
    @NotNull
    private Long id;
    private String memo;
    private List<PositionMoveForSample> frozenTubeDTOS;

    private String ColumnsInShelf;
    private String RowsInShelf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<PositionMoveForSample> getFrozenTubeDTOS() {
        return frozenTubeDTOS;
    }

    public void setFrozenTubeDTOS(List<PositionMoveForSample> frozenTubeDTOS) {
        this.frozenTubeDTOS = frozenTubeDTOS;
    }

    public String getColumnsInShelf() {
        return ColumnsInShelf;
    }

    public void setColumnsInShelf(String columnsInShelf) {
        ColumnsInShelf = columnsInShelf;
    }

    public String getRowsInShelf() {
        return RowsInShelf;
    }

    public void setRowsInShelf(String rowsInShelf) {
        RowsInShelf = rowsInShelf;
    }
}
