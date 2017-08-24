package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockOutApplyProject.
 */
@Entity
@Table(name = "stock_out_apply_project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOutApplyProject extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_out_apply_project")
    @SequenceGenerator(name = "seq_stock_out_apply_project",sequenceName = "seq_stock_out_apply_project",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private StockOutApply stockOutApply;

    @ManyToOne(optional = false)
    @NotNull
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public StockOutApplyProject status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public StockOutApplyProject memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public StockOutApply getStockOutApply() {
        return stockOutApply;
    }

    public StockOutApplyProject stockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
        return this;
    }

    public void setStockOutApply(StockOutApply stockOutApply) {
        this.stockOutApply = stockOutApply;
    }

    public Project getProject() {
        return project;
    }

    public StockOutApplyProject project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockOutApplyProject stockOutApplyProject = (StockOutApplyProject) o;
        if (stockOutApplyProject.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockOutApplyProject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockOutApplyProject{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
