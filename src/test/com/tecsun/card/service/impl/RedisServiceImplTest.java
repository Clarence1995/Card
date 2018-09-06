package com.tecsun.card.service.impl;

import com.tecsun.card.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/beans.xml")
@Transactional
public class RedisServiceImplTest {
    @Autowired
    RedisService redisService;

    @Autowired
    JedisPool jedisPool;
    @Test
    public void get () {
        // String value = redisService.get("jedis-key");
        // System.out.println(value);
        System.out.println("JedisPool" + jedisPool.getResource());
    }

    @Test
    public void set () {
    }

    @Test
    public void hget () {
    }

    @Test
    public void hset () {
    }
}