package com.tecsun.card.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tecsun.card.dao.card.CardDao;
import com.tecsun.card.entity.po.Ac01PO;
import com.tecsun.card.entity.po.BusApplyPO;
import com.tecsun.card.entity.vo.GongAnInfoVO;
import com.tecsun.card.service.DataHandleService;
import com.tecsun.card.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 0214
 * @createTime 2018/10/25
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/beans.xml")
public class CardServiceTest {
    @Autowired
    private CardDao           cardDao;
    @Autowired
    private DataHandleService dataHandleService;
    @Autowired
    private RedisService      redisService;

    @Test
    public void testBatchInsertAC01() {
        List<Ac01PO> list = new ArrayList<>(9);

        for (int i = 100; i < 109; i++) {
            Ac01PO ac01PO = new Ac01PO();
            ac01PO.setIdKey(i);
            ac01PO.setAac003("张汶沣,我想你了");
            ac01PO.setAab099("20991230");
            list.add(ac01PO);
        }
        cardDao.insertUserBatch(list);
    }

    @Test
    public void testRedis() {
        // 1000: 694K 0S/10000:2.85 8S/ 100000: 5.81
        Long   startTime = System.currentTimeMillis();
        String root      = "CARD_REDIS";
        for (int i = 0; i < 100000; i++) {
            GongAnInfoVO gongAnInfoVO = new GongAnInfoVO();
            gongAnInfoVO.setXM("0214 I Love You");
            gongAnInfoVO.setMZ("01");
            gongAnInfoVO.setHKSZD("四川岳池县");
            gongAnInfoVO.setSFZH("511621199501157759" + i);
            gongAnInfoVO.setXB(0);
            redisService.hset(root, gongAnInfoVO.getSFZH(), JSONObject.toJSONString(gongAnInfoVO));
        }
        Long endTiem = System.currentTimeMillis();
        System.out.println("Redis HASH 存入时间: " + (endTiem - startTime) / 1000 + "秒");
    }

    @Test
    public void testDataHandle() throws Exception {
        List<Ac01PO>            list          = new ArrayList<>(9);
        Map<String, BusApplyPO> busApplyPOMap = new HashMap<>(9);
        for (int i = 0; i < 9; i++) {
            Ac01PO ac01PO = new Ac01PO();
            ac01PO.setAac147("51162119950115775" + i);
            ac01PO.setMarkStatus("1");
            ac01PO.setAac003("张汶沣 我想你了。真的每天都想你");
            list.add(ac01PO);

            BusApplyPO busApplyPO = new BusApplyPO();
            busApplyPO.setApplyName(ac01PO.getAac003());
            busApplyPO.setApplyName(ac01PO.getAac147());

            busApplyPOMap.put(ac01PO.getAac147(), busApplyPO);
        }

        dataHandleService.handleSynchroByUserListBusList(list, busApplyPOMap, null);
    }

    @Test
    public void testGetBatchSQE() {
        List<Long> result = cardDao.getAC01SequenceBatch(10);
        for (Long aLong : result) {
            System.out.println("获取到序列结果为: " + aLong);
        }
    }

    @Test
    public void testAC01BatchInsert() {
        List<Ac01PO> list = new ArrayList<>(1000);
        for (int i = 0; i < 9; i++) {
            Ac01PO ac01PO = new Ac01PO();
            ac01PO.setAac147("511621199501157759");
            ac01PO.setMarkStatus("1");
            ac01PO.setAac003("张汶沣 我想你了");
            list.add(ac01PO);
        }
        cardDao.insertUserBatch(list);
    }

    @Test
    public void test() {
        // 测试结果: 1000条记录 单条插入: 15016/ 多条同时插入: 6068
        Long             start = System.currentTimeMillis();
        List<BusApplyPO> list  = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            BusApplyPO busApplyPO = new BusApplyPO();
            busApplyPO.setApplyFormCode("1");
            busApplyPO.setStatus("00");
            busApplyPO.setRegionalId("54000");
            busApplyPO.setBusinessType("3");
            busApplyPO.setPersonId(300);
            busApplyPO.setIsexpress("00");
            busApplyPO.setApplyMobile("18813295493");
            busApplyPO.setApplyName("hello");
            list.add(busApplyPO);
        }
        cardDao.insertBusApplyBatch(list);
        Long end = System.currentTimeMillis();
        System.out.println("测试完成: 所花费时间: " + (end - start));
        // List<BusApplyPO> list = new ArrayList<>();
        // BusApplyPO busApplyPO = new BusApplyPO();
        // busApplyPO.setApplyFormCode("1");
        // busApplyPO.setStatus("00");
        // busApplyPO.setRegionalId("54000");
        // busApplyPO.setBusinessType("3");
        // busApplyPO.setPersonId(300);
        // busApplyPO.setIsexpress("00");
        // busApplyPO.setApplyMobile("18813295493");
        // busApplyPO.setApplyName("hello");
        // BusApplyPO busApplyPO1 = new BusApplyPO();
        // busApplyPO1.setApplyFormCode("1");
        // busApplyPO1.setStatus("00");
        // busApplyPO1.setRegionalId("54000");
        // busApplyPO1.setBusinessType("3");
        // busApplyPO1.setPersonId(301);
        // busApplyPO1.setIsexpress("00");
        // busApplyPO1.setApplyMobile("18813295493");
        // busApplyPO1.setApplyName("hello");
        // list.add(busApplyPO);
        // list.add(busApplyPO1);
        // cardDao.insertBusApplyBatch(list);
    }
}
