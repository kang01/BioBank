package org.fwoxford.service.dto.response;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by gengluying on 2017/7/12.
 */
public class PositionMoveForBox {
    @NotNull
    private Long id;
    private String memo;
    private List<PositionMoveForSample> positionMoveForSampleList;

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

    public List<PositionMoveForSample> getPositionMoveForSampleList() {
        return positionMoveForSampleList;
    }

    public void setPositionMoveForSampleList(List<PositionMoveForSample> positionMoveForSampleList) {
        this.positionMoveForSampleList = positionMoveForSampleList;
    }
}
