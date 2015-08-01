package com.proper.enterprise.platform.core.json;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class JSONObject implements Map<String, Object>, Cloneable, Serializable {

    private static final long serialVersionUID = -6896564586841449574L;
    
    private com.alibaba.fastjson.JSONObject obj;
    
    public JSONObject(com.alibaba.fastjson.JSONObject obj) {
        this.obj = obj;
    }
    
    @Override
    public boolean containsKey(Object key) {
        return obj.containsKey(key);
    }
    
    @Override
    public Object remove(Object key) {
        return obj.remove(key);
    }
    
    @Override
    public Object put(String key, Object value) {
        return obj.put(key, value);
    }

    @Override
    public int size() {
        return obj.size();
    }

    @Override
    public boolean isEmpty() {
        return obj.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return obj.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return obj.get(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        obj.putAll(m);
    }

    @Override
    public void clear() {
        obj.clear();
    }

    @Override
    public Set<String> keySet() {
        return obj.keySet();
    }

    @Override
    public Collection<Object> values() {
        return obj.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return obj.entrySet();
    }

}
