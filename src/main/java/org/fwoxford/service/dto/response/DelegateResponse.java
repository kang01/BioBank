package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/5/15.
 */
public class DelegateResponse {
    private Long id;

    private String delegateCode;

    private String delegateName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDelegateCode() {
        return delegateCode;
    }

    public void setDelegateCode(String delegateCode) {
        this.delegateCode = delegateCode;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }
}
