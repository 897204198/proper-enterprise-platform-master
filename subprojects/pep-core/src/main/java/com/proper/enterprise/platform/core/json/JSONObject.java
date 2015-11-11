package com.proper.enterprise.platform.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JSONObject implements Serializable {

    private static final long serialVersionUID = -6896564586841449574L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONObject.class);
    
    private transient ObjectNode obj;
    
    public JSONObject(ObjectNode obj) {
        this.obj = obj;
    }

    public int size() {
        return obj.size();
    }

    public boolean isEmpty() {
        return obj.size() == 0;
    }

    public boolean containsKey(Object key) {
        return obj.has((String) key);
    }

    public boolean containsValue(Object value) {
        return obj.findValue((String) value) != null;
    }

    public String get(Object key) {
        return obj.get((String) key).asText();
    }

    public String put(String key, String value) {
        return obj.put(key, value).asText();
    }

    public String remove(Object key) {
        return obj.remove((String) key).asText();
    }

    public void putAll(Map<? extends String, ? extends String> m) {
        for (Map.Entry entry : m.entrySet()) {
            obj.put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public void clear() {
        obj.removeAll();
    }

    public Set<String> keySet() {
        Iterator<String> iter = obj.fieldNames();
        Set<String> set = new HashSet<String>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    public String toString() {
        String str = "";
        try {
            str = new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException jpe) {
            LOGGER.error("Object to json error!", jpe);
        }
        return str;
    }

}
