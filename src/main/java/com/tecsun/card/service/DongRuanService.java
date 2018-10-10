package com.tecsun.card.service;

import com.tecsun.card.dao.dongruan.DongRuanDao;
import com.tecsun.card.entity.beandao.dongruan.DongRuanUserInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 0214
 * @createTime 2018/9/25
 * @description
 */
public interface DongRuanService {

    /**
     * @return com.tecsun.card.entity.beandao.dongruan.DongRuanUserInfoDAO
     * @Description 根据IDCard和姓名从东软视图中获取此人的单位名称和单位编号
     * @param: idCard
     * @param: userName
     * @author 0214
     * @createTime 2018-09-25 13:16
     * @updateTime
     */
    public DongRuanUserInfoDAO getDongRuanUserInfoByIdAndName(String idCard, String userName);
}
