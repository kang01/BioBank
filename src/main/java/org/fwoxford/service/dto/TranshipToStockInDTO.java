package org.fwoxford.service.dto;

import java.time.LocalDate;

/**
 * Created by gengluying on 2017/4/25.
 */
public class TranshipToStockInDTO {
    Long receiveId;
    String login;
    String password;
    LocalDate receiveDate;

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

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
