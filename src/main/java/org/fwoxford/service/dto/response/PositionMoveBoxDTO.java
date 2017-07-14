package org.fwoxford.service.dto.response;

import org.fwoxford.service.dto.PositionMoveDTO;

import java.util.List;

/**
 * Created by gengluying on 2017/7/13.
 */
public class PositionMoveBoxDTO extends PositionMoveDTO{
    private List<PositionMoveForShelves> positionMoveForShelves;

    public List<PositionMoveForShelves> getPositionMoveForShelves() {
        return positionMoveForShelves;
    }

    public void setPositionMoveForShelves(List<PositionMoveForShelves> positionMoveForShelves) {
        this.positionMoveForShelves = positionMoveForShelves;
    }
}
