package org.fwoxford.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    /**
     * 入库状态 ：7001：进行中
     */
    public static final String STORANGE_IN_PENDING = "7001";
    /**
     * 入库类型：8001：首次入库
     */
    public static final String STORANGE_IN_TYPE_1ST = "8001";

    private Constants() {
    }
}
