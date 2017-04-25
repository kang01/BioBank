package org.fwoxford.service.dto;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/4/25.
 */
public class TranshipToStockInDTO {
    String login;
    String password;
    LocalDate receiveDate;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }
}
