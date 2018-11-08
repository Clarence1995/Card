package com.tecsun.card.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tecsun.card.common.clarencezeroutils.PropertyUtils;
import com.tecsun.card.dao.card.CardDao;
import com.tecsun.card.dao.pub.PubDao;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.pub.RedisDic;
import com.tecsun.card.entity.beandao.pub.RedisDictionaryDAO;
import com.tecsun.card.entity.po.TDistinct;
import com.tecsun.card.entity.vo.RedisDictVO;
import com.tecsun.card.service.RedisService;
import com.tecsun.card.service.SystemService;
import net.sf.jsqlparser.statement.select.Distinct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service("systemService")
public class SystemServiceImpl implements SystemService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PubDao       pubDao;
    @Autowired
    private CardDao      cardDao;
    @Autowired
    private RedisService redisService;

    @Override
    public Result initRedis() {
        Result                   result        = new Result();
        List<RedisDictionaryDAO> resultListDao = pubDao.initRedis();
        // 更新Redis
        if (null != resultListDao && resultListDao.size() > 0) {
            redisService.hset("CARD:HASH:SYSTEM", "listDict", JSONObject.toJSONString(resultListDao));
        }
        result.success(JSONObject.toJSON(resultListDao));
        return result;
    }

    @Override
    public Result updateRedisDict(RedisDictVO redisDictVO) {
        Result   result   = new Result();
        RedisDic redisDic = new RedisDic();
        redisDic.setDictionId(redisDictVO.getDictionId());
        redisDic.setName(redisDictVO.getName());
        redisDic.setCode(redisDictVO.getCode());
        int resultInt = pubDao.updateDict(redisDic);
        if (resultInt > 0) {
            // 更新Redis
            List<RedisDictionaryDAO> resultListDao = pubDao.initRedis();
            if (null != resultListDao && resultListDao.size() > 0) {
                redisService.hset("CARD:HASH:SYSTEM", "listDict", JSONObject.toJSONString(resultListDao));
            }
            result.success("更新成功");
            return result;
        }
        result.fail("更新失败");
        return result;
    }

    @Override
    public Result getControllerURL() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        return null;
    }

    @Override
    public Result getDatabaseDetail(String userName) {
        Result result = new Result();
        result.setStateCode(200);
        result.setMsg("请求成功");
        List<Object> list = new ArrayList<>();
        list.add(cardDao.getUserTable(userName));
        list.add(cardDao.getUserTable("SISP_MIDDATA"));
        list.add(cardDao.getUserTable("SISP_PUBLIC"));
        String JSONResult = JSONObject.toJSONString(list);
        redisService.hset("CARD:HASH:SYSTEM", userName, JSONResult);
        result.setData(JSONResult);
        return result;
    }

    @Override
    public boolean judgeReigonalCodeExit(String regionalCode) {
        boolean result = true;
        String redisResult = redisService.hget(PropertyUtils.get("CARD_DISTINCT_CODE_REDIS"), regionalCode);
        if (null == redisResult) {
            TDistinct tDistinct = pubDao.getTDistinctByRegionCode(regionalCode);
            if (null != tDistinct) {
                redisService.hset(PropertyUtils.get("CARD_DISTINCT_CODE_REDIS"), tDistinct.getCode(), JSONObject.toJSONString(tDistinct));
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 初始化REDIS机构编码表
     */
    @Override
    public void initRedisFromReigonalCode() {
        String redisKey = PropertyUtils.get("CARD_DISTINCT_CODE_REDIS");
        if (null != redisKey) {
            redisService.del(redisKey);
        }
        List<TDistinct> tDistincts = pubDao.listTDistincts();
        for (TDistinct tDistinct : tDistincts) {
            redisService.hset(redisKey, tDistinct.getCode(), JSONObject.toJSONString(tDistinct));
        }

        logger.info("[0214] REDIS 机构 初始化完成。数目: {} ", tDistincts.size());
    }


}
