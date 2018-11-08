package com.tecsun.card.entity;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    // ~-----------
    public static final String SEPARATOR                     = File.separator;
    public static final String IMG_SUFFIX                    = ".jpg";
    public static final String TXT_SUFFIX                    = ".txt";
    public static final String EXCEL_SUFFIX                  = ".xlsx";
    // ------------ 状态码 --------------
    public static final int    FAIL_RESULT_CODE              = 0;
    public static final int    SUCCESS_RESULT_CODE           = 200;
    public static final int    GONG_AN_EXCEPTION_RESULT_CODE = 777;
    public static final int    EXCEPTION_RESULT_CODE         = 999;
    // ------------ 状态码 --------------

    public static final String GONG_AN_SUCCESS_RESULT_CODE = "000";
    public static final String IMG_113_FILE_ROOT_PATH      = "E://tecsun//file//photo//personPhoto";


    // 采集同步标志
    public static final String COLLECT_HAD_SYNCHRO = "1";
    public static final String COLLECT_NO_SYNCHRO  = "0";
    public static final String COLLECT_USER_REPEAT = "9";

    //  ---------- 采集人员状态标志 START ------------
    // 未处理
    public static final String COLLECT_NO_DEAL                     = "00";
    // 合格
    public static final String COLLECT_QUALIFIED                   = "01";
    // 图片错误
    public static final String COLLECT_IMG_ERROR                   = "02";
    // 藏文错误
    public static final String COLLECT_ZANG_ERROR                  = "03";
    // 人员基础信息错误
    public static final String COLLECT_USERINFO_ERROR              = "04";
    // 图片 & 藏文出错
    public static final String COLLECT_IMG_AND_ZANG_ERROR          = "05";
    // 图片 & 人员基础信息 出错
    public static final String COLLECT_IMG__AND_USERINFO_ERROR     = "06";
    // 藏文 & 人员基础信息 出错
    public static final String COLLECT_ZANG_AND_USERINFO_ERROR     = "07";
    // 人员重复
    public static final String COLLECT_USER_REPEAT_ERROR           = "08";
    // 照片 & 藏文 & 人员基础信息 出错
    public static final String COLLECT_IMG_AND_ZANG_USERINFO_ERROR = "09";
    // 有TSB采集照片 & 没有公安照片
    public static final String COLLECT_HAD_TSB_IMG_NO_GONGANIMG    = "10";
    // 区划代码出错
    public static final String COLLECT_REGIONALCODE_ERROR          = "11";
    //  ---------- 采集人员状态标志 END ------------

    // 没有藏文名字,默认为 00
    public static final String ZANG_NAME_NOT_EXIST = "00";

    // ----------- 公安比对情况 START---------------
    public static final String COLLECT_GONGAN_COMPARE_NO        = "00";
    public static final String COLLECT_GONGAN_COMPARE_UPDATE    = "01";
    public static final String COLLECT_GONGAN_COMPARE_YES       = "02";
    public static final String COLLECT_GONGAN_COMPARE_EXCEPTION = "03";
    public static final String COLLECT_GONGAN_COMPARE_NO_DATA   = "04";
    // ----------- 公安比对情况 END---------------

    // 区外异地编码 AAC301
    public static final String OUTER_CODE_REGIONAL = "549900";


    // 婴儿卡 0:婴儿卡/1:非婴儿卡
    public static final String COLLECT_IS_BABY  = "0";
    public static final String COLLECT_NOT_BABY = "1";
    public static final Long   BABY_TIME        = 189216000000L;

    //  ----------证件类型 START------------
    // 居民身份证(户口簿)
    public static final String IDCARD_TYPE_JUMING          = "01";
    // 中国人民解放军军官证
    public static final String IDCARD_TYPE_JUNGUAN         = "02";
    // 中国人民武装警察警官证
    public static final String IDCARD_TYPE_WUJING          = "03";
    // 香港特区护照/港澳居民来往内地通行证
    public static final String IDCARD_TYPE_XIANGGANG       = "04";
    // 澳门区护照/港澳居民来往内地通行证
    public static final String IDCARD_TYPE_AOMEN           = "05";
    // 台湾居民来往大陆通行证
    public static final String IDCARD_TYPE_TAIWAN          = "06";
    // 外国人永久居留证
    public static final String IDCARD_TYPE_GUOWAI_YONGJIU  = "07";
    // 外国人护照
    public static final String IDCARD_TYPE_GUOWAI_PASSPORT = "08";
    //  ----------证件类型 END------------


    // 常住地地址编码
    public static final String USUAL_ADDRESS_CODE = "854300";
    // ------------证件类型 END-----------------


    // -----------人员状态 START ----------------
    // 初始
    public static final String USER_STATUS_INIT                                = "00";
    // 制卡中
    public static final String USER_STATUS_MARKINGCARD                         = "01";
    // 待领卡
    public static final String USER_STATUS_CARD_READY                          = "02";
    // 已领卡
    public static final String USER_STATUS_HAD_CARD                            = "03";
    // 已激活
    public static final String USER_STATUS_CARD_ACTIVATION                     = "04";
    // 正式挂失
    public static final String USER_STATUS_CARD_REPORT                         = "05";
    // 临时挂失
    public static final String USER_STATUS_CARD_REPORT_TEMPORARY               = "06";
    // 补卡中
    public static final String USER_STATUS_REPLAYING                           = "07";
    // 换卡中
    public static final String USER_STATUS_CHNAGING_CARD                       = "08";
    // 注销
    public static final String USER_STATUS_CANCELLATION                        = "09";
    // 银行导出失败
    public static final String USER_STATUS_FAIL_BANK_EXPORT                    = "10";
    // 银行开户回盘失败
    public static final String USER_STATUS_FAIL_BANK_COUNTEROFFER              = "11";
    // 工厂制卡数据导出失败
    public static final String USER_STATUS_FAIL_FACTORY_EXPORT                 = "12";
    // 工厂制卡数据回盘失败
    public static final String USER_STATUS_FAIL_FACTORY_COUNTEROFFER           = "13";
    // 中心制卡数据导出失败
    public static final String USER_STATUS__FAIL_MAKE_CARD_CENTER_EXPORT       = "14";
    // 中心制卡数据回盘失败
    public static final String USER_STATUS__FAIL_MAKE_CARD_CENTER_COUNTEROFFER = "15";
    // 生成批次失败
    public static final String USER_STATUS__FAIL_GENERATE_BATCH                = "16";
    // 即使制卡失败
    public static final String USER_STATUS_FAIL_INSTANT_CARD                   = "17";
    // 中心制卡失败
    public static final String USER_STATUS_FAIL_MAKE_CARD_CENTER               = "18";
    // 异常
    public static final String USER_STATUS_EXCEPTION                           = "19";
    // -----------人员状态 END ----------------

    // -----------有效标志 START --------------
    // 有效
    public static final String USER_VALIDTAG_VALID   = "00";
    // 无效
    public static final String USER_VALIDTAG_UNVALID = "01";
    // -----------有效标志 END --------------

    // -----------生存状态 START ------------
    // 生存
    public static final String USER_AAC060_ALIVE        = "1";
    // 死亡
    public static final String USER_AAC060_DEATH        = "2";
    // 监禁
    public static final String USER_AAC060_IMPRISON     = "3";
    // 失踪
    public static final String USER_AAC060_MISSING      = "4";
    // 无法定义
    public static final String USER_AAC060_UNIDENTIFIED = "5";
    // -----------生存状态 END ------------


    // ---------- 人员标记状态 START ----------
    // 标记状态
    // 正常
    public static final String USER_MARK_STATUS_NORMAL            = "0";
    // 优先
    public static final String USER_MARK_STATUS_PRIORITY          = "1";
    // 即时制卡
    public static final String USER_MARK_STATUS_MAKE_INSTANT_CARD = "2";
    // 中心制卡
    public static final String USER_MARK_STATUS_MAKE_CENTER_CARD  = "3";
    // ---------- 人员标记状态 START ----------


    // ---------- 人员申领类型 START ----------
    // 新申领
    public static final String USER_APPLY_TYPE_NEW_APPLY    = "00";
    // 补换申领
    public static final String USER_APPLY_TYPE_CHANGE_APPLY = "01";
    // ---------- 人员申领类型 END ----------

    // ---------- 人员采集标志 START ----------
    // 采集库
    public static final String USER_COLLECT_FLAG_COLLECT = "00";
    // 业务系统
    public static final String USER_COLLECT_FLAG_SYSTEM  = "01";
    // ---------- 人员采集标志 END----------

    // ---------- 人员申领状态 START ----------
    // 默认
    public static final String USER_APPLY_STATUS_DEFAULT  = "00";
    // 第三方
    public static final String USER_APPLY_STATUS_THIRD    = "01";
    // 批次中
    public static final String USER_APPLY_STATUS_AT_BATCH = "02";
    // ---------- 人员申领状态 END----------


    // ---------- 卡应用状态 START ----------
    //0封存,1正常，2 正式挂失，3应用锁定,4临时挂失，9注销
    public static final String CARD_APPLICATION_STATUS_MOTHBALL         = "0";
    public static final String CARD_APPLICATION_STATUS_NORMAL           = "1";
    public static final String CARD_APPLICATION_STATUS_REPORT           = "2";
    public static final String CARD_APPLICATION_STATUS_LOCK             = "3";
    public static final String CARD_APPLICATION_STATUS_REPORT_TEMPORARY = "4";
    public static final String CARD_APPLICATION_STATUS_CANCELLATION     = "9";
    // ---------- 卡应用状态 START ----------


    // BUS_APPLY 申领
    // ----------- 申领业务类型 START--------------
    // 申领 业务类型 BUSINESS_TYPE
    // 新申领
    public static final String BUS_APLY_BUSINESSTYPE_NEW_APPLY           = "01";
    // 遗失补卡
    public static final String BUS_APLY_BUSINESSTYPE_REPLAY_LOSS         = "05";
    // 普通换卡
    public static final String BUS_APPLY_BUSINESSTYPE_CHANGECARD_NORMAL  = "06";
    // 质保换卡
    public static final String BUS_APPLY_BUSINESSTYPE_CHANGECARD_QUALITY = "07";
    // 预制卡
    public static final String BUS_APPLY_BUSINESSTYPE_PRE_MAKE_CARD      = "08";
    // ----------- 申领业务类型 END--------------


    // ----------- 申领业务类型 START--------------
    // 申领业务类型
    public static final String BUS_APPLY_SOURCE_PERSON     = "01";
    public static final String BUS_APPLY_SOURCE_COLLECTIVE = "02";
    // ----------- 申领业务类型 START--------------

    // ----------- 申领状态 START--------------
    //申领状态
    // 申请
    public static final String BUS_APPLY_STATUS_APPLY                          = "00";
    // 撤销
    public static final String BUS_APPLY_STATUS_CANCEL                         = "01";
    // 业务成功
    public static final String BUS_APPLY_STATUS_APPLY_SUCCESS                  = "02";
    // 业务失败
    public static final String BUS_APPLY_STATUS_APPLY_FAIL                     = "03";
    // 异常处理
    public static final String BUS_APPLY_STATUS_APPLY_HANDING_EXCEPTION        = "04";
    // 申领中
    public static final String BUS_APPLY_STATUS_APPLYING                       = "05";
    // 异常处理成功
    public static final String BUS_APPLY_STATUS_APPLY_HANDLE_EXCEPTION_SUCCESS = "06";
    // 异常处理失败
    public static final String BUS_APPLY_STATUS_APPLY_HANDLE_EXCEPTION_FAIL    = "07";
    // ----------- 申领状态 END --------------

    // ----------- 申领 是否选号 START---------
    // 不选号
    public static final String BUS_APPLY_FLAG_CHOOSE_NUM_NO  = "00";
    // 选号
    public static final String BUS_APPLY_FLAG_CHOOSE_NUM_YES = "01";
    // ----------- 申领 是否选号 END ----------

    // ----------- 申领 是否更换银行卡号 START---------
    // 不更换
    public static final String BUS_APPLY_CHANGE_BANK_NUM_NO  = "00";
    // 更换
    public static final String BUS_APPLY_CHANGE_BANK_NUM_YES = "01";
    // ----------- 申领 是否更换银行卡号 END ----------

    // ----------- 申领 是否邮寄 START -----------
    // 不邮寄
    public static final String BUS_APPLY_EXPRESS_NO  = "00";
    // 邮寄
    public static final String BUS_APPLY_EXPRESS_YES = "01";
    // ----------- 申领 是否邮寄 END -----------


    // 人员序列 REIDS 的 KEY
    public static final String REDIS_USER_SERIAL_KEY = "sisp:cardmanagement:user_serial";

    // ----------- 是否已完成东软单位信息同步 START-----------
    // 未同步
    public static final int DONGRUAN_SYNCHRO_NO             = 0;
    // 已同步
    public static final int DONGRUAN_SYNCHRO_YES            = 1;
    // 此人在东软视图中不存在信息
    public static final int DONGRUAN_SYNCHRO_NO_INFORMATION = 2;
    // ----------- 是否已完成东软单位信息同步 END-----------

    // 超级管理员用户名
    public static final String ADMIN = "admin";

    // 公安接口返回
    public static final String GONGAN_SUCCESS_RESULT = "000";


    // ~ HashMap --------------------------------------------------------
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

    // 采集同步标志字典
    public static final Map<Integer, String> COLLECT_SYNCHROSTATUS_DIC = new HashMap<>(5);

    static {
        COLLECT_SYNCHROSTATUS_DIC.put(-1, " ");
        COLLECT_SYNCHROSTATUS_DIC.put(0, "未同步到卡管");
        COLLECT_SYNCHROSTATUS_DIC.put(1, "已同步到卡管");
        COLLECT_SYNCHROSTATUS_DIC.put(42, "最近一批未同步到卡管，且人员数据不合格");
        COLLECT_SYNCHROSTATUS_DIC.put(44, "最近一批未同步到卡管，人员数据未经过处理");
        COLLECT_SYNCHROSTATUS_DIC.put(99, "同步状态未知");
    }

    // ~----AC01 人员状态字典
    public static final Map<String, String> CAED_PERSON_STATUS_DIC = new HashMap<>(21);

    static {
        CAED_PERSON_STATUS_DIC.put("-1", " ");
        CAED_PERSON_STATUS_DIC.put("00", "初始");
        CAED_PERSON_STATUS_DIC.put("01", "制卡中");
        CAED_PERSON_STATUS_DIC.put("02", "待领卡");
        CAED_PERSON_STATUS_DIC.put("03", "已领卡");
        CAED_PERSON_STATUS_DIC.put("04", "已激活");
        CAED_PERSON_STATUS_DIC.put("05", "正式挂失");
        CAED_PERSON_STATUS_DIC.put("06", "临时挂失");
        CAED_PERSON_STATUS_DIC.put("07", "补卡中");
        CAED_PERSON_STATUS_DIC.put("08", "换卡中");
        CAED_PERSON_STATUS_DIC.put("09", "注销");
        CAED_PERSON_STATUS_DIC.put("10", "银行开户导出失败");
        CAED_PERSON_STATUS_DIC.put("11", "银行开户回盘失败");
        CAED_PERSON_STATUS_DIC.put("12", "工厂制卡数据导出失败");
        CAED_PERSON_STATUS_DIC.put("13", "工厂制卡数据回盘失败");
        CAED_PERSON_STATUS_DIC.put("14", "中心制卡数据导出失败");
        CAED_PERSON_STATUS_DIC.put("15", "中心制卡数据回盘失败");
        CAED_PERSON_STATUS_DIC.put("16", "生成批次失败");
        CAED_PERSON_STATUS_DIC.put("17", "即使制卡失败");
        CAED_PERSON_STATUS_DIC.put("18", "中心制卡失败");
        CAED_PERSON_STATUS_DIC.put("19", "异常");

    }

    // ~----AZ01 卡状态字典
    public static final Map<String, String> CARD_CARD_STATUS_DIC = new HashMap<>(18);

    static {
        CARD_CARD_STATUS_DIC.put("-1", " ");
        CARD_CARD_STATUS_DIC.put("00", "数据上传到卡管");
        CARD_CARD_STATUS_DIC.put("10", "生成批次");
        CARD_CARD_STATUS_DIC.put("20", "银行已导出");
        CARD_CARD_STATUS_DIC.put("21", "银行已回盘");
        CARD_CARD_STATUS_DIC.put("22", "银行开户导出失败");
        CARD_CARD_STATUS_DIC.put("23", "银行开户回盘失败");
        CARD_CARD_STATUS_DIC.put("2a", "ca证书申请导出成功");
        CARD_CARD_STATUS_DIC.put("2b", "ca证书申请回盘成功");
        CARD_CARD_STATUS_DIC.put("2c", "ca证书申请导出失败");
        CARD_CARD_STATUS_DIC.put("2d", "ca证书申请回盘失败");
        CARD_CARD_STATUS_DIC.put("30", "工厂制卡导出");
        CARD_CARD_STATUS_DIC.put("31", "工厂制卡回盘");
        CARD_CARD_STATUS_DIC.put("32", "工厂制卡数据导出失败");
        CARD_CARD_STATUS_DIC.put("33", "工厂制卡数据回盘失败");
        CARD_CARD_STATUS_DIC.put("40", "中心制卡导出");
        CARD_CARD_STATUS_DIC.put("41", "中心制卡回盘");
        CARD_CARD_STATUS_DIC.put("60", "已发放到网点");
        CARD_CARD_STATUS_DIC.put("70", "已发卡");
    }

    // ~----卡应用状态字典
    public static final Map<String, String> CARD_CARD_CARD_APPLY_DIC = new HashMap<>(7);

    static {
        CARD_CARD_CARD_APPLY_DIC.put("-1", " ");
        CARD_CARD_CARD_APPLY_DIC.put("0", "封存");
        CARD_CARD_CARD_APPLY_DIC.put("1", "正常");
        CARD_CARD_CARD_APPLY_DIC.put("2", "正式挂失");
        CARD_CARD_CARD_APPLY_DIC.put("3", "应用锁定");
        CARD_CARD_CARD_APPLY_DIC.put("4", "临时挂失");
        CARD_CARD_CARD_APPLY_DIC.put("9", "注销");
    }


    // //批次状态//
    // // 10生成批次  20 银行已导出  21 银行已回盘    30 工厂已导出  31 工厂已回盘  40 初始化中心已导出  41 初始化中心已回盘   50 已发放到网点
    // public static final String BATCH_STATUS_10 = "10";
    // public static final String BATCH_STATUS_20 = "20";
    // public static final String BATCH_STATUS_21 = "21";
    // public static final String BATCH_STATUS_30 = "30";
    // public static final String BATCH_STATUS_31 = "31";
    // public static final String BATCH_STATUS_40 = "40";
    // public static final String BATCH_STATUS_41 = "41";
    // public static final String BATCH_STATUS_50 = "50";
    //
    // //批次明细状态
    // //10生成批次    20已有银行帐号   21无银行帐号     22匹配银行帐号失败  30工厂制卡成功    31工厂制卡失败
    // // 32工厂制卡异常    40 成品卡（卡中心替换密钥成功）   41密钥错误（卡中心替换密钥失败）   60 已发卡（网点已领卡）
    // public static final String BATCH_DETAIL_STATUS_10 = "10";
    // public static final String BATCH_DETAIL_STATUS_20 = "20";
    // public static final String BATCH_DETAIL_STATUS_21 = "21";
    // public static final String BATCH_DETAIL_STATUS_22 = "22";
    // public static final String BATCH_DETAIL_STATUS_30 = "30";
    // public static final String BATCH_DETAIL_STATUS_31 = "31";
    // public static final String BATCH_DETAIL_STATUS_32 = "32";
    // public static final String BATCH_DETAIL_STATUS_40 = "40";
    // public static final String BATCH_DETAIL_STATUS_41 = "41";
    // public static final String BATCH_DETAIL_STATUS_60 = "60";
    //
    // //卡状态
    //10 生成批次20 银行已导出21 银行已回盘22 银行开户导出失败23银行开户回盘失败30 工厂已导出31 工厂已回盘32:工厂制卡数据导出失败
// 33:工厂制卡数据回盘失败40 初始化中心已导出41 初始化中心已回盘
// 42:中心制卡数据导出失败43:中心制卡数据回盘失败
// 50:即使制卡已回盘51:即时制卡失败
// 60 网点已领卡70 dd
//     public static final String CARD_STATUS_00 = "00"; //数据上传到卡管
//     public static final String CARD_STATUS_10 = "10"; //生成批次
//     public static final String CARD_STATUS_20 = "20"; //银行已导出
//     public static final String CARD_STATUS_21 = "21"; //银行已回盘
//     public static final String CARD_STATUS_22 = "22"; //银行开户导出失败
//     public static final String CARD_STATUS_23 = "23"; //银行开户回盘失败
//     public static final String CARD_STATUS_2a = "2a"; //ca证书申请导出成功
//     public static final String CARD_STATUS_2b = "2b"; //ca证书申请回盘成功
//     public static final String CARD_STATUS_2c = "2c"; //ca证书申请导出失败
//     public static final String CARD_STATUS_2d = "2d"; //ca证书申请回盘失败
//     public static final String CARD_STATUS_30 = "30";
//     public static final String CARD_STATUS_31 = "31";
//     public static final String CARD_STATUS_32 = "32"; //工厂制卡数据导出失败
//     public static final String CARD_STATUS_33 = "33"; //工厂制卡数据回盘失败
//     public static final String CARD_STATUS_40 = "40";
//     public static final String CARD_STATUS_41 = "41";
//     public static final String CARD_STATUS_60 = "60"; //已发放到网点
//     public static final String CARD_STATUS_70 = "70"; //已发卡


}
