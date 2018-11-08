package com.tecsun.card.service;

import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.vo.RedisDictVO;

import java.lang.reflect.InvocationTargetException;

public interface SystemService {
    Result initRedis();

    Result updateRedisDict (RedisDictVO redisDictVO);

    Result getControllerURL() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    Result getDatabaseDetail(String userName);


    /**
     * 判断区域编码是否存在
     * @param regionalCode
     * @return
     */
    boolean judgeReigonalCodeExit(String regionalCode);


    void initRedisFromReigonalCode();

}
