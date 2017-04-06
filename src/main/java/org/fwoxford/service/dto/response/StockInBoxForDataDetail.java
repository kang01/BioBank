package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/4/5.
 */
public class StockInBoxForDataDetail extends StockInBoxForDataTable{
    private  String stockInCode;
    private Long equipmentId;
    private Long areaId;
    private Long supportRackId;
    private String memo;

    @Override
    public String toString() {
        return "StockInBoxForDataDetail{" +
            "stockInCode=" + stockInCode +
            "equipmentId=" + equipmentId +
            ", areaId=" + areaId +
            ", supportRackId=" + supportRackId +
            ", memo='" + memo + '\'' +
            '}';
    }

    public String getStockInCode() {
        return stockInCode;
    }

    public void setStockInCode(String stockInCode) {
        this.stockInCode = stockInCode;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getSupportRackId() {
        return supportRackId;
    }

    public void setSupportRackId(Long supportRackId) {
        this.supportRackId = supportRackId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
