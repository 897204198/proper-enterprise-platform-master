package com.proper.enterprise.platform.push.common.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.nutz.ioc.loader.json.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PushGlobalInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushGlobalInfo.class);

    public static final String JSM_DES_APP_SERVER_REQUEST = "properpush/jms/AppServerRequest";

    private Map<String, Map<String, Object>> appconfigs;

    public Map<String, Map<String, Object>> getPushConfigs() {
        if (appconfigs == null) {
            ClassPathResource configResource = new ClassPathResource("conf/push/common/pushConfigs.js");
            Reader pushConfigsReader = null;
            try {
                pushConfigsReader = new InputStreamReader(configResource.getInputStream(), "UTF-8");
                JsonLoader jr = new JsonLoader(pushConfigsReader);
                appconfigs = jr.getMap();
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                if (pushConfigsReader != null) {
                    try {
                        pushConfigsReader.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }

        }
        return appconfigs;
    }

    /**
     * 获取数据库的批处理大小
     * 
     * @return 现在默认写死1000
     */
    public int getDbBatchSize() {
        return 1000;
    }

}
