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
     * 冻存盒状态：2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废，2006：已上架,2008:待出库
     */
    public static final  String FROZEN_BOX_NEW = "2001" ;
    public static final  String FROZEN_BOX_STOCKING = "2002" ;
    public static final  String FROZEN_BOX_SPLITED = "2003" ;
    public static final  String FROZEN_BOX_STOCKED = "2004" ;
    public static final  String FROZEN_BOX_INVALID = "2005" ;
    public static final String FROZEN_BOX_PUT_SHELVES = "2006";
    public static final String FROZEN_BOX_SPLITING = "2007";
    public static final String FROZEN_BOX_STOCK_OUT_PENDING = "2008";
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
    public static final String STOCK_OUT_APPROVE_REFUSED = "1104";
    public static final String STOCK_OUT_INVALID = "1105";
    /**
     * 需求状态 ：1201：待核对，1202：库存不够，1203：库存满足"
     */
    public static final String STOCK_OUT_REQUIREMENT_CKECKING = "1201";
    public static final String STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT = "1202";
    public static final String STOCK_OUT_REQUIREMENT_CHECKED_PASS = "1203";
    public static final  Map SEX_MAP = new HashMap(){{
        put("f","女");
        put("m","男");
        put("n","不详");
        put("F","女");
        put("M","男");
        put("N","不详");
    }};
    /**
     * 需求样本，已被使用：1301，释放样本：1302
     */
    public static final String STOCK_OUT_SAMPLE_IN_USE = "1301";
    public static final String STOCK_OUT_SAMPLE_IN_USE_NOT = "1302";
    /**
     *  样本数量，权重系数0（因为所有的需求都有这个基本条件）
     项目编码，权重系数0（因为所有的需求都有这个基本条件）
     样本编码，权重系数101（因为已经具体到管子，没有比它更具体的需求了）
     样本类型，权重系数1
     样本分类，权重系数2
     冻存管类型，权重系数1
     性别，权重系数1
     年龄段，权重系数3
     疾病类型，权重系数5
     溶血，权重系数5
     脂质血，权重系数5
     */
    public static final Map KEY_NUMBER_MAP = new HashMap(){{
        put("countOfSample",0);
        put("projectCode",0);
        put("sampleCode",101);
        put("sampleType",1);
        put("sampleClassificationType",2);
        put("frozenTubeType",1);
        put("sex",1);
        put("ages",3);
        put("diseaseType",5);
        put("isHemolysis",5);
        put("isBloodLipid",5);
    }};
    /**
     * 出库计划状态：1401:进行中，1402：已完成，1403：已作废
     */
    public static final String STOCK_OUT_PLAN_INVALID = "1403";
    public static final String STOCK_OUT_PLAN_PENDING = "1401";
    public static final String STOCK_OUT_PLAN_COMPLETED = "1402";
    /**
     * 出库计划样本状态：1501：新建,1502：撤销出库
     */
    public static final String STOCK_OUT_PLAN_TUBE_PENDING = "1501";
    public static final String STOCK_OUT_PLAN_TUBE_CANCEL = "1502";
    /**
     * 出库任务状态：1601：待出库，1602：进行中，1603：已出库，1604：异常出库，1605：已作废
     */
    public static final String STOCK_OUT_TASK_NEW = "1601";
    public static final String STOCK_OUT_TASK_PENDING = "1602";
    public static final String STOCK_OUT_TASK_COMPLETED = "1603";
    public static final String STOCK_OUT_TASK_ABNORMAL = "1604";
    public static final String STOCK_OUT_TASK_INVALID = "1605";
    /**
     * 出库冻存盒状态：1701：待出库；1702：已出库；1703：已交接
     */
    public static final String STOCK_OUT_FROZEN_BOX_NEW = "1701";
    public static final String STOCK_OUT_FROZEN_BOX_COMPLETED = "1702";
    public static final String STOCK_OUT_FROZEN_BOX_HANDOVER = "1703";
    /**
     * 出库任务冻存管状态：1801：待出库,1802:撤销出库
     */
    public static final String STOCK_OUT_FROZEN_TUBE_NEW = "1801";
    public static final String STOCK_OUT_FROZEN_TUBE_CANCEL = "1802";

    /**
     * 出库交接状态：2101：进行中，2102：已交接
     */
    public static final String STOCK_OUT_HANDOVER_PENDING = "2101";
    public static final String STOCK_OUT_HANDOVER_COMPLETED = "2102";
    /**
     * 冻存盒与冻存管的关系 2202：待出库;2203:取消出库，2204：已出库
     */
    public static final String FROZEN_BOX_TUBE_STOCKOUT_PENDING = "2202";
    public static final String FROZEN_BOX_TUBE_STOCKOUT_CANCEL = "2203";
    public static final String FROZEN_BOX_TUBE_STOCKOUT_COMPLETED= "2204";
    public static final String FROZEN_BOX_TUBE_STOCKOUT_HANDOVER= "2205";
    private Constants() {
    }
}
