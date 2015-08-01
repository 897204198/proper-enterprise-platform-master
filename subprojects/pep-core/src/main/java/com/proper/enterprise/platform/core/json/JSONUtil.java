package com.proper.enterprise.platform.core.json;

import com.alibaba.fastjson.JSON;

public class JSONUtil {
    
    private JSONUtil() { }
    
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

}
