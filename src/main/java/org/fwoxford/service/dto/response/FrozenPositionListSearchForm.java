package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by gengluying on 2017/5/23.
 */

public class FrozenPositionListSearchForm extends FrozenPositionListBaseDataTableEntity{

    private String[] projectCodeStr;
    /**
     * 空间条件：1：已用：2：剩余
     */
    private Integer spaceType;
    /**
     * 比较类型：1：大于，2：大于等于，3：等于，4：小于，5：小于等于
     */
    private Integer compareType;
    /**
     * 数值
     */
    private Long number;

    public Integer getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(Integer spaceType) {
        this.spaceType = spaceType;
    }

    public Integer getCompareType() {
        return compareType;
    }

    public void setCompareType(Integer compareType) {
        this.compareType = compareType;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String[] getProjectCodeStr() {
        return projectCodeStr;
    }
    public void setProjectCodeStr(String[] projectCodeStr) {
        this.projectCodeStr = projectCodeStr;
    }
}
