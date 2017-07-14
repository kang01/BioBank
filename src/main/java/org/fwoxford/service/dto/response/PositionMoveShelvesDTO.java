package org.fwoxford.service.dto.response;

import org.fwoxford.service.dto.PositionMoveDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/7/13.
 */
public class PositionMoveShelvesDTO extends PositionMoveDTO{
    private List<PositionMoveForArea> positionMoveForAreas;

    public List<PositionMoveForArea> getPositionMoveForAreas() {
        return positionMoveForAreas;
    }

    public void setPositionMoveForAreas(List<PositionMoveForArea> positionMoveForAreas) {
        this.positionMoveForAreas = positionMoveForAreas;
    }
}
