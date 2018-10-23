package com.tecsun.card.common.clarencezeroutils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 0214
 * @Description 获取配置文件
 * @createTime 2018-10-11 16:11
 * @updateTime
 */
public class PropertyUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

    private static final String        PROPERTIES_FILE_PATH = "/clarencezero.properties";
    private static       PropertyUtils instance             = null;
    private static       Properties    props;

    static {
        // 初始化读取配置文件
        init();
    }

    synchronized static private void init() {
        InputStream in = null;
        props = new Properties();
        try {
            in = PropertyUtils.class.getResourceAsStream(PROPERTIES_FILE_PATH);
            props.load(in);
        } catch (IOException e) {
            logger.error("文件名: {} 加载异常,异常原因: {}", PROPERTIES_FILE_PATH, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("[0214] 配置文件加载 流关闭异常,异常原因: {}", e);
                }
            }
        }
    }

    public static String get(String key) {
        if (null == props) {
            init();
        }
        return props.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        if (null == props) {
            init();
        }
        return props.getProperty(key, defaultValue);
    }
}
