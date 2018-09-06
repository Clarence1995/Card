package com.tecsun.card.controller.threadtask.synchro.collectdeal;

import com.tecsun.card.common.clarencezeroutils.ValidateUtil;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CollectPersonInfoValidateUtil {
    public static boolean validateBasicInfoNotNull(BasicPersonInfoPO bvo) {
        String msg = "";
        if (StringUtils.isBlank(bvo.getName())) {
            msg = "姓名为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getCertNum())) {
            msg = "身份证为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getSex())) {
            msg = "性别为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getNation())) {
            msg = "民族为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getGuoJi())) {
            msg = "国籍为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getAddress())) {
            msg = "户籍地址为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getCertValidity())) {
            msg = "证件有效期为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getRegionalCode())) {
            msg = "区域编码为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getParmanentAddress())) {
            msg = "常住地址为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getCertType())) {
            msg = "证件类型为空";
            bvo.setDealMsg(msg);
            return false;
        }
        if (StringUtils.isBlank(bvo.getMobile()) || ValidateUtil.isMobile(bvo.getMobile())) {
            msg = "手机号码不正确或为空值";
            bvo.setDealMsg(msg);
            return false;
        }
        return true;
    }

    /**
     * 判断是否婴儿卡
     * @param certNum
     * @return
     * @throws ParseException
     */
    public static boolean BabyCard(String certNum) throws ParseException{
        String year=certNum.substring(6, 10);
        String month=certNum.substring(10, 12);
        String day=certNum.substring(12,14);
        String birthday=year+"/"+month+"/"+day;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date birth=sdf.parse(birthday);
            Date now=new Date();
            return (now.getTime()-birth.getTime())<3600L*24L*365L*6L*1000L?true:false;//60*60*24*365*1000
        } catch (ParseException e) {
            throw e;
        }
    }
}
