package com.tecsun.card.dao.pub;

import com.tecsun.card.entity.beandao.pub.RedisDic;
import com.tecsun.card.entity.beandao.pub.RedisDictionaryDAO;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface PubDao {
    List<RedisDictionaryDAO> initRedis ();
    RedisDic getChildByGroupId(String groupId);

    int updateDict (RedisDic redisDic);
}
