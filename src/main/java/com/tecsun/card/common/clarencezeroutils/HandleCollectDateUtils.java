package com.tecsun.card.common.clarencezeroutils;

import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 0214
 * @createTime 2018/9/17
 * @description 采集库数据处理工具类
 */
public class HandleCollectDateUtils {
    private final static Long   BABY_TIME                = Constants.BABY_TIME;
    private final static String BIRTH_TIME_PATTERN       = "yyyyMMdd";
    private static       int    SUCCESS_CODE             = 200;
    private static       int    FAIL_CODE                = 0;
    private static       int    REGIONAL_CODE_MIN_LENGTH = 6;
    private static       int    REGIONAL_CODE_MAZ_LENGTH = 8;
    private static       String XZ_54_PREFIX             = "54";

    public static void main(String[] args) {
        String s11 = "52010010";
        Result result = validateRegionalCode(s11);
        System.out.println(result);
    }

    /**
     * @return
     * @Description 判断是否为婴儿卡
     * @params
     * @author 0214
     * @createTime 2018-09-17 10:48
     * @updateTime
     */
    public static boolean eBabyCard(String birthDayStr) {
        Date birthFormatDate = DateUtils.getDateByString(birthDayStr, BIRTH_TIME_PATTERN);
        Date nowDate         = new Date();
        return (nowDate.getTime() - birthFormatDate.getTime()) < BABY_TIME ? true : false;
    }

    public static Result validateRegionalCode(String regionalCode) {
        Result result = new Result();
        if (ObjectUtils.isEmpty(regionalCode)) {
            throw new NullPointerException("[0214 工具类] 区域校验异常: filePath不能为空");
        }
        int currentRegLength = regionalCode.length();
        if (REGIONAL_CODE_MIN_LENGTH == currentRegLength || REGIONAL_CODE_MAZ_LENGTH == currentRegLength) {
            // regionalCode为6位或8位
            if (REGIONAL_CODE_MIN_LENGTH == currentRegLength) {
                if (!Constants.OUTERCODELIST.contains(regionalCode)) {
                    result.setStateCode(FAIL_CODE);
                    result.setMsg("[区划校验出错]: 区划编码为6位,但不属于区外异地编码");
                    return result;
                } else {
                    result.setStateCode(SUCCESS_CODE);
                    result.setMsg("[区划校验成功]: 区划编码为6位并属于区外异地编码 ");
                    return result;
                }
            } else {
                if (XZ_54_PREFIX.equals(regionalCode.substring(0, 2))) {
                    result.setStateCode(SUCCESS_CODE);
                    result.setMsg("[区划校验成功]: 区划编码为8位并属于存在西藏区划编码内");
                    return result;
                } else {
                    result.setStateCode(FAIL_CODE);
                    result.setMsg("[区划校验成功]: 区划编码为8位,但不属于西藏区划编码");
                    return result;
                }
            }
        } else {
            // 出错
            result.setStateCode(FAIL_CODE);
            result.setMsg("[区划校验出错]: 区划编码不为6位或8位");
            return result;
        }
    }





}
