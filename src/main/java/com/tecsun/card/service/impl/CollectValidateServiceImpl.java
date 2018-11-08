package com.tecsun.card.service.impl;

import com.tecsun.card.common.clarencezeroutils.DateUtils;
import com.tecsun.card.common.clarencezeroutils.IdcardValidator;
import com.tecsun.card.common.clarencezeroutils.ObjectUtils;
import com.tecsun.card.common.clarencezeroutils.ValidateUtils;
import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfo;
import com.tecsun.card.service.RedisService;
import com.tecsun.card.service.SystemService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 0214
 * @createTime 2018/9/19
 * @description
 */
@Service("collectValidateService")
public class CollectValidateServiceImpl {
    private final  Logger logger                     = LoggerFactory.getLogger(CollectValidateServiceImpl.class);
    private final  String BIRTH_TIME_PATTERN         = "yyyyMMdd";
    private static int    REGIONAL_CODE_SIX_LENGTH   = 6;
    private static int    REGIONAL_CODE_EIGHT_LENGTH = 8;
    private static String XZ_54_PREFIX               = "54";


    @Autowired
    private RedisService  redisService;
    @Autowired
    private SystemService systemService;

    /**
     * @return boolean
     * @Description 采集基本人员信息校验
     * @param: basicBean
     * @author 0214
     * @createTime 2018-09-20 09:51
     * @updateTime
     */
    public boolean checkCollectUserInfo(BasicPersonInfo basicBean) {
        boolean       validateResult = true;
        StringBuilder dealMsg        = new StringBuilder();
        dealMsg.append("基本信息缺失:");
        String  idCard       = basicBean.getCertNum();
        String  regionalCode = basicBean.getRegionalCode();
        String  name         = basicBean.getName();
        Integer sex          = basicBean.getSex();
        String  nation       = basicBean.getNation();
        String  guoJi        = basicBean.getGuoJi();
        String  hujiAddress  = basicBean.getAddress();
        String  certValidity = basicBean.getCertValidity();
        String eBaby = basicBean.getIsBaby();
        if (StringUtils.isBlank(idCard)) {
            validateResult = false;
            basicBean.setDealMsg(" 身份证号码为空");
            return validateResult;
        } else {
            boolean valResult = ValidateUtils.isIdCard(idCard);
            if (!valResult) {
                validateResult = false;
                basicBean.setDealMsg("身份证号码校验失败");
                return validateResult;
            }
        }
        if (StringUtils.isBlank(regionalCode)) {
            // 行政区划代码
            validateResult = false;
            basicBean.setDealMsg(" 行政区划代码为空");
            return validateResult;
        } else {
            boolean result = systemService.judgeReigonalCodeExit(regionalCode);
            if (!result) {
                validateResult = false;
                basicBean.setDealMsg("行政区域查找不到");
                return validateResult;
            }
        }
        if (StringUtils.isBlank(name)) {
            validateResult = false;
            dealMsg.append(" 姓名为空");
        }
        if (!ObjectUtils.notEmpty(sex)) {
            validateResult = false;
            dealMsg.append(" 性别为空");
        } else {
            int sexFromId = ValidateUtils.getGenderByIdCard(idCard);
            if (sex != sexFromId) {
                validateResult = false;
                dealMsg.append(" 性别校验出错:数据库信息与身份证不符合");
            }
        }
        if (StringUtils.isBlank(nation)) {
            validateResult = false;
            dealMsg.append(" 民族为空");
        }
        if (StringUtils.isBlank(guoJi)) {
            validateResult = false;
            dealMsg.append(" 国籍为空");
        }
        if (StringUtils.isBlank(hujiAddress)) {
            // 户籍地址
            validateResult = false;
            dealMsg.append(" 户籍地址为空");
        }
        if (StringUtils.isBlank(certValidity)) {
            // 证件有效期
            validateResult = false;
            dealMsg.append(" 证件有效期为空");
        } else {
            boolean val = DateUtils.isValidateyyyymmdd(certValidity);
            if (!val) {
                validateResult = false;
                dealMsg.append("证件有效期不符合规范");
            }
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

        basicBean.setBirthday(basicBean.getCertNum().substring(6, 14));

        // 2、判断是否为婴儿卡(以当前时间判断)
        if (ObjectUtils.isEmpty(eBaby)) {
            boolean eBabyCard = IdcardValidator.isBaby(idCard);
            if (eBabyCard) {
                basicBean.setIsBaby(Constants.COLLECT_IS_BABY);
            } else {
                basicBean.setIsBaby(Constants.COLLECT_NOT_BABY);
            }
        }

        if (!validateResult) {
            basicBean.setDealMsg(dealMsg.toString());
        }
        return validateResult;
    }

    public static void main(String[] args) {
        String bir = "20121025";
        CollectValidateServiceImpl collectValidateService = new CollectValidateServiceImpl();
        System.out.println(collectValidateService.checkBabyCard(bir));
    }
    /**
     * @return
     * @Description 判断是否为婴儿卡
     * @params
     * @author 0214
     * @createTime 2018-09-17 10:48
     * @updateTime
     */
    public boolean checkBabyCard(String birthDayStr) {
        Date birthFormatDate = DateUtils.getDateByString(birthDayStr, BIRTH_TIME_PATTERN);
        Date nowDate         = new Date();
        return (nowDate.getTime() - birthFormatDate.getTime()) < Constants.BABY_TIME ? true : false;
    }


    /**
     * @return java.lang.Boolean
     * @Description 行政区划校验
     * @param: userInfo 需要设置AC01PO aac301b字段为6位或8位的行政区划
     * @author 0214
     * @createTime 2018-09-20 09:41
     * @updateTime
     */
    public Boolean checkRegionalcode(Ac01PO userInfo) {
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
