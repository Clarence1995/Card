package com.tecsun.card.common;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thl on 2015/9/27.
 */
public class Constants {

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
    public static final long IS_NOT_UPDATE_PWD =1;//未修改过密码
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

    public static final String KEY_DICTDATA="siboss:cmp:dictionary_detaillist:{val}";
    public static final String KEY_DISTINCTDATA="siboss:cmp:distinct_list:{val}";

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
    public static final String SEX="SEX";//性别
    public static final String CERTIFICATE_TYPE="CERTIFICATE_TYPE";//证件类型
    public static final String USER_STATUS="USER_STATUS";//用户状态
    public static final String PARAM_NATION="PARAM_NATION";//民族
    public static final String USER_JOB="USER_JOB";//用户个人职业
    public static final String USER_TYPE="USER_TYPE";//用户个人身份
    public static final String MARK_STATUS="MARK_STATUS";//标记状态
    public static final String MARKSTATUS="MARKSTATUS";//标记状态二（包含 即时制卡）
    public static final String CARD_APPLICATION_STATUS="CARD_APPLICATION_STATUS";//卡应用状态
    public static final String GRANT_TYPE="GRANT_TYPE";//发卡领取类型
    public static final String CHECK_STATUS="CHECK_STATUS";//审核状态  通过/不通过
    public static final String INIT_ORG_CODE="INIT_ORG_CODE";//初始化机构编码
    public static final String BATCH_STATUS="BATCH_STATUS";//批次状态
    public static final String BUSINESS_TYPE="APPLY_BUS_TYPE";//业务类型
    public static final String BANK="bank";//银行
    public static final String VALIDTAG="VALIDTAG";//人员有效状态
    public static final String CARD_NO_STATUS="CARDNO_STATUS";//卡号使用状态
    //审核状态
    public static final String CHECK_STATUS_00="00";//未审核
    public static final String CHECK_STATUS_01="01";//审核通过
    public static final String CHECK_STATUS_02="02";//审核不通过
    public static final String CHECK_STATUS_03="03";//业务失败
    //卡号状态
//    00：初始、解锁
//    01：暂用
//    02：已使用
//    03：锁定
    public static final String CARDNO_STATUS_00="00";//初始、解锁
    public static final String CARDNO_STATUS_01="01";//暂用
    public static final String CARDNO_STATUS_02="02";//已使用
    public static final String CARDNO_STATUS_03="03";//锁定



    //交易号
    public static final String	bsId_checkCard="015" ;//卡鉴权

    public static final String bsId_identifyOuter="026";// 卡鉴权及外部认证

    public static final String bsId_identifyInner="028";//  卡内部认证

    //个人状态
    public static final String USER_STATUS_00="00";//初始
    public static final String USER_STATUS_01="01";//制卡中
    public static final String USER_STATUS_02="02";//待领卡
    public static final String USER_STATUS_03="03";//已领卡
    public static final String USER_STATUS_04="04";//已激活
    public static final String USER_STATUS_05="05"; //正式挂失
    public static final String USER_STATUS_06="06";//临时挂失
    public static final String USER_STATUS_07="07";//补卡中
    public static final String USER_STATUS_08="08";//换卡中
    public static final String USER_STATUS_09="09";//注销
    public static final String USER_STATUS_10="10"; //
    public static final String USER_STATUS_11="11";//
    public static final String USER_STATUS_12="12";//
    public static final String USER_STATUS_13="13";//
    public static final String USER_STATUS_14="14";
    public static final String USER_STATUS_15="15";
    public static final String USER_STATUS_16="16";//生成批次失败
    public static final String USER_STATUS_17="17";//即使制卡失败
    public static final String USER_STATUS_18="18";//中心制卡失败
    public static final String USER_STATUS_19="19";//异常
    
  //有效标志
    public static final String USER_VALIDTAG_00="00";//有效
    public static final String USER_VALIDTAG_01="01";//无效
    
    //中间表用户记录 状态
    public static final String USER_RECODS_STATUS_00="00";//初始
    public static final String USER_RECODS_STATUS_01="01";//已同步
    public static final String USER_RECODS_STATUS_02="02";//修改


    //申领 业务类型
    //01个人申请、02网上申请、03批量申请、05遗失补卡、06普通换卡、07质保换卡 08预制卡
    public static final String BUSINESS_TYPE_01="01";//新申领
//    public static final String BUSINESS_TYPE_02="02";//网上申请
//    public static final String BUSINESS_TYPE_03="03";//批量申请
    public static final String BUSINESS_TYPE_05="05";//遗失补卡
    public static final String BUSINESS_TYPE_06="06"; //普通换卡
    public static final String BUSINESS_TYPE_07="07";//质保换卡
    public static final String BUSINESS_TYPE_08="08";//预制卡

    //申领数据来源
    //01个人  02单位、03网点
    public static final String SOURCE_01="01";//个人申请
    public static final String SOURCE_02="02";//02单位
//    public static final String SOURCE_03="03";//03网点

    //申领 状态
    public static final String APPLY_STATUS_00="00";//申请
    public static final String APPLY_STATUS_01="01";//撤销
    public static final String APPLY_STATUS_02="02";//业务成功
    public static final String APPLY_STATUS_03="03";//业务失败
    public static final String APPLY_STATUS_04="04"; //异常处理
    public static final String APPLY_STATUS_05="05";//申领中
    public static final String APPLY_STATUS_06="06";//异常处理成功
    public static final String APPLY_STATUS_07="07";//异常处理失败

    //标记 状态
    public static final String MARK_STATUS_0="0";//正常
    public static final String MARK_STATUS_1="1";//优先
    public static final String MARK_STATUS_2="2";//即时制卡
    public static final String MARK_STATUS_3="3";//中心制卡

   /* public static Map<String,String> MARK_STATUS=new HashMap<String, String>();
    static {
        USER_STATUS.put("0", "正常");
        USER_STATUS.put("1", "优先");
        USER_STATUS.put("2", "即时制卡");
    }*/

    //制卡类型
    public static final String MAKE_CARD_TYPE_01="01";//成品卡
    public static final String MAKE_CARD_TYPE_02="02";//预制卡
    public static final String MAKE_CARD_TYPE_03="03";//即时制卡
    public static final String MAKE_CARD_TYPE_04="04";//中心制卡

    //批次状态//
    // 10生成批次  20 银行已导出  21 银行已回盘    30 工厂已导出  31 工厂已回盘  40 初始化中心已导出  41 初始化中心已回盘   50 网点已领卡
    public static final String BATCH_STATUS_10="10";
    public static final String BATCH_STATUS_20="20";
    public static final String BATCH_STATUS_21="21";
    public static final String BATCH_STATUS_30="30";
    public static final String BATCH_STATUS_31="31";
    public static final String BATCH_STATUS_40="40";
    public static final String BATCH_STATUS_41="41";
    public static final String BATCH_STATUS_50="50";

   //批次明细状态
   //10生成批次    20已有银行帐号   21无银行帐号     22匹配银行帐号失败  30工厂制卡成功    31工厂制卡失败
   // 32工厂制卡异常    40 成品卡（卡中心替换密钥成功）   41密钥错误（卡中心替换密钥失败）   60 已发卡（网点已领卡）
    public static final String BATCH_DETAIL_STATUS_10="10";
    public static final String BATCH_DETAIL_STATUS_20="20";
    public static final String BATCH_DETAIL_STATUS_21="21";
    public static final String BATCH_DETAIL_STATUS_22="22";
    public static final String BATCH_DETAIL_STATUS_30="30";
    public static final String BATCH_DETAIL_STATUS_31="31";
    public static final String BATCH_DETAIL_STATUS_32="32";
    public static final String BATCH_DETAIL_STATUS_40="40";
    public static final String BATCH_DETAIL_STATUS_41="41";
    public static final String BATCH_DETAIL_STATUS_60="60";

    //卡状态
    //10 生成批次   20 银行已导出    21 银行已回盘    30 工厂已导出
    // 31 工厂已回盘     40 初始化中心已导出 41 初始化中心已回盘 60 网点已领卡    70 已发卡

    public static final String CARD_STATUS_00="00"; //数据上传到卡管
    public static final String CARD_STATUS_10="10"; //生成批次
    public static final String CARD_STATUS_20="20"; //银行已导出
    public static final String CARD_STATUS_21="21"; //银行已回盘
    public static final String CARD_STATUS_22="22"; //银行开户导出失败
    public static final String CARD_STATUS_23="23"; //银行开户回盘失败
    public static final String CARD_STATUS_2a="2a"; //ca证书申请导出成功
    public static final String CARD_STATUS_2b="2b"; //ca证书申请回盘成功
    public static final String CARD_STATUS_2c="2c"; //ca证书申请导出失败
    public static final String CARD_STATUS_2d="2d"; //ca证书申请回盘失败
    public static final String CARD_STATUS_30="30";
    public static final String CARD_STATUS_31="31";
    public static final String CARD_STATUS_32="32"; //工厂制卡数据导出失败
    public static final String CARD_STATUS_33="33"; //工厂制卡数据回盘失败
    public static final String CARD_STATUS_40="40";
    public static final String CARD_STATUS_41="41";
    public static final String CARD_STATUS_60="60"; //网点已领卡
    public static final String CARD_STATUS_70="70"; //已发卡

    //0封存,1正常，2 正式挂失，3应用锁定,4临时挂失，9注销
    public static final String CARD_APPLICATION_STATUS_0="0";
    public static final String CARD_APPLICATION_STATUS_1="1";
    public static final String CARD_APPLICATION_STATUS_2="2";
    public static final String CARD_APPLICATION_STATUS_3="3";
    public static final String CARD_APPLICATION_STATUS_4="4";
    public static final String CARD_APPLICATION_STATUS_9="9";

    //卡号序列 reids的key
    public static final String SERIAL="sisp:card:serial";
    //批次号序列 reids的key
    public static final String BATCH_SERIAL="sisp:card:batch_serial";
    //人员序列 reids的key
    public static final String USER_SERIAL="sisp:card:user_serial";

    //城市编码对应
    public static final String CARDCODE="CITY_CODE";


    public static final String BUS_TYPE_01="01";//日志业务类型  01 临时挂失
    public static final String BUS_TYPE_02="02";//02 正式挂失
    public static final String BUS_TYPE_03="03";  //03 激活
    public static final String BUS_TYPE_04="04";  // 04 注销
    public static final String BUS_TYPE_05="05";//解挂
    public static final String BUS_TYPE_06="06";//鉴权
    public static final String BUS_TYPE_07="07";//人员信息修改
    public static final String BUS_TYPE_08="08";//发卡
    public static final String BUS_TYPE_09="09";//有效
    public static final String BUS_TYPE_10="10";//无效
    public static final String BUS_TYPE_11="11";//初始、解锁
    public static final String BUS_TYPE_12="12";//暂用
    public static final String BUS_TYPE_13="13";//已使用
    public static final String BUS_TYPE_14="14";//锁定
    public static final String BUS_TYPE_15="15";//还原批次
    //中间库同步状态
    public static final String mid_init_synStatus="00";//新增记录
    public static final String mid_update_synStatus="02";//修改记录

    //采集库同步状态
    public static final String notSynchro="0";//未同步
    public static final String hadSynchro="1";//已同步

    //预制卡表是否使用 状态
    public static final String IS_USED_00="00";//未
    public static final String IS_USED_01="01";//已

    //生存状态
    public static final String USER_LIVE_STATUS_1="1"; //正常
    public static final String USER_LIVE_STATUS_2="2"; //死亡
    public static final String USER_LIVE_STATUS_3="3"; //被判刑收监或劳动教养
    public static final String USER_LIVE_STATUS_4="4"; //失踪
    public static final String USER_LIVE_STATUS_5="5"; //状态不明

    //证件类型
    public static final String IDCARD_TYPE_01="01"; //居民身份证(户口簿)
    public static final String IDCARD_TYPE_02="02"; //中国人民解放军军官证
    public static final String IDCARD_TYPE_03="03"; //中国人民武装警察警官证
    public static final String IDCARD_TYPE_04="04"; //香港特区护照/港澳居民来往内地通行证
    public static final String IDCARD_TYPE_05="05"; //澳门区护照/港澳居民来往内地通行证
    public static final String IDCARD_TYPE_06="06"; //台湾居民来往大陆通行证
    public static final String IDCARD_TYPE_07="07"; //外国人永久居留证
    public static final String IDCARD_TYPE_08="08"; //外国人护照

    public static Map<String,String> APPLY_STATUS_CONTENT=new HashMap<String, String>();
    static{
    	APPLY_STATUS_CONTENT.put("03", "业务失败");
    	APPLY_STATUS_CONTENT.put("04", "异常处理");
    }
    ///加权因子
    public static Map<String,Integer> CODE_JQYZ=new HashMap<String, Integer>();
    static {
        CODE_JQYZ.put("1", 3);
        CODE_JQYZ.put("2", 7);
        CODE_JQYZ.put("3", 9);
        CODE_JQYZ.put("4", 10);
        CODE_JQYZ.put("5", 5);
        CODE_JQYZ.put("6", 8);
        CODE_JQYZ.put("7", 4);
        CODE_JQYZ.put("8", 2);
    }
    //本地代码 字母
    public static Map<String,Integer> CODE_BTDM=new HashMap<String, Integer>();
    static {
        CODE_BTDM.put("0", 0);
        CODE_BTDM.put("1", 1);
        CODE_BTDM.put("2", 2);
        CODE_BTDM.put("3", 3);
        CODE_BTDM.put("4", 4);
        CODE_BTDM.put("5", 5);
        CODE_BTDM.put("6", 6);
        CODE_BTDM.put("7", 7);
        CODE_BTDM.put("8", 8);
        CODE_BTDM.put("9", 9);
        CODE_BTDM.put("A", 10);
        CODE_BTDM.put("B", 11);
        CODE_BTDM.put("C", 12);
        CODE_BTDM.put("D", 13);
        CODE_BTDM.put("E", 14);
        CODE_BTDM.put("F", 15);
        CODE_BTDM.put("G", 16);
        CODE_BTDM.put("H", 17);
        CODE_BTDM.put("I", 18);
        CODE_BTDM.put("J", 19);
        CODE_BTDM.put("K", 20);
        CODE_BTDM.put("L", 21);
        CODE_BTDM.put("M", 22);
        CODE_BTDM.put("N", 23);
        CODE_BTDM.put("O", 24);
        CODE_BTDM.put("P", 25);
        CODE_BTDM.put("Q", 26);
        CODE_BTDM.put("R", 27);
        CODE_BTDM.put("S", 28);
        CODE_BTDM.put("T", 29);
        CODE_BTDM.put("U", 30);
        CODE_BTDM.put("V", 31);
        CODE_BTDM.put("W", 32);
        CODE_BTDM.put("X", 33);
        CODE_BTDM.put("Y", 34);
        CODE_BTDM.put("Z", 35);

    }

    /**CA 批量处理 status
     * */
    public static final String CA_BATCH_STATUS_00="00";    //初始
    public static final String CA_BATCH_STATUS_01="01";    //网络错误
    public static final String CA_BATCH_STATUS_02="02";    //文件上传成功
    public static final String CA_BATCH_STATUS_03="03";    //文件上传失败
    public static final String CA_BATCH_STATUS_04="04";    //文件下载成功
    public static final String CA_BATCH_STATUS_05="05";    //文件下载失败
    public static final String CA_BATCH_STATUS_06="06";    //webService返回异常

    public static final String CA_ZIP_STATUS_00 = "00";        //未处理
    public static final String CA_ZIP_STATUS_01 = "01";        //报盘处理中
    public static final String CA_ZIP_STATUS_02 = "02";        //回盘处理中
    public static final String CA_ZIP_STATUS_03 = "03";        //已处理
    public static final String CA_ZIP_STATUS_04 = "04";        //处理失败

    public static final String isNotPriority ="00"; //不优先
    public static final String isPriority ="01"; //优先

    /*CA errorCode*/
    public static Map<String,String> CA_ERRORCODE=new HashMap<String, String>();
    static {
        CA_ERRORCODE.put("0","处理成功");
        CA_ERRORCODE.put("10001","数据检查失败");
        CA_ERRORCODE.put("10002","密码机连接失败");
        CA_ERRORCODE.put("10003","签发系统连接失败");
        CA_ERRORCODE.put("10004","调用 CA 签发证书异常");
        CA_ERRORCODE.put("10005","签发失败返回为空");
        CA_ERRORCODE.put("10006","请求报盘文件为空");
        CA_ERRORCODE.put("10007","未查询到对应证书");
        CA_ERRORCODE.put("10008","报盘文件非文本文件");
        CA_ERRORCODE.put("10009","报盘文件名不符合要求");
        CA_ERRORCODE.put("10010","已存在同名报盘文件");
        CA_ERRORCODE.put("10011","密文结构错误");
        CA_ERRORCODE.put("10012","报盘文件处理异常");
        CA_ERRORCODE.put("10013","系统异常");
        CA_ERRORCODE.put("10014","报盘文件未上传");
        CA_ERRORCODE.put("10015","文件校验未通过");
        CA_ERRORCODE.put("10016","报盘文件待处理");
        CA_ERRORCODE.put("10017","报盘文件本地化处理");
        CA_ERRORCODE.put("10018","报盘文件本地化处理完成");
        CA_ERRORCODE.put("10019","报盘文件证书已签发，待转换回盘文件");
        CA_ERRORCODE.put("10020","数据处理中，未形成回盘文件");
        CA_ERRORCODE.put("10021","回盘文件读取异常");
        CA_ERRORCODE.put("10022","请求数据名称为空");
        CA_ERRORCODE.put("10023","请求数据卡号为空");
        CA_ERRORCODE.put("10024","请求数据社会保障号码为空");
        CA_ERRORCODE.put("10025","请求数据签名公钥为空");
        CA_ERRORCODE.put("10026","请求数据发卡地区行政区划代码为空");
        CA_ERRORCODE.put("10027","请求数据证书序列号为空");
        CA_ERRORCODE.put("10028","请求数据操作类型为空");
        CA_ERRORCODE.put("10029","请求数据注销原因为空");
        CA_ERRORCODE.put("10030","请求数据业务系统代码为空");
        CA_ERRORCODE.put("10031","请求数据卡号数据错误");
        CA_ERRORCODE.put("10032","请求数据社会保障号码数据错误");
        CA_ERRORCODE.put("10033","请求数据签名公钥数据错误");
        CA_ERRORCODE.put("10034","请求数据发卡地区行政区划代码数据错误");
        CA_ERRORCODE.put("10035","请求数据操作类型数据错误");
        CA_ERRORCODE.put("10036","请求数据注销原因数据错误");
        CA_ERRORCODE.put("20001","CA-证书签发密码机初始化失败");
        CA_ERRORCODE.put("20002","CA-密钥申请数据加密异常");
        CA_ERRORCODE.put("20003","CA-密钥申请异常");
        CA_ERRORCODE.put("20004","CA-证书签发异常");
        CA_ERRORCODE.put("20005","CA-哈希异常");
        CA_ERRORCODE.put("20006","CA-解密证书签发数据失败");
        CA_ERRORCODE.put("20007","CA-证书签发请求数据解密后类型错误");
        CA_ERRORCODE.put("20008","CA-证书签发请求数据类型错误");
        CA_ERRORCODE.put("30001","KM-备用库分发密钥失败");
        CA_ERRORCODE.put("30002","KM-密钥解密异常");
        CA_ERRORCODE.put("30003","KM-密钥封装失败");
        CA_ERRORCODE.put("30004","KM-密钥申请异常");
        CA_ERRORCODE.put("30005","KM-密钥生产异常");
        CA_ERRORCODE.put("30006","KM-密钥申请数据解密失败");
        CA_ERRORCODE.put("30007","KM-密钥申请数据格式错误");
        CA_ERRORCODE.put("30008","KM-密钥申请数据解密异常");
    }
    // 区域信息编码
    public static List<String> codeList = new ArrayList<>();
    static {
        codeList.add("110000");
        codeList.add("310000");
        codeList.add("410100");
        codeList.add("410200");
        codeList.add("500000");
        codeList.add("510100");
        codeList.add("632801");
        codeList.add("610100");
        codeList.add("610400");
        codeList.add("620100");
    }

    public static final String babyCard = "0";// 婴儿卡（六岁以下）
    public static final String notBabyCard = "1";// 非婴儿卡
}



















