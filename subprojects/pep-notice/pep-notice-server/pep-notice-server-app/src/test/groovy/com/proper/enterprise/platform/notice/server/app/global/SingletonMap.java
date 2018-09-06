package com.proper.enterprise.platform.notice.server.app.global;

import java.util.HashMap;
import java.util.Map;

public class SingletonMap {

    private SingletonMap() {

    }

    private static Map<String, String> singletMap;

    public static Map getSingletMap() {
        if (null == singletMap) {
            singletMap = new HashMap<>(16);
        }
        return singletMap;
    }
}
