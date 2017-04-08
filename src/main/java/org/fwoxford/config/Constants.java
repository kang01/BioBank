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
     * 入库状态 ：7001：进行中，7002已入库
     */
    public static final String STOCK_IN_PENDING = "7001";
    public static final String STOCK_IN_COMPLETE = "7002";
    /**
     * 入库类型：8001：首次入库
     */
    public static final String STORANGE_IN_TYPE_1ST = "8001";
    /**
     * 数据状态：0000：无效，0001：有效
     */
    public static final String VALID = "0001";
    public static final String INVALID = "0000";
    /**
     * 转运状态：1001：进行中，1002：待入库，1003：已入库，1004：已作废
     */
    public static final String TRANSHIPE_IN_PENDING = "1001";
    public static final String TRANSHIPE_IN_STOCKING = "1002";
    public static final String TRANSHIPE_IN_STOCKED = "1003";
    /**
     * 冻存盒状态：2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废
     */
    public static final  String FROZEN_BOX_NEW = "2001" ;
    public static final  String FROZEN_BOX_STOCKING = "2002" ;
    public static final  String FROZEN_BOX_SPLITED = "2003" ;
    public static final  String FROZEN_BOX_STOCKED = "2004" ;
    /**
     * 冻存管状态：3001：正常，3002：空管，3003：空孔；3004：异常
     */
    public static final  String FROZEN_TUBE_NORMAL = "3001" ;
    public static final  String FROZEN_TUBE_EMPTY = "3002" ;
    public static final  String FROZEN_TUBE_HOLE_EMPTY = "3003" ;
    public static final  String FROZEN_TUBE_ABNORMAL = "3004" ;
    /**
     * 是：1，否：0
     */
    public static final Integer NO = 0;
    public static final Integer YES = 1;

    private Constants() {
    }
}
