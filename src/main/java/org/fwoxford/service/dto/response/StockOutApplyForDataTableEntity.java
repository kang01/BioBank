package org.fwoxford.service.dto.response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/5/15.
 */
public class StockOutApplyForDataTableEntity {
    /**
     * 申请ID
     */
    private Long id;
    /**
     * 样本用途
     */
    private String purposeOfSample;
    /**
     * 申请时间
     */
    private String applyTime;
    /**
     * 委托人
     */
    @Size(max = 255)
    private String applyPersonName;
    /**
     * 申请单号
     */
    @NotNull
    private String applyCode;
    /**
     * 委托方名称
     */
    private String delegateName;
    /**
     * 样本类型字符串，如：血浆，血清，
     */
    private String sampleTypes;
    /**
     * 状态
     */
    private String status;
    /**
     * 样本需求量
     */
    private Long countOfSample;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
