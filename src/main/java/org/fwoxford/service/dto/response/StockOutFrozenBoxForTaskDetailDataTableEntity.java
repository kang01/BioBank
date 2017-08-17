package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by gengluying on 2017/5/23.
 */
@Entity
@Table(name = "view_frozen_box_for_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutFrozenBoxForTaskDetailDataTableEntity extends StockOutFrozenBoxBaseDataTableEntity implements Serializable {

    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    @Column(name ="stock_out_task_id")
    private Long stockOutTaskId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockOutTaskId() {
        return stockOutTaskId;
    }

    public void setStockOutTaskId(Long stockOutTaskId) {
        this.stockOutTaskId = stockOutTaskId;
    }
}
