package org.fwoxford.web.rest.errors;

/**
 * Created by gengluying on 2017/3/28.
 */
public class BankServiceException extends CustomParameterizedException{

    public BankServiceException(String message, String... params) {
        super(message, params);
    }
}
