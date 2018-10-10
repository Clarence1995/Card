package com.tecsun.card.service.threadrunnable;

import com.tecsun.card.entity.Constants;
import com.tecsun.card.entity.Result;
import com.tecsun.card.entity.beandao.card.Ac01DAO;
import com.tecsun.card.entity.beandao.collect.BasicPersonInfoDAO;
import com.tecsun.card.entity.beandao.dongruan.DongRuanUserInfoDAO;
import com.tecsun.card.entity.vo.CollectVO;
import com.tecsun.card.service.CardService;
import com.tecsun.card.service.CollectService;
import com.tecsun.card.service.DataHandleService;
import com.tecsun.card.service.DongRuanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 0214
 * @createTime 2018/9/25
 * @description
 */
public class DongRuanSynchroRunnable implements Runnable {
    private final Logger                   logger = LoggerFactory.getLogger(getClass());
    private       List<BasicPersonInfoDAO> collectList;
    private       List<Ac01DAO>            cardList;
    private       DataHandleService        dataHandleService;
    private String logFilePath ;

    public DongRuanSynchroRunnable(List<BasicPersonInfoDAO> collectList, List<Ac01DAO> cardList, DataHandleService dataHandleService, String logFilePath) {
        this.collectList = collectList;
        this.cardList = cardList;
        this.dataHandleService = dataHandleService;
        this.logFilePath = logFilePath;
    }

    @Override
    @Transactional(value = "springJTATransactionManager", rollbackFor = {Exception.class})
    public void run() {
        Result result = dataHandleService.handleDongRuanSynchro(collectList, cardList, logFilePath);
        logger.info("[0214] 当前线程:{} 已完成从东软视图中完成单位名称、单位编号同步, 结果为: {}", Thread.currentThread().getName(), result.getMsg());
    }


}
