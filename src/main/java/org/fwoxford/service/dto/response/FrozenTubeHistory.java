package org.fwoxford.service.dto.response;

import io.swagger.models.auth.In;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by gengluying on 2017/6/19.
 */
@Entity
@Table(name = "view_sample_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FrozenTubeHistory {
    //样本ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id ;
    //转运ID
    @Column(name = "tranship_id")
    private Long transhipId ;
    //转运编码
    @Column(name = "tranship_code")
    private String transhipCode ;
    //入库ID
    @Column(name = "stock_in_id")
    private Long stockInId ;
    //入库编码
    @Column(name = "stock_in_code")
    private String stockInCode;
    //任务ID
    @Column(name = "stock_out_task_id")
    private Long stockOutTaskId ;
    //任务编码
    @Column(name = "stock_out_task_code")
    private String stockOutTaskCode;
    //交接ID
    @Column(name = "handover_id")
    private Long handoverId ;
    //交接编码
    @Column(name = "handover_code")
    private String handoverCode;
    //项目编码
    @Column(name = "project_code")
    private String projectCode;
    //样本编码
    @Column(name = "sample_code")
    private String  sampleCode;
    @Column(name = "type")
    private Integer type;
    //样本状态
    @Column(name = "status")
    private String status;
    //冻存盒编码
    @Column(name = "frozen_box_code")
    private String frozenBoxCode;
    //盒内行数
    @Column(name = "tube_rows")
    private String tubeRows;
    //盒内列数
    @Column(name = "tube_columns")
    private String tubeColumns;
//    操作时间
    @Column(name = "operate_time")
    private ZonedDateTime operateTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTranshipId() {
        return transhipId;
    }

    public void setTranshipId(Long transhipId) {
        this.transhipId = transhipId;
    }

    public String getTranshipCode() {
        return transhipCode;
    }

    public void setTranshipCode(String transhipCode) {
        this.transhipCode = transhipCode;
    }

    public Long getStockInId() {
        return stockInId;
    }

    public void setStockInId(Long stockInId) {
        this.stockInId = stockInId;
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public Long getStockOutTaskId() {
        return stockOutTaskId;
    }

    public void setStockOutTaskId(Long stockOutTaskId) {
        this.stockOutTaskId = stockOutTaskId;
    }

    public String getStockOutTaskCode() {
        return stockOutTaskCode;
    }

    public void setStockOutTaskCode(String stockOutTaskCode) {
        this.stockOutTaskCode = stockOutTaskCode;
    }

    public Long getHandoverId() {
        return handoverId;
    }

    public void setHandoverId(Long handoverId) {
        this.handoverId = handoverId;
    }

    public String getHandoverCode() {
        return handoverCode;
    }

    public void setHandoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }

    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }

    public ZonedDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(ZonedDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public FrozenTubeHistory(Long transhipId, String transhipCode, Long stockInId, String stockInCode, Long stockOutTaskId, String stockOutTaskCode, Long handoverId, String handoverCode, String projectCode, String sampleCode, Integer type, String status, String frozenBoxCode, String tubeRows, String tubeColumns, ZonedDateTime operateTime) {
        this.transhipId = transhipId;
        this.transhipCode = transhipCode;
        this.stockInId = stockInId;
        this.stockInCode = stockInCode;
        this.stockOutTaskId = stockOutTaskId;
        this.stockOutTaskCode = stockOutTaskCode;
        this.handoverId = handoverId;
        this.handoverCode = handoverCode;
        this.projectCode = projectCode;
        this.sampleCode = sampleCode;
        this.type = type;
        this.status = status;
        this.frozenBoxCode = frozenBoxCode;
        this.tubeRows = tubeRows;
        this.tubeColumns = tubeColumns;
        this.operateTime = operateTime;
    }
}
