package org.fwoxford.service.dto.response;

import org.fwoxford.service.dto.PositionMoveDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/7/13.
 */
public class PositionMoveSampleDTO extends PositionMoveDTO {
    private List<PositionMoveForBox> positionMoveForBoxList;

    public List<PositionMoveForBox> getPositionMoveForBoxList() {
        return positionMoveForBoxList;
    }

    public void setPositionMoveForBoxList(List<PositionMoveForBox> positionMoveForBoxList) {
        this.positionMoveForBoxList = positionMoveForBoxList;
    }
}
