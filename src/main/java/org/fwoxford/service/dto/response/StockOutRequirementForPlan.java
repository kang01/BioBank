package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/5/23.
 */
public class StockOutRequirementForPlan {
    private Long id;
    private String requirementName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }
}
