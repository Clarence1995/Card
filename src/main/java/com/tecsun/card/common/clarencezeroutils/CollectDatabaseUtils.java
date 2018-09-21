package com.tecsun.card.common.clarencezeroutils;

import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author 0214
 * @createTime 2018/9/19
 * @description
 */
public class CollectDatabaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(CollectDatabaseUtils.class);
    private final static Long   BABY_TIME                  = Constants.BABY_TIME;
    private final static String BIRTH_TIME_PATTERN         = "yyyyMMdd";
    private static       int    REGIONAL_CODE_SIX_LENGTH   = 6;
    private static       int    REGIONAL_CODE_EIGHT_LENGTH = 8;
    private static       String XZ_54_PREFIX               = "54";


    /**
     * @Description  采集基本人员信息校验
     * @param: basicBean
     * @return  boolean
     * @author  0214
     * @createTime 2018-09-20 09:51
     * @updateTime
     */
    public static boolean checkCollectUserInfo(BasicPersonInfoPO basicBean) {
        boolean       validateResult = true;
        StringBuilder dealMsg        = new StringBuilder();
        dealMsg.append("基本信息缺失:");
        if (StringUtils.isBlank(basicBean.getCertNum())) {
            validateResult = false;
            basicBean.setDealMsg(" 身份证号码为空");
            return validateResult;
        }
        if (StringUtils.isBlank(basicBean.getRegionalCode())) {
            // 行政区划代码
            validateResult = false;
            basicBean.setDealMsg(" 行政区划代码为空");
            return validateResult;
        }
        if (StringUtils.isBlank(basicBean.getName())) {
            validateResult = false;
            dealMsg.append(" 姓名为空");
        }
        if (StringUtils.isBlank(basicBean.getSex())) {
            validateResult = false;
            dealMsg.append(" 性别为空");
        }
        if (StringUtils.isBlank(basicBean.getNation())) {
            validateResult = false;
            dealMsg.append(" 民族为空");
        }
        if (StringUtils.isBlank(basicBean.getGuoJi())) {
            validateResult = false;
            dealMsg.append(" 国籍为空");
        }
        if (StringUtils.isBlank(basicBean.getAddress())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(" 户籍地址为空");
        }
        if (StringUtils.isBlank(basicBean.getCertValidity())) {
            // 证件有效期
            validateResult = false;
            dealMsg.append(" 证件有效期为空");
        }
        if (StringUtils.isBlank(basicBean.getParmanentAddress())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(" 常住地址为空");
        }
        if (StringUtils.isBlank(basicBean.getCertType())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(" 证件类型为空");
        }
        if (StringUtils.isBlank(basicBean.getMobile())) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(" 手机号为空");
        }
        if (StringUtils.isBlank(basicBean.getBirthday())) {
            basicBean.setBirthday(basicBean.getCertNum().substring(6, 14));
        }

        // 2、判断是否为婴儿卡(以当前时间判断)
        boolean eBabyCard = checkBabyCard(basicBean.getBirthday());
        if (eBabyCard) {
            basicBean.setIsBaby(Constants.COLLECT_IS_BABY);
        } else {
            basicBean.setIsBaby(Constants.COLLECT_NOT_BABY);
        }

        if (!validateResult) {
            basicBean.setDealMsg(dealMsg.toString());
        }
        return validateResult;
    }

    /**
     * @Description 判断是否为婴儿卡
     * @params
     * @return
     * @author 0214
     * @createTime 2018-09-17 10:48
     * @updateTime
     */
    public static boolean checkBabyCard(String birthDayStr) {
        Date birthFormatDate = DateUtils.getDateByString(birthDayStr, BIRTH_TIME_PATTERN);
        Date nowDate         = new Date();
        return (nowDate.getTime() - birthFormatDate.getTime()) < BABY_TIME ? true : false;
    }


    /**
     * @Description 行政区划校验
     * @param: userInfo 需要设置AC01PO aac301b字段为6位或8位的行政区划
     * @return  java.lang.Boolean
     * @author  0214
     * @createTime 2018-09-20 09:41
     * @updateTime
     */
    public static Boolean checkRegionalcode(Ac01PO userInfo) {
        String regionalCode = userInfo.getAac301b();
        if (ObjectUtils.isEmpty(regionalCode)) {
            throw new NullPointerException("[0214 区域校验异常] filePath不能为空");
        }
        int currentRegLength = regionalCode.length();
        if (REGIONAL_CODE_SIX_LENGTH == currentRegLength || REGIONAL_CODE_EIGHT_LENGTH == currentRegLength) {
            // regionalCode为6位或8位
            if (REGIONAL_CODE_SIX_LENGTH == currentRegLength) {
                if (!Constants.OUTERCODELIST.contains(regionalCode)) {
                    logger.error("人员ID: {} 区划编码校验: 长度为6位,但不属于区外异地编码", userInfo.getAac147());
                    userInfo.setDealMsg(" 区划编码校验: 长度为6位,但不属于区外异地编码");
                    return false;
                } else {
                    // 区外异地校验成功
                    userInfo.setAac301(Constants.OUTER_CODE_REGIONAL);
                    userInfo.setAac301a(regionalCode);
                    userInfo.setAac301b(regionalCode + "00");
                    return true;
                }
            } else {
                if (XZ_54_PREFIX.equals(regionalCode.substring(0, 2))) {
                    String codeRoot = regionalCode.substring(0, 4);
                    codeRoot = StringUtils.rightPad(codeRoot, 6, "0");
                    userInfo.setAac301(codeRoot);
                    userInfo.setAac301a(regionalCode.substring(0, 6));
                    return true;
                } else {
                    userInfo.setDealMsg(" 区划编码校验: 长度为8位,但不以54开头");
                    logger.error("人员ID: {} 区划编码校验: 长度为8位,但不以54开头", userInfo.getAac147());
                    return false;
                }
            }
        } else {
            userInfo.setDealMsg(" 区划编码校验: 长度不为6位或8位,无法判断");
            logger.error("人员ID: {} 区划编码校验: 长度不为6位或8位,无法判断", userInfo.getAac147());
            return false;
        }
    }
}
