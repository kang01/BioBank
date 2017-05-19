package org.fwoxford.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String TRANSHIPE_IN_INVALID = "1004";
    /**
     * 冻存盒状态：2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废，2006：已上架
     */
    public static final  String FROZEN_BOX_NEW = "2001" ;
    public static final  String FROZEN_BOX_STOCKING = "2002" ;
    public static final  String FROZEN_BOX_SPLITED = "2003" ;
    public static final  String FROZEN_BOX_STOCKED = "2004" ;
    public static final  String FROZEN_BOX_INVALID = "2005" ;
    public static final String FROZEN_BOX_PUT_SHELVES = "2006";
    public static final String FROZEN_BOX_SPLITING = "2007";
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
    public static final List<String> LOGIN_NOT_STOCK_LIST = new ArrayList<String>(){{add("system");add("admin");add("user");add("anonymoususer");}};
    /**
     * 出库状态：1101：进行中，1102：待批准，1103：已批准，1104：已作废
     */
    public static final String STOCK_OUT_PENDING = "1101";
    public static final String STOCK_OUT_PENDING_APPROVAL = "1102";
    public static final String STOCK_OUT_APPROVED = "1103";
    public static final String STOCK_OUT_INVALID = "1104";

    public static final String STOCK_OUT_REQUIREMENT_CKECKING = "1201";
    public static final String STOCK_OUT_REQUIREMENT_CHECKED_PASS = "1202";
    public static final String STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT = "1203";
    public static final  Map SEX_MAP = new HashMap(){{
        put("f","女");
        put("m","男");
        put("n","不详");
    }};
    /**
     * 需求样本，已被使用：1301，释放样本：1302
     */
    public static final String STOCK_OUT_SAMPLE_IN_USE = "1301";
    public static final String STOCK_OUT_SAMPLE_IN_USE_NOT = "1302";
    private Constants() {
    }
}
