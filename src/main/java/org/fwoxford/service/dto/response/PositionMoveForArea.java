package org.fwoxford.service.dto.response;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by gengluying on 2017/7/13.
 */
public class PositionMoveForArea {
    @NotNull
    private Long id;
    private String memo;
    private List<PositionMoveForShelves> positionMoveForShelves;

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

    public List<PositionMoveForShelves> getPositionMoveForShelves() {
        return positionMoveForShelves;
    }

    public void setPositionMoveForShelves(List<PositionMoveForShelves> positionMoveForShelves) {
        this.positionMoveForShelves = positionMoveForShelves;
    }
}
