package com.proper.enterprise.platform.push.config;

import com.proper.enterprise.platform.core.utils.ConfCenter;
import org.nutz.ioc.loader.json.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Component
public class PushGlobalInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushGlobalInfo.class);

    public static final String JSM_DES_APP_SERVER_REQUEST = "properpush/jms/AppServerRequest";

    private Map<String, Map<String, Object>> appconfigs;

    public Map<String, Map<String, Object>> getPushConfigs() {
        if (appconfigs == null) {
            ClassPathResource configResource = new ClassPathResource("conf/push/pushConfigs.js");
            Reader pushConfigsReader = null;
            try {
                pushConfigsReader = new InputStreamReader(configResource.getInputStream(), "UTF-8");
                JsonLoader jr = new JsonLoader(pushConfigsReader);
                appconfigs = jr.getMap();
            } catch (Exception e) {
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
     * @return 现在默认1000
     */
    public int getDbBatchSize() {
        return Integer.parseInt(ConfCenter.get("pep_push_db_batch_size"));
    }

}
