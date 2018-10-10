package com.tecsun.card.service.impl;

import com.tecsun.card.dao.dongruan.DongRuanDao;
import com.tecsun.card.entity.beandao.dongruan.DongRuanUserInfoDAO;
import com.tecsun.card.service.DongRuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 0214
 * @createTime 2018/9/25
 * @description
 */
@Service("dongRuanService")
public class DongRuanServiceImpl implements DongRuanService {
    @Autowired
    private DongRuanDao dongRuanDao;

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public DongRuanUserInfoDAO getDongRuanUserInfoByIdAndName(String idCard, String userName) {
        return dongRuanDao.getDongRuanUserInfoByIdAndName(idCard, userName);
    }
}
