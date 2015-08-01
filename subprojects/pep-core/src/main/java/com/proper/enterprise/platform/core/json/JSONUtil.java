package com.proper.enterprise.platform.core.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.proper.enterprise.platform.core.utils.StringUtil;

public class JSONUtil {
    
    private JSONUtil() { }
    
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }
    
    public static Object parse(String str) {
        Object obj = JSON.parse(str);
        if (obj == null) {
            return null;
        }
        
        if (obj instanceof com.alibaba.fastjson.JSONObject) {
            return new JSONObject((com.alibaba.fastjson.JSONObject) obj);
        } else if(obj instanceof JSONArray) {
            return toJSONObjectArray((JSONArray) obj);
        } else {
            return null;
        }
    }
    
    private static JSONObject[] toJSONObjectArray(JSONArray array) {
        com.alibaba.fastjson.JSONObject[] objs = array.toArray(new com.alibaba.fastjson.JSONObject[0]);
        int len = objs.length;
        JSONObject[] result = new JSONObject[len];
        for (int i = 0; i < len; i++) {
            result[i] = new JSONObject(objs[i]);
        }
        return result;
    }
    
    public static JSONObject parseObject(String str) {
        if (StringUtil.isNull(str)) {
            return null;
        }
        return new JSONObject((com.alibaba.fastjson.JSONObject) JSON.parse(str));
    }
    
    public static JSONObject[] parseArray(String str) {
        if (StringUtil.isNull(str)) {
            return null;
        }
        return toJSONObjectArray((com.alibaba.fastjson.JSONArray) JSON.parse(str));
    }

}
