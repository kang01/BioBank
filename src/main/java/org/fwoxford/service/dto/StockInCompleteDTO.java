package org.fwoxford.service.dto;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/4/25.
 */
public class StockInCompleteDTO {
    String loginName1;
    String password1;
    String loginName2;
    String password2;
    LocalDate stockInDate;

    public String getLoginName1() {
        return loginName1;
    }

    public void setLoginName1(String loginName1) {
        this.loginName1 = loginName1;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getLoginName2() {
        return loginName2;
    }

    public void setLoginName2(String loginName2) {
        this.loginName2 = loginName2;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public LocalDate getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
    }
}
