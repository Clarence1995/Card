package com.tecsun.card.dao.midtwenty;

import com.tecsun.card.entity.po.BasicPersonInfo;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface MidTwenty {
    // ~ GET

    /**
     * @return java.util.List<com.tecsun.card.entity.po.BasicPersonInfo>
     * @Description 中间表20获取采集人员详情, 因为区外异地是保存在这里
     * @param:
     * @author 0214
     * @createTime 2018-09-27 09:02
     * @updateTime
     */
    BasicPersonInfo getSingleBasicPersonByIdcardFromMidTwenty(@Param("idCard")String idCard, @Param("name")String name);
}
