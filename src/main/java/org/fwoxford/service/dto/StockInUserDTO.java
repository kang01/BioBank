package org.fwoxford.service.dto;

import org.fwoxford.config.Constants;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2017/4/24.
 */
public class StockInUserDTO {
    private Long id;
    private String login;
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
