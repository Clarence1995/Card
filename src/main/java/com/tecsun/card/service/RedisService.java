package com.tecsun.card.service;

/**
 *@Description Redis服务
 *@params  
 *@return  
 *@author  0214
 *@createTime  2018/9/6
 *@updateTime
 */
public interface RedisService {

    /**
     *@Description 获取键值
     *@params  
     *@return  
     *@author  0214
     *@createTime  2018/9/6
     *@updateTime
     */
    String get (String key);
    
    /**
     *@Description 设置键值
     *@params  
     *@return  
     *@author  0214
     *@createTime  2018/9/6
     *@updateTime
     */ 
    String set (String key, String value);
    
    /**
     *@Description 获取hash键值
     *@params  
     *@return  
     *@author  0214
     *@createTime  2018/9/6
     *@updateTime
     */
    String hget (String hkey, String field);

    /**
     *@Description Hash设置键值
     *@params
     *@return
     *@author  0214
     *@createTime  2018/9/6
     *@updateTime
     */
    Long hset (String hkey, String field, String value);

    /**
     * 删除键
     * @param key
     */
    void del(String key);

}
