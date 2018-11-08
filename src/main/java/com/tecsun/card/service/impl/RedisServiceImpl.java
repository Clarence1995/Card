package com.tecsun.card.service.impl;

import com.tecsun.card.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("redisService")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public String get (String key) {
        Jedis resource = jedisPool.getResource();
        String result = resource.get(key);
        jedisPool.returnResource(resource);
        return result;
    }

    @Override
    public String set (String key, String value) {
        // return jedisPool.getResource().set(key, value);
        Jedis resource = jedisPool.getResource();
        String result = resource.set(key, value );
        jedisPool.returnResource(resource);
        return result;
    }

    @Override
    public String hget (String hkey, String field) {
        // return jedisPool.getResource().hget(hkey, field);
        Jedis resource = jedisPool.getResource();
        String result = resource.hget(hkey, field);;
        jedisPool.returnResource(resource);
        return result;
    }

    @Override
    public Long hset (String hkey, String field, String value) {
        // return jedisPool.getResource().hset(hkey, field, value);
        Jedis resource = jedisPool.getResource();
        Long result = resource.hset(hkey, field, value);
        jedisPool.returnResource(resource);
        return result;
    }

    @Override
    public void del(String key) {
        Jedis resource = jedisPool.getResource();
        resource.del(key);
    }
}
