package com.tecsun.card.service.impl;

import com.tecsun.card.service.RedisService;
import com.tecsun.card.service.SystemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/beans.xml")
public class SystemServiceTest {
    @Autowired
    SystemService systemService;


    @Test
    public void testRegionalCodeExit() {
        String regionalCode = "54212726";
        System.out.println(systemService.judgeReigonalCodeExit(regionalCode));
    }
}