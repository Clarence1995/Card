package com.tecsun.card.service.threadrunnable;

import com.alibaba.fastjson.JSONObject;
import com.tecsun.card.common.clarencezeroutils.PropertyUtils;
import com.tecsun.card.entity.vo.GongAnInfoVO;
import com.tecsun.card.service.DataHandleService;
import com.tecsun.card.service.RedisService;
import com.tecsun.card.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 0214
 * @createTime 2018/10/26
 * @description
 */
public class GongAnGetUserInfoBatchRunnable implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 身份证集合
     */
    private List<GongAnInfoVO> idCardList;

    /**
     * service
     */
    private DataHandleService dataHandleService;
    private RedisService      redisService;

    public GongAnGetUserInfoBatchRunnable(List<GongAnInfoVO> idCardList, DataHandleService dataHandleService, RedisService redisService) {
        this.idCardList = idCardList;
        this.dataHandleService = dataHandleService;
        this.redisService = redisService;
    }

    @Override
    public void run() {
        final int batchCount = Integer.parseInt(PropertyUtils.get("GONG_AN_BATCH_COUNT"));
        int       listSize   = idCardList.size();
        for (int i = 0; i < idCardList.size(); i += batchCount) {
            if (i + batchCount > listSize) {
                listSize = listSize - i;
            }
            List newList = idCardList.subList(i, i + listSize);
            try {
                List<GongAnInfoVO> result = dataHandleService.getUserInfoFromGongAnByIdCardList(newList);
                logger.info("[0214 批量调用公安接口] 待获取信息人数: {}, 实际获取到信息人数: {}", newList.size(), result.size());
                for (GongAnInfoVO gongAnInfoVO : result) {
                    redisService.hset(PropertyUtils.get("CARD_GONGAN_USER_INFO"), gongAnInfoVO.getSFZH(), JSONObject.toJSONString(gongAnInfoVO));
                }
            } catch (Exception e) {
                logger.error("[0214] 线程处理公安接口异常。原因: {}", e);
            }
        }
    }
}
