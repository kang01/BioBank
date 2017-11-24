package org.fwoxford.config;

import org.fwoxford.domain.PositionMove;

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
     * 入库状态：7001：进行中，7002：已入库，7090：已作废
     */
    public static final String STOCK_IN_PENDING = "7001";
    public static final String STOCK_IN_COMPLETE = "7002";
    public static final String STOCK_IN_INVALID= "7090";
    /**
     * 入库类型：8001：首次入库
     */
    public static final String STORANGE_IN_TYPE_1ST = "8001";
    public static final String STORANGE_IN_TYPE_MOVE = "8002";
    public static final String STORANGE_IN_TYPE_REVERT = "8003";
    /**
     * 数据状态：0000：无效，0001：有效
     */
    public static final String VALID = "0001";
    public static final String INVALID = "0000";
    /**
     * 转运状态：1001：进行中，1002：待入库，1003：已入库，1090：已作废，1005:转运完成
     */
    public static final String TRANSHIPE_IN_PENDING = "1001";
    public static final String TRANSHIPE_IN_STOCKING = "1002";
    public static final String TRANSHIPE_IN_STOCKED = "1003";
    public static final String TRANSHIPE_IN_INVALID = "1090";
    public static final String TRANSHIPE_IN_COMPLETE = "1005";
    /**
     * 冻存盒状态：2001：新建，2011：转运完成；2002：待入库，2003：已分装，2004：已入库，2090：已作废，2006：已上架，2008：待出库，2009：已出库，2010：已交接,2012：已销毁
     */
    public static final  String FROZEN_BOX_NEW = "2001" ;
    public static final  String FROZEN_BOX_TRANSHIP_COMPLETE = "2011" ;
    public static final  String FROZEN_BOX_STOCKING = "2002" ;
    public static final  String FROZEN_BOX_SPLITED = "2003" ;
    public static final  String FROZEN_BOX_STOCKED = "2004" ;
    public static final  String FROZEN_BOX_INVALID = "2090" ;
    public static final String FROZEN_BOX_PUT_SHELVES = "2006";
    public static final String FROZEN_BOX_SPLITING = "2007";
    public static final String FROZEN_BOX_STOCK_OUT_PENDING = "2008";
    public static final String FROZEN_BOX_STOCK_OUT_COMPLETED = "2009";
    public static final String FROZEN_BOX_STOCK_OUT_HANDOVER = "2010";
    public static final String FROZEN_BOX_DESTROY = "2012";
    /**
     * 冻存管状态：3001：正常，3002：空管，3003：空孔；3004：异常; 3005:销毁
     */
    public static final  String FROZEN_TUBE_NORMAL = "3001" ;
    public static final  String FROZEN_TUBE_EMPTY = "3002" ;
    public static final  String FROZEN_TUBE_HOLE_EMPTY = "3003" ;
    public static final  String FROZEN_TUBE_ABNORMAL = "3004" ;
    public static final  String FROZEN_TUBE_DESTROY = "3005" ;
    /**
     * 是：1，否：0
     */
    public static final Integer NO = 0;
    public static final Integer YES = 1;
    public static final List<String> LOGIN_NOT_STOCK_LIST = new ArrayList<String>(){{add("system");add("admin");add("user");add("anonymoususer");}};
    /**
     * 出库状态：1101：进行中，1102：待批准，1103：已批准，1104：已拒绝批准，1190：已作废
     */
    public static final String STOCK_OUT_PENDING = "1101";
    public static final String STOCK_OUT_PENDING_APPROVAL = "1102";
    public static final String STOCK_OUT_APPROVED = "1103";
    public static final String STOCK_OUT_APPROVE_REFUSED = "1104";
    public static final String STOCK_OUT_INVALID = "1190";
    /**
     * 需求状态 ：1201：待核对，1202：库存不够，1203：库存满足
     */
    public static final String STOCK_OUT_REQUIREMENT_CKECKING = "1201";
    public static final String STOCK_OUT_REQUIREMENT_CHECKED_PASS_OUT = "1202";
    public static final String STOCK_OUT_REQUIREMENT_CHECKED_PASS = "1203";
    public static final  Map<String,String> SEX_MAP = new HashMap(){{
        put("f","女");
        put("m","男");
        put("n","不详");
        put("F","女");
        put("M","男");
        put("N","不详");
        put("2","F");
        put("1","M");
    }};
    /**
     * 需求样本，已被使用：1301，释放样本：1302，待出库：1303，已出库：1304，撤销出库：1305
     */
    public static final String STOCK_OUT_SAMPLE_IN_USE = "1301";
    public static final String STOCK_OUT_SAMPLE_IN_USE_NOT = "1302";
    public static final String STOCK_OUT_SAMPLE_WAITING_OUT = "1303";
    public static final String STOCK_OUT_SAMPLE_COMPLETED = "1304";
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
     * 出库计划状态：1401:进行中，1402：已完成，1490：已作废
     */
    public static final String STOCK_OUT_PLAN_INVALID = "1490";
    public static final String STOCK_OUT_PLAN_PENDING = "1401";
    public static final String STOCK_OUT_PLAN_COMPLETED = "1402";
    /**
     * 出库计划样本状态：1501：新建,1502：撤销出库,1503：已出库
     */
    public static final String STOCK_OUT_PLAN_TUBE_PENDING = "1501";
    public static final String STOCK_OUT_PLAN_TUBE_CANCEL = "1502";
    public static final String STOCK_OUT_PLAN_TUBE_COMPLETED = "1503";
    /**
     * 出库任务状态：1601：待出库，1602：进行中，1603：已出库，1604：异常出库，1690：已作废
     */
    public static final String STOCK_OUT_TASK_NEW = "1601";
    public static final String STOCK_OUT_TASK_PENDING = "1602";
    public static final String STOCK_OUT_TASK_COMPLETED = "1603";
    public static final String STOCK_OUT_TASK_ABNORMAL = "1604";
    public static final String STOCK_OUT_TASK_INVALID = "1690";
    /**
     * 出库冻存盒状态：1701：待出库；1702：已出库；1703：已交接
     */
    public static final String STOCK_OUT_FROZEN_BOX_NEW = "1701";
    public static final String STOCK_OUT_FROZEN_BOX_COMPLETED = "1702";
    public static final String STOCK_OUT_FROZEN_BOX_HANDOVER = "1703";
    /**
     * 出库任务冻存管状态：1801：待出库,1802:撤销出库 1803:已出库
     */
    public static final String STOCK_OUT_FROZEN_TUBE_NEW = "1801";
    public static final String STOCK_OUT_FROZEN_TUBE_CANCEL = "1802";
    public static final String STOCK_OUT_FROZEN_TUBE_COMPLETED = "1803";
    /**
     * 出库交接状态：2101：进行中，2102：已交接,2190：已作废
     */
    public static final String STOCK_OUT_HANDOVER_PENDING = "2101";
    public static final String STOCK_OUT_HANDOVER_COMPLETED = "2102";
    public static final String STOCK_OUT_HANDOVER_INVALID = "2190";

    /**
     * 撤销出库标识
     */
    public static final Integer TUBE_CANCEL = 2;
    /**
     * 级别1，级别2
     */
    public static final Integer LEVEL_ONE = 1;
    public static final Integer LEVEL_TWO = 2;
    /**
     * 入库盒子状态:2401:已上架，2402：已入库,2403:已撤销
     */
    public static final String STOCK_IN_BOX_POSITION_PENDING = "2401";
    public static final String STOCK_IN_BOX_POSITION_COMPLETE = "2402";
    public static final String STOCK_IN_BOX_POSITION_CANCEL = "2403";
    /**
     * 3001：正常，3002：空管，3003：空孔；3004：异常
     */
    public static final Map FROZEN_TUBE_MAP = new HashMap(){{
            put("3001","正常");
            put("3002","空管");
            put("3003","空孔");
            put("3004","异常");
        }};
    public static final  Map FROZEN_TUBE_STATUS_MAP = new HashMap(){{
        put("样本","3001");
        put("空管","3002");
        put("空孔","3003");
    }};
    public static final  Map SAMPLECLASSIFICATION_COLOR_MAP = new HashMap(){{
        put("01","rgb(240,224,255)");
        put("02","rgb(240,224,255)");
        put("03","rgb(255,255,255)");
        put("04","rgb(236,236,236)");
        put("05","rgb(179,255,179)");
        put("06","rgb(179,255,179)");
        put("07","rgb(255,179,179)");
        put("08","rgb(255,179,179)");
        put("09","rgb(255,255,179)");
        put("10","rgb(255,255,179)");
        put("11","rgb(255,220,165)");
    }};
    public static final Map<String,Long> RECEIVER_MAP =  new HashMap<String,Long>(){{
        put("钟慧",5L);
        put("张丽萍",6L);
        put("丽萍",6L);
        put("王铁柱",7L);
        put("铁柱",7L);
        put("1王铁柱",7L);
        put("王东",8L);
        put("王思铭",9L);
        put("王莹",10L);
        put("王仁杰",11L);
        put("董晨",12L);
        put("李聪",13L);
        put("李蕊鑫",14L);
        put("刘启轩",15L);
        put("屈鑫镁",16L);
        put("杨博彦",17L);
        put("周欣月",18L);
        put("何炜",19L);
        put("1何炜",19L);
        put("朱志鸿",20L);
        put("戴浩",21L);
        put("NA",22L);
        put("连向华",23L);
        put("向华",23L);
        put("1连向华",23L);
        put("萨尹晰",24L);
        put("刘元东",25L);
        put("汪蒙",26L);
        put("李彩云",27L);
    }};

    public static final  Map<String,String> STOCK_IN_TYPE_MAP = new HashMap<String,String>(){{
        put("首次入库","8001");
        put("移位入库","8002");
    }};
    public static final Map<String,String> COLUMNNUMBER_MAP = new HashMap<String,String>(){{
        put("01","1");
        put("02","2");
        put("03","3");
        put("04","4");
        put("05","5");
        put("06","6");
        put("07","7");
        put("08","8");
        put("09","9");
        put("10","10");
        put("11","");
    }};
    public static final Map<String,String> SAMPLECLASS_MAP = new HashMap<String,String>(){{
        put("1","01");
        put("2","02");
        put("3","03");
        put("4","04");
        put("5","05");
        put("6","06");
        put("7","07");
        put("8","08");
        put("9","09");
        put("10","10");
    }};
    public static final Map<String,String> SAMPLE_TYPE_MAP = new HashMap<String,String>(){{
        put("01","EDTA抗凝血浆1");
        put("02","EDTA抗凝血浆2");
        put("03","EDTA抗凝白细胞");
        put("04","EDTA抗凝红细胞");
        put("05","肝素抗凝血浆1");
        put("06","肝素抗凝血浆2");
        put("07","血清1");
        put("08","血清2");
        put("09","尿1");
        put("10","尿2");
        put("11","RNA");
    }};
    public static final String MOVE_TYPE_FOR_TUBE = "1";
    public static final String MOVE_TYPE_FOR_BOX = "2";
    public static final String MOVE_TYPE_FOR_SHELF = "3";
    public static final String DESTROY_TYPE_FOR_TUBE = "1";
    public static final String DESTROY_TYPE_FOR_BOX = "2";


    public static final String SAMPLE_HISTORY_TRANSHIP = "101";
    public static final String SAMPLE_HISTORY_STOCK_IN = "102";
    public static final String SAMPLE_HISTORY_STOCK_OUT = "103";
    public static final String SAMPLE_HISTORY_HAND_OVER = "104";
    public static final String SAMPLE_HISTORY_MOVE = "105";
    public static final String SAMPLE_HISTORY_CHANGE_POSITION = "106";
    public static final String SAMPLE_HISTORY_DESTORY = "107";
    public static final Map<String,String> SAMPLE_TYPE_CODE_MAP = new HashMap<String,String>(){{
        put("A","血浆");
        put("W","白细胞");
        put("F","血清");
        put("E","尿");
        put("R","红细胞");
        put("RNA","RNA");
    }};

    //1：有出库记录再回来，2:原盒样本，3：盒内新增样本
    public static final String FROZEN_FLAG_STOCKIN_AGAIN = "1";
    public static final String FROZEN_FLAG_ORIGINAL = "2";
    public static final String FROZEN_FLAG_NEW = "3";

    /**
     * 导入数据接口地址
     */
    public static final String HTTPURL = "http://10.24.10.16:8912/scm/tubes";
//    http://10.24.10.16:8912/scm/tubes?projectId=007&boxCode=110130048
//    高危一期projectId=001
//    高危二期projectId=006
//    高危三期projectId=007
    public static final String PEACE_I_ID="001";
    public static final String PEACE_II_ID="006";
    public static final String PEACE_III_ID="007";

    public static final Map<String,String> UBE_STATUS_MAP = new HashMap(){{
        put("1","3001");
        put("NA","3002");
        put("0","3003");
    }};
    /**
     * 冻存盒锁定标识：1：分装锁，0：无锁
     */
    public static final Integer FROZEN_BOX_LOCKED_FOR_SPLIT = 1;
    //冻存盒状态
    public static final Map<String,String> FROZEN_BOX_STATUS_MAP = new HashMap(){{
        put(FROZEN_BOX_NEW,"新建");
        put(FROZEN_BOX_TRANSHIP_COMPLETE,"转运完成");
        put(FROZEN_BOX_STOCKING,"待入库");
        put(FROZEN_BOX_SPLITED,"已分装");
        put(FROZEN_BOX_STOCKED,"已入库");
        put(FROZEN_BOX_INVALID,"已作废");
        put(FROZEN_BOX_PUT_SHELVES,"已上架");
        put(FROZEN_BOX_SPLITING,"待分装");
        put(FROZEN_BOX_STOCK_OUT_PENDING,"待出库");
        put(FROZEN_BOX_STOCK_OUT_COMPLETED,"已出库");
        put(FROZEN_BOX_STOCK_OUT_HANDOVER,"已交接");
        put(FROZEN_BOX_DESTROY,"已销毁");
    }};


    public static final Map<String,String> FROZEN_BOX_STATUS_MAP = new HashMap(){{
        put(FROZEN_BOX_NEW,"新建");
        put(FROZEN_BOX_TRANSHIP_COMPLETE,"转运完成");
        put(FROZEN_BOX_STOCKING,"待入库");
        put(FROZEN_BOX_SPLITED,"已分装");
        put(FROZEN_BOX_STOCKED,"已入库");
        put(FROZEN_BOX_INVALID,"已作废");
        put(FROZEN_BOX_PUT_SHELVES,"已上架");
        put(FROZEN_BOX_SPLITING,"待分装");
        put(FROZEN_BOX_STOCK_OUT_PENDING,"待出库");
        put(FROZEN_BOX_STOCK_OUT_COMPLETED,"已出库");
        put(FROZEN_BOX_STOCK_OUT_HANDOVER,"已交接");
        put(FROZEN_BOX_DESTROY,"已销毁");
    }};

    private Constants() {
    }

}
