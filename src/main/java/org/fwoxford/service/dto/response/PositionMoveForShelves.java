package org.fwoxford.service.dto.response;

import org.fwoxford.domain.PositionMove;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by gengluying on 2017/7/13.
 */
public class PositionMoveForShelves {
    @NotNull
    private Long id;
    private String memo;
    private List<PositionMoveForBox> positionMoveForBoxList;

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

    public List<PositionMoveForBox> getPositionMoveForBoxList() {
        return positionMoveForBoxList;
    }

    public void setPositionMoveForBoxList(List<PositionMoveForBox> positionMoveForBoxList) {
        this.positionMoveForBoxList = positionMoveForBoxList;
    }
}
