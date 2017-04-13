package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StockIn.
 */
@Entity
@Table(name = "stock_in")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockIn extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
//    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    /**
     * 入库编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "stock_in_code", length = 100, nullable = false)
    private String stockInCode;
    /**
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;
    /**
     * 项目点编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "project_site_code", length = 100, nullable = false)
    private String projectSiteCode;
    /**
     * 接收日期
     */
    @Column(name = "receive_date")
    private LocalDate receiveDate;
    /**
     * 接收人ID
     */
    @Max(value = 100)
    @Column(name = "receive_id")
    private Long receiveId;
    /**
     * 接收人姓名
     */
    @Size(max = 100)
    @Column(name = "receive_name", length = 100)
    private String receiveName;
    /**
     * 入库类型 ：8001：首次入库，8002：移位入库，8003：调整入库
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "stock_in_type", length = 20, nullable = false)
    private String stockInType;
    /**
     * 入库人1ID
     */
    @Max(value = 100)
    @Column(name = "store_keeper_id_1")
    private Long storeKeeperId1;
    /**
     * 入库人1名称
     */
    @Size(max = 100)
    @Column(name = "store_keeper_1", length = 100)
    private String storeKeeper1;
    /**
     * 入库人2ID
     */
    @Max(value = 100)
    @Column(name = "store_keeper_id_2")
    private Long storeKeeperId2;
    /**
     * 入库人2名称
     */
    @Size(max = 100)
    @Column(name = "store_keeper_2", length = 100)
    private String storeKeeper2;
    /**
     * 入库日期
     */
    @Column(name = "stock_in_date")
    private LocalDate stockInDate;
    /**
     * 样本数量
     */
    @NotNull
    @Column(name = "count_of_sample", nullable = false)
    private Integer countOfSample;
    /**
     * 签名人ID
     */
    @Max(value = 100)
    @Column(name = "sign_id")
    private Long signId;
    /**
     * 签名人姓名
     */
    @Size(max = 100)
    @Column(name = "sign_name", length = 100)
    private String signName;
    /**
     * 签名日期
     */
    @Column(name = "sign_date")
    private LocalDate signDate;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态 :7001：进行中，7002已入库
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 转运
     */
    @ManyToOne
    private Tranship tranship;
    /**
     * 项目
     */
    @ManyToOne(optional = false)
    @NotNull
    private Project project;
    /**
     * 项目点
     */
    @ManyToOne(optional = false)
    @NotNull
    private ProjectSite projectSite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getStockInCode() {
        return stockInCode;
    }

    public StockIn stockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
        return this;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public StockIn projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public StockIn projectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
        return this;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public StockIn receiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
        return this;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public StockIn receiveId(Long receiveId) {
        this.receiveId = receiveId;
        return this;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public StockIn receiveName(String receiveName) {
        this.receiveName = receiveName;
        return this;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getStockInType() {
        return stockInType;
    }

    public StockIn stockInType(String stockInType) {
        this.stockInType = stockInType;
        return this;
    }

    public void setStockInType(String stockInType) {
        this.stockInType = stockInType;
    }

    public Long getStoreKeeperId1() {
        return storeKeeperId1;
    }

    public StockIn storeKeeperId1(Long storeKeeperId1) {
        this.storeKeeperId1 = storeKeeperId1;
        return this;
    }

    public void setStoreKeeperId1(Long storeKeeperId1) {
        this.storeKeeperId1 = storeKeeperId1;
    }

    public String getStoreKeeper1() {
        return storeKeeper1;
    }

    public StockIn storeKeeper1(String storeKeeper1) {
        this.storeKeeper1 = storeKeeper1;
        return this;
    }

    public void setStoreKeeper1(String storeKeeper1) {
        this.storeKeeper1 = storeKeeper1;
    }

    public Long getStoreKeeperId2() {
        return storeKeeperId2;
    }

    public StockIn storeKeeperId2(Long storeKeeperId2) {
        this.storeKeeperId2 = storeKeeperId2;
        return this;
    }

    public void setStoreKeeperId2(Long storeKeeperId2) {
        this.storeKeeperId2 = storeKeeperId2;
    }

    public String getStoreKeeper2() {
        return storeKeeper2;
    }

    public StockIn storeKeeper2(String storeKeeper2) {
        this.storeKeeper2 = storeKeeper2;
        return this;
    }

    public void setStoreKeeper2(String storeKeeper2) {
        this.storeKeeper2 = storeKeeper2;
    }

    public LocalDate getStockInDate() {
        return stockInDate;
    }

    public StockIn stockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
        return this;
    }

    public void setStockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
    }

    public Integer getCountOfSample() {
        return countOfSample;
    }

    public StockIn countOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }

    public void setCountOfSample(Integer countOfSample) {
        this.countOfSample = countOfSample;
    }

    public Long getSignId() {
        return signId;
    }

    public StockIn signId(Long signId) {
        this.signId = signId;
        return this;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public String getSignName() {
        return signName;
    }

    public StockIn signName(String signName) {
        this.signName = signName;
        return this;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public StockIn signDate(LocalDate signDate) {
        this.signDate = signDate;
        return this;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }

    public String getMemo() {
        return memo;
    }

    public StockIn memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public StockIn status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tranship getTranship() {
        return tranship;
    }

    public StockIn tranship(Tranship tranship) {
        this.tranship = tranship;
        return this;
    }

    public void setTranship(Tranship tranship) {
        this.tranship = tranship;
    }

    public Project getProject() {
        return project;
    }

    public StockIn project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public StockIn projectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
        return this;
    }

    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockIn stockIn = (StockIn) o;
        if (stockIn.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stockIn.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StockIn{" +
            "id=" + id +
            ", stockInCode='" + stockInCode + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectSiteCode='" + projectSiteCode + "'" +
            ", receiveDate='" + receiveDate + "'" +
            ", receiveId='" + receiveId + "'" +
            ", receiveName='" + receiveName + "'" +
            ", stockInType='" + stockInType + "'" +
            ", storeKeeperId1='" + storeKeeperId1 + "'" +
            ", storeKeeper1='" + storeKeeper1 + "'" +
            ", storeKeeperId2='" + storeKeeperId2 + "'" +
            ", storeKeeper2='" + storeKeeper2 + "'" +
            ", stockInDate='" + stockInDate + "'" +
            ", countOfSample='" + countOfSample + "'" +
            ", signId='" + signId + "'" +
            ", signName='" + signName + "'" +
            ", signDate='" + signDate + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
