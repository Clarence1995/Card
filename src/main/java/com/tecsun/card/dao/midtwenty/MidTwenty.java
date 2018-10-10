package com.tecsun.card.dao.midtwenty;

import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.visualdata.VisualDataDoughunDAO;
import com.tecsun.card.entity.po.BasicPersonInfoPO;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface MidTwenty {
    // ~ GET

    /**
     * @return java.util.List<com.tecsun.card.entity.po.BasicPersonInfoPO>
     * @Description 中间表20获取采集人员详情, 因为区外异地是保存在这里
     * @param:
     * @author 0214
     * @createTime 2018-09-27 09:02
     * @updateTime
     */
    BasicPersonInfoPO getSingleBasicPersonByIdcardFromMidTwenty(@Param("idCard")String idCard, @Param("name")String name);
}
