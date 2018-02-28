package com.proper.enterprise.platform.search.common.util;

import java.util.HashMap;

/**
 * 双向关联映射
 * author wanghp
 */
public class RelationMap<K, V> {
    private HashMap<K, V> normal = new HashMap<K, V>();
    private HashMap<V, K> reverse = new HashMap<V, K>();

    public boolean put(K k, V v) {
        if (k == null || v == null) {
            return false;
        }
        normal.put(k, v);
        reverse.put(v, k);
        return true;
    }

    public V getNormalValue(K k) {
        return normal.get(k);
    }

    public K getReverseValue(V v) {
        return reverse.get(v);
    }

    public boolean containsNormalValue(K k) {
        return normal.containsKey(k);
    }

    public boolean containsReverseValue(V v) {
        return reverse.containsKey(v);
    }

    public Integer size() {
        return normal.size();
    }

}
