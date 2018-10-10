package com.tecsun.card.dao.dongruan;

import com.tecsun.card.entity.beandao.dongruan.DongRuanUserInfoDAO;import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

/**
 * @author 0214
 * @createTime 2018/9/25
 * @description
 */
@MapperScan
public interface DongRuanDao {
    public DongRuanUserInfoDAO getDongRuanUserInfoByIdAndName(@Param("idCard")String idCard, @Param("userName")String userName);
}
