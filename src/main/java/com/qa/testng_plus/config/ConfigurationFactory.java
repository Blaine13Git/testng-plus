package com.qa.testng_plus.config;

import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.qa.testng_plus.common_utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationFactory {
    public static final String CONFIG_BASE_DIR = "config/";
    public static final String CONFIG_FILE_NAME = "config.properties";
    public static final String EXT_CONFIG_KEY = "ext_config_file";
    private static Configuration configImpl;
    protected static Logger logger = LoggerFactory.getLogger(ConfigurationFactory.class);

    public static Configuration getConfigration() {
        if (configImpl == null) {
            configImpl = (Configuration) new ConfigurationImpl();
            loadFromConfig("config/config.properties");
        }
        return configImpl;
    }

    public static void loadFromConfig(String confName) {
        logger.info("加载配置文件 [" + confName + "]");
        URL configUrl = Thread.currentThread().getContextClassLoader().getResource(confName);
        if (configUrl == null) {
            logger.error("can not find config [" + confName + "]!");
            return;
        }
        Properties properties = new Properties();
        try {
            properties.load(configUrl.openStream());
            for (Map.Entry entry : properties.entrySet()) {
                Object keyObject = entry.getKey();
                Object valueObject = entry.getValue();
                String envConfigValue = System.getProperty("config." + keyObject.toString());
                if (StringUtils.isNotBlank(envConfigValue)) {
                    configImpl.setProperty(keyObject.toString(), envConfigValue);
                } else {
                    configImpl.setProperty(keyObject.toString(), valueObject.toString());
                }
            }
        } catch (Exception var10) {
            logger.error("can not find ats config [" + confName + "] details [" + var10.getMessage() + "]");
        }
    }
}
