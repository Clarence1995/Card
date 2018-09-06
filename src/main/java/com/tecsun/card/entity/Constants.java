package com.tecsun.card.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thl on 2015/9/27.
 */
public class Constants {
    public static String COLLECT_HAD_SYNCHRO = "1";
    public static String COLLECT_NO_SYNCHRO = "0";

    public static String COLLECT_NO_DEAL = "00";
    public static String COLLECT_QUALIFIED = "01";
    public static String COLLECT_IMG_ERROR = "02";
    public static String COLLECT_ZANG_ERROR = "03";
    public static String COLLECT_USERINFO_ERROR = "04";
    public static String COLLECT_IMG_AND_ZANG_ERROR = "05";
    public static String COLLECT_IMG__AND_USERINFO_ERROR = "06";
    public static String COLLECT_ZANG_AND_USERINFO_ERROR = "07";
    public static String COLLECT_USER_REPEAT_ERROR = "08";
    public static String COLLECT_IMG_AND_ZANG_USERINFO_ERROR = "09";
    public static String COLLECT_HAD_TSB_IMG_NO_GONGANIMG = "10";
    public static String COLLECT_REGIONALCODE_ERROR = "11";

    public static String ZANG_NAME_NOT_EXIST = "00";


    // 区域信息编码(用于判断是否是区外异地
    public static List<String> OUTERCODELIST = new ArrayList<>();

    static {
        OUTERCODELIST.add("110000");
        OUTERCODELIST.add("310000");
        OUTERCODELIST.add("410100");
        OUTERCODELIST.add("410200");
        OUTERCODELIST.add("500000");
        OUTERCODELIST.add("510100");
        OUTERCODELIST.add("632801");
        OUTERCODELIST.add("610100");
        OUTERCODELIST.add("610400");
        OUTERCODELIST.add("620100");
    }

    public static String OUTER_CODE_REGIONAL = "549900";


    // 婴儿卡
    public static final String COLLECT_IS_BABY = "0";
    public static final String COLLECT_NOT_BABY = "1";
    //证件类型
    public static final String IDCARD_TYPE_JUMING = "01"; //居民身份证(户口簿)
    public static final String IDCARD_TYPE_JUNGUAN = "02"; //中国人民解放军军官证
    public static final String IDCARD_TYPE_WUJING = "03"; //中国人民武装警察警官证
    public static final String IDCARD_TYPE_XIANGGANG = "04"; //香港特区护照/港澳居民来往内地通行证
    public static final String IDCARD_TYPE_AOMEN = "05"; //澳门区护照/港澳居民来往内地通行证
    public static final String IDCARD_TYPE_TAIWAN = "06"; //台湾居民来往大陆通行证
    public static final String IDCARD_TYPE_GUOWAI = "07"; //外国人永久居留证
    public static final String IDCARD_TYPE_PASSPORT = "08"; //外国人护照


    public static final String USUAL_ADDRESS_CODE = "854300";


    public static final Map<Integer, String> COLLECT_SYNCHROSTATUS_DIC = new HashMap<>();

    static {
        COLLECT_SYNCHROSTATUS_DIC.put(0, "未同步到卡管");
        COLLECT_SYNCHROSTATUS_DIC.put(1, "已同步到卡管");
        COLLECT_SYNCHROSTATUS_DIC.put(42, "最近一批未同步到卡管，且人员数据不合格");
        COLLECT_SYNCHROSTATUS_DIC.put(44, "最近一批未同步到卡管，人员数据未经过处理");
        COLLECT_SYNCHROSTATUS_DIC.put(99, "同步状态未知");
    }

    public static final Map<String, String> CAED_PERSON_STATUS_DIC = new HashMap<>();

    static {
        CAED_PERSON_STATUS_DIC.put("00","初始");
        CAED_PERSON_STATUS_DIC.put("01","制卡中");
        CAED_PERSON_STATUS_DIC.put("02","待领卡");
        CAED_PERSON_STATUS_DIC.put("03","已领卡");
        CAED_PERSON_STATUS_DIC.put("04","已激活");
        CAED_PERSON_STATUS_DIC.put("05","正式挂失");
        CAED_PERSON_STATUS_DIC.put("06","临时挂失");
        CAED_PERSON_STATUS_DIC.put("07","补卡中");
        CAED_PERSON_STATUS_DIC.put("08","换卡中");
        CAED_PERSON_STATUS_DIC.put("09","注销");
        CAED_PERSON_STATUS_DIC.put("16","生成批次失败");
        CAED_PERSON_STATUS_DIC.put("17","即使制卡失败");
        CAED_PERSON_STATUS_DIC.put("18","中心制卡失败");
        CAED_PERSON_STATUS_DIC.put("19","异常");
        CAED_PERSON_STATUS_DIC.put("10","银行开户导出失败");
        CAED_PERSON_STATUS_DIC.put("11","银行开户回盘失败");
        CAED_PERSON_STATUS_DIC.put("12","工厂制卡数据导出失败");
        CAED_PERSON_STATUS_DIC.put("13","工厂制卡数据回盘失败");
        CAED_PERSON_STATUS_DIC.put("14","中心制卡数据导出失败");
        CAED_PERSON_STATUS_DIC.put("15","中心制卡数据回盘失败");

    }
    //=============================================


    public static String RESULT_MESSAGE_SUCCESS = "200";
    public static String RESULT_MESSAGE_EXCEPTION = "999";
    public static String RESULT_MESSAGE_ERROR = "0";

    public static final String TSB = "TSB";//德生宝
    public static final String APP = "App";//手机应用
    public static final String NETPORTAL = "NetPortal";//网办
    public static final String SELFSERVICE = "SelfService";//自助终端
    public static final String WECHAT = "WeChat";//微信

    //自助终端，网办等渠道签到用到的key前缀+uuid,value为当前客户端的ip
    public static String TOKENNAME = "sisp_iface:token:";

    public static String USERKEY = "userkey:";

    public static String USERAREAID = "userAreaid:";

    //自助终端超时时间 单位分钟
    public static Integer SELFSERVICETIMEOUT = 120;

    //系统公告-- 类型
    public static String SYSTEM_NOTICE_TYPE = "system_notice_type";

    //用户类型
    public static final long TYPE_OF_ADMIN = 1;//1管理员
    public static final long TYPE_OF_PERSON = 2;//2个人
    public static final long TYPE_OF_UNIT = 3;//1单位

    //角色名称
    public static final String ROLE_PERSON = "个人角色";
    public static final String ROLE_UNIT = "单位角色";

    //是否修改过密码
    public static final long IS_NOT_UPDATE_PWD = 1;//未修改过密码
    public static final long IS_UPDATE_PWD = 2;//修改过密码

    //用户初始密码
    public static final String USER_INIT_PASSWORD = "123456";

    //短信业务类型
    public static final long MESSAGE_BUS_TYPE_PASSWORD = 1; //修改密码发送短信验证码
    public static final long MESSAGE_BUS_TYPE_PAY = 2; //缴费发送短信验证码

    //redis键值
    //参数字典
    public static final String RK_PF_OF_PARAMETER = "siweb:cmp_core:parameter:{%s}";

    //字典组 key
    public static String DICTIONGROUPKEY = "sisp_public:cmp_core:dictionary:{groupId}:detaillist";

    public static final String KEY_DICTDATA = "siboss:cmp:dictionary_detaillist:{val}";
    public static final String KEY_DISTINCTDATA = "siboss:cmp:distinct_list:{val}";

    //字典组 key
    public static String DICTIONGROUP_URI = "/dev/deviceRegist/checkDevExist";

    /*public static Map<String,String> USER_STATUS=new HashMap<String, String>();
    static {
        USER_STATUS.put("00", "初始");
        USER_STATUS.put("01", "制卡中");
        USER_STATUS.put("02", "待领卡");
        USER_STATUS.put("03", "已领卡");
        USER_STATUS.put("04", "已激活");
        USER_STATUS.put("05", "正式挂失");
        USER_STATUS.put("06", "临时挂失");
        USER_STATUS.put("07", "补卡中");
        USER_STATUS.put("08", "换卡中");
        USER_STATUS.put("09", "注销");
        USER_STATUS.put("10", "银行开户导出失败");
        USER_STATUS.put("11", "银行开户回盘失败");
        USER_STATUS.put("12", "工厂制卡数据导出失败");
        USER_STATUS.put("13", "工厂制卡数据回盘失败");
        USER_STATUS.put("14", "中心制卡数据导出失败");
        USER_STATUS.put("15", "中心制卡数据回盘失败");

    }*/
    public static final String SEX = "SEX";//性别
    public static final String CERTIFICATE_TYPE = "CERTIFICATE_TYPE";//证件类型
    public static final String USER_STATUS = "USER_STATUS";//用户状态
    public static final String PARAM_NATION = "PARAM_NATION";//民族
    public static final String USER_JOB = "USER_JOB";//用户个人职业
    public static final String USER_TYPE = "USER_TYPE";//用户个人身份
    public static final String MARK_STATUS = "MARK_STATUS";//标记状态
    public static final String MARKSTATUS = "MARKSTATUS";//标记状态二（包含 即时制卡）
    public static final String CARD_APPLICATION_STATUS = "CARD_APPLICATION_STATUS";//卡应用状态
    public static final String GRANT_TYPE = "GRANT_TYPE";//发卡领取类型
    public static final String CHECK_STATUS = "CHECK_STATUS";//审核状态  通过/不通过
    public static final String INIT_ORG_CODE = "INIT_ORG_CODE";//初始化机构编码
    public static final String BATCH_STATUS = "BATCH_STATUS";//批次状态
    public static final String BUSINESS_TYPE = "APPLY_BUS_TYPE";//业务类型
    public static final String BANK = "bank";//银行
    public static final String VALIDTAG = "VALIDTAG";//人员有效状态
    public static final String CARD_NO_STATUS = "CARDNO_STATUS";//卡号使用状态
    //审核状态
    public static final String CHECK_STATUS_00 = "00";//未审核
    public static final String CHECK_STATUS_01 = "01";//审核通过
    public static final String CHECK_STATUS_02 = "02";//审核不通过
    public static final String CHECK_STATUS_03 = "03";//业务失败
    //卡号状态
//    00：初始、解锁
//    01：暂用
//    02：已使用
//    03：锁定
    public static final String CARDNO_STATUS_00 = "00";//初始、解锁
    public static final String CARDNO_STATUS_01 = "01";//暂用
    public static final String CARDNO_STATUS_02 = "02";//已使用
    public static final String CARDNO_STATUS_03 = "03";//锁定


    //交易号
    public static final String bsId_checkCard = "015";//卡鉴权

    public static final String bsId_identifyOuter = "026";// 卡鉴权及外部认证

    public static final String bsId_identifyInner = "028";//  卡内部认证

    //个人状态
//    00:初始,01:制卡中,02:待领卡,03:已领卡,04:已激活 05:正式挂失,06:临时挂失 07:补卡中,08:换卡中,09:注销
//10:银行开户导出失败 11:银行开户回盘失败 12:工厂制卡数据导出失败 13:工厂制卡数据回盘失败 14:中心制卡数据导出失败 15:中心制卡数据回盘失败
//16 生成批次失败   17 即使制卡失败   18  中心制卡失败  19 异常

    public static final String USER_STATUS_00 = "00";//初始
    public static final String USER_STATUS_01 = "01";//制卡中
    public static final String USER_STATUS_02 = "02";//待领卡
    public static final String USER_STATUS_03 = "03";//已领卡
    public static final String USER_STATUS_04 = "04";//已激活
    public static final String USER_STATUS_05 = "05"; //正式挂失
    public static final String USER_STATUS_06 = "06";//临时挂失
    public static final String USER_STATUS_07 = "07";//补卡中
    public static final String USER_STATUS_08 = "08";//换卡中
    public static final String USER_STATUS_09 = "09";//注销
    public static final String USER_STATUS_10 = "10"; //
    public static final String USER_STATUS_11 = "11";//
    public static final String USER_STATUS_12 = "12";//
    public static final String USER_STATUS_13 = "13";//
    public static final String USER_STATUS_14 = "14";
    public static final String USER_STATUS_15 = "15";
    public static final String USER_STATUS_16 = "16";//生成批次失败
    public static final String USER_STATUS_17 = "17";//即使制卡失败
    public static final String USER_STATUS_18 = "18";//中心制卡失败
    public static final String USER_STATUS_19 = "19";//异常

    //有效标志
    public static final String USER_VALIDTAG_00 = "00";//有效
    public static final String USER_VALIDTAG_01 = "01";//无效

    //中间表用户记录 状态
    public static final String USER_RECODS_STATUS_00 = "00";//初始
    public static final String USER_RECODS_STATUS_01 = "01";//已同步
    public static final String USER_RECODS_STATUS_02 = "02";//修改


    //申领 业务类型
    //01个人申请、02网上申请、03批量申请、05遗失补卡、06普通换卡、07质保换卡 08预制卡
    public static final String BUSINESS_TYPE_01 = "01";//新申领
    //    public static final String BUSINESS_TYPE_02="02";//网上申请
//    public static final String BUSINESS_TYPE_03="03";//批量申请
    public static final String BUSINESS_TYPE_05 = "05";//遗失补卡
    public static final String BUSINESS_TYPE_06 = "06"; //普通换卡
    public static final String BUSINESS_TYPE_07 = "07";//质保换卡
    public static final String BUSINESS_TYPE_08 = "08";//预制卡

    //申领数据来源
    //01个人  02单位、03网点
    public static final String SOURCE_01 = "01";//个人申请
    public static final String SOURCE_02 = "02";//02单位
//    public static final String SOURCE_03="03";//03网点

    //申领 状态
    public static final String APPLY_STATUS_00 = "00";//申请
    public static final String APPLY_STATUS_01 = "01";//撤销
    public static final String APPLY_STATUS_02 = "02";//业务成功
    public static final String APPLY_STATUS_03 = "03";//业务失败
    public static final String APPLY_STATUS_04 = "04"; //异常处理
    public static final String APPLY_STATUS_05 = "05";//申领中
    public static final String APPLY_STATUS_06 = "06";//异常处理成功
    public static final String APPLY_STATUS_07 = "07";//异常处理失败

    //标记 状态
    public static final String MARK_STATUS_0 = "0";//正常
    public static final String MARK_STATUS_1 = "1";//优先
    public static final String MARK_STATUS_2 = "2";//即时制卡
    public static final String MARK_STATUS_3 = "3";//中心制卡

   /* public static Map<String,String> MARK_STATUS=new HashMap<String, String>();
    static {
        USER_STATUS.put("0", "正常");
        USER_STATUS.put("1", "优先");
        USER_STATUS.put("2", "即时制卡");
    }*/

    //制卡类型
    public static final String MAKE_CARD_TYPE_01 = "01";//成品卡
    public static final String MAKE_CARD_TYPE_02 = "02";//预制卡
    public static final String MAKE_CARD_TYPE_03 = "03";//即时制卡
    public static final String MAKE_CARD_TYPE_04 = "04";//中心制卡

    //批次状态//
    // 10生成批次  20 银行已导出  21 银行已回盘    30 工厂已导出  31 工厂已回盘  40 初始化中心已导出  41 初始化中心已回盘   50 已发放到网点
    public static final String BATCH_STATUS_10 = "10";
    public static final String BATCH_STATUS_20 = "20";
    public static final String BATCH_STATUS_21 = "21";
    public static final String BATCH_STATUS_30 = "30";
    public static final String BATCH_STATUS_31 = "31";
    public static final String BATCH_STATUS_40 = "40";
    public static final String BATCH_STATUS_41 = "41";
    public static final String BATCH_STATUS_50 = "50";

    //批次明细状态
    //10生成批次    20已有银行帐号   21无银行帐号     22匹配银行帐号失败  30工厂制卡成功    31工厂制卡失败
    // 32工厂制卡异常    40 成品卡（卡中心替换密钥成功）   41密钥错误（卡中心替换密钥失败）   60 已发卡（网点已领卡）
    public static final String BATCH_DETAIL_STATUS_10 = "10";
    public static final String BATCH_DETAIL_STATUS_20 = "20";
    public static final String BATCH_DETAIL_STATUS_21 = "21";
    public static final String BATCH_DETAIL_STATUS_22 = "22";
    public static final String BATCH_DETAIL_STATUS_30 = "30";
    public static final String BATCH_DETAIL_STATUS_31 = "31";
    public static final String BATCH_DETAIL_STATUS_32 = "32";
    public static final String BATCH_DETAIL_STATUS_40 = "40";
    public static final String BATCH_DETAIL_STATUS_41 = "41";
    public static final String BATCH_DETAIL_STATUS_60 = "60";

    //卡状态
    //10 生成批次20 银行已导出21 银行已回盘22 银行开户导出失败23银行开户回盘失败30 工厂已导出31 工厂已回盘32:工厂制卡数据导出失败
// 33:工厂制卡数据回盘失败40 初始化中心已导出41 初始化中心已回盘
// 42:中心制卡数据导出失败43:中心制卡数据回盘失败
// 50:即使制卡已回盘51:即时制卡失败
// 60 网点已领卡70 dd
    public static final String CARD_STATUS_00 = "00"; //数据上传到卡管
    public static final String CARD_STATUS_10 = "10"; //生成批次
    public static final String CARD_STATUS_20 = "20"; //银行已导出
    public static final String CARD_STATUS_21 = "21"; //银行已回盘
    public static final String CARD_STATUS_22 = "22"; //银行开户导出失败
    public static final String CARD_STATUS_23 = "23"; //银行开户回盘失败
    public static final String CARD_STATUS_2a = "2a"; //ca证书申请导出成功
    public static final String CARD_STATUS_2b = "2b"; //ca证书申请回盘成功
    public static final String CARD_STATUS_2c = "2c"; //ca证书申请导出失败
    public static final String CARD_STATUS_2d = "2d"; //ca证书申请回盘失败
    public static final String CARD_STATUS_30 = "30";
    public static final String CARD_STATUS_31 = "31";
    public static final String CARD_STATUS_32 = "32"; //工厂制卡数据导出失败
    public static final String CARD_STATUS_33 = "33"; //工厂制卡数据回盘失败
    public static final String CARD_STATUS_40 = "40";
    public static final String CARD_STATUS_41 = "41";
    public static final String CARD_STATUS_60 = "60"; //已发放到网点
    public static final String CARD_STATUS_70 = "70"; //已发卡

    //0封存,1正常，2 正式挂失，3应用锁定,4临时挂失，9注销
    public static final String CARD_APPLICATION_STATUS_0 = "0";
    public static final String CARD_APPLICATION_STATUS_1 = "1";
    public static final String CARD_APPLICATION_STATUS_2 = "2";
    public static final String CARD_APPLICATION_STATUS_3 = "3";
    public static final String CARD_APPLICATION_STATUS_4 = "4";
    public static final String CARD_APPLICATION_STATUS_9 = "9";

    //卡号序列 reids的key
    public static final String SERIAL = "sisp:cardmanagement:serial";
    //批次号序列 reids的key
    public static final String BATCH_SERIAL = "sisp:cardmanagement:batch_serial";
    //人员序列 reids的key
    public static final String USER_SERIAL = "sisp:cardmanagement:user_serial";

    //城市编码对应
    public static final String CARDCODE = "CITY_CODE";


    public static final String BUS_TYPE_01 = "01";//日志业务类型  01 临时挂失
    public static final String BUS_TYPE_02 = "02";//02 正式挂失
    public static final String BUS_TYPE_03 = "03";  //03 激活
    public static final String BUS_TYPE_04 = "04";  // 04 注销
    public static final String BUS_TYPE_05 = "05";//解挂
    public static final String BUS_TYPE_06 = "06";//鉴权
    public static final String BUS_TYPE_07 = "07";//人员信息修改
    public static final String BUS_TYPE_08 = "08";//发卡
    public static final String BUS_TYPE_09 = "09";//有效
    public static final String BUS_TYPE_10 = "10";//无效
    public static final String BUS_TYPE_11 = "11";//初始、解锁
    public static final String BUS_TYPE_12 = "12";//暂用
    public static final String BUS_TYPE_13 = "13";//已使用
    public static final String BUS_TYPE_14 = "14";//锁定
    public static final String BUS_TYPE_15 = "15";//还原批次
    //中间库同步状态
    public static final String mid_init_synStatus = "00";//新增记录
    public static final String mid_update_synStatus = "02";//修改记录

    //采集库同步状态
    public static final String notSynchro = "0";//未同步
    public static final String hadSynchro = "1";//已同步

    //预制卡表是否使用 状态
    public static final String IS_USED_00 = "00";//未
    public static final String IS_USED_01 = "01";//已

    //生存状态
    public static final String USER_LIVE_STATUS_1 = "1"; //正常
    public static final String USER_LIVE_STATUS_2 = "2"; //死亡
    public static final String USER_LIVE_STATUS_3 = "3"; //被判刑收监或劳动教养
    public static final String USER_LIVE_STATUS_4 = "4"; //失踪
    public static final String USER_LIVE_STATUS_5 = "5"; //状态不明


}
