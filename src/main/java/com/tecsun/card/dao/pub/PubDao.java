package com.tecsun.card.dao.pub;

import com.tecsun.card.entity.beandao.pub.RedisDic;
import com.tecsun.card.entity.beandao.pub.RedisDictionaryDAO;
import com.tecsun.card.entity.po.TDistinct;
import org.apache.ibatis.annotations.Param;import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface PubDao {

    // ~ GET

    /**
     * GET
     * @param groupId
     * @return
     */
    RedisDic getChildByGroupId(String groupId);

    int countRegionalCode(@Param("regionalCode")String regionalCode);
    // ~ INSERT



    // ~ UPDATE

    /**
     * 更新字典
     * @param redisDic
     * @return
     */
    int updateDict(RedisDic redisDic);

    /**
     * 初始化Redis
     * @return
     */
    List<RedisDictionaryDAO> initRedis();

    /**
     * 通过机构ID获取机构详情
     *
     * @param regionalCode
     * @return
     */
    TDistinct getTDistinctByRegionCode(@Param("regionalCode")String regionalCode);

    /**
     * 列举所有T_DISTINCT表数据
     * @return
     */
    List<TDistinct> listTDistincts();
    // ~ DELETE
}
