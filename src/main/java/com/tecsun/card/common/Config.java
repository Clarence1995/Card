package com.tecsun.card.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by thl on 15-5-15.
 */
public class Config {
    private Log log = LogFactory.getLog(Config.class);
    private static final String CONIFG_FILE = "/config_1.8.properties";
    private static Config instance = null;
    private Properties props = new Properties();

    private Config() {
        this.init();
    }

    private void init() {
        InputStream in = null;

        try {
            in = Config.class.getResourceAsStream("/config_1.8.properties");
            this.props.load(in);
        } catch (IOException var11) {
            if(this.log.isErrorEnabled()) {
                this.log.error("loading /config_1.8.properties fail!", var11);
            }
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

        }

    }

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }

        return instance;
    }

    public String get(String key) {
        return this.get(key, (String)null);
    }

    public String get(String key, String defaultValue) {
        return this.props.getProperty(key, defaultValue);
    }

}
