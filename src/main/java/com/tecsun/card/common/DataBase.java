package com.tecsun.card.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * ClassName: DataBase
 * Description: 初始化redis连接池
 * Author： thl
 * CreateTime： 2015年06月07日 13时:59分
 */
public class DataBase {

    protected static final Log logger = LogFactory.getLog(DataBase.class);

    private static JedisPool jedisPublicPool;
    private static boolean isInit = false;

    public static final void init() {
        if (!isInit) {
            initJedisCenterPool();
        }
    }

    private static final void initJedisCenterPool() {
        // String jedisInfoStr = Config.getInstance().get("redis_core_database");
        String redisURL = "127.0.0.1_6379";
        if (StringUtils.isNotBlank(redisURL)) {
            String[] jedisInfo = redisURL.split("_");
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(20);
            config.setMaxWaitMillis(5000L);
            config.setTestOnBorrow(true);
            jedisPublicPool = new JedisPool(config, jedisInfo[0], Integer.valueOf(jedisInfo[1]).intValue(), 10000);
            logger.info("初始化redis成功=============");
        } else {
            logger.info(">>>>>>public redis is not write on config.properties");
        }
    }

    public static JedisPool getJedisPublicPool() {
        if (jedisPublicPool == null) {
            initJedisCenterPool();
        }
        return jedisPublicPool;
    }
}
